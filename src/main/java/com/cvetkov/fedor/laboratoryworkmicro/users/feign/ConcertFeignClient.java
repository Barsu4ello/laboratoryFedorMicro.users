package com.cvetkov.fedor.laboratoryworkmicro.users.feign;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.AudioResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.CityResponse;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ExceptionResponseStatusChecker;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "concerts-client")
public interface ConcertFeignClient {

    @CircuitBreaker(name = "concertServiceCircuitBreaker", fallbackMethod = "getCityByIdUnavailable")
    @GetMapping("/api/v1/city/{id}")
    CityResponse getCityById(@PathVariable Long id);

    default CityResponse getCityByIdUnavailable(Exception e) {
        ExceptionResponseStatusChecker.check404StatusAndExceptionFeignType("City", e);
        throw new ServiceUnavailableException("Concert server is not available", e);
    }
}
