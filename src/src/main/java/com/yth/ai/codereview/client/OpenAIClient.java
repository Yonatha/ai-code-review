package com.yth.ai.codereview.client;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import com.yth.ai.codereview.configuration.Message;
import com.yth.ai.codereview.configuration.PluginPropertiesEnum;

import java.util.Objects;

public class OpenAIClient {

    private OpenAIResponse responce = new OpenAIResponse();
    private final String OPEN_AI_BASE_URL = "https://api.openai.com";

    public OpenAIResponse request(OpenAIRequest request) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String secretKey = propertiesComponent.getValue(PluginPropertiesEnum.SECRET_KEY_PROPERTY.getPropertyName());

        ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
            IOpenAIClient client = Feign.builder()
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(IOpenAIClient.class, OPEN_AI_BASE_URL);

            ProgressIndicator progress = ProgressManager.getInstance().getProgressIndicator();
            progress.setIndeterminate(true);
            progress.setText(Message.getMessage("review_code_wait_message"));

            responce = client.request(secretKey, request);

            progress.stop();
        }, "AI Code Review", true, null);

        return responce;
    }
}
