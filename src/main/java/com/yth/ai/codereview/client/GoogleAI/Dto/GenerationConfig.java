package com.yth.ai.codereview.client.GoogleAI.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GenerationConfig {
    @JsonProperty("temperature")
    private double  temperature;

    @JsonProperty("topK")
    private int topK;

    @JsonProperty("topP")
    private int topP;

    @JsonProperty("maxOutputTokens")
    private int maxOutputTokens;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double  temperature) {
        this.temperature = temperature;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    public int getTopP() {
        return topP;
    }

    public void setTopP(int topP) {
        this.topP = topP;
    }

    public int getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens(int maxOutputTokens) {
        this.maxOutputTokens = maxOutputTokens;
    }
}