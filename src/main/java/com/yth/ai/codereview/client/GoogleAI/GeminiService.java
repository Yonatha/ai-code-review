package com.yth.ai.codereview.client.GoogleAI;

import com.yth.ai.codereview.client.GoogleAI.Dto.*;
import com.yth.ai.codereview.client.GoogleAI.Dto.Part;
import com.yth.ai.codereview.configuration.Message;

import java.util.List;

public class GeminiService {
    public String geminiMoutRequest(String selectedText) {

        GeminiAIClient client = new GeminiAIClient();

        GeminiAIRequest request = new GeminiAIRequest();
        Content content = new Content();
        com.yth.ai.codereview.client.GoogleAI.Dto.Part part = new Part();

        String prompt = Message.getMessage("reviewCodeMessage") + ": " + selectedText;
        part.setText(prompt);
        content.getParts().add(part);

        GenerationConfig generationConfig = new GenerationConfig();
        generationConfig.setTemperature(0.9);
        generationConfig.setTopK(1);
        generationConfig.setTopP(1);
        generationConfig.setMaxOutputTokens(2048);

        SafetySetting harassmentSetting = new SafetySetting();
        harassmentSetting.setCategory("HARM_CATEGORY_HARASSMENT");
        harassmentSetting.setThreshold("BLOCK_ONLY_HIGH");

        SafetySetting hateSpeechSetting = new SafetySetting();
        hateSpeechSetting.setCategory("HARM_CATEGORY_HATE_SPEECH");
        hateSpeechSetting.setThreshold("BLOCK_ONLY_HIGH");

        SafetySetting explicitSetting = new SafetySetting();
        explicitSetting.setCategory("HARM_CATEGORY_SEXUALLY_EXPLICIT");
        explicitSetting.setThreshold("BLOCK_ONLY_HIGH");

        SafetySetting dangerousContentSetting = new SafetySetting();
        dangerousContentSetting.setCategory("HARM_CATEGORY_DANGEROUS_CONTENT");
        dangerousContentSetting.setThreshold("BLOCK_ONLY_HIGH");

        request.getContents().add(content);
        request.setGenerationConfig(generationConfig);
        request.getSafetySettings().add(harassmentSetting);
        request.getSafetySettings().add(hateSpeechSetting);
        request.getSafetySettings().add(explicitSetting);
        request.getSafetySettings().add(dangerousContentSetting);
        GeminiAIResponse response = client.request(request);

        if (!response.getCandidates().isEmpty())
            return this.mountAiSuggestions(response.getCandidates());

        return null;
    }

    public String mountAiSuggestions(List<Candidate> response) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < response.size(); i++) {
            response.get(i).getContent();
            Content content = response.get(i).getContent();
            for (int j = 0; j < content.getParts().size(); j++) {
                String text = content.getParts().get(j).getText();
                sb.append(text);
            }
        }
        return sb.toString();
    }
}
