package com.madirex.funkosspringrest.rest.entities.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.exceptions.*;
import com.madirex.funkosspringrest.rest.entities.order.models.Address;
import com.madirex.funkosspringrest.rest.entities.order.models.Client;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.models.OrderLine;
import com.madirex.funkosspringrest.rest.entities.order.services.OrderService;
import com.madirex.funkosspringrest.rest.pagination.model.PageResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Clase OrderControllerTest
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    private final String myEndpoint = "/api/orders";
    private final ObjectMapper mapper = new ObjectMapper();
    private final String productId = UUID.randomUUID().toString();
    private final Order order = Order.builder()
            .id(new ObjectId("655a54c83f761d101f941f4e"))
            .userId(1L)
            .client(
                    new Client("Madirex", "contact@madirex.com", "1234",
                            new Address("Calle", "1", "Ciudad",
                                    "Provincia", "País", "12345")
                    )
            )
            .orderLineList(List.of(OrderLine.builder()
                    .productId(productId)
                    .quantity(42)
                    .productPrice(42.42)
                    .build()))
            .quantity(42)
            .build();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    /**
     * Constructor de la clase Test
     *
     * @param orderService Servicio de pedidos
     */
    @Autowired
    public OrderControllerTest(OrderService orderService) {
        this.orderService = orderService;
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Test para comprobar que se obtienen todos los pedidos
     *
     * @throws Exception Excepción
     */
    @Test
    void getAllOrder() throws Exception {
        var orderList = List.of(order);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(orderList);
        when(orderService.findAll(pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<Order> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll("findAll",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );
        verify(orderService, times(1)).findAll(any(Pageable.class));
    }

    /**
     * Test para comprobar que se obtiene un pedido por su ID
     *
     * @throws Exception Excepción
     */
    @Test
    void getOrderById() throws Exception {
        var myLocalEndpoint = myEndpoint + "/655a54c83f761d101f941f4e";
        when(orderService.findById(any(ObjectId.class))).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        Order res = mapper.readValue(response.getContentAsString(), Order.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(order.getCreatedAt(), res.getCreatedAt()),
                () -> assertEquals(order.getQuantity(), res.getQuantity()),
                () -> assertEquals(order.getTotal(), res.getTotal())
        );
        verify(orderService, times(1)).findById(any(ObjectId.class));
    }

    /**
     * Test para comprobar que no se ha encontrado un pedido por su ID
     *
     * @throws Exception Excepción
     */
    @Test
    void getOrderByIdNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/655a54c83f761d101f941f4e";
        when(orderService.findById(any(ObjectId.class)))
                .thenThrow(new OrderNotFound("655a54c83f761d101f941f4e"));
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(orderService, times(1)).findById(any(ObjectId.class));
    }

    /**
     * Test para comprobar que se crea un pedido
     *
     * @throws Exception Excepción
     */
    @Test
    void createOrder() throws Exception {
        when(orderService.save(any(CreateOrder.class))).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();
        Order res = mapper.readValue(response.getContentAsString(), Order.class);
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(order.getCreatedAt(), res.getCreatedAt()),
                () -> assertEquals(order.getQuantity(), res.getQuantity()),
                () -> assertEquals(order.getTotal(), res.getTotal())
        );
        verify(orderService, times(1)).save(any(CreateOrder.class));
    }

    /**
     * Test para comprobar caso incorrecto al intentar crear un pedido sin items
     *
     * @throws Exception Excepción
     */
    @Test
    void createOrderNoItemsBadRequest() throws Exception {
        when(orderService.save(any(CreateOrder.class))).thenThrow(new OrderNotItems("655a54c83f761d101f941f4e"));
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(orderService).save(any(CreateOrder.class));
    }

    /**
     * Test para comprobar caso incorrecto al intentar crear un pedido con un precio incorrecto
     *
     * @throws Exception Excepción
     */
    @Test
    void createOrderProductBadPriceBadRequest() throws Exception {
        when(orderService.save(any(CreateOrder.class))).thenThrow(new ProductBadPrice(productId));
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(orderService).save(any(CreateOrder.class));
    }

    /**
     * Test para comprobar caso incorrecto al intentar crear un pedido con un producto no encontrado
     *
     * @throws Exception Excepción
     */
    @Test
    void createOrderProductNotFoundBadRequest() throws Exception {
        when(orderService.save(any(CreateOrder.class))).thenThrow(new ProductNotFound(productId));
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(orderService).save(any(CreateOrder.class));
    }

    /**
     * Test para comprobar caso incorrecto al intentar crear un pedido con un producto sin stock
     *
     * @throws Exception Excepción
     */
    @Test
    void getOrderProductWithoutStockBadRequest() throws Exception {
        when(orderService.save(any(CreateOrder.class))).thenThrow(new ProductWithoutStock(productId));
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus())
        );
        verify(orderService).save(any(CreateOrder.class));
    }

    /**
     * Test para comprobar que se actualiza un pedido
     *
     * @throws Exception Excepción
     */
    @Test
    void updateProduct() throws Exception {
        var myLocalEndpoint = myEndpoint + "/655a54c83f761d101f941f4e";
        when(orderService.update(any(ObjectId.class), any(UpdateOrder.class))).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();
        Order res = mapper.readValue(response.getContentAsString(), Order.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(order.getCreatedAt(), res.getCreatedAt()),
                () -> assertEquals(order.getQuantity(), res.getQuantity()),
                () -> assertEquals(order.getTotal(), res.getTotal())
        );
        verify(orderService, times(1)).update(any(ObjectId.class), any(UpdateOrder.class));
    }

    /**
     * Test para comprobar que no se ha encontrado un pedido al intentar actualizarlo
     *
     * @throws Exception Excepción
     */
    @Test
    void updateOrderNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/655a54c83f761d101f941f4e";
        when(orderService.update(any(ObjectId.class), any(UpdateOrder.class)))
                .thenThrow(new OrderNotFound("655a54c83f761d101f941f4e"));
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(order)))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(orderService, times(1)).update(any(ObjectId.class), any(UpdateOrder.class));
    }

    /**
     * Test de Delete Order
     *
     * @throws Exception Excepción
     */
    @Test
    void deleteOrder() throws Exception {
        var myLocalEndpoint = myEndpoint + "/655a54c83f761d101f941f4e";
        doNothing().when(orderService).delete(any(ObjectId.class));
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );
        verify(orderService, times(1)).delete(any(ObjectId.class));
    }

    /**
     * Test para comprobar que no se ha podido eliminar el Order al no haberse encontrado
     *
     * @throws Exception Excepción
     */
    @Test
    void deleteOrderNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/655a54c83f761d101f941f4e";
        doThrow(new OrderNotFound("655a54c83f761d101f941f4e")).when(orderService).delete(any(ObjectId.class));
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(404, response.getStatus())
        );
        verify(orderService, times(1)).delete(any(ObjectId.class));
    }
}