package com.cvetkov.fedor.laboratoryworkmicro.users.dtoKecloak;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserKeycloak implements Serializable {
    private String id;
    private String email;
    private String username;
    private String password;
}
