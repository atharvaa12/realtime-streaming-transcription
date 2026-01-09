package com.stream.realtimestreamingtranscription.gemini;

import com.google.genai.AsyncSession;
import com.google.genai.types.Blob;
import com.google.genai.types.LiveSendRealtimeInputParameters;
import com.google.genai.types.LiveServerContent;
import com.google.genai.types.Transcription;
import org.springframework.core.io.buffer.DataBuffer;

import reactor.core.publisher.Flux;


import java.util.concurrent.CompletableFuture;


public class GeminiStreamer {

    public static Flux<String> bind(CompletableFuture<AsyncSession> sessionFuture,
                                    Flux<DataBuffer> audioFlux) {
        return Flux.create(sink->{
            sessionFuture.thenAccept(session->{

                audioFlux.doOnNext(dataBuffer -> {
                    byte[] bytes=new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);

                    Blob audioBlob=Blob.builder().mimeType("audio/pcm;rate=16000").data(bytes).build();
                    session.sendRealtimeInput(LiveSendRealtimeInputParameters.builder().audio(audioBlob).build());

                }).doOnTerminate(session::close).subscribe();
                session.receive(message->{
                    message.serverContent().ifPresent(content->{

                        content.inputTranscription()
                                .flatMap(Transcription::text).ifPresent(t -> sink.next("You: " + t));
                    });


                });



            });
            sink.onCancel(()->sessionFuture.thenAccept(AsyncSession::close));

        });


    }
}

