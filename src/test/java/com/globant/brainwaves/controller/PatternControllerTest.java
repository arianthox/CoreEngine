package com.globant.brainwaves.controller;

import com.globant.brainwaves.CoreEngineApplication;
import com.globant.brainwaves.model.Pattern;
import com.globant.brainwaves.service.PatternService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreEngineApplication.class)
@AutoConfigureMockMvc
public class PatternControllerTest {

    private static final String API_CORE_ENGINE_ADMIN = "/api/core-engine/admin/pattern";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PatternService patternService;


    @Test
    public void all() throws Exception {
        Pattern pattern = createPattern();
        when(patternService.findAll()).thenReturn(Collections.singletonList(pattern));

        mvc.perform(MockMvcRequestBuilders.get(API_CORE_ENGINE_ADMIN + "/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(Collections.singletonList(pattern)), false));
    }

    @Test
    public void patternById() throws Exception {
        Pattern pattern = createPattern();
        when(patternService.findById("1")).thenReturn(Optional.of(pattern));

        mvc.perform(MockMvcRequestBuilders.get(API_CORE_ENGINE_ADMIN + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(pattern), false));
    }

    @Test
    public void patternsByDeviceId() throws Exception {
        Pattern pattern = createPattern();
        when(patternService.findAllByDeviceId("deviceId123")).thenReturn(Optional.of(Collections.singletonList(pattern)));

        mvc.perform(MockMvcRequestBuilders.get(API_CORE_ENGINE_ADMIN + "/device/deviceId123").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(Collections.singletonList(pattern)), false));


    }

    @Test
    public void savePattern() throws Exception {
        Pattern pattern = createPattern();
        when(patternService.save(pattern)).thenReturn(pattern);

        mvc.perform(MockMvcRequestBuilders.post(API_CORE_ENGINE_ADMIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(pattern))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(toJson(pattern), false));
    }

    private Pattern createPattern() {
        Pattern pattern = new Pattern();
        pattern.setId("1");
        pattern.setDeviceId("deviceId123");
        pattern.setPattern(new short[][]{{1, 2, 3}, {11, 22, 33}});
        return pattern;
    }

    private String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}