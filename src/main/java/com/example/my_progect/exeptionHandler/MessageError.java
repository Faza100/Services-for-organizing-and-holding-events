package com.example.my_progect.exeptionHandler;

import java.time.LocalDateTime;

public record MessageError(
        String message,
        String detailMessagr,
        LocalDateTime errorTime) {

}
