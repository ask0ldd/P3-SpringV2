package com.example.immo.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RentalsResponseDto {
    private RentalResponseDto[] rentals;

    public RentalsResponseDto(RentalResponseDto[] rentals) {
        this.rentals = rentals;
    }
}
