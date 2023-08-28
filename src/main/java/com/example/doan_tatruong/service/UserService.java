package com.example.doan_tatruong.service;

import com.example.doan_tatruong.dto.UserDto;
import com.example.doan_tatruong.model.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface UserService {
    User findByUsername(String username);
    User save(UserDto userDto);
    void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;
    boolean verify(String verificationCode);
    User saveInfo(User user);
    boolean isUserEnabled(String username);

}
