package com.madirex.funkosspringrest.rest.entities.order;

import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import com.madirex.funkosspringrest.rest.entities.funko.models.Funko;
import com.madirex.funkosspringrest.rest.entities.funko.repository.FunkoRepository;
import com.madirex.funkosspringrest.rest.entities.order.dto.CreateOrder;
import com.madirex.funkosspringrest.rest.entities.order.dto.UpdateOrder;
import com.madirex.funkosspringrest.rest.entities.order.exceptions.OrderNotFound;
import com.madirex.funkosspringrest.rest.entities.order.exceptions.ProductBadPrice;
import com.madirex.funkosspringrest.rest.entities.order.exceptions.ProductNotFound;
import com.madirex.funkosspringrest.rest.entities.order.exceptions.ProductWithoutStock;
import com.madirex.funkosspringrest.rest.entities.order.mappers.OrderMapper;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.models.OrderLine;
import com.madirex.funkosspringrest.rest.entities.order.repository.OrderRepository;
import com.madirex.funkosspringrest.rest.entities.order.services.OrderServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase OrderServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private FunkoRepository funkoRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    /**
     * Test para comprobar que se obtienen todos los pedidos
     */
    @Test
    void testFindAllReturnsPageOfOrder() {
        List<Order> order = List.of(new Order(), new Order());
        Page<Order> expectedPage = new PageImpl<>(order);
        Pageable pageable = PageRequest.of(0, 10);
        when(orderRepository.findAll(pageable)).thenReturn(expectedPage);
        Page<Order> result = orderService.findAll(pageable);
        assertAll(
                () -> assertEquals(expectedPage, result),
                () -> assertEquals(expectedPage.getContent(), result.getContent()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements())
        );
        verify(orderRepository, times(1)).findAll(pageable);
    }

    /**
     * Test para comprobar que se obtiene un pedido por su ID
     */
    @Test
    void testFindById() {
        ObjectId idOrder = new ObjectId();
        Order expectedOrder = new Order();
        when(orderRepository.findById(idOrder)).thenReturn(Optional.of(expectedOrder));
        Order resultOrder = orderService.findById(idOrder);
        assertEquals(expectedOrder, resultOrder);
        verify(orderRepository).findById(idOrder);
    }

    /**
     * Test para comprobar caso incorrecto al obtener un pedido por su ID
     */
    @Test
    void testFindByIdThrowsOrderNotFound() {
        ObjectId idOrder = new ObjectId();
        when(orderRepository.findById(idOrder)).thenReturn(Optional.empty());
        assertThrows(OrderNotFound.class, () -> orderService.findById(idOrder));
        verify(orderRepository).findById(idOrder);
    }

    /**
     * Test para comprobar que se guarda un pedido
     */
    @Test
    void testSave() {
        UUID id = UUID.randomUUID();
        Funko funko = Funko.builder()
                .id(id)
                .name("nombre")
                .price(10.0)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("DISNEY").active(true)
                        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(2)
                .productPrice(10.0)
                .build();

        var order = CreateOrder.builder().orderLineList(List.of(orderLine)).build();
        Order orderToSave = new Order();
        orderToSave.setOrderLineList(List.of(orderLine));

        when(orderRepository.save(any(Order.class))).thenReturn(orderToSave);
        when(funkoRepository.findById(any())).thenReturn(Optional.of(funko));
        when(orderMapper.createOrderToOrder(any(CreateOrder.class))).thenReturn(orderToSave);

        Order resultOrder = orderService.save(order);
        assertAll(
                () -> assertEquals(orderToSave, resultOrder),
                () -> assertEquals(orderToSave.getOrderLineList(), resultOrder.getOrderLineList()),
                () -> assertEquals(orderToSave.getOrderLineList().size(), resultOrder.getOrderLineList().size())
        );
        verify(orderRepository).save(any(Order.class));
        verify(funkoRepository, times(2)).findById(any());
    }

    /**
     * Test para comprobar la eliminación de un pedido
     */
    @Test
    void testDelete() {
        ObjectId idOrder = new ObjectId();
        Order orderToDelete = new Order();
        when(orderRepository.findById(idOrder)).thenReturn(Optional.of(orderToDelete));
        orderService.delete(idOrder);
        verify(orderRepository).findById(idOrder);
        verify(orderRepository).deleteById(idOrder);
    }

    /**
     * Test para comprobar caso incorrecto al eliminar un pedido (OrderNotFound)
     */
    @Test
    void testDeleteThrowsOrderNotFound() {
        ObjectId idOrder = new ObjectId();
        when(orderRepository.findById(idOrder)).thenReturn(Optional.empty());
        assertThrows(OrderNotFound.class, () -> orderService.delete(idOrder));
        verify(orderRepository).findById(idOrder);
        verify(orderRepository, never()).deleteById(idOrder);
    }

    /**
     * Test para comprobar la actualización de un pedido
     */
    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Funko funko = Funko.builder()
                .id(id)
                .name("nombre")
                .price(10.0)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("DISNEY").active(true)
                        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(2)
                .productPrice(10.0)
                .build();

        ObjectId idOrder = new ObjectId();
        UpdateOrder order = UpdateOrder.builder().build();
        order.setOrderLineList(List.of(orderLine));
        Order orderToUpdate = Order.builder().orderLineList(List.of(orderLine)).build();
        orderToUpdate.setOrderLineList(List.of(orderLine));

        when(orderRepository.findById(idOrder)).thenReturn(Optional.of(orderToUpdate));
        when(orderRepository.save(any(Order.class))).thenReturn(orderToUpdate);
        when(funkoRepository.findById(any())).thenReturn(Optional.of(funko));
        when(orderMapper.updateOrderToOrder(any(Order.class), any(UpdateOrder.class))).thenReturn(orderToUpdate);

        Order resultOrder = orderService.update(idOrder, order);

        assertAll(
                () -> assertEquals(orderToUpdate, resultOrder),
                () -> assertEquals(orderToUpdate.getOrderLineList(), resultOrder.getOrderLineList()),
                () -> assertEquals(orderToUpdate.getOrderLineList().size(), resultOrder.getOrderLineList().size())
        );

        verify(orderRepository).findById(idOrder);
        verify(orderRepository).save(any(Order.class));
        verify(funkoRepository, times(3)).findById(any());
    }

    /**
     * Test para comprobar caso incorrecto al actualizar un pedido (OrderNotFound)
     */
    @Test
    void testUpdateThrowsOrderNotFound() {
        ObjectId idOrder = new ObjectId();
        UpdateOrder order = UpdateOrder.builder().build();
        when(orderRepository.findById(idOrder)).thenReturn(Optional.empty());

        assertThrows(OrderNotFound.class, () -> orderService.update(idOrder, order));

        verify(orderRepository).findById(idOrder);
        verify(orderRepository, never()).save(any(Order.class));
        verify(funkoRepository, never()).findById(any());
    }

    /**
     * Test para comprobar que se reserva el stock de un pedido
     *
     * @throws OrderNotFound   excepción
     * @throws ProductNotFound excepción
     * @throws ProductBadPrice excepción
     */
    @Test
    void testReserveStockOrder() throws OrderNotFound, ProductNotFound, ProductBadPrice {
        UUID id = UUID.randomUUID();
        Funko funko = Funko.builder()
                .id(id)
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("DISNEY").active(true)
                        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(2)
                .productPrice(10.0)
                .build();

        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine);
        order.setOrderLineList(orderLines);

        when(funkoRepository.findById(id)).thenReturn(Optional.of(funko));

        Order result = orderService.reserveStockOrder(order);

        assertAll(
                () -> assertEquals(0, funko.getQuantity()),
                () -> assertEquals(20.0, orderLine.getTotal()),
                () -> assertEquals(20.0, result.getTotal())
        );

        verify(funkoRepository, times(1)).findById(id);
        verify(funkoRepository, times(1)).save(funko);
    }

    /**
     * Test para comprobar StockOrder y que se actualiza el stock de un pedido
     */
    @Test
    void testReturnStockOrderShouldReturnOrderWithUpdatedStock() {
        UUID id = UUID.randomUUID();
        Funko funko = Funko.builder()
                .id(id)
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("DISNEY").active(true)
                        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(2)
                .productPrice(2.2)
                .build();

        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<>();

        orderLines.add(orderLine);
        order.setOrderLineList(orderLines);

        when(funkoRepository.findById(id)).thenReturn(Optional.of(funko));

        Order result = orderService.checkValidOrder(order);

        assertEquals(2, funko.getQuantity());
        assertEquals(order, result);

        verify(funkoRepository, times(1)).findById(id);
    }

    /**
     * Test para comprobar checkValidOrder y que se comprueba que el pedido es válido
     * Producto existe y tiene stock
     */
    @Test
    void testCheckOrderProductExistsAndHaveStock() {
        UUID id = UUID.randomUUID();
        Funko funko = Funko.builder()
                .id(id)
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("DISNEY").active(true)
                        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(2)
                .productPrice(2.2)
                .build();

        Order order = new Order();
        List<OrderLine> lineasOrder = new ArrayList<>();
        lineasOrder.add(orderLine);
        order.setOrderLineList(lineasOrder);

        when(funkoRepository.findById(id)).thenReturn(Optional.of(funko));

        assertDoesNotThrow(() -> orderService.checkValidOrder(order));
        verify(funkoRepository, times(1)).findById(id);
    }

    /**
     * Comprueba que el pedido no se ha encontrado
     */
    @Test
    void testCheckOrderProductNotFound() {
        UUID id = UUID.randomUUID();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(2)
                .productPrice(10.0)
                .build();
        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine);
        order.setOrderLineList(orderLines);

        when(funkoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFound.class, () -> orderService.checkValidOrder(order));
        verify(funkoRepository, times(1)).findById(id);
    }

    /**
     * Comprueba que el pedido no tiene stock
     */
    @Test
    void testCheckOrderProductEnoughStock() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<>();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(1)
                .productPrice(2.2)
                .build();
        Funko funko = Funko.builder()
                .id(id)
                .name("nombre")
                .price(2.2)
                .quantity(0)
                .image("imagen")
                .category(Category.builder().id(1L).type("DISNEY").active(true)
                        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        orderLines.add(orderLine);
        order.setOrderLineList(orderLines);

        when(funkoRepository.findById(id)).thenReturn(Optional.of(funko));
        assertThrows(ProductWithoutStock.class, () -> orderService.checkValidOrder(order));
        verify(funkoRepository, times(1)).findById(id);
    }

    /**
     * Comprueba que el precio del producto es incorrecto (difiere)
     */
    @Test
    void checkOrderBadProductPrice() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        OrderLine orderLine = OrderLine.builder()
                .productId(id.toString())
                .quantity(2)
                .productPrice(10.0)
                .build();
        Funko funko = Funko.builder()
                .id(id)
                .name("nombre")
                .price(2.2)
                .quantity(2)
                .image("imagen")
                .category(Category.builder().id(1L).type("DISNEY").active(true)
                        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine);
        order.setOrderLineList(orderLines);

        when(funkoRepository.findById(id)).thenReturn(Optional.of(funko));

        assertThrows(ProductBadPrice.class, () -> orderService.checkValidOrder(order));
        verify(funkoRepository, times(1)).findById(id);
    }

    /**
     * Test para comprobar que se obtienen los pedidos de un usuario por su ID
     */
    @Test
    void testFindByUserIdReturnsPageOfOrder() {
        String userId = "exampleUserId";
        List<Order> userOrders = List.of(new Order(), new Order());
        Page<Order> expectedPage = new PageImpl<>(userOrders);
        Pageable pageable = PageRequest.of(0, 10);
        when(orderRepository.findOrdersByUserId(userId)).thenReturn(userOrders);
        Page<Order> result = orderService.findByUserId(userId, pageable);
        assertAll(
                () -> assertEquals(expectedPage.getContent(), result.getContent()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements())
        );
        verify(orderRepository, times(1)).findOrdersByUserId(userId);
    }
}