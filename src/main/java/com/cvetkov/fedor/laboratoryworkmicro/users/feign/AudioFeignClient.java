package com.cvetkov.fedor.laboratoryworkmicro.users.feign;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.UserResponse;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ExceptionResponseStatusChecker;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "audios-client")
public interface AudioFeignClient {

    @CircuitBreaker(name = "audioServiceCircuitBreaker", fallbackMethod = "getAuthorByIdUnavailable")
    @GetMapping("/api/v1/author/{id}")
    AudioResponse getAuthorById(@PathVariable Long id);

    default AudioResponse getAuthorByIdUnavailable(Exception e) {
        ExceptionResponseStatusChecker.check404StatusAndExceptionFeignType("Author", e);
        throw new ServiceUnavailableException("Author server is not available", e);
    }
}
