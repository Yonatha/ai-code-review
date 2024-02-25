package com.yth.ai.codereview.client.GoogleAI.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Part {
    @JsonProperty("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

