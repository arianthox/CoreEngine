package com.globant.brainwaves.controller;

import com.globant.brainwaves.model.Pattern;
import com.globant.brainwaves.service.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/core-engine/admin/pattern")
public class PatternController {

    @Autowired
    private transient PatternService patternService;

    @GetMapping("/all")
    @ResponseBody
    public Iterable<Pattern> all() {
        return patternService.findAll();
    }

    @GetMapping("{id}")
    @ResponseBody
    public Pattern patternById(@PathVariable("id") String id) {
        return patternService.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
    }

    @GetMapping("/device/{id}")
    @ResponseBody
    public List<Pattern> patternsByDeviceId(@PathVariable("id") String id) {
        return patternService.findAllByDeviceId(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pattern savePattern(@RequestBody @Valid Pattern pattern) {
        patternService.save(pattern);
        return pattern;
    }

}
