package com.company.enroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.company.enroller")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}