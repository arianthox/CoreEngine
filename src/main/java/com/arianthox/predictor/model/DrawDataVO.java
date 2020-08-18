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

    private List<Integer> n;

    private Integer multiplier;
    private Integer megaball;

    private int score=0;


    public DrawDataVO(Date drawDate, String winningNumbers, Integer multiplier, Integer megaball) {
        this.drawDate = drawDate;
        this.n = Arrays.asList(winningNumbers.split(" ")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        this.multiplier = multiplier;
        this.megaball = megaball;

        this.id = this.drawDate.getTime();

    }

    public String toString() {
        return String.format("[%s] [%s] [%s] [%s] [%s]",this.drawDate, n.stream().map(Object::toString)
                .collect(Collectors.joining("-")),this.megaball,this.multiplier,this.getScore());
    }

    public int getScore(){
        if(score==0) {
            Collections.sort(n);
            score= IntStream.range(0, n.size() - 1).map(i -> n.get(i + 1) - n.get(i)).sum();
        }
        return score;
    }



}
