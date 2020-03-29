package com.arianthox.predictor.controller;

import com.arianthox.predictor.model.DrawAccumulatedPerYearVO;
import com.arianthox.predictor.service.DataService;
import com.arianthox.predictor.model.DrawDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/draw")
public class DrawController {

    @Autowired
    private transient DataService dataService;


    @GetMapping("/upload")
    @ResponseBody
    public HttpStatus upload() {
        dataService.uploadDraws();
        return HttpStatus.OK;
    }

    @GetMapping("/purge")
    @ResponseBody
    public HttpStatus purge() {
        dataService.purgeDraws();
        return HttpStatus.OK;
    }

    @GetMapping("/all")
    @ResponseBody
    public Iterable<DrawDataVO> all() {
        return dataService.getAllDraws();

    }

    @GetMapping("/process")
    @ResponseBody
    public DrawAccumulatedPerYearVO process() {
        return dataService.processDraws();

    }




}
