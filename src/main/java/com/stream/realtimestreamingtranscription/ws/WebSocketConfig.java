package com.stream.realtimestreamingtranscription.ws;

import org.springframework.context.annotation.*;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.*;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.*;
import java.util.*;

@Configuration
@EnableWebFlux
public class WebSocketConfig implements WebFluxConfigurer {

    @Bean
    public HandlerMapping handlerMapping(AudioWebSocketHandler h) {
        Map<String, Object> map = new HashMap<>();
        map.put("/ws/audio", h);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
