package com.example.jwt.conf;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelConfig {
    /**
     * DTO TO Entity, Entity To DTO Converter
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
