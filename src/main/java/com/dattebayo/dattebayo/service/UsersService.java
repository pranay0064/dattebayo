package com.dattebayo.dattebayo.service;

import com.dattebayo.dattebayo.api.UsersProfileResponse;
import com.dattebayo.dattebayo.api.UsersRegisterRequest;
import com.dattebayo.dattebayo.model.Users;
import com.dattebayo.dattebayo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            newUser.setLeetcodeProfileUrl(request.getLeetcodeProfileName());
            newUser.setVisibility(request.isVisibility());
            newUser = usersRepository.save(newUser);
            if (newUser.getId()!=null) {
                return "Given user details are successfully registered";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Failed to register User";
    }

    public UsersProfileResponse getUserProfileDetails(String userName){
        Optional<Users> user=usersRepository.findByUsername(userName);
        if(user.isPresent()){
            return getUserProfileResponseFromUsers(user.get());
        }else{
            throw new NoSuchElementException("Please enter valid username");
        }
    }

    private UsersProfileResponse getUserProfileResponseFromUsers(Users user){
        UsersProfileResponse response=new UsersProfileResponse();
        response.setUsername(user.getUsername());
        response.setVisibility(user.isVisibility());
        response.setLeetcodeProfileName(user.getLeetcodeProfileUrl());
        return response;
    }
}
