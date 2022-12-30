package com.cvetkov.fedor.laboratoryworkmicro.users.controller;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.UserAndAudioRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.UserRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.UserResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.UserUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Page<UserResponse> getAllUsers(@PageableDefault(size = 5) Pageable pageable) {
        return userService.getAllPage(pageable);
    }

    @GetMapping("/all-user")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllList();
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    // @PreAuthorize("hasAuthority('ADMIN')")
    public void addUser(@Valid @RequestBody UserRequest userRequest) {
        userService.save(userRequest);
    }

    @PutMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void updateUser(@Valid @RequestBody UserUpdate userUpdate) {
        userService.update(userUpdate);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PostMapping("/add-audio")
    // @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void addAudiosForUser(@Valid @RequestBody UserAndAudioRequest request) {
        userService.addAudiosByIdForUser(request.getUserId(), request.getAudiosId());
    }
}
