package com.example.immo.dto.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(type = "object", requiredProperties = {"name", "surface", "price", "description"})
public class PayloadBaseRentalDto {

    @Schema(description = "The name of the rental")
    @JsonProperty("name")
    @NotEmpty
    @Size(min=2, max=255, message="The name of the rental must be between {min} and {max} characters long")
    private String name;

    @Schema(description = "The description of the rental")
    @JsonProperty("description")
    @NotEmpty
    @Size(min=2, max=200, message="The description of the rental must be between {min} and {max} characters long")
    private String description;

    @Schema(description = "The surface of the rental")
    @JsonProperty("surface")
    @Min(value = 0, message = "The surface must be greater than or equal to 0")
    @Max(value = 1000, message = "The surface must be less than or equal to 1000")
    private Integer surface;

    @Schema(description = "The price of the rental")
    @JsonProperty("price")
    @Min(value = 0, message = "The price must be greater than or equal to 0")
    @Max(value = 10000, message = "The price must be less than or equal to 10000")
    private Integer price;
}
