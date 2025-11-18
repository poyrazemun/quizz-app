package com.poyraz.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorDTO {
    private String message;
    private HttpStatus error;
    private Integer status;
    private LocalDateTime timestamp;
}
