package com.stream.realtimestreamingtranscription.gemini;

import com.google.genai.AsyncSession;
import com.google.genai.types.LiveConnectConfig;
import org.springframework.stereotype.Service;
import com.google.genai.Client;

import java.util.concurrent.CompletableFuture;

@Service
public class GeminiSessionFactory {
    private final Client client;
    private final LiveConnectConfig config;
    public GeminiSessionFactory(Client client, LiveConnectConfig config){
        this.client=client;
        this.config=config;

    }
    public CompletableFuture<AsyncSession> createSession(){
        return client.async.live.connect("gemini-2.5-flash-native-audio-preview-12-2025",config);
    }
}
