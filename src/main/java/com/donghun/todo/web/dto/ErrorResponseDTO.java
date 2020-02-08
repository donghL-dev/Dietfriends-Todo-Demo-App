package com.donghun.todo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
public class ErrorResponseDTO {

    private String status;

    private String error;

    private String message;

    public ErrorResponseDTO(String status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
