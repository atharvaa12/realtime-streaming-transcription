package com.stream.realtimestreamingtranscription.ws;


import com.google.genai.AsyncSession;
import com.stream.realtimestreamingtranscription.gemini.GeminiSessionFactory;
import com.stream.realtimestreamingtranscription.gemini.GeminiStreamer;
import io.netty.util.concurrent.CompleteFuture;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class AudioWebSocketHandler implements WebSocketHandler {

    private final GeminiSessionFactory geminiSessionFactory;
    public AudioWebSocketHandler(GeminiSessionFactory factory){
        this.geminiSessionFactory=factory;
    }
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Flux<DataBuffer> audio=session.receive().map(WebSocketMessage::getPayload);

       Flux<String> Transcript=GeminiStreamer.bind(geminiSessionFactory.createSession(),audio);

        return session.send(Transcript.map(session::textMessage));
    }
}
