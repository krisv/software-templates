package io.quarkiverse.langchain4j.sample;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import org.eclipse.microprofile.context.ManagedExecutor;

import io.smallrye.mutiny.infrastructure.Infrastructure;

@ServerEndpoint("/chatbot")
public class ChatBotWebSocket {

    @Inject
    Bot bot;

    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    ChatBotMemoryBean chatMemoryBean;

    @OnOpen
    public void onOpen(Session session) {
        managedExecutor.execute(() -> {
            String response = bot.chat(session, "Hello. \n");
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
            String response = bot.chat(session, m);
            try {
                session.getBasicRemote().sendText(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
