package com.example.immo.dto.responses;

import com.example.immo.models.User;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
public class LoggedUserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private String created_at;
    private String updated_at;

    public LoggedUserResponseDto(User user) throws ParseException {
        this.id = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created_at = formatDate(user.getCreation().toString());
        this.updated_at = formatDate(user.getUpdate().toString());
    }

    private String formatDate(String date) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        return outputFormat.format(inputFormat.parse(date));
    }
}