package com.arianthox.predictor.service;

import com.arianthox.predictor.adapter.DrawConnectorAdapter;
import com.arianthox.predictor.client.BWMatcherClient;
import com.arianthox.predictor.commons.adapter.KafkaProducer;
import com.arianthox.predictor.commons.model.DrawVO;
import com.arianthox.predictor.util.DrawGenerator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Log
public class ProcessService {

    private final DrawConnectorAdapter drawConnectorAdapter;

    private final BWMatcherClient bwMatcherClient;

    private final KafkaProducer kafkaProducer;

    private final DrawGenerator drawGenerator;

    @Value("${draw.start}")
    private Integer start;

    @Value("${draw.end}")
    private Integer end;

    @Value("${draw.factor}")
    private Integer factor;


    public ProcessService(DrawConnectorAdapter drawConnectorAdapter, BWMatcherClient bwMatcherClient, KafkaProducer kafkaProducer, DrawGenerator drawGenerator) {
        this.drawConnectorAdapter = drawConnectorAdapter;
        this.bwMatcherClient = bwMatcherClient;
        this.kafkaProducer = kafkaProducer;
        this.drawGenerator = drawGenerator;
    }

    //@PostConstruct
    public void process() {

        CompletionStage<List<DrawVO>> listCompletionStage = drawConnectorAdapter.pull()
                .thenApply(drawDataDTOS -> drawDataDTOS.stream()
                        .peek(drawDataDTO -> {
                            log.info("Processing:" + drawDataDTO.toDrawData().toString());
                        })
                        .map(drawDataDTO -> drawDataDTO.toDrawData())
                        .map(drawDataVO -> DrawVO.builder().drawDate(drawDataVO.getDrawDate()).megaBall(drawDataVO.getMegaball()).n(drawDataVO.getN()).multiplier(drawDataVO.getMultiplier()).build())
                        .collect(Collectors.toList())
                );

        CompletionStage<IntSummaryStatistics> intSummaryStatisticsCompletionStage = listCompletionStage.thenApply(drawVOS -> {
            Set<DrawVO> hSet = new HashSet<>(drawVOS);
            log.info("List Size:" + drawVOS.size());
            log.info("Set Size:" + hSet.size());


            for (int train = 0; train < 1; train++) {
                DrawVO target = drawVOS.get(train);

                log.info("Target[" + train + "]: " + target.toString());

                List<int[]> collect = IntStream.rangeClosed(train + 1, train + 104).boxed().map(index -> drawVOS.get(index)).map(drawVO -> {
                    log.info("Parsing Samples: " + drawVO.toString());
                    List<Integer> n = new ArrayList<>(drawVO.getN());
                    n.add(drawVO.getFactor());
                    int[] values = n.stream().mapToInt(Integer::intValue).toArray();
                    return values;
                }).collect(Collectors.toList());


                ResponseEntity responseEntity = bwMatcherClient.addKey(collect, String.format("%s-%s-%s", target.getYear(), target.getMonth(), target.getDay()));

                int[] targetValues = target.getN().stream().mapToInt(Integer::intValue).toArray();
                HashMap<String, Double> stringDoubleHashMap = bwMatcherClient.matchKey(targetValues);

                log.info("Result:" + Collections.singletonList(stringDoubleHashMap).toString());


            }


            return drawVOS.stream().map(drawVO -> drawVO.getScore()).mapToInt(x -> x).summaryStatistics();
        });

        intSummaryStatisticsCompletionStage.thenAccept(stats -> {
            log.info("Average:" + stats.getAverage());
            log.info("Count:" + stats.getCount());
            log.info("Min:" + stats.getMin());
            log.info("Max:" + stats.getMax());

            AtomicInteger counter = new AtomicInteger(0);
            AtomicInteger excluded = new AtomicInteger(0);

            log.info("Starting Process");
            drawGenerator.permutations(start, end,
                    factor,
                    stats.getMin(), stats.getMax(),
                    (draw) -> {

                        kafkaProducer.send("draws", draw, done -> {
                            //log.info("Result:" + draw.toString() );
                            counter.incrementAndGet();
                        });
//
//                        int[] values = draw.getN().stream().mapToInt(Integer::intValue).toArray();
//                        HashMap<String, Double> stringDoubleHashMap = bwMatcherClient.matchKey(values);
//                        if (stringDoubleHashMap.isEmpty() || stringDoubleHashMap.entrySet().stream().findFirst().get().getValue() < 1) {
//                            draw.setMatch(stringDoubleHashMap);
//                            counter.incrementAndGet();
//                            kafkaProducer.send("draws", draw, done -> {
//                                log.info("Result:" + draw.toString() + " - " + Collections.singletonList(stringDoubleHashMap).toString());
//                            });
//                        }

                    }
            );


            log.info(String.format("Permutations[%d] - Excluded[%d] ", counter.get(), excluded.get()));
        });


    }


}
