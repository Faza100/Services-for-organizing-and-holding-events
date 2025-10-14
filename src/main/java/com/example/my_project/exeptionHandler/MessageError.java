package com.example.my_project.exeptionHandler;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MessageError(
        String errorResponse,
        String detailMessage,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime errorTime) {

}