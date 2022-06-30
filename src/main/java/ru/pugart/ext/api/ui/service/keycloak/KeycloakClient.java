package ru.pugart.ext.api.ui.service.keycloak;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pugart.ext.api.ui.config.KeycloakConfig;
import ru.pugart.ext.api.ui.dto.JwtDto;
import ru.pugart.ext.api.ui.dto.UserDto;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
@Slf4j
public class KeycloakClient {

    private final Keycloak kc;
    private final AuthzClient authz;

    private final KeycloakConfig keycloakConfig;
    private final RealmResource realm;

    @Autowired
    public KeycloakClient(KeycloakConfig keycloakConfig) {
        this.keycloakConfig = keycloakConfig;
        String authServerUrl = keycloakConfig.getAuthServerUrl();
        String realm = keycloakConfig.getRealm();

        KeycloakConfig.Credentials userManagerCredentials =
                keycloakConfig.getCredentials().get(KeycloakConfig.KeycloakUserType.USER_MANAGER.getName());

        kc = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(userManagerCredentials.getClientId())
                .clientSecret(userManagerCredentials.getSecret())
                .build();

        KeycloakConfig.Credentials authCredentials =
                keycloakConfig.getCredentials().get(KeycloakConfig.KeycloakUserType.AUTH.getName());

        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", authCredentials.getSecret());
        clientCredentials.put("resource", keycloakConfig.getResource());
        clientCredentials.put("ssl-required", keycloakConfig.getSslRequired());

        Configuration configuration = new Configuration(
                authServerUrl,
                realm,
                authCredentials.getClientId(),
                clientCredentials,
                null);
        String realmName = keycloakConfig.getRealm();
        this.realm = kc.realm(realmName);

        authz = AuthzClient.create(configuration);
    }

    private Optional<UserRepresentation> findUser(String phone) {
        try {
            Optional<UserRepresentation> userOpt = realm.users().search(phone).stream().filter(UserRepresentation::isEnabled).findFirst();

            return userOpt;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            throw ex;
        }
    }

    public List<String> getAdminRoles(){
        return getRolesByGroupName(keycloakConfig.getAdminGroup());
    }

    public List<String> getDefaultRoles(){
        return getRolesByGroupName(keycloakConfig.getDefaultGroup());
    }

    @SneakyThrows
    public List<String> getRolesByGroupName(String groupName){
        String representationId = realm.groups().groups().stream()
                .filter(group -> group.getName().equalsIgnoreCase(groupName))
                .map(GroupRepresentation::getId)
                .findFirst().orElse(null);

        if(representationId == null){
            log.error("role with name: {} not found, check keycloak!", groupName);
            throw new IllegalAccessException("critical error!");
        }
        List<String> realmRoles = realm.groups().group(representationId).toRepresentation().getRealmRoles();
        log.info("by group name: {} found roles: {}", groupName, realmRoles);
        return realmRoles;
    }

    @SneakyThrows
    public boolean validate(String token){
        try {
            authz.authorization(token).authorize();
            return true;
        } catch (Exception ex){
            log.error(ex.getLocalizedMessage(), ex);
            return false;
        }
    }

    @SneakyThrows
    public boolean createOrUpdateUser(UserDto userDto) {
        Optional<UserRepresentation> userOpt = findUser(userDto.getPhone());
        UserRepresentation representation = userOpt.orElseGet(() -> {
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(userDto.getPhone());
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstname());
            user.setLastName(userDto.getLastname());
            user.setGroups(Collections.singletonList(keycloakConfig.getDefaultGroup()));
            return user;
        });
        CredentialRepresentation code = new CredentialRepresentation();

        code.setValue(userDto.getPassword());
        code.setDigits(userDto.getPassword().length());
        code.setType(CredentialRepresentation.PASSWORD);
        code.setCreatedDate(System.currentTimeMillis());
        representation.setCredentials(List.of(code));
        if (representation.getId() == null) {
            Response response = realm.users().create(representation);
            if (response.getStatus() != 201) {
                log.error("can`t create new user in KC, status code:{}, failing:{}", response.getStatusInfo(), response.getEntity());
                throw new RuntimeException("can`t create new user");
            }
        } else {
            log.warn("user already exists! user representation: {}", representation);
//            throw new CreateException(String.format("user with phone: %s already exits", userDto.getPhone()));
//            realm.users().get(representation.getId()).update(representation);
        }
        return true;
    }

    public JwtDto getTokenAndReset(UserDto userDto) {
        try {
            AccessTokenResponse token = authz.obtainAccessToken(userDto.getPhone(), userDto.getPassword());
            log.info("token: {}; refresh token: {}\"", token.getToken(), token.getRefreshToken());
            return new JwtDto(token.getToken());
        } catch (Exception ex) {
            log.error("can`t obtain access token for user:{}", userDto.getPhone(), ex);
            throw new RuntimeException(ex);
        }
    }
}
