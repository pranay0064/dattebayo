package com.dattebayo.dattebayo.controller;

import com.dattebayo.dattebayo.api.UsersProfileResponse;
import com.dattebayo.dattebayo.api.UsersRegisterRequest;
import com.dattebayo.dattebayo.model.Users;
import com.dattebayo.dattebayo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    @PreFilter("filterObject.username != 'test'")
    public String registerUser(@RequestBody List<UsersRegisterRequest> request) {
        return usersService.registerUser(request.get(0));
    }

    @GetMapping("/{username}/info")
    @PreAuthorize("@usersService.isMyPage(authentication, #username)")
    public UsersProfileResponse getUser(@PathVariable(name = "username") final String username){
        return usersService.getUserProfileDetails(username);
    }

    @RequestMapping("/login")
    public Users getUserDetailsAfterLogin(Authentication authentication) {
        return usersService.getUserDetailsAfterLogin(authentication);
    }
}
