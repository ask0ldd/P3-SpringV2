package com.example.immo.dto.responses;

import com.example.immo.models.Rental;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
@Getter
public class RentalResponseDto {
    private Integer id;
    private String description;
    private String name;
    // private ReturnableUserDto owner;
    private Integer owner_id;
    private String picture;
    private Integer surface;
    private Integer price;
    private Date created_at;
    private Date updated_at;

    public RentalResponseDto(Rental rental) {
        this.id = rental.getRentalId();
        this.description = rental.getDescription();
        this.name = rental.getName();
        this.picture = rental.getPicture();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.owner_id = rental.getOwner().getUserId();
        this.created_at = rental.getCreation();
        this.updated_at = rental.getUpdate();
    }
}
