package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.dto.Item;
import com.bubusyaka.demo.model.dto.UserDTO;
import com.bubusyaka.demo.model.entity.UserEntity;
import com.bubusyaka.demo.repository.jpa.UserRepository;
import com.bubusyaka.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Shows info about users")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @Operation(summary = "Shows info about all users")
    public List<UserDTO> allItems() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.allUsers();
    }
}
