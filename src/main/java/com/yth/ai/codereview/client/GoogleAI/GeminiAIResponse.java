package com.yth.ai.codereview.client.GoogleAI;

import com.yth.ai.codereview.client.GoogleAI.Dto.Candidate;

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

class Part {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
