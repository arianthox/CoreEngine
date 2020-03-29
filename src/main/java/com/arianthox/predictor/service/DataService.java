package com.arianthox.predictor.service;

import com.arianthox.predictor.model.DrawAccumulatedPerYearVO;
import com.arianthox.predictor.model.DrawDataVO;


public interface DataService {

    void uploadDraws();

    void purgeDraws();

    Iterable<DrawDataVO> getAllDraws();

    DrawAccumulatedPerYearVO processDraws();


}
