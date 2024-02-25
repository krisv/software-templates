package io.quarkiverse.langchain4j.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import org.eclipse.microprofile.context.ManagedExecutor;

@ServerEndpoint("/chatbot")
public class ChatBotWebSocket {

    private static String PROMPT;
    
    static {
        String company_prompt = readFromInputStream(ChatBotWebSocket.class.getResourceAsStream("/prompts/company_prompt.md"));
        String application_prompt = readFromInputStream(ChatBotWebSocket.class.getResourceAsStream("/prompts/application_prompt.md"));
        PROMPT = company_prompt + "\n" + application_prompt;
    }

    @Inject
    Bot bot;

    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    ChatBotMemoryBean chatMemoryBean;

    @OnOpen
    public void onOpen(Session session) {
        managedExecutor.execute(() -> {
            String response = bot.chat(session, "Hello. \n", PROMPT);
            try {
                session.getBasicRemote().sendText(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @OnClose
    void onClose(Session session) {
        chatMemoryBean.clear(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        managedExecutor.execute(() -> {
            String m = message.replace("<p>", "\n");
            m = m.replace("</p>", "\n");
            String response = bot.chat(session, m, PROMPT);
            try {
                session.getBasicRemote().sendText(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private static String readFromInputStream(InputStream inputStream) {
        try {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            return resultStringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
