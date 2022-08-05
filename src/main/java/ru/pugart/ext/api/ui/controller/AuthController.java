package ru.pugart.ext.api.ui.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.pugart.ext.api.ui.api.dto.Profile;
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
    public Mono<Profile> createUser(@RequestBody Mono<UserDto> userDto){
        return keycloakClient.userCreate(userDto)
                .log()
                .switchIfEmpty(Mono.empty());
    }

    @PostMapping("auth")
    public Mono<JwtDto> authUser(@RequestBody Mono<UserDto> userDto){
        return userDto
                .log()
                .flatMap(u -> Mono.just(keycloakClient.getTokenAndReset(u.getPhone(), u.getPassword())))
                .switchIfEmpty(Mono.empty());
    }
}
