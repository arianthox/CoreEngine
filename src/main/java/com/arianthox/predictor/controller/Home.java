package com.arianthox.predictor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class Home {

    public String home() {
        return "Core-Engine";
    }

}
