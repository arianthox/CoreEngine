package com.arianthox.predictor.model;


import lombok.*;
import org.springframework.data.annotation.Id;


import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DrawDataVO implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    private Date drawDate;
    private Integer year;
    private Integer month;
    private Integer day;
    private List<Integer> n;
    private Integer factor;
    private String multiplier;
    private int score=0;


    public DrawDataVO(Date drawDate, String winningNumbers, String multiplier) {
        this.drawDate = drawDate;
        this.n = Arrays.asList(winningNumbers.split(" ")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        this.factor=this.n.remove(this.n.size()-1);
        this.multiplier = multiplier;
        this.id = this.drawDate.getTime();
        LocalDate localDate = LocalDate.ofInstant(this.drawDate.toInstant(), ZoneId.systemDefault());
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.day = localDate.getDayOfMonth();

    }

    public String toString() {
        return String.format("[%s] [%s] [%s] [%s]",this.drawDate, n.stream().map(Object::toString)
                .collect(Collectors.joining("-")),this.factor,this.getScore());
    }

    public int getScore(){
        if(score==0) {
            Collections.sort(n);
            score= IntStream.range(0, n.size() - 1).map(i -> n.get(i + 1) - n.get(i)).sum();
        }
        return score;
    }



}
