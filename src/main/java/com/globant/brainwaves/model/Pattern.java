package com.globant.brainwaves.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Document(indexName = "elastichq", type = "pattern")
public class Pattern implements Serializable {

    private static final long serialVersionUID = 2019128099084761251L;

    @Id
    private String id;

    @NotBlank(message = "Device Id is mandatory")
    private String deviceId;

    private short[][] pattern;

    public Pattern() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public short[][] getPattern() {
        return pattern;
    }

    public void setPattern(short[][] pattern) {
        this.pattern = pattern;
    }
}
