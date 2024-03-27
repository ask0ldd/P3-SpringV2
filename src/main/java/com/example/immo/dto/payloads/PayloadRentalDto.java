package com.example.immo.dto.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Schema(type = "object", requiredProperties = {"name", "surface", "price", "description", "picture"})
public class PayloadRentalDto extends PayloadBaseRentalDto {

    @Schema(description = "Picture of the rental", type = "string", format = "binary")
    @JsonProperty("picture")
    private MultipartFile picture;

    public PayloadRentalDto(
            @NotEmpty
            @Size(min=2, max=255, message="The name of the rental must be between {min} and {max} characters long")
            String name,
            @NotEmpty
            @Size(min=2, max=200, message="The description of the rental must be between {min} and {max} characters long")
            String description, Integer price, Integer surface, MultipartFile picture)
    {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setSurface(surface);
        this.picture = picture;
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PayloadRentalDto>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation error", violations);
        }
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
