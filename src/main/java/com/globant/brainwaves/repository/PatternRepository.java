package com.globant.brainwaves.repository;

import com.globant.brainwaves.model.Pattern;
import com.globant.brainwaves.model.Wave;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatternRepository extends ElasticsearchRepository<Pattern, String> {

    Optional<List<Pattern>> findAllByDeviceId(String deviceId);

}
