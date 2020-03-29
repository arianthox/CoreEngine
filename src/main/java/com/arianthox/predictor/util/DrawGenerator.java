package com.arianthox.predictor.util;

import akka.japi.Creator;
import com.arianthox.predictor.model.DrawDataVO;

public class DrawGenerator implements Creator<Iterable<DrawDataVO>> {
    @Override
    public Iterable<DrawDataVO> create() throws Exception {
        return null;
    }
}
