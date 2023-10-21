package com.dattebayo.dattebayo.controller;

import com.dattebayo.dattebayo.api.LeetCodeStatsResponse;
import com.dattebayo.dattebayo.api.UsersProfileResponse;
import com.dattebayo.dattebayo.api.UsersRegisterRequest;
import com.dattebayo.dattebayo.model.Users;
import com.dattebayo.dattebayo.service.LeetCodeStatsService;
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
    private LeetCodeStatsService leetCodeStatsService;

    @Autowired
    public UsersController(UsersService usersService,LeetCodeStatsService leetCodeStatsService) {
        this.usersService = usersService;
        this.leetCodeStatsService=leetCodeStatsService;
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

    @GetMapping("/{username}/stats")
    public LeetCodeStatsResponse getLeetCodeStats(@PathVariable(name = "username") final String username) throws NoSuchFieldException {
        return leetCodeStatsService.getStats(username);
    }
}
