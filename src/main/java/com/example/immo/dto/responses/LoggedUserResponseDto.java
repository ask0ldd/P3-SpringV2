package com.example.immo.dto.responses;

import com.example.immo.models.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class LoggedUserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private Date created_at;
    private Date updated_at;

    public LoggedUserResponseDto(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created_at = user.getCreation();
        this.updated_at = user.getUpdate();
    }
}