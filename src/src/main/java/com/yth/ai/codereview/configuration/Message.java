package com.yth.ai.codereview.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.intellij.ide.util.PropertiesComponent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Message {

    public static String getMessage(String key) {
        String language = PropertiesComponent.getInstance()
                .getValue(PluginPropertiesEnum.LANGUAGE_PROPERTY.getPropertyName());
        String source = String.format("/messages/messages_%s.json", language == null ? "en_US": language);
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(Message.class.getResourceAsStream(source)),
                StandardCharsets.UTF_8
        )) {
            JsonObject messages = new Gson().fromJson(reader, JsonObject.class);
            return messages.get(key).getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
