package com.cvetkov.fedor.laboratoryworkmicro.users.controller;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.User;
import com.cvetkov.fedor.laboratoryworkmicro.users.dtoKecloak.RequestUserKeycloak;
import com.cvetkov.fedor.laboratoryworkmicro.users.keycloak.KeycloakUtils;
import com.cvetkov.fedor.laboratoryworkmicro.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
public class AdminController {

    public static final String ID_COLUMN = "id";
    public static final int CONFLICT = 409;
    public static final String USER_ROLE_NAME = "user";
    private final UserService userService;
    private final KeycloakUtils keycloakUtils;

    @PostMapping("/add-user")
    public ResponseEntity addUser(@RequestBody RequestUserKeycloak user) {

        Response response = keycloakUtils.createKeycloakUser(user);
        if (response.getStatus() == CONFLICT) {
            return new ResponseEntity("user with email: " + user.getEmail() + " already exist", HttpStatus.CONFLICT);
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        System.out.println("User id = " + userId);

        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add(USER_ROLE_NAME);
        defaultRoles.add("admin");

        keycloakUtils.addRoles(userId, defaultRoles);

        return ResponseEntity.status(response.getStatus()).build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity deleteUserById(@PathVariable String userId) {

        keycloakUtils.deleteKeycloakUser(userId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/find-by-id/{userId}")
    public ResponseEntity<UserRepresentation> findUserById(@PathVariable String userId) {

        return ResponseEntity.ok(keycloakUtils.findUserById(userId));
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<UserRepresentation>> search(@PathVariable String search) {

        return ResponseEntity.ok(keycloakUtils.searchKeycloakUsers(search));
    }

    @PutMapping("/update")
    public ResponseEntity search(@RequestBody RequestUserKeycloak user) {

        if (user.getId().isBlank()) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        keycloakUtils.updateKeycloakUser(user);

        return new ResponseEntity(HttpStatus.OK);
    }
}
