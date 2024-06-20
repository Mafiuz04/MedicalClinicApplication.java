package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.UserMapper;
import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.model.UserDto;
import com.Mafiuz04.medicalclinic.repository.JPAUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    JPAUserRepository userRepository;
    UserMapper userMapper;
    UserService userService;

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(JPAUserRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.userService = new UserService(userRepository, userMapper);
    }

    @Test
    void changeUserPassword_UserDtoReturned() {
        //given
        MedicalUser user = createUser(1L, "marko@");
        ChangePassword changePassword = new ChangePassword("Olinko");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //when
        UserDto userDto = userService.changeUserPassword(1L, changePassword);
        //then
        Assertions.assertEquals("marko@", userDto.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void changeUserPassword_UserDOesNotExist_ThrowException(){
        Long userId = 1L;
        ChangePassword password = new ChangePassword("Sadasd");

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> userService.changeUserPassword(userId, password));

        Assertions.assertEquals("No user with given ID", exception.getMessage());
    }

    @Test
    void createUser_UserSaved_UserDtoReturned() {
        //given
        MedicalUser user = createUser(1L, "marko@");
        when(userRepository.save(user)).thenReturn(user);
        //when
        UserDto user1 = userService.createUser(user);
        //then
        Assertions.assertEquals(user.getId(), user1.getId());
        Assertions.assertEquals(user.getEmail(), user1.getEmail());
    }

    MedicalUser createUser(Long id, String email) {
        return new MedicalUser(id, "Marek", "Przemek", email, "sadasda");
    }
}
