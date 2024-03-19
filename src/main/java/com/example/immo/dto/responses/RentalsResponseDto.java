package com.example.immo.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RentalsResponseDto {
    private Iterable<RentalResponseDto> rentals;

    public RentalsResponseDto(Iterable<RentalResponseDto> rentals) {
        this.rentals = rentals;
    }
}
