package com.gamboatech.infrastructure.entrypoints.rest.commons;

import com.gamboatech.domain.commons.BusinessException;
import com.gamboatech.domain.commons.ErrorCodes;
import com.gamboatech.infrastructure.entrypoints.rest.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.EnumMap;

import static com.gamboatech.domain.commons.ErrorCodes.NOT_FOUND;
import static com.gamboatech.domain.commons.ErrorCodes.UNAVAILABLE_BALANCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorDto> businessExceptionHandler(BusinessException ex){
        ErrorDto error = ErrorDto.builder()
                .code(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error,httpStatusCodeMapper(ex.getErrorCode()));
    }

    private HttpStatus httpStatusCodeMapper(ErrorCodes errorCode){

        EnumMap<ErrorCodes, HttpStatus> errorCodeMapper = new EnumMap<>(ErrorCodes.class);
        errorCodeMapper.put(UNAVAILABLE_BALANCE,BAD_REQUEST);
        errorCodeMapper.put(NOT_FOUND,HttpStatus.NOT_FOUND);

        return errorCodeMapper.getOrDefault(errorCode,INTERNAL_SERVER_ERROR);
    }
}
