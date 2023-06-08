package ua.lviv.javaclub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "TrickyService", url = "localhost:8080")
public interface TrickyServiceFeign {

    @GetMapping("/unstable")
    String getUnstable();

    @GetMapping("/slowrunning")
    String getSlowRunning();
}
