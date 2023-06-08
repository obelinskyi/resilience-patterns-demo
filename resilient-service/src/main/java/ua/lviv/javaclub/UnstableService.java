package ua.lviv.javaclub;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnstableService {

    private final TrickyServiceFeign trickyServiceFeign;

    @CircuitBreaker(name = "trickyService", fallbackMethod = "fallbackAnswer")
    @Retry(name = "trickyService", fallbackMethod = "fallbackAnswer")
    public String unstableEndpoint() {
        return "Unstable service answer is: " +  trickyServiceFeign.getUnstable();
    }

//    @CircuitBreaker(name = "trickyService", fallbackMethod = "fallbackAnswer")
//    @Bulkhead(name = "trickyService", fallbackMethod = "fallbackAnswer")
    @RateLimiter(name = "trickyService", fallbackMethod = "fallbackAnswer")
    public String slowRunning() {
        return "Unstable service answer is: " +  trickyServiceFeign.getSlowRunning();
    }

    public String fallbackAnswer(Throwable t) {
        return "This is default value answer";
    }
}
