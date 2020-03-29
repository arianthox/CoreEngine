package com.arianthox.predictor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.util.Pair;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawAccumulatedPerYearVO implements Serializable{
    public HashMap<Integer,  DrawAccumulatedVO> data=new HashMap<>();
    public List<Tuple2<List<Tuple2<Integer,Double>>,HashMap<String, Double>>> draws=new ArrayList<>();
}
