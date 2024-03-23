package com.example.immo.controllers;

import com.example.immo.dto.payloads.PayloadRentalDto;
import com.example.immo.dto.responses.DefaultResponseDto;
import com.example.immo.dto.responses.RentalResponseDto;
import com.example.immo.dto.responses.RentalsResponseDto;
import com.example.immo.models.Rental;
import com.example.immo.models.User;
import com.example.immo.services.FileService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.text.ParseException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = {"http://localhost:4200"})
@SecurityRequirement(name = "bearerAuth")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final FileService fileService;

    public RentalController(RentalService rentalService, UserService userService, FileService fileService) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.fileService = fileService;
    }

    // Retrieve all Rentals
    @GetMapping("/rentals")
    @Operation(
            summary = "Get all rentals",
            description = "Retrieve all rentals available.",
            tags = {"Rentals"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = RentalsResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "No rentals found", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class)))
            }
    )
    public ResponseEntity<?> getRentals() { // !!! should return an empty array when no rentals?
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Iterable<RentalResponseDto> rentals = rentalService.getReturnableRentals();
            Iterable<Rental> rentals = rentalService.getRentals();
            Iterable<RentalResponseDto> rentalsResponse = StreamSupport.stream(rentals.spliterator(), false)
                    .map(rental -> {
                        try {
                            return new RentalResponseDto(rental);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new RentalsResponseDto(rentalsResponse), headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Can't find any Rental."), HttpStatus.NOT_FOUND);
        }
    }

    // Retrieve the target Rental
    @GetMapping("/rentals/{id}")
    @Operation(summary = "Get a specific rental by ID",
            description = "Retrieves details of a rental based on the provided ID.",
            tags = {"Rentals"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the rental", content = @Content(schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class)))
    })
    public ResponseEntity<?> getRental(@PathVariable("id") final Integer id) {
        try {
            Rental rental = rentalService.getRental(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(new RentalResponseDto(rental), headers, HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("\u001B[31m" + exception + "\u001B[0m");
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Can't find the requested Rental."), HttpStatus.NOT_FOUND);
        }
    }

    // Update the target Rental
    @PutMapping("/rentals/{id}")
    @Operation(summary = "Update a Rental",
            description = "Update details of a rental property by ID",
            tags = {"Rentals"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rental updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Rental not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<?> updateRental(@PathVariable("id") final Integer id,
                                          @RequestParam("name") String name,
                                          @RequestParam("surface") Integer surface,
                                          @RequestParam("price") Integer price,
                                          @RequestParam("description") String description) {
        try {
            Rental rental = rentalService.getRental(id);

            if (rental == null) {
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Can't update the requested Rental."), HttpStatus.NOT_FOUND);
            }

            // !!!!!!!!!!! should validate datas
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);

            Rental modifiedRental = rentalService.saveRental(rental);
            System.out.println(modifiedRental);

            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Rental updated !"),
                    HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create a new Rental // spring boot validator
    @PostMapping("/rentals")
    @Operation(summary = "Create a Rental",
            tags = {"Rentals"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental created successfully", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, a picture is needed", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class)))
    })
    public ResponseEntity<?> createRental(HttpServletRequest request, @RequestParam("name") String name, @RequestParam("surface") String surface, @RequestParam("price") String price, @RequestParam("picture") MultipartFile picture, @RequestParam("description") String description) {
    // public ResponseEntity<?> createRental(HttpServletRequest request, @RequestBody PayloadRentalDto payloadRentalDto) {
        try{
            if (picture.isEmpty()) {
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("A picture is needed!"), HttpStatus.BAD_REQUEST);
            }
            // !!!!!!!!!!! should validate datas
            String filename = fileService.save(picture);

            Principal principal = request.getUserPrincipal();
            String email = principal.getName();
            User loggedUser = userService.getUserByEmail(email);

            Rental rental = Rental.builder().name(name).rentalId(null).owner(loggedUser)
                    .description(description)
                    .picture("http://127.0.0.1:3001/img/rental/" + filename)
                    .surface(Integer.parseInt(surface)).price(Integer.parseInt(price)).build();

            rentalService.saveRental(rental);

            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Rental created !"), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
