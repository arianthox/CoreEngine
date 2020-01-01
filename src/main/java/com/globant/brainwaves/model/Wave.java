package com.globant.brainwaves.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Document(indexName = "elastichq", type = "wave")
public class Wave implements Serializable {

    private static final long serialVersionUID = 7192043801352630806L;

    @Id
    private String id;

    @NotBlank(message = "Device Id is mandatory")
    private String deviceId;
    public List<Short> input;

    public Wave() {
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

    public List<Short> getInput() {
        return input;
    }

    public void setInput(List<Short> input) {
        this.input = input;
    }
}
