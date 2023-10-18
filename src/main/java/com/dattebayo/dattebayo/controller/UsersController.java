package com.dattebayo.dattebayo.controller;

import com.dattebayo.dattebayo.api.UsersProfileResponse;
import com.dattebayo.dattebayo.api.UsersRegisterRequest;
import com.dattebayo.dattebayo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UsersRegisterRequest request) {
        return usersService.registerUser(request);
    }

    @GetMapping("/{username}/info")
    public UsersProfileResponse getUser(@PathVariable(name = "username") final String username){
        return usersService.getUserProfileDetails(username);
    }
}
