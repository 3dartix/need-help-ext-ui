package ru.pugart.ext.ui.config.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.pugart.ext.ui.service.keycloak.JwtKeycloakDataDto;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtTool {

    private final ObjectMapper mapper;
    private final JwtConsumer consumer;

    @Data
    @Builder
    public static class User{
        private String name;
        private List<String> roles;
    }

    @Autowired
    public JwtTool() {
        mapper = new ObjectMapper();
        consumer = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();
    }

    public User getUser(String jwt){
        JwtKeycloakDataDto jwtKeycloakDataDto = parseJwt(jwt);

        if(jwtKeycloakDataDto == null) {
            return null;
        }

        return User.builder()
                .name(getName(jwtKeycloakDataDto))
                .roles(getRoles(jwtKeycloakDataDto))
                .build();
    }

    private String getName(JwtKeycloakDataDto jwtKeycloakDataDto){
        if(jwtKeycloakDataDto != null && jwtKeycloakDataDto.getPreferredUsername() != null){
            String username = jwtKeycloakDataDto.getPreferredUsername();
            log.info("username: {}", username);
            return username;
        }
        log.error("username not found!");
        return null;
    }

    private List<String> getRoles(JwtKeycloakDataDto jwtKeycloakDataDto){
        if(jwtKeycloakDataDto != null && jwtKeycloakDataDto.getRealmAccess() != null && jwtKeycloakDataDto.getRealmAccess().getRoles() != null){
            ArrayList<String> roles = jwtKeycloakDataDto.getRealmAccess().getRoles();
            log.info("roles: {}", roles);
            return roles;
        }
        log.error("roles not found!");
        return new ArrayList<>();
    }

    private JwtKeycloakDataDto parseJwt(String jwt){
        try {
            JwtClaims claims = consumer.processToClaims(jwt);
            JwtKeycloakDataDto data = mapper.readValue(claims.getRawJson(), JwtKeycloakDataDto.class);
            log.info("parse result: {}", data);
            return data;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return null;
        }
    }
}
