package ru.pugart.ext.api.ui.config.auth;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.pugart.ext.api.ui.config.utils.JwtTool;
import ru.pugart.ext.api.ui.service.keycloak.KeycloakClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * basic authentication
 */
@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final KeycloakClient keycloakClient;
    private final JwtTool jwtTool;

    @SneakyThrows
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        if(!keycloakClient.validate(authToken)) {
            log.warn("keycloak authentication error!");
            return Mono.empty();
        }

        JwtTool.User user = jwtTool.getUser(authToken);

        if (user != null && user.getName() != null && !user.getRoles().isEmpty()) {

            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getName(),
                    null,
                    authorities
            );

            return Mono.just(authenticationToken);
        } else {
            return Mono.empty();
        }
    }
}
