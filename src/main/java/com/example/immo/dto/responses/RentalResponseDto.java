package com.example.immo.dto.responses;

import com.example.immo.models.Rental;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
public class RentalResponseDto {
    private Integer id;
    private String description;
    private String name;
    private Integer owner_id;
    private String picture;
    private Integer surface;
    private Integer price;
    private String created_at;
    private String updated_at;

    public RentalResponseDto(Rental rental) throws ParseException {
        this.id = rental.getRentalId();
        this.description = rental.getDescription();
        this.name = rental.getName();
        this.picture = rental.getPicture();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.owner_id = rental.getOwner().getUserId();
        this.created_at = formatDate(rental.getCreation().toString());
        this.updated_at = formatDate(rental.getUpdate().toString());
    }

    private String formatDate(String date) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        return outputFormat.format(inputFormat.parse(date));
    }

}
