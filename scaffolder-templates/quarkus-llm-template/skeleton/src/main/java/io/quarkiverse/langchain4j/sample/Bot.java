package io.quarkiverse.langchain4j.sample;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Singleton;

@Singleton // this is singleton because WebSockets currently never closes the scope
@RegisterAiService(chatMemoryProviderSupplier = RegisterAiService.BeanChatMemoryProviderSupplier.class)
public interface Bot {

    @SystemMessage("""
            You are an AI answering questions.
            Your response must be polite, use the same language as the question, and be relevant to the question.
            When you don't know, respond that you don't know the answer and the bank will contact the customer directly.
            Introduce yourself with: Hello, I am Bob, do you have any questions?
            Give a short answer no longer than 75 characters to each question and then wait for further questions.
            """)
    String chat(@MemoryId Object session, @UserMessage String question);
}
