package ru.pugart.ext.api.ui.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.pugart.ext.api.ui.config.auth.AuthenticationManager;
import ru.pugart.ext.api.ui.config.auth.SecurityContextRepository;
import ru.pugart.ext.api.ui.service.keycloak.KeycloakClient;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/swagger-ui/**",
            "/user/**"
    };

    private static final String[] AUTH_USER_WHITELIST = {
            "/api/v1/test2",
            "/api/v1/task/**"
    };

    private static final String[] AUTH_ADMIN_WHITELIST = {
            "/api/v1/test3",
            "/api/config/**"
    };

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, KeycloakClient keycloakClient) {
        String[] adminAndUserWhiteList = ArrayUtils.addAll(AUTH_ADMIN_WHITELIST, AUTH_USER_WHITELIST);
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .pathMatchers(AUTH_USER_WHITELIST).hasAnyAuthority(keycloakClient.getDefaultRoles().toArray(String[]::new)) // hasAnyRole for webflux
                .pathMatchers(adminAndUserWhiteList).hasAnyAuthority(keycloakClient.getAdminRoles().toArray(String[]::new))
                .anyExchange().authenticated()
                .and()
                .build();
    }
}
