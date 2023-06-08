package ua.lviv.javaclub;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
@RestController
@EnableFeignClients
@RequiredArgsConstructor
public class ResilientServiceApplication {

    private final UnstableService unstableService;

    public static void main(String[] args) {
        SpringApplication.run(ResilientServiceApplication.class, args);
    }

    @GetMapping("/unstable")
    public String callUnstable() {
        try {
            return unstableService.unstableEndpoint();
        } catch (CallNotPermittedException e) {
            return "Call is not permitted now";
        }
    }

    @GetMapping("/slow")
    public String slowRunning() {
        try {
            return unstableService.slowRunning();
        } catch (CallNotPermittedException e) {
            return "Call is not permitted now";
        }
    }

    @GetMapping("/parallel")
    @SneakyThrows
    public List<String> parallel() {
        List<Callable<String>> tasks = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            tasks.add(() -> {
                System.out.println("Started at: " + LocalDateTime.now() + " " + finalI);
                String s = unstableService.slowRunning();
                System.out.println("Finished at: " + LocalDateTime.now() + " " + finalI + " with result: " + s);
                return s;
            });
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<String> result = new ArrayList<>();
        for (Future<String> stringFuture : executorService.invokeAll(tasks)) {
            result.add(stringFuture.get());
        }
        executorService.shutdown();
        return result;
    }

    @GetMapping("/retry")
    public String retry() {
        return unstableService.unstableEndpoint();
    }
}
