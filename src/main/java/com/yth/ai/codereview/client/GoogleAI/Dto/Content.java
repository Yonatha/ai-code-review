package com.yth.ai.codereview.client.GoogleAI.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Content {
    @JsonProperty("parts")
    private List<Part> parts;

    public Content() {
        this.parts = new ArrayList<>();
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}