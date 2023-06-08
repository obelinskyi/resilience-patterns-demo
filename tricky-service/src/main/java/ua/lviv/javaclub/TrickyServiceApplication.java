package ua.lviv.javaclub;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@RestController
public class TrickyServiceApplication {

    private static final Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(TrickyServiceApplication.class, args);
    }

    @SneakyThrows
    @GetMapping("/unstable")
    public String unstable() {
        if (random.nextInt(4) != 2) {
            System.out.println("Exception is thrown");
            throw new TimeoutException();
        }
        System.out.println("Happy path");
        return "Hello from Tricky Service";
    }

    @SneakyThrows
    @GetMapping("/slowrunning")
    public String longRunning() {
        if (random.nextInt(4) != 2) {
            Thread.sleep(2000);
        }
        return "Loooooong Hello from Tricky Service";
    }
}
