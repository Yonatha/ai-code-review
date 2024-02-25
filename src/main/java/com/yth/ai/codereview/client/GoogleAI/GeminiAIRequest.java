package com.yth.ai.codereview.client.GoogleAI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yth.ai.codereview.client.GoogleAI.Dto.Content;
import com.yth.ai.codereview.client.GoogleAI.Dto.GenerationConfig;
import com.yth.ai.codereview.client.GoogleAI.Dto.SafetySetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeminiAIRequest {
    @JsonProperty("contents")
    private List<Content> contents;

    @JsonProperty("generationConfig")
    private GenerationConfig generationConfig;

    @JsonProperty("safetySettings")
    private List<SafetySetting> safetySettings;

    public GeminiAIRequest() {
        this.contents = new ArrayList<>();
        this.generationConfig = new GenerationConfig();
        this.safetySettings = new ArrayList<>();
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public GenerationConfig getGenerationConfig() {
        return generationConfig;
    }

    public void setGenerationConfig(GenerationConfig generationConfig) {
        this.generationConfig = generationConfig;
    }

    public List<SafetySetting> getSafetySettings() {
        return safetySettings;
    }

    public void setSafetySettings(List<SafetySetting> safetySettings) {
        this.safetySettings = safetySettings;
    }
}