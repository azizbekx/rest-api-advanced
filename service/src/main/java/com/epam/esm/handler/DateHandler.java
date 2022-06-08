package com.epam.esm.handler;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateHandler {
    public String getCurrentDate() {
        return LocalDateTime.now().toString();
    }
}
