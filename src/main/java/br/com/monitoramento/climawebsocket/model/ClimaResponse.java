package br.com.monitoramento.climawebsocket.model;

public class ClimaResponse {
    public String cidade;
    public double temperatura;
    public String descricao;
    public String horario;

    public ClimaResponse(String cidade, double temperatura, String descricao, String horario) {
        this.cidade = cidade;
        this.temperatura = temperatura;
        this.descricao = descricao;
        this.horario = horario;
    }
}
