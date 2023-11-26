package com.madirex.funkosspringrest.rest.entities.users;

import com.madirex.funkosspringrest.rest.entities.order.repository.OrderRepository;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserInfoResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserUpdate;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserNotFound;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UsernameOrEmailExists;
import com.madirex.funkosspringrest.rest.entities.user.mappers.UsersMapper;
import com.madirex.funkosspringrest.rest.entities.user.models.User;
import com.madirex.funkosspringrest.rest.entities.user.repository.UsersRepository;
import com.madirex.funkosspringrest.rest.entities.user.services.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Class UsersServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    private final UserUpdate userUpdate = UserUpdate.builder().email("test@test.com")
            .name("test").surname("test2").password("tesstt2").build();
    private final UserRequest userRequest = UserRequest.builder().email("test@test.com").build();
    private final User user = User.builder().id(UUID.randomUUID()).username("test").email("test@test.com").build();
    private final UserResponse userResponse = UserResponse.builder().username("test").email("test@test.com").build();
    private final UserInfoResponse userIResponse = UserInfoResponse.builder().username("test")
            .email("test@test.com").build();
    private final UUID uuid = UUID.randomUUID();
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UsersMapper usersMapper;
    @InjectMocks
    private UsersServiceImpl usersService;


    /**
     * Test findAll no filters returns page of users.
     */
    @Test
    void testFindAll_NoFilters_ReturnsPageOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        Page<User> page = new PageImpl<>(users);
        Specification<User> anySpecification = any();
        when(usersRepository.findAll(anySpecification, any(Pageable.class))).thenReturn(page);
        when(usersMapper.toUserResponse(any(User.class))).thenReturn(new UserResponse());
        Page<UserResponse> result = usersService.findAll(Optional.empty(), Optional.empty(),
                Optional.empty(), Pageable.unpaged());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements())
        );
    }

    /**
     * Test findById valid id returns user info response.
     */
    @Test
    void testFindById() {
        when(usersRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(orderRepository.findOrdersByUserId(uuid.toString())).thenReturn(List.of());
        when(usersMapper.toUserInfoResponse(any(User.class), anyList())).thenReturn(userIResponse);
        UserInfoResponse result = usersService.findById(uuid.toString());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponse.getUsername(), result.getUsername()),
                () -> assertEquals(userResponse.getEmail(), result.getEmail()),
                () -> assertEquals(userUpdate.getName(), result.getName()),
                () -> assertEquals(userUpdate.getSurname(), result.getSurname())
        );
        verify(usersRepository, times(1)).findById(uuid);
        verify(orderRepository, times(1)).findOrdersByUserId(uuid.toString());
        verify(usersMapper, times(1)).toUserInfoResponse(user, List.of());
    }

    /**
     * Test findById user not found throws user not found.
     */
    @Test
    void testFindById_UserNotFound_ThrowsUserNotFound() {
        var id = uuid.toString();
        when(usersRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(UserNotFound.class, () -> usersService.findById(id));
        verify(usersRepository, times(1)).findById(uuid);
    }

    /**
     * Test save valid user request returns user response.
     */
    @Test
    void testSave_ValidUserRequest_ReturnsUserResponse() {
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(),
                userRequest.getEmail()))
                .thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);
        UserResponse result = usersService.save(userRequest);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getEmail(), result.getEmail()),
                () -> assertEquals(userUpdate.getName(), result.getName()),
                () -> assertEquals(userUpdate.getSurname(), result.getSurname())
        );
        verify(usersRepository, times(1))
                .findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(),
                        userRequest.getEmail());
        verify(usersMapper, times(1)).toUser(userRequest);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);

    }

    /**
     * Test save duplicate username or email throws username or email exists.
     */
//    @Test
//    void testSave_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
//        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(),
//                userRequest.getEmail())).thenReturn(Optional.of(new User()));
//        assertThrows(UsernameOrEmailExists.class, () -> usersService.save(userRequest));
//    }

    /**
     * Test update valid user request returns user response.
     */
    @Test
    void testUpdate_ValidUserRequest_ReturnsUserResponse() {
        String id = uuid.toString();
        when(usersRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersMapper.toUser(userUpdate, uuid, user.getUsername())).thenReturn(user);
        when(usersRepository.save(user)).thenReturn(user);
        UserResponse result = usersService.update(id, userUpdate);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userUpdate.getEmail(), result.getEmail()),
                () -> assertEquals(userUpdate.getName(), result.getName()),
                () -> assertEquals(userUpdate.getSurname(), result.getSurname())
        );
        verify(usersRepository, times(1)).findById(uuid);
        verify(usersMapper, times(1)).toUser(userUpdate, uuid, user.getUsername());
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);
    }

    /**
     * Test update duplicate username or email throws username or email exists.
     */
//    @Test
//    void testUpdate_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
//        var id = uuid.toString();
//        when(usersRepository.findById(uuid)).thenReturn(Optional.of(user));
//        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(),
//                userRequest.getEmail()))
//                .thenReturn(Optional.of(user));
//        assertThrows(UsernameOrEmailExists.class, () -> usersService.update(id, userUpdate));
//    }

    /**
     * Test update user not found throws user not found.
     */
    @Test
    void testUpdate_UserNotFound_ThrowsUserNotFound() {
        when(usersRepository.findById(uuid)).thenReturn(Optional.empty());
        var id = uuid.toString();
        assertThrows(UserNotFound.class, () -> usersService.update(id, userUpdate));
    }

    /**
     * Test deleteById physical delete.
     */
    @Test
    void testDeleteById_PhysicalDelete() {
        when(usersRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(orderRepository.existsByUserId(uuid.toString())).thenReturn(false);
        usersService.deleteById(uuid.toString());
        verify(usersRepository, times(1)).delete(user);
        verify(orderRepository, times(1)).existsByUserId(uuid.toString());
    }

    /**
     * Test deleteById logical delete.
     */
    @Test
    void testDeleteById_LogicalDelete() {
        when(usersRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(orderRepository.existsByUserId(uuid.toString())).thenReturn(true);
        doNothing().when(usersRepository).updateIsDeletedToTrueById(uuid);
        usersService.deleteById(uuid.toString());
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(uuid);
        verify(orderRepository, times(1)).existsByUserId(uuid.toString());
    }

    /**
     * Test deleteById not exists.
     */
    @Test
    void testDeleteByIdNotExists() {
        User user = new User();
        when(usersRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(orderRepository.existsByUserId(uuid.toString())).thenReturn(true);
        usersService.deleteById(uuid.toString());
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(uuid);
        verify(orderRepository, times(1)).existsByUserId(uuid.toString());
    }
}