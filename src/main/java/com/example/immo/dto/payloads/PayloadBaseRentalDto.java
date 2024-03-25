package com.example.immo.dto.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Schema(type = "object", requiredProperties = {"name", "surface", "price", "description"})
public class PayloadBaseRentalDto {

    @Schema(description = "The name of the rental")
    @JsonProperty("name")
    @NotEmpty
    @Size(min=2, max=255, message="the name of the rental cannot exceed 255 characters")
    private String name;

    @Schema(description = "The description of the rental")
    @JsonProperty("description")
    @NotEmpty
    @Size(min=2, max=200, message="the name of the rental cannot exceed 2000 characters")
    private String description;

    @Schema(description = "The surface of the rental")
    @JsonProperty("surface")
    private Integer surface;

    @Schema(description = "The price of the rental")
    @JsonProperty("price")
    private Integer price;

    /*public void validate(){
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("the name of the rental cannot be empty");
        }

        if (name.length() > 255) {
            throw new IllegalArgumentException("the name of the rental cannot exceed 255 characters");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("the name of the rental cannot be empty");
        }

        if (description.length() > 2000) {
            throw new IllegalArgumentException("the name of the rental cannot exceed 2000 characters");
        }
    }*/
}
