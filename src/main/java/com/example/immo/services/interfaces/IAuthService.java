package com.example.immo.services.interfaces;

import com.example.immo.dto.responses.TokenResponseDto;
import com.example.immo.models.User;

public interface IAuthService {

    String registerUser(String email, String username, String password);

    String loginUser(String email, String password);

    // User getUserInfos();
}
