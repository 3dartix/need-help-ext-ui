package ru.pugart.ext.api.ui.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.pugart.ext.api.ui.api.TaskServiceApi;
import ru.pugart.ext.api.ui.api.dto.Task;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/task/")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    private final TaskServiceApi taskServiceApi;

    @PostMapping(value = "create-or-update")
    public Mono<Task> createOrUpdate(@RequestBody Mono<Task> task, Principal principal) {
        return task
                .log()
                .flatMap(t -> {
                    t.setAuthor(principal.getName());
                    return taskServiceApi.createOrUpdateTask(Mono.just(t));
                });
    }
}
