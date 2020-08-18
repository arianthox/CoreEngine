package com.arianthox.predictor.util;

import com.arianthox.predictor.commons.model.DrawVO;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Log
@Component
@Profile("powerball")
public class DrawGeneratorPowerBall implements DrawGenerator {

    public void permutations(final int start, final int end, final int factor, final double low, final double high, final Consumer<DrawVO> consumer) {
        IntStream.rangeClosed(start, end).boxed().parallel()
                .forEach(n1 -> IntStream.rangeClosed(n1 + 1, end).boxed().parallel()
                        .forEach(n2 -> IntStream.rangeClosed(n2 + 1, end).boxed().parallel()
                                .forEach(n3 -> IntStream.rangeClosed(n3 + 1, end).boxed().parallel()
                                        .forEach(n4 -> IntStream.rangeClosed(n4 + 1, end).boxed().parallel()
                                                .forEach(n5 -> IntStream.rangeClosed(start, factor).boxed().parallel()
                                                        .map(r ->
                                                                DrawVO.builder().n(Arrays.asList(n1, n2, n3, n4, n5,r)).factor(r).build()
                                                        )
                                                        .filter(drawVO -> drawVO.getScore() >= low && drawVO.getScore() <= high
                                                        ).forEach(consumer)
                                                )
                                        )
                                )
                        )

                );
    }
}
