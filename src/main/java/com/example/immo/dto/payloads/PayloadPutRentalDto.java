package com.example.immo.dto.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Schema(type = "object", requiredProperties = {"name", "surface", "price", "description"})
public class PayloadPutRentalDto extends PayloadBaseRentalDto {

}
