package ru.pugart.ext.api.ui.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.pugart.ext.api.ui.service.keycloak.KeycloakClient;

@RestController
@RequestMapping("/api/config")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class ConfigController {

    private final KeycloakClient keycloakClient;

    @GetMapping("get-roles/{group}")
    public Flux<String> getRolesByGroup(@PathVariable String group){
        try {
            return Flux.fromIterable(keycloakClient.getRolesByGroupName(group));
        } catch (Exception ex) {
            return Flux.empty();
        }
    }
}
