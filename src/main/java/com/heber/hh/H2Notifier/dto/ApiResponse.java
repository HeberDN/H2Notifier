package com.heber.hh.H2Notifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse <T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message){
        this.success = success;
        this.message = message;
        this.data = null;
    }
}
