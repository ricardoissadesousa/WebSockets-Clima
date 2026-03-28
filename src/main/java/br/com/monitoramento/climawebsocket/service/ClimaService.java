package br.com.monitoramento.climawebsocket.service;

import br.com.monitoramento.climawebsocket.model.ClimaResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class ClimaService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    // Ferramenta que vai ler a String e transformar em JSON com segurança
    private final ObjectMapper objectMapper = new ObjectMapper();

    record Cidade(String nome, double lat, double lon) {}

    private final List<Cidade> cidades = List.of(
            new Cidade("Vianópolis", -16.74, -48.50),
            new Cidade("Urutaí", -17.46, -48.20),
            new Cidade("Orizona", -17.03, -48.29),
            new Cidade("Goiânia", -16.67, -49.25),
            new Cidade("Pires do Rio", -17.30, -48.28),
            new Cidade("Catalão", -18.16, -47.94),
            new Cidade("Caldas Novas", -17.74, -48.62),
            new Cidade("Cristalina", -16.76, -47.61),
            new Cidade("Anápolis", -16.32, -48.95),
            new Cidade("Brasília", -15.78, -47.92)
    );

    public ClimaService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void enviarClima() {
        Cidade cidadeSorteada = cidades.get(random.nextInt(cidades.size()));

        String url = String.format(Locale.US, "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
                cidadeSorteada.lat(),
                cidadeSorteada.lon());

        try {
            // 1 Pega o resultado como Texto (String) para não dar erro
            String jsonString = restTemplate.getForObject(url, String.class);

            if (jsonString != null) {
                // 2 Lê o texto e transforma em JsonNode manualmente
                JsonNode resposta = objectMapper.readTree(jsonString);

                if (resposta.has("current_weather")) {
                    double temp = resposta.path("current_weather").path("temperature").asDouble();
                    int condicao = resposta.path("current_weather").path("weathercode").asInt();

                    String descricao = traduzirCondicao(condicao);
                    String horarioAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    ClimaResponse clima = new ClimaResponse(cidadeSorteada.nome(), temp, descricao, horarioAtual);

                    messagingTemplate.convertAndSend("/topic/clima", clima);
                    System.out.println("Enviado: " + cidadeSorteada.nome() + " - " + temp + "°C");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar clima: " + e.getMessage());
        }
    }

    private String traduzirCondicao(int code) {
        if (code == 0) return "Céu Limpo";
        if (code == 1 || code == 2 || code == 3) return "Parcialmente Nublado";
        if (code >= 51 && code <= 67) return "Chuva";
        if (code >= 95) return "Tempestade";
        return "Tempo Instável";
    }
}