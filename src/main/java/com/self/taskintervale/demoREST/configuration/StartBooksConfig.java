package com.self.taskintervale.demoREST.configuration;

import com.self.taskintervale.demoREST.repository.BooksRepositoryImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartBooksConfig {

    @Bean
    public CommandLineRunner commandLineRunner(BooksRepositoryImpl booksRepository){
        return args -> {

        };
    }
}
