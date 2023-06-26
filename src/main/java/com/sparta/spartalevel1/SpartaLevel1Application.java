package com.sparta.spartalevel1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpartaLevel1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpartaLevel1Application.class, args);
    }

}
