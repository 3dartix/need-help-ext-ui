package ru.pugart.ext.ui.config.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * getting data from JWT token
 */
@Component
@AllArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;
    private final static String BEARER = "Bearer ";

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new RuntimeException("method save not supported");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String authToken = authHeader.substring(BEARER.getBytes().length);

            UsernamePasswordAuthenticationToken auth
                    = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return authenticationManager
                    .authenticate(auth)
                    .map(SecurityContextImpl::new);
        }

        return Mono.empty();
    }
}
