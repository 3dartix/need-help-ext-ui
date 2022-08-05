package ru.pugart.ext.api.ui.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.pugart.ext.api.ui.api.dto.Profile;
import ru.pugart.ext.api.ui.api.dto.Task;

@ReactiveFeignClient(value = "extUi", url = "${app.services.task-api-service}")
public interface TaskServiceApi {

    @PostMapping(value = "profile/create-or-update")
    Mono<Profile> createOrUpdateProfile(@RequestBody Mono<Profile> profile) throws RuntimeException;

    @PostMapping(value = "task/create-or-update")
    Mono<Task> createOrUpdateTask(@RequestBody Mono<Task> task);

}
