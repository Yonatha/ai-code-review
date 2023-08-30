package com.yth.ai.codereview.client;

import com.google.gson.annotations.SerializedName;

public class OpenAIResponse {
    public String id;
    public String object;
    public long created;
    public String model;
    public Choice[] choices;
    public Usage usage;

    public static class Choice {
        public String text;
        public int index;
        public Object logprobs;
        @SerializedName("finish_reason")
        public String finishReason;
    }

    public static class Usage {
        @SerializedName("prompt_tokens")
        public int promptTokens;
        @SerializedName("completion_tokens")
        public int completionTokens;
        @SerializedName("total_tokens")
        public int totalTokens;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(id).append("\n");
        sb.append("object: ").append(object).append("\n");
        sb.append("created: ").append(created).append("\n");
        sb.append("model: ").append(model).append("\n");
        sb.append("choices: \n");
        for (Choice choice : choices) {
            sb.append("  text: ").append(choice.text).append("\n");
            sb.append("  index: ").append(choice.index).append("\n");
            sb.append("  finishReason: ").append(choice.finishReason).append("\n");
        }
        sb.append("usage: \n");
        sb.append("  promptTokens: ").append(usage.promptTokens).append("\n");
        sb.append("  completionTokens: ").append(usage.completionTokens).append("\n");
        sb.append("  totalTokens: ").append(usage.totalTokens).append("\n");
        return sb.toString();
    }

    public Choice[] getChoices() {
        return choices;
    }
}