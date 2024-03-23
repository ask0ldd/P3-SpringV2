package com.example.immo.services.interfaces;

import com.example.immo.dto.responses.RentalResponseDto;
import com.example.immo.models.Rental;

public interface IRentalService {
    Rental getRental(final Integer id);
    Iterable<Rental> getRentals();
    Rental saveRental(Rental rental);
    void deleteRental(final Integer id);
}
