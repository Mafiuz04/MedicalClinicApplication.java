package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.UserMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPAUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JPAUserRepository userRepository;
    private final UserMapper userMapper;

    //TC1:W przypadku gdy user o danym ID istnieje,
    @Transactional
    public UserDto changeUserPassword(Long id, ChangePassword newPassword) {
        MedicalUser user = userRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("No user with given ID", HttpStatus.BAD_REQUEST));
        user.setPassword(newPassword.getPassword());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto createUser(MedicalUser user) {
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public List<UserDto> getUsers(){
        List<MedicalUser> all = userRepository.findAll();
       return userMapper.listToDto(all);
    }
}
