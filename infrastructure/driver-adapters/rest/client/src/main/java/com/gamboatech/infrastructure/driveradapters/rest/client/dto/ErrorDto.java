package com.gamboatech.infrastructure.driveradapters.rest.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorDto {
    private String code;
    private String message;
}
