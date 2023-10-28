package com.yth.ai.codereview.client;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.yth.ai.codereview.configuration.Message;
import com.yth.ai.codereview.configuration.PluginPropertiesEnum;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

public class OpenAIClient {

    private static OpenAIResponse responce = new OpenAIResponse();
    private static final String OPEN_AI_BASE_URL = "https://api.openai.com";

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

            try {
                responce = client.request(secretKey, request);
            } catch (Exception e){
                e.printStackTrace();
            }
            progress.stop();
        }, "AI Code Review", true, null);

        return responce;
    }

    public static Boolean testConnection() throws ConfigurationException {

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String secretKey = propertiesComponent.getValue(PluginPropertiesEnum.SECRET_KEY_PROPERTY.getPropertyName());
        String model = propertiesComponent.getValue(PluginPropertiesEnum.MODEL_PROPERTY.getPropertyName());
        String temperature = propertiesComponent.getValue(PluginPropertiesEnum.TEMPERATURE_PROPERTY.getPropertyName());
        String maxTokens = propertiesComponent.getValue(PluginPropertiesEnum.TOKENS_PROPERTY.getPropertyName());

        OpenAIRequest request = new OpenAIRequest(
                model.toLowerCase(),
                "Ping",
                Double.parseDouble(temperature),
                Integer.parseInt(maxTokens),
                1,
                0,
                0
        );

        ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
            IOpenAIClient client = Feign.builder()
                    .encoder(new GsonEncoder())
                    .decoder(new GsonDecoder())
                    .target(IOpenAIClient.class, OPEN_AI_BASE_URL);

            ProgressIndicator progress = ProgressManager.getInstance().getProgressIndicator();
            progress.setIndeterminate(true);
            progress.setText(Message.getMessage("success_testing"));

            try {
                responce = client.request(secretKey, request);
            } catch (Exception e){
                e.printStackTrace();
            }
            progress.stop();
        }, "AI Code Review", true, null);
        return  responce.id != null;
    }
}
