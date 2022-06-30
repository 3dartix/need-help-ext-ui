package ru.pugart.ext.api.ui.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Mono;
import reactor.core.publisher.Mono;
import ru.pugart.ext.api.ui.dto.JwtDto;
import ru.pugart.ext.api.ui.dto.UserDto;
import ru.pugart.ext.api.ui.service.keycloak.KeycloakClient;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class AuthController {
    private final KeycloakClient keycloakClient;

    @PostMapping("create")
    public Mono<JwtDto> createUser(@RequestBody Mono<UserDto> userDto){
        return userDto
                .log()
                .flatMap(u -> {
                    log.info("userDto: {}", u);
                    if(keycloakClient.createOrUpdateUser(u)) {
                        return Mono.just(keycloakClient.getTokenAndReset(u));
                    }
                    return Mono.empty();
                }).switchIfEmpty(Mono.empty());
    }
}
