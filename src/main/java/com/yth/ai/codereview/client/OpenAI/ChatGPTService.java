package com.yth.ai.codereview.client.OpenAI;

import com.yth.ai.codereview.configuration.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatGPTService {
    public String openAiMountRequest(String model, String temperature, String selectedText) {
        List<OpenAIRequest.Message> messages = new ArrayList<>();
        messages.add(new OpenAIRequest.Message("system", Message.getMessage("reviewCodeMessage")));
        messages.add(new OpenAIRequest.Message("user", selectedText));
        OpenAIRequest request = new OpenAIRequest(
                model.toLowerCase(),
                messages,
                Double.parseDouble(temperature)
        );

        OpenAIClient client = new OpenAIClient();
        OpenAIResponse response = client.request(request);
        if (response.choices != null)
            return this.mountAiSuggestions(response);

        return null;
    }

    public String mountAiSuggestions(OpenAIResponse response) {
        StringBuilder sb = new StringBuilder();
        for (OpenAIResponse.Choice choice : response.getChoices()) {
            sb.append(choice.message.content);
        }
        return sb.toString();
    }
}
