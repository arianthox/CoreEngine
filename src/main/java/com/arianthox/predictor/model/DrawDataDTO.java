package com.arianthox.predictor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawDataDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty("draw_date")
    private Date drawDate;

    @JsonProperty("winning_numbers")
    private String winningNumbers;

    @JsonProperty("mega_ball")
    private Integer megaBall;

    @JsonProperty("multiplier")
    private Integer multiplier;

    public DrawDataVO toDrawData() {
        return new DrawDataVO(drawDate, winningNumbers, multiplier,megaBall);
    }

    public String toString() {
        return String.format("%s - %s (%s)", drawDate, winningNumbers, multiplier);
    }



}
