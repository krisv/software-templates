package io.quarkiverse.langchain4j.sample;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;

@ApplicationScoped
public class EmbeddingRetriever implements dev.langchain4j.retriever.Retriever<TextSegment> {

    private final EmbeddingStoreRetriever retriever;

    EmbeddingRetriever(RedisEmbeddingStore store, EmbeddingModel model) {
        retriever = EmbeddingStoreRetriever.from(store, model, 20);
    }

    @Override
    public List<TextSegment> findRelevant(String s) {
        List<TextSegment> list = retriever.findRelevant(s);
        System.out.println("Found relevant text segments: " + list.size() + " for " + s);
        // for (TextSegment t: list) {
        //     System.out.println("---");
        //     System.out.println(t);
        // }
        // System.out.println("*****");
        return retriever.findRelevant(s);
    }
}
