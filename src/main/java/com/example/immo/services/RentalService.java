package com.example.immo.services;

import java.util.Optional;

import com.example.immo.exceptions.RentalNotFoundException;
import org.springframework.stereotype.Service;

import com.example.immo.models.Rental;
import com.example.immo.repositories.RentalRepository;
import com.example.immo.services.interfaces.IRentalService;

@Service
public class RentalService implements IRentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Rental getRental(final Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Target rental cannot be found."));
    }

    public Iterable<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public Rental saveRental(Rental rental) {
        return Optional.of(rentalRepository.save(rental))
                .orElseThrow(() -> new RuntimeException("Failed to save the rental."));
    }

    public void deleteRental(final Integer id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Target rental cannot be found."));
        rentalRepository.delete(rental);
    }
}