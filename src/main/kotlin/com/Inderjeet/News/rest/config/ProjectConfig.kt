package com.Inderjeet.News.rest.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProjectConfig {
    @Bean
    fun modelMapper(): ModelMapper = ModelMapper()
}