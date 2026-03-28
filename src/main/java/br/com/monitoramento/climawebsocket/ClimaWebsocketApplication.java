package br.com.monitoramento.climawebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClimaWebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClimaWebsocketApplication.class, args);
    }
}
