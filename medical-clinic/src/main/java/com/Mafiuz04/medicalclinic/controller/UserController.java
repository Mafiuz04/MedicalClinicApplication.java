package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.model.UserDto;
import com.Mafiuz04.medicalclinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @PostMapping
    public UserDto addUser(@RequestBody MedicalUser user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUserPassword(@PathVariable Long id, @RequestBody ChangePassword newPassword) {
        return userService.changeUserPassword(id, newPassword);
    }
}
