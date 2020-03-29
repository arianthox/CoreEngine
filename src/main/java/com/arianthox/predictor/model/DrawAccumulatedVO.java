package com.arianthox.predictor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawAccumulatedVO implements Serializable {
    public HashMap<Integer,  Integer> data=new HashMap<>();
    public HashMap<Integer,  Double> coefficient=new HashMap<>();
}

