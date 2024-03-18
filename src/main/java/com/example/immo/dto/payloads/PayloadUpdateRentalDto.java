package com.example.immo.dto.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PayloadUpdateRentalDto {

    private String name;
    private Integer surface;
    private Integer price;
    private String description;
}