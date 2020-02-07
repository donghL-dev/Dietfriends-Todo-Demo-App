package com.donghun.todo.web.dto;

import lombok.Getter;

@Getter
public class DefaultSuccessDTO {
    private String status;

    private String message;

    public DefaultSuccessDTO() {
        this.status = "200 OK";
        this.message = "Your request has been successfully processed.";
    }
}
