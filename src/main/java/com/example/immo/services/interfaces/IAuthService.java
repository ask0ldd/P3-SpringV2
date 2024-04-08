package com.example.immo.services.interfaces;

public interface IAuthService {

    String registerUser(String email, String username, String password);

    String loginUser(String email, String password);

    // User getUserInfos();
}
