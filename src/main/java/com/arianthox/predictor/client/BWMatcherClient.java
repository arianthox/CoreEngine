package com.arianthox.predictor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

@FeignClient("BWMatcher")
@RequestMapping("/api/matcher")
public interface BWMatcherClient {
    @RequestMapping(value="/add/{key}", method = RequestMethod.PUT, produces="application/json")
    ResponseEntity addKey(@RequestBody List<int[]> samples, @PathVariable String key);

    @RequestMapping(value="/match", method = RequestMethod.POST, produces="application/json")
    HashMap<String, Double> matchKey(@RequestBody int[] signal);
}
