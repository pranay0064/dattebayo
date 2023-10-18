package com.dattebayo.dattebayo.api;

import lombok.Data;

@Data
public class UsersLoginRequest {

    private String username;

    private String password;
}
