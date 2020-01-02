package com.arianthox.predictor.repository;

import com.arianthox.predictor.model.DrawDataVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DrawRepository extends ElasticsearchRepository<DrawDataVO, Long> {

}