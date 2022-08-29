package com.example.springintegration.multirabbit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
//@EnableAutoConfiguration
public class Config {

    @Bean
    public Function<String, String> toUpperCase() {
        return String::toUpperCase;
    }

    @Bean
    public Function<String, String> reverse() {
        return value -> new StringBuilder(value).reverse().toString();
    }
}
