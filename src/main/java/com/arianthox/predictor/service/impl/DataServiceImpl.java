package com.arianthox.predictor.service.impl;


import com.arianthox.predictor.adapter.DrawConnectorAdapter;
import com.arianthox.predictor.repository.DrawRepository;
import com.arianthox.predictor.service.DataService;
import com.arianthox.predictor.model.DrawDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DrawConnectorAdapter drawConnectorAdapter;

    @Autowired
    private DrawRepository drawRepository;

    public void purgeDraws() {
        drawRepository.deleteAll();
    }

    public Iterable<DrawDataVO> getAllDraws() {
        return drawRepository.findAll();
    }

    public void uploadDraws() {
        drawConnectorAdapter.upload();
    }

}
