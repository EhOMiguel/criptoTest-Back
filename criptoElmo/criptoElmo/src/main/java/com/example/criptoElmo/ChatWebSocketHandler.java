package com.example.criptoElmo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {// Lógica para processar a mensagem recebida
        
        System.out.println("Mensagem recebida: " + message.getPayload());
        String msgContent = message.getPayload();
        
        for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
            WebSocketSession otherSession = entry.getValue();
            if (!otherSession.getId().equals(session.getId())) { // Verifica se não é o remetente.
                otherSession.sendMessage(new TextMessage(msgContent));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Nova conexão WebSocket: " + session.getId());
        // Exemplo de como obter os dados e enviá-los como uma mensagem
        userSessions.put(session.getId(), session);
        sendCurrentState(session);
    }

    @Scheduled(fixedDelay = 600000) // 600000 milissegundos = 10 minutos
    public void updateAndNotify() {
        if (PrimoInteiroState.isUpdateRequired()) {
            
            StringBuffer responseBuffer = ApiClient.getPrimoInt();
            System.out.println(responseBuffer);
            
            String responseJson = responseBuffer.toString();
            PrimoInteiroState.updateJson(responseJson);
            userSessions.values().forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(responseJson)); 
                } catch (Exception e) {
                    System.err.println("Erro ao enviar a mensagem: " + e.getMessage());
                }
            });
        }
    }

    private void sendCurrentState(WebSocketSession session) throws Exception {
        System.out.println(PrimoInteiroState.getCurrentStateJson());
        session.sendMessage(new TextMessage(PrimoInteiroState.getCurrentStateJson()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove a sessão do mapa ao fechar a conexão.
        userSessions.remove(session.getId());
    }
}
