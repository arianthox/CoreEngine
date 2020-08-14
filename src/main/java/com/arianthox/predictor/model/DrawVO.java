package com.arianthox.predictor.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//import org.springframework.data.annotation.Id;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@Document(indexName = "draws", type = "allDraws")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DrawVO implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    private Date drawDate;
    private Integer year;
    private Integer month;
    private Integer day;
    @EqualsAndHashCode.Include
    private List<Integer> n;
    @EqualsAndHashCode.Include
    private Integer factor;
    private String multiplier;
    private int score=0;

    private HashMap<String, Double> match;

    public void setMultiplier(String multiplier){
        this.multiplier=multiplier;
        if(null!=multiplier && !multiplier.isEmpty()){
            this.factor=Integer.parseInt(multiplier);
        }
    }

    public Integer getFactor(){
        if(this.factor==null && this.multiplier!=null){
            this.factor = Integer.parseInt(this.multiplier);
        }
        return factor;
    }


    public DrawVO(Date drawDate, String winningNumbers, String multiplier) {
        this.drawDate = drawDate;
        this.n = Arrays.asList(winningNumbers.split(" ")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        this.multiplier = multiplier;
        this.id = this.drawDate.getTime();
        LocalDate localDate = LocalDate.ofInstant(this.drawDate.toInstant(), ZoneId.systemDefault());
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();

    }
    public int getScore(){
        if(score==0) {
            Collections.sort(n);
            score=IntStream.range(0, n.size() - 1).map(i -> n.get(i + 1) - n.get(i)).sum();
        }
        return score;
    }

    public String toString() {
        return String.format("%s [%s] [%s]", n.stream().map(Object::toString)
                .collect(Collectors.joining("-")),this.factor,this.getScore());
    }

}
