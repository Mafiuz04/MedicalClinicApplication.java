package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.model.UserDto;
import com.Mafiuz04.medicalclinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users existing in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All available users listed below",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))})
    })
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Add user to app.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))})
    })
    @PostMapping
    public UserDto addUser(@RequestBody MedicalUser user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Update user password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User password changed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "400", description = "No user with given ID.",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public UserDto updateUserPassword(@PathVariable Long id, @RequestBody ChangePassword newPassword) {
        return userService.changeUserPassword(id, newPassword);
    }
}
