package com.yth.ai.codereview.client.GoogleAI;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.yth.ai.codereview.client.OpenAI.IOpenAIClient;
import com.yth.ai.codereview.client.OpenAI.OpenAIRequest;
import com.yth.ai.codereview.configuration.Message;
import com.yth.ai.codereview.configuration.PluginPropertiesEnum;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import java.util.ArrayList;
import java.util.List;

public class GeminiAIClient {
    private static final String GOOGLE_AI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta"; // TODO mover esse valor para um local apropriado
    private static GeminiAIResponse response = new GeminiAIResponse();

    public GeminiAIResponse request(GeminiAIRequest request) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String secretKey = propertiesComponent.getValue(PluginPropertiesEnum.SECRET_KEY_PROPERTY.getPropertyName());
        String model = "gemini-pro"; // TODO mover para local adequado

        ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
            IGeminiAIClient client = Feign.builder()
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(IGeminiAIClient.class, GOOGLE_AI_BASE_URL);

            ProgressIndicator progress = ProgressManager.getInstance().getProgressIndicator();
            progress.setIndeterminate(true);
            progress.setText(Message.getMessage("review_code_wait_message"));

            try {
                response = client.request(model, secretKey, request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progress.stop();
        }, "AI Code Review", true, null);

        return response;
    }

    public static Boolean testConnection() throws ConfigurationException {
        GeminiService geminiService = new GeminiService();
        String suggestion = geminiService.geminiMoutRequest("Ping");

        if (suggestion!= null)
            Message.getMessage("success_testing");

        return  suggestion != null;
    }
}
