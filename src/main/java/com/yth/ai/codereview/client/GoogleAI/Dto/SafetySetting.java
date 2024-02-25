package com.yth.ai.codereview.client.GoogleAI.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SafetySetting {
    @JsonProperty("category")
    private String category;

    @JsonProperty("threshold")
    private String threshold;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }
}