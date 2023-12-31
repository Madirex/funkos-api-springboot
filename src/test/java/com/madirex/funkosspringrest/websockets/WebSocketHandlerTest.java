package com.madirex.funkosspringrest.websockets;

import com.madirex.funkosspringrest.config.websockets.WebSocketHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase WebSocketHandlerTest
 */
@ExtendWith(MockitoExtension.class)
class WebSocketHandlerTest {

    @Mock
    private WebSocketSession sessionMock;

    /**
     * Test para comprobar que se establece la conexión
     *
     * @throws Exception excepción
     */
    @Test
    void testAfterConnectionEstablished() throws Exception {
        WebSocketHandler handler = new WebSocketHandler("testEntity");
        handler.afterConnectionEstablished(sessionMock);
        verify(sessionMock, times(1)).sendMessage(any(TextMessage.class));
    }

    /**
     * Test para comprobar que se cierra la conexión
     */
    @Test
    void testAfterConnectionClosed() {
        WebSocketHandler handler = new WebSocketHandler("testEntity");
        assertDoesNotThrow(() -> handler.afterConnectionClosed(sessionMock, CloseStatus.NORMAL));
    }

    /**
     * Test para comprobar que se envía un mensaje
     *
     * @throws Exception excepción
     */
    @Test
    void testSendMessage() throws Exception {
        WebSocketHandler handler = new WebSocketHandler("testEntity");
        when(sessionMock.isOpen()).thenReturn(true);
        handler.afterConnectionEstablished(sessionMock);
        handler.sendMessage("Test message");
        verify(sessionMock, times(2)).sendMessage(any(TextMessage.class));
    }

    /**
     * Test para comprobar que se envían mensajes periódicamente
     *
     * @throws Exception excepción
     */
    @Test
    void testSendPeriodicMessages() throws Exception {
        WebSocketHandler handler = new WebSocketHandler("testEntity");
        when(sessionMock.isOpen()).thenReturn(true);
        handler.afterConnectionEstablished(sessionMock);
        handler.sendPeriodicMessages();
        verify(sessionMock, atLeast(1)).sendMessage(any(TextMessage.class));
    }

    /**
     * Test para comprobar que se maneja un error de transporte
     */
    @Test
    void testHandleTransportError() {
        WebSocketHandler handler = new WebSocketHandler("testEntity");
        Throwable exception = new RuntimeException("Test error");
        handler.handleTransportError(sessionMock, exception);
        assertDoesNotThrow(() -> handler.handleTransportError(sessionMock, exception));
    }


    /**
     * Test para comprobar que se obtienen los subprotocolos
     */
    @Test
    void testGetSubProtocols() {
        WebSocketHandler handler = new WebSocketHandler("testEntity");
        assertEquals(List.of("subprotocol.demo.websocket"), handler.getSubProtocols());
    }
}