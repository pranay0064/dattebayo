package com.dattebayo.dattebayo.service;

import com.dattebayo.dattebayo.api.UsersProfileResponse;
import com.dattebayo.dattebayo.api.UsersRegisterRequest;
import com.dattebayo.dattebayo.model.Users;
import com.dattebayo.dattebayo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String registerUser(UsersRegisterRequest request) {
        Users users=new Users();
        users.setUsername(request.getUsername());
        users.setPassword(request.getPassword());
        users.setLeetcodeProfileUrl(request.getLeetcodeProfileName());
        users.setVisibility(request.isVisibility());
        try{
            usersRepository.save(users);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "successful";
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
