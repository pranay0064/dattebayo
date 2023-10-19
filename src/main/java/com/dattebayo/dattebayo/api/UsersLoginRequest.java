package com.dattebayo.dattebayo.api;

import lombok.Data;

@Data
public class UsersLoginRequest {

    private String email;

    private String password;
}
