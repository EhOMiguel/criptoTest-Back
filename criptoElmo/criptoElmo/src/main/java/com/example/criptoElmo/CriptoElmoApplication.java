package com.example.criptoElmo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.TextMessage;

@SpringBootApplication
@EnableScheduling 
public class CriptoElmoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CriptoElmoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("ON e roteando");
            StringBuffer responseBuffer = ApiClient.getPrimoInt();
            System.out.println(responseBuffer);
            
            String responseJson = responseBuffer.toString();
            PrimoInteiroState.updateJson(responseJson);
            // StringBuffer response = ApiClient.getPrimoInt();
            // System.out.println(response);
        };
    }
}
