package com.upfordown.ismuer.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class IsmuerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsmuerApplication.class, args);
    }
}
