package com.globant.brainwaves.service;

import com.globant.brainwaves.model.Pattern;
import com.globant.brainwaves.repository.PatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatternService {

    @Autowired
    private transient PatternRepository repository;

    public Pattern save(Pattern pattern) {
        return repository.save(pattern);
    }

    public Iterable<Pattern> findAll() {
        return repository.findAll();
    }

    public Optional<Pattern> findById(String id) {
        return repository.findById(id);
    }

    public Optional<List<Pattern>> findAllByDeviceId(String deviceId) {
        return repository.findAllByDeviceId(deviceId);
    }


}
