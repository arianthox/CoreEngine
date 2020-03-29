package com.arianthox.predictor.service.impl;


import akka.japi.Creator;
import akka.stream.javadsl.Source;
import com.arianthox.predictor.adapter.DrawConnectorAdapter;
import com.arianthox.predictor.client.BWMatcherClient;
import com.arianthox.predictor.model.DrawAccumulatedPerYearVO;
import com.arianthox.predictor.model.DrawAccumulatedVO;
import com.arianthox.predictor.model.DrawDataVO;
import com.arianthox.predictor.repository.DrawRepository;
import com.arianthox.predictor.service.DataService;
import com.arianthox.predictor.util.DrawGenerator;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class DataServiceImpl implements DataService {

    private Logger logger= Logger.getLogger(DataServiceImpl.class.getName());

    @Autowired
    private DrawConnectorAdapter drawConnectorAdapter;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private BWMatcherClient bwMatcherClient;

    public void purgeDraws() {
        drawRepository.deleteAll();
    }

    public Iterable<DrawDataVO> getAllDraws() {
        return drawRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @SuppressWarnings("unchecked")
    private static <DrawDataVO> Stream<DrawDataVO> reverse(Stream<DrawDataVO> input) {
        DrawDataVO[] temp = (DrawDataVO[]) input.toArray();
        return IntStream.range(0, temp.length)
                .mapToObj(i -> temp[temp.length - i - 1]);
    }

    public DrawAccumulatedPerYearVO analyze(){
        DrawGenerator drawGenerator=new DrawGenerator();
        Source.from(Arrays.asList(new DrawDataVO()));
        return  null;
    }

    public DrawAccumulatedPerYearVO processDraws(){
        final DrawAccumulatedPerYearVO result =  new DrawAccumulatedPerYearVO();



        StreamSupport.stream(getAllDraws().spliterator(), false).limit(108).parallel().forEach(drawDataVO -> {


            List<short[]> collect = drawRepository.findAllByIdLessThanOrderByDrawDateDesc(drawDataVO.getDrawDate().getTime()).stream().limit(108).map(draw -> ArrayUtils.toPrimitive(draw.getN().stream().map(integer -> integer.shortValue()).collect(Collectors.toList()).toArray(Short[]::new))).collect(Collectors.toList());
            bwMatcherClient.addKey(collect,drawDataVO.toString());


            if(!result.data.containsKey(drawDataVO.getYear())){
                result.data.put(drawDataVO.getYear(),new DrawAccumulatedVO());
            }
            DrawAccumulatedVO drawAccumulatedVO = result.data.get(drawDataVO.getYear());
            List<Tuple2<Integer,Double>> draw=new ArrayList<>();


            HashMap<String, Double> stringDoubleHashMap = bwMatcherClient.matchKey(ArrayUtils.toPrimitive(drawDataVO.getN().stream().map(integer -> integer.shortValue()).collect(Collectors.toList()).toArray(Short[]::new)));



            drawDataVO.getN().stream().forEach(n -> {

                double  coefficient=0;
                if(!drawAccumulatedVO.data.containsKey(n)){
                    drawAccumulatedVO.data.put(n,1);
                }else{
                    int total = drawAccumulatedVO.data.values().stream().reduce(0, Integer::sum);
                    int count = drawAccumulatedVO.data.get(n);
                    coefficient= ((double)count)/total;
                    drawAccumulatedVO.coefficient.put(n,coefficient);
                    drawAccumulatedVO.data.put(n,count+1);
                }

                draw.add(Tuple2.apply(n,coefficient));
            });
            result.draws.add(Tuple2.apply(draw,stringDoubleHashMap));
            result.data.put(drawDataVO.getYear(),drawAccumulatedVO);
            logger.info(String.format("[%s] Match Done!", drawDataVO.toString()));
        });
        return result;
    }

    public void uploadDraws() {
        drawConnectorAdapter.upload();
    }

}
