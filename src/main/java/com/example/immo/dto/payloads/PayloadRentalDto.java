package com.example.immo.dto.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Schema(type = "object", requiredProperties = {"name", "surface", "price", "description", "picture"})
public class PayloadRentalDto extends PayloadBaseRentalDto {

    @Schema(description = "Picture of the rental", type = "string", format = "binary")
    @JsonProperty("picture")
    private MultipartFile picture;

    public PayloadRentalDto(String name, String description, Integer price, Integer surface, MultipartFile picture){
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setSurface(surface);
        this.picture = picture;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
