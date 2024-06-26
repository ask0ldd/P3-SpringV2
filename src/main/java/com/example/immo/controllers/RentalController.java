package com.example.immo.controllers;

import com.example.immo.dto.payloads.PayloadPutRentalDto;
import com.example.immo.dto.payloads.PayloadRentalDto;
import com.example.immo.dto.responses.DefaultResponseDto;
import com.example.immo.dto.responses.RentalResponseDto;
import com.example.immo.dto.responses.RentalsResponseDto;
import com.example.immo.exceptions.RentalNotFoundException;
import com.example.immo.models.Rental;
import com.example.immo.models.User;
import com.example.immo.services.FileSystemService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.text.ParseException;
import java.util.Objects;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = { "http://localhost:4200" })
@SecurityRequirement(name = "bearerAuth")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final FileSystemService fileSystemService;

    public RentalController(RentalService rentalService, UserService userService, FileSystemService fileSystemService) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.fileSystemService = fileSystemService;
    }

    // Retrieve the target Rental
    @GetMapping("/rentals/{id}")
    @Operation(summary = "Get a specific rental by ID", description = "Retrieves details of a rental based on the provided ID.", tags = {
            "Rentals" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the rental.", content = @Content(schema = @Schema(implementation = RentalResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "Rental not found.", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), mediaType = "application/json"))
    })
    public ResponseEntity<?> getRental(@PathVariable("id") final Integer id) {
        try {
            Rental rental = rentalService.getRental(id);
            return new ResponseEntity<RentalResponseDto>(new RentalResponseDto(rental), HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("\u001B[31m" + exception + "\u001B[0m");
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Can't find the requested Rental."),
                    HttpStatus.NOT_FOUND);
        }
    }

    // Retrieve all Rentals
    @GetMapping("/rentals")
    @Operation(summary = "Get all rentals", description = "Retrieve all rentals available.", tags = {
            "Rentals" }, responses = {
                    @ApiResponse(responseCode = "200", description = "Rentals retrieved.", content = @Content(schema = @Schema(implementation = RentalsResponseDto.class), mediaType = "application/json", examples = @ExampleObject(value = "{\"rentals\" : [{\"id\": 0, \"description\": \"string\", \"name\": \"string\", \"owner_id\": 0, \"picture\": \"string\", \"surface\": 0, \"price\": 0, \"created_at\": \"string\", \"updated_at\": \"string\"}]}"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized."),
                    @ApiResponse(responseCode = "404", description = "No rentals found.", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), mediaType = "application/json"))
            })
    public ResponseEntity<?> getRentals() {
        try {
            Iterable<Rental> rentals = rentalService.getRentals();
            // cycle through the rentals to create a RentalsResponseDto being an array of
            // RentalResponseDto
            RentalResponseDto[] rentalsResponse = StreamSupport.stream(rentals.spliterator(), false)
                    .map(rental -> {
                        try {
                            return new RentalResponseDto(rental);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(RentalResponseDto[]::new);
            return new ResponseEntity<RentalsResponseDto>(new RentalsResponseDto(rentalsResponse), HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("\u001B[31m" + exception + "\u001B[0m");
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Can't find any Rental."),
                    HttpStatus.NOT_FOUND);
        }
    }

    // Update the target Rental
    @PutMapping("/rentals/{id}")
    @Operation(summary = "Update a Rental", description = "Update details of a rental property by ID", tags = {
            "Rentals" }, responses = {
                    @ApiResponse(responseCode = "200", description = "Rental updated successfully.", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), examples = @ExampleObject(value = "{\"message\" : \"Rental updated !\"}"), mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid input."),
                    @ApiResponse(responseCode = "401", description = "Unauthorized."),
                    @ApiResponse(responseCode = "404", description = "Rental not found.", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), examples = @ExampleObject(value = "{\"message\" : \"Can't update the requested Rental.\"}"), mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error.")
            })
    public ResponseEntity<?> updateRental(@PathVariable("id") final Integer id,
            @Valid @ModelAttribute PayloadPutRentalDto payloadPutRentalDto, Authentication authentication,
            Principal principal) {
        try {
            Rental rental = rentalService.getRental(id);

            if (rental == null) {
                return new ResponseEntity<DefaultResponseDto>(
                        new DefaultResponseDto("Can't update the requested Rental."), HttpStatus.NOT_FOUND);
            }

            // the authenticated user needs to be an admin or the owner of the rental
            // to be able to update it
            if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                if (!Objects.equals(rental.getOwner().getEmail(), principal.getName()))
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // data validation is done through some dto annotations

            rental.setName(payloadPutRentalDto.getName());
            rental.setSurface(payloadPutRentalDto.getSurface());
            rental.setPrice(payloadPutRentalDto.getPrice());
            rental.setDescription(payloadPutRentalDto.getDescription());

            rentalService.saveRental(rental);

            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Rental updated !"),
                    HttpStatus.OK);
        } catch (Exception exception) {
            System.out.println("\u001B[31m" + exception + "\u001B[0m");
            if (exception instanceof RentalNotFoundException)
                return new ResponseEntity<DefaultResponseDto>(
                        new DefaultResponseDto("Can't update the requested Rental."), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Creating a new Rental
    @PostMapping(value = "/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a Rental", tags = { "Rentals" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental created successfully.", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), examples = @ExampleObject(value = "{\"message\" : \"Rental created !\"}"), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid Input.", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class), examples = @ExampleObject(value = "{\"message\" : \"Invalid Input.\"}"), mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<?> createRental(HttpServletRequest request,
            @RequestParam final String name,
            @RequestParam final String description,
            @RequestParam final Integer surface,
            @RequestParam final Integer price,
            @RequestPart(required = true) final MultipartFile picture) {
        try {
            if (picture == null || picture.isEmpty()) {
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Invalid Picture."),
                        HttpStatus.BAD_REQUEST);
            }

            // validating data through the Dto constructor, throws a
            // ConstraintViolationException if the validation fails
            PayloadRentalDto rentalDto = new PayloadRentalDto(name, description, price, surface, picture);

            // try saving the picture, throws an exception if the file format is unknown
            String filename = fileSystemService.saveImg(picture);

            // retrieve the logged user using the email (name) from the principal
            Principal principal = request.getUserPrincipal();
            String email = principal.getName();
            User loggedUser = userService.getUserByEmail(email);

            Rental rental = Rental.builder().name(name).rentalId(null).owner(loggedUser)
                    .description(description)
                    .picture("http://127.0.0.1:3001/img/rental/" + filename)
                    .surface(surface).price(price).build();

            rentalService.saveRental(rental);

            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Rental created !"), HttpStatus.OK);
        } catch (Exception exception) {
            if (exception.getClass().equals(ConstraintViolationException.class)) {
                return new ResponseEntity<>(new DefaultResponseDto("Invalid Input."), HttpStatus.BAD_REQUEST);
            }
            System.out.println("\u001B[31m" + exception + "\u001B[0m");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
