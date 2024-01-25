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
            {prompt}
            """)
    String chat(@MemoryId Object session, @UserMessage String question, String prompt);
}
