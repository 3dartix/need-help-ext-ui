package ru.pugart.ext.api.ui.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.pugart.ext.api.ui.api.dto.Profile;

@ReactiveFeignClient(value = "extUi", url = "${app.services.task-api-service}")
public interface TaskServiceApi {

    @PostMapping(value = "profile/create-or-update")
    Mono<Profile> createOrUpdate(@RequestBody Mono<Profile> profile) throws RuntimeException;

}
