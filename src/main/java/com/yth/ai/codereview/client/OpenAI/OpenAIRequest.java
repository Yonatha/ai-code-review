package com.yth.ai.codereview.client.OpenAI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenAIRequest {

    @SerializedName("model")
    private String model;

    @SerializedName("messages")
    private List<Message> messages;

    @SerializedName("temperature")
    private double temperature;

    public OpenAIRequest(String model, List<Message> messages, double temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }

    public static class Message {
        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}