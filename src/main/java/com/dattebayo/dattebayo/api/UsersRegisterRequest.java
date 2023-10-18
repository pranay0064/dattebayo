package com.dattebayo.dattebayo.api;

import lombok.Data;

@Data
public class UsersRegisterRequest {

    private String username;

    private String password;

    private String leetcodeProfileName;

    private boolean visibility;
}
