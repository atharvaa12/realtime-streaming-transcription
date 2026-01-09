package com.stream.realtimestreamingtranscription.gemini;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.LiveConnectConfig;
import com.google.genai.types.Modality;
import com.google.genai.types.Part;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
public class GeminiConfig {
    @Value("${gemini.api-key}")
    private String apiKey;
     @Bean
     Client getGeminiClient(){
        return  Client.builder().apiKey(apiKey).build();



    }
    @Bean
    LiveConnectConfig getGeminiConfig(){
        Content systemRule= Content.builder().parts(List.of(Part.fromText("You are a Audio Transcriber."))
        ).build();
        return LiveConnectConfig.builder()
                .responseModalities(List.of(new Modality(Modality.Known.AUDIO)))
                .speechConfig(com.google.genai.types.SpeechConfig.builder()
                        .voiceConfig(com.google.genai.types.VoiceConfig.builder()
                                .prebuiltVoiceConfig(com.google.genai.types.PrebuiltVoiceConfig.builder()
                                        .voiceName("Puck")
                                        .build())
                                .build())
                        .build())

                .inputAudioTranscription(com.google.genai.types.AudioTranscriptionConfig.builder().build())
               // .outputAudioTranscription(com.google.genai.types.AudioTranscriptionConfig.builder().build())
                .build();

    }
}






