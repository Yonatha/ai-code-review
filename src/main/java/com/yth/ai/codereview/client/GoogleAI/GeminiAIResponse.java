package com.yth.ai.codereview.client.GoogleAI;

import java.util.List;

public class GeminiAIResponse {

    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

}

class Content {

    private List<Part> parts;

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

}

class Part {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}