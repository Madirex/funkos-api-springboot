package com.madirex.funkosspringrest.rest.entities.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.madirex.funkosspringrest.rest.entities.order.models.Address;
import com.madirex.funkosspringrest.rest.entities.order.models.Client;
import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.models.OrderLine;
import com.madirex.funkosspringrest.rest.entities.order.services.OrderService;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserInfoResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserUpdate;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserNotFound;
import com.madirex.funkosspringrest.rest.entities.user.services.UsersService;
import com.madirex.funkosspringrest.rest.pagination.model.PageResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Class UsersRestControllerTest
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class UsersRestControllerTest {

    private final UUID uuid = UUID.randomUUID();
    private final UserRequest userRequest = UserRequest.builder()
            .name("test")
            .surname("test")
            .password("test1234")
            .username("test")
            .email("test@test.com")
            .build();
    private final UserResponse userResponse = UserResponse.builder()
            .id(UUID.randomUUID())
            .name("test")
            .surname("test")
            .username("test")
            .email("test@test.com")
            .build();
    private final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
            .id(UUID.randomUUID())
            .name("test")
            .surname("test")
            .username("test")
            .email("test@test.com")
            .build();

    private final Order order = Order.builder()
            .id(ObjectId.get())
            .userId(UUID.randomUUID().toString())
            .client(new Client("test", "test", "test",
                    new Address("test", "test", "test", "test", "test", "test")))
            .orderLineList(Collections.singletonList(new OrderLine(23,
                    UUID.randomUUID().toString(), 10.0, 230.0)))
            .quantity(1)
            .total(10.0)
            .build();

    private final String myEndpoint = "/api/users";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @MockBean
    private OrderService orderService;

    /**
     * Constructor UsersRestControllerTest
     *
     * @param UsersService UsersService
     */
    @Autowired
    public UsersRestControllerTest(UsersService UsersService) {
        this.usersService = UsersService;
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Test NotAuthenticated
     *
     * @throws Exception Exception
     */
    @Test
    @WithAnonymousUser
    void testNotAuthenticated() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(403, response.getStatus());
    }

    /**
     * Test findAll
     *
     * @throws Exception Exception
     */
    @Test
    void testFindAll() throws Exception {
        var list = List.of(userResponse);
        Page<UserResponse> page = new PageImpl<>(list);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(usersService.findAll(Optional.empty(), Optional.empty(),
                Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        PageResponse<UserResponse> res = mapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertAll("findAllUsers",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.content().size())
        );
        verify(usersService, times(1)).findAll(Optional.empty(),
                Optional.empty(), Optional.empty(), pageable);
    }

    /**
     * Test findAll with parameters
     *
     * @throws Exception Exception
     */
    @Test
    void testFindById() throws Exception {
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        when(usersService.findById(any())).thenReturn(userInfoResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        var res = mapper.readValue(response.getContentAsString(), UserInfoResponse.class);
        assertAll("findById",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse.getEmail(), res.getEmail()),
                () -> assertEquals(userResponse.getUsername(), res.getUsername()),
                () -> assertEquals(userResponse.getName(), res.getName()),
                () -> assertEquals(userResponse.getSurname(), res.getSurname())
        );
        verify(usersService, times(1)).findById(any());
    }

    /**
     * Test findByIdNotFound
     *
     * @throws Exception Exception
     */
    @Test
    void testFindByIdNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        when(usersService.findById(any())).thenThrow(new UserNotFound("No existe el usuario"));
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());
        verify(usersService, times(1)).findById(any());
    }

    /**
     * Test createUser
     *
     * @throws Exception Exception
     */
    @Test
    void testCreateUser() throws Exception {
        when(usersService.save(any(UserRequest.class))).thenReturn(userResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();
        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(userResponse.getEmail(), res.getEmail()),
                () -> assertEquals(userResponse.getUsername(), res.getUsername()),
                () -> assertEquals(userResponse.getName(), res.getName()),
                () -> assertEquals(userResponse.getSurname(), res.getSurname()),
                () -> assertEquals(userResponse.getId(), res.getId())
        );
        verify(usersService, times(1)).save(any(UserRequest.class));
    }

    /**
     * Test createUserBadRequest Password menor a 5 caracteres
     *
     * @throws Exception exception
     */
    @Test
    void testCreateUserBadRequestPasswordLowerFiveChars() throws Exception {
        var userRequest = UserRequest.builder()
                .name("test")
                .surname("test")
                .password("test")
                .username("test")
                .email("test@test.com")
                .build();
        when(usersService.save(any(UserRequest.class))).thenReturn(userResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();
        assertEquals(400, response.getStatus());
        verify(usersService, times(0)).save(any(UserRequest.class));
    }

    /**
     * Test createUserBadRequest en blanco salvo password y username
     *
     * @throws Exception exception
     */
    @Test
    void testCreateUserBadRequest_whenNameSurnameAndEmailIsEmpty() throws Exception {
        var userRequest = UserRequest.builder()
                .name("")
                .surname("")
                .password("test1234")
                .username("test")
                .email("")
                .build();
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Name no puede estar")),
                () -> assertTrue(response.getContentAsString().contains("Surname no puede estar"))
        );
    }


    /**
     * Test Update User
     *
     * @throws Exception exception
     */
    @Test
    void testUpdateUser() throws Exception {
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        when(usersService.update(any(), any(UserUpdate.class))).thenReturn(userResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();
        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse.getEmail(), res.getEmail()),
                () -> assertEquals(userResponse.getUsername(), res.getUsername()),
                () -> assertEquals(userResponse.getName(), res.getName()),
                () -> assertEquals(userResponse.getSurname(), res.getSurname()),
                () -> assertEquals(userResponse.getId(), res.getId())
        );
        verify(usersService, times(1)).update(any(), any(UserUpdate.class));
    }

    /**
     * Test Update User Not Found
     *
     * @throws Exception exception
     */
    @Test
    void testUpdateUserNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        when(usersService.update(any(), any(UserUpdate.class))).thenThrow(new UserNotFound("No existe el usuario"));
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());
        verify(usersService, times(1)).update(any(), any(UserUpdate.class));
    }

    /**
     * Test Delete User
     *
     * @throws Exception exception
     */
    @Test
    void testDeleteUser() throws Exception {
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        doNothing().when(usersService).deleteById(any());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(204, response.getStatus());
        verify(usersService, times(1)).deleteById(any());
    }

    /**
     * Test Delete User Not Found
     *
     * @throws Exception exception
     */
    @Test
    void testDeleteUserNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/" + uuid;
        doThrow(new UserNotFound("No existe el usuario")).when(usersService).deleteById(any());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());
        verify(usersService, times(1)).deleteById(any());
    }

    /**
     * Test me
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testMe() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/profile";
        when(usersService.findById(any())).thenReturn(userInfoResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test Update Me
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testUpdateMe() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/profile";
        when(usersService.update(any(), any())).thenReturn(userResponse);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Madirex",
                                            "surname": "Madirex",
                                            "username": "Madiland",
                                            "email": "contact2@madirex.com",
                                            "password": "4'9fj04g03h0asdasd",
                                            "roles": [0,1],
                                            "isDeleted": 0
                                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test Delete Me
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testDeleteMe() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/profile";
        doNothing().when(usersService).deleteById(any());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(204, response.getStatus());
    }

    /**
     * Test me AnonymousUser
     *
     * @throws Exception exception
     */
    @Test
    @WithAnonymousUser
    void testMeAnonymousUser() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/profile";
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(403, response.getStatus());
    }

    /**
     * Test me Get Oder By User
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testGetOrderByUser() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/orders";
        var list = List.of(order);
        Page<Order> order = new PageImpl<>(list);
        when(orderService.findByUserId(any(), any())).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test me Get Oder By User
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testGetOrder() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/orders/{id}";
        when(orderService.findById(any())).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint, "6561d787fa4e216b1c4ded1f")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test me Save Order
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testSaveOrder() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/orders";
        when(orderService.save(any())).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "userId":"a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b",
                                            "client":{
                                                "phone": 234324,
                                                "address": {
                                                    "country": -234,
                                                    "province": -23,
                                                    "cp": "45345",
                                                    "street": -23,
                                                    "city": -23,
                                                    "number": "dfgdfgfdg-0"
                                                },
                                                "email": "client@madirex.com",
                                                "fullName": -342
                                            },
                                            "orderLineList":[
                                                {
                                                    "productId": "8919f2e4-193b-4928-b4d4-5e5fb88653e7",
                                                    "productPrice": 29.99,
                                                    "quantity": 1
                                                }
                                            ]
                                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
    }

    /**
     * Test me Update Order
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testUpdateOrder() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/orders/{id}";
        when(orderService.update(any(), any())).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint, "6561d787fa4e216b1c4ded1f")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "userId":"a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b",
                                            "client":{
                                                "phone": 234324,
                                                "address": {
                                                    "country": -234,
                                                    "province": -23,
                                                    "cp": "45345",
                                                    "street": -23,
                                                    "city": -23,
                                                    "number": "dfgdfgfdg-0"
                                                },
                                                "email": "client@madirex.com",
                                                "fullName": -342
                                            },
                                            "orderLineList":[
                                                {
                                                    "productId": "8919f2e4-193b-4928-b4d4-5e5fb88653e7",
                                                    "productPrice": 29.99,
                                                    "quantity": 1
                                                }
                                            ]
                                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test me Save Order (USER DIFFERS)
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testSaveOrderUserDiffers() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/orders";
        when(orderService.save(any())).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "userId":"d4515840-8fe2-4107-8bde-0f1c545ef4c5",
                                            "client":{
                                                "phone": 234324,
                                                "address": {
                                                    "country": -234,
                                                    "province": -23,
                                                    "cp": "45345",
                                                    "street": -23,
                                                    "city": -23,
                                                    "number": "dfgdfgfdg-0"
                                                },
                                                "email": "client@madirex.com",
                                                "fullName": -342
                                            },
                                            "orderLineList":[
                                                {
                                                    "productId": "8919f2e4-193b-4928-b4d4-5e5fb88653e7",
                                                    "productPrice": 29.99,
                                                    "quantity": 1
                                                }
                                            ]
                                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Test me Update Order (USER DIFFERS)
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testUpdateOrderUserDiffers() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/orders/{id}";
        when(orderService.update(any(), any())).thenReturn(order);
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint, "6561d787fa4e216b1c4ded1f")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "userId":"d4515840-8fe2-4107-8bde-0f1c545ef4c5",
                                            "client":{
                                                "phone": 234324,
                                                "address": {
                                                    "country": -234,
                                                    "province": -23,
                                                    "cp": "45345",
                                                    "street": -23,
                                                    "city": -23,
                                                    "number": "dfgdfgfdg-0"
                                                },
                                                "email": "client@madirex.com",
                                                "fullName": -342
                                            },
                                            "orderLineList":[
                                                {
                                                    "productId": "8919f2e4-193b-4928-b4d4-5e5fb88653e7",
                                                    "productPrice": 29.99,
                                                    "quantity": 1
                                                }
                                            ]
                                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Test Delete Order
     *
     * @throws Exception exception
     */
    @Test
    @WithUserDetails("Madirex")
    void testDeleteOrder() throws Exception {
        var myLocalEndpoint = myEndpoint + "/me/orders/{id}";
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint, "6561d787fa4e216b1c4ded1f")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertEquals(204, response.getStatus());
    }
}