package com.arianthox.predictor.controller;

import com.arianthox.predictor.service.ProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class DrawController {

    private final transient ProcessService processService;

    public DrawController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/process")
    @ResponseBody
    public HttpStatus process() {
        processService.process();
        return HttpStatus.OK;
    }

}
