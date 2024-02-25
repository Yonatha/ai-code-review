package com.yth.ai.codereview.configuration;

public enum PluginPropertiesEnum {
    ENGINE("com.yth.ai.codereview.engine"),
    SECRET_KEY_PROPERTY("com.yth.ai.codereview.secretKey"),
    MODEL_PROPERTY("com.yth.ai.codereview.model"),
    LANGUAGE_PROPERTY("com.yth.ai.codereview.language"),
    TOKENS_PROPERTY("com.yth.ai.codereview.tokens"),
    TEMPERATURE_PROPERTY("com.yth.ai.codereview.temperature");

    private final String propertyName;

    private PluginPropertiesEnum(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}

