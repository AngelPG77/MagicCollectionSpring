package edu.pga.psp.magiccollectionspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import java.util.Scanner;

@Configuration
public class AppConfig {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }

    @Bean
    public RestClient scryfallClient() {
        return RestClient.builder()
                .baseUrl("https://api.scryfall.com")
                .defaultHeader(HttpHeaders.USER_AGENT, "MagicCollectionSpring/1.0 (angelpugagomez@gmail.com)")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
