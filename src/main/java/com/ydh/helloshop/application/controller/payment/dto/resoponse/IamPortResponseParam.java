package com.ydh.helloshop.application.controller.payment.dto.resoponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IamPortResponseParam {

    private Long code;
    private String message;
}
