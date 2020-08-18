package com.arianthox.predictor.util;

import com.arianthox.predictor.commons.model.DrawVO;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;


public interface DrawGenerator {

    void permutations(final int start, final int end, final int factor, final double low, final double high, final Consumer<DrawVO> consumer);

}
