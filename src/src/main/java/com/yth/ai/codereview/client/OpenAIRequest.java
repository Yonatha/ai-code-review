package com.yth.ai.codereview.client;

import com.google.gson.annotations.SerializedName;

public class OpenAIRequest {

    @SerializedName("model")
    private String modelo;

    @SerializedName("prompt")
    private String prompt;

    @SerializedName("temperature")
    private double temperature;

    @SerializedName("max_tokens")
    private int maxTokens;

    @SerializedName("top_p")
    private double topP;

    @SerializedName("frequency_penalty")
    private double frequencyPenalty;

    @SerializedName("presence_penalty")
    private double presencePenalty;

    public OpenAIRequest(String modelo, String prompt, double temperature, int maxTokens, double topP,
                         double frequencyPenalty, double presencePenalty) {
        this.modelo = modelo;
        this.prompt = prompt;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP = topP;
        this.frequencyPenalty = frequencyPenalty;
        this.presencePenalty = presencePenalty;
    }
}

