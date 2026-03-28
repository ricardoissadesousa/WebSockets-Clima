# 🌦️ Monitoramento Climático em Tempo Real (WebSockets)

## 📝 Resumo do Projeto
O projeto **Monitoramento Climático** é um sistema distribuído desenvolvido em Spring Boot focado na comunicação bidirecional utilizando o protocolo **STOMP** sobre **WebSockets**. O objetivo principal é demonstrar a capacidade de um servidor de "empurrar" (*push*) dados ativamente para múltiplos clientes conectados, sem a necessidade de requisições repetitivas (*polling*).

---

## 🚀 Instruções de como rodar o projeto

Para executar esta aplicação na sua máquina, siga os passos abaixo:

### Pré-requisitos
- **Java 17** ou superior instalado.
- **Maven** instalado (ou utilizar o wrapper do projeto).
- Uma IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code).

### Passos para execução
1. Clone este repositório para a sua máquina local:
   ```bash
   git clone https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git
   ```

2. Abra a pasta do projeto na sua IDE.

3. Aguarde o Maven baixar todas as dependências do `pom.xml`.

4. Execute a classe principal da aplicação: `ClimaWebsocketApplication.java`

   Ou rode via terminal com:
   ```bash
   ./mvnw spring-boot:run
   ```

5. Com o servidor rodando (Tomcat iniciado na porta `8080`), abra o seu navegador.

6. Acesse a URL:
   ```text
   http://localhost:8080
   ```

7. O Dashboard aparecerá com o status **"Conectado"** e os cards começarão a surgir a cada **5 segundos**.

---

## 🔄 Explicação do fluxo de mensagens

O sistema utiliza uma arquitetura baseada em eventos e comunicação assíncrona. O fluxo de dados acontece na seguinte ordem:

### 1) Conexão Inicial (Handshake)
Ao abrir a página `index.html`, o cliente (navegador) utiliza a biblioteca **SockJS** para estabelecer uma conexão persistente com o servidor Spring Boot através do endpoint `/ws`.

### 2) Inscrição (Subscribe)
Imediatamente após conectar, o cliente se inscreve no tópico de mensagens `/topic/clima` utilizando o protocolo **STOMP**, ficando no modo **escuta**.

### 3) Geração do Dado (Servidor)
A cada **5 segundos** (controlado pela anotação `@Scheduled`), o servidor:

- Sorteia uma cidade de uma lista pré-definida.
- Faz uma requisição HTTP REST para a API externa **Open-Meteo** buscando o clima atual daquela cidade.
- Converte os dados recebidos em um objeto JSON personalizado (`ClimaResponse`).

### 4) Envio da Mensagem (Broadcast/Push)
O servidor pega esse JSON e o **empurra ativamente** para o tópico `/topic/clima`.

### 5) Atualização da Interface (Cliente)
O navegador recebe a mensagem instantaneamente através do WebSocket, processa o JSON no JavaScript e cria um novo card dinâmico no Dashboard, mudando a cor conforme a temperatura.

Esse fluxo elimina a sobrecarga da rede, pois o cliente não precisa ficar perguntando repetidamente se a temperatura mudou; ele apenas recebe os dados quando eles são gerados.

---

## 📸 Print da tela do sistema funcionando
![Print 201 Created](/.png)





<table>
<tr>
<td align="center">
<a href="https://github.com/ricardoissadesousa">
<img src="https://github.com/ricardoissadesousa.png" width="100px;" alt="Foto do Ricardo"/>
<br />
<sub>
<p><b>Ricardo Issa de Sousa</b></p>
<p>Função: Configuração Spring Boot, WebSockets (STOMP) e Front-end dinâmico</p>
</sub>
</a>
</td>
</tr>
</table>
