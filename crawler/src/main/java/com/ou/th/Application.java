package com.ou.th;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * @author kpkym
 * Date: 2020-03-19 06:37
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/sadfsdf")
    public void s() {

    }
}
