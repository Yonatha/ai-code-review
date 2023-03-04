package com.yth.ai.codereview.configuration;

import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

public class ValidateSettings {
    private static final int MAX_TEMPERATURE = 2048;

    public static boolean validateTemperatureInput(JTextField tokens) throws ConfigurationException {
        String temperatureText = tokens.getText().trim();
        String errorMessage = Message.getMessage("error_token_invalid_value");
        if (temperatureText.isEmpty()) {
            throw new ConfigurationException(errorMessage);
        }

        try {
            double number = Integer.parseInt(temperatureText);
            if (number > MAX_TEMPERATURE || number < 0) {
                throw new ConfigurationException(errorMessage);
            }
        } catch (NumberFormatException e) {
            throw new ConfigurationException(errorMessage);
        }
        return true;
    }

    public static boolean validateTemperatureField(JTextField temperature) throws ConfigurationException {
        String temperatureText = temperature.getText().trim();
        String errorMessage = Message.getMessage("error_temperature_invalid_value");
        if (temperatureText.isEmpty()) {
            throw new ConfigurationException(errorMessage);
        }

        try {
            double number = Double.parseDouble(temperatureText);
            if (number > 1.0 || number < 0.1) {
                throw new ConfigurationException(errorMessage);
            }
        } catch (NumberFormatException e) {
            throw new ConfigurationException(errorMessage);
        }
        return true;
    }
}
