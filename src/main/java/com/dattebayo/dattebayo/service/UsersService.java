package com.dattebayo.dattebayo.service;

import com.dattebayo.dattebayo.api.UsersProfileResponse;
import com.dattebayo.dattebayo.api.UsersRegisterRequest;
import com.dattebayo.dattebayo.model.Users;
import com.dattebayo.dattebayo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String registerUser(UsersRegisterRequest request) {
        Users newUser=new Users();
        try {
            String hashPwd = passwordEncoder.encode(request.getPassword());
            newUser.setPassword(hashPwd);
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setLeetcodeProfileUrl(request.getLeetcodeProfileName());
            newUser.setVisibility(request.isVisibility());
            newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            newUser = usersRepository.save(newUser);
            if (newUser.getId()!=null) {
                return "Given user details are successfully registered";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Failed to register User";
    }

    public boolean isMyPage(Principal principal, String username){
        Optional<Users> user = usersRepository.findByEmail(principal.getName());
        if(user.isPresent()){
            return user.get().getUsername().equals(username);
        }
        return false;
    }

    public UsersProfileResponse getUserProfileDetails(String userName){
        Optional<Users> user=usersRepository.findByUsername(userName);
        if(user.isPresent()){
            return getUserProfileResponseFromUsers(user.get());
        }else{
            throw new NoSuchElementException("Please enter valid username");
        }
    }

    public Users getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Users> user = usersRepository.findByEmail(authentication.getName());
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    private UsersProfileResponse getUserProfileResponseFromUsers(Users user){
        UsersProfileResponse response=new UsersProfileResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setVisibility(user.isVisibility());
        response.setLeetcodeProfileName(user.getLeetcodeProfileUrl());
        return response;
    }
}
