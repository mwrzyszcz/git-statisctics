package com.gitstatisctics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GitStatiscticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitStatiscticsApplication.class, args);
    }

}
