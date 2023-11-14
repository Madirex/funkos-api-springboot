package com.madirex.funkosspringrest.config.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Class WebSocketConfig
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * Método registerWebSocketHandlers
     *
     * @param registry WebSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/" + "api" + "/funkos");
    }

    /**
     * Método webSocketHandler
     *
     * @return WebSocketHandler
     */
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler("Funkos");
    }

}