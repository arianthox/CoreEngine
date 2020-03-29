package com.arianthox.predictor.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(indexName = "draws", type = "allDraws")
public class DrawDataVO implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    private Date drawDate;
    private Integer year;
    private Integer month;
    private Integer day;
    private List<Integer> n;
    private String multiplier;


    public DrawDataVO(Date drawDate, String winningNumbers, String multiplier) {
        this.drawDate = drawDate;
        this.n = Arrays.asList(winningNumbers.split(" ")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        this.multiplier = multiplier;
        this.id = this.drawDate.getTime();
        LocalDate localDate = LocalDate.ofInstant(this.drawDate.toInstant(), ZoneId.systemDefault());
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();

    }

    public String toString() {
        return String.format("%s-%s-%s", this.year,this.month,this.day);
    }

}