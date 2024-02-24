package com.yth.ai.codereview.client;

import com.google.gson.annotations.SerializedName;

public class OpenAIResponse {
    public String id;
    public String object;
    public long created;
    public String model;
    public Choice[] choices;
    public Usage usage;
    public String systemFingerprint;

    public static class Choice {
        public int index;
        public Message message;
        public Object logprobs;
        @SerializedName("finish_reason")
        public String finishReason;
    }

    public static class Message {
        public String role;
        public String content;
    }

    public static class Usage {

        @SerializedName("prompt_tokens")
        private int promptTokens;

        @SerializedName("completion_tokens")
        private int completionTokens;

        @SerializedName("total_tokens")
        private int totalTokens;

        public Usage(int promptTokens, int completionTokens, int totalTokens) {
            this.promptTokens = promptTokens;
            this.completionTokens = completionTokens;
            this.totalTokens = totalTokens;
        }

        public int getPromptTokens() {
            return promptTokens;
        }

        public int getCompletionTokens() {
            return completionTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        // Métodos adicionais

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("  promptTokens: ").append(promptTokens).append("\n");
            sb.append("  completionTokens: ").append(completionTokens).append("\n");
            sb.append("  totalTokens: ").append(totalTokens).append("\n");
            return sb.toString();
        }
    }

    // Métodos da classe OpenAIResponse

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(id).append("\n");
        sb.append("object: ").append(object).append("\n");
        sb.append("created: ").append(created).append("\n");
        sb.append("model: ").append(model).append("\n");
        sb.append("choices: \n");
        for (Choice choice : choices) {
            sb.append("  index: ").append(choice.index).append("\n");
            sb.append("  message: ").append(choice.message.content).append("\n");
            sb.append("  finishReason: ").append(choice.finishReason).append("\n");
        }
        sb.append("usage: \n").append(usage.toString());
        sb.append("system_fingerprint: ").append(systemFingerprint).append("\n");
        return sb.toString();
    }

    public Choice[] getChoices() {
        return choices;
    }
}