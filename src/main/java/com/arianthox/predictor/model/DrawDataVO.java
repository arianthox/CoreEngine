package com.arianthox.predictor.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import java.io.Serializable;
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
        this.year = this.drawDate.getYear();
        this.month = this.drawDate.getMonth();
        this.day = this.drawDate.getDay();

    }

    public String toString() {
        return String.format("%s - %s (%s)", drawDate, Arrays.deepToString(this.n.toArray()), multiplier);
    }

}