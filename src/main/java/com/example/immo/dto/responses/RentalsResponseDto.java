package com.example.immo.dto.responses;

import lombok.Data;

@Data
public class RentalsResponseDto {
    private RentalResponseDto[] rentals;

    public RentalsResponseDto(RentalResponseDto[] rentals) {
        this.rentals = rentals;
    }
}
