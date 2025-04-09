package com.bubusyaka.demo.gigachat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GigaChatRequest {

    private final String model;
    private final List<String> messages;
    private final boolean stream;
}
