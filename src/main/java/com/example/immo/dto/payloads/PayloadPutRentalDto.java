package com.example.immo.dto.payloads;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Schema(type = "object", requiredProperties = {"name", "surface", "price", "description"})
public class PayloadPutRentalDto extends PayloadBaseRentalDto {

}
