package ru.pugart.ext.ui.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class MainController {

//    @PreAuthorize("hasAuthority('default-user')")
    @GetMapping("test2")
    public Mono<String> test(){
        return Mono.just("successful");
    }

//    @PreAuthorize("hasAuthority('admin-user')")
    @GetMapping("test3")
    public Mono<String> test3(){
        return Mono.just("successful");
    }
}
