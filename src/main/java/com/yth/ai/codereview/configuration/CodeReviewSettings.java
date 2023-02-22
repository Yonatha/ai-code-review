package com.yth.ai.codereview.configuration;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@State(
        name = "CodeReviewSettings",
        storages = {
                @Storage("CodeReviewSettings.form")
        }
)
public class CodeReviewSettings implements Configurable {

    private Editor editor;

    private static final String SECRET_KEY_PROPERTY = "com.yth.ai.codereview.secretKey";
    private static final String MODEL_PROPERTY = "com.yth.ai.codereview.model";
    private static final String LANGUAGE_PROPERTY = "com.yth.ai.codereview.language";
    private static final String TOKENS_PROPERTY = "com.yth.ai.codereview.tokens";
    private static final String TEMPERATURE_PROPERTY = "com.yth.ai.codereview.temperature";

    private JPanel panel;
    private JTextField secretKeyField;
    private JComboBox languageCombo;
    private JTextField tokens;
    private JTextField temperature;
    private JComboBox modelField;
    private JRadioButton aiEngineChatGpt;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "My Plugin Settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String secretKey = getSecretToken();
        this.validateTokensField();
        this.validateTemperatureField();

        propertiesComponent.setValue(SECRET_KEY_PROPERTY, secretKey);
        propertiesComponent.setValue(MODEL_PROPERTY, (String) modelField.getSelectedItem());
        propertiesComponent.setValue(LANGUAGE_PROPERTY, (String) languageCombo.getSelectedItem());
        propertiesComponent.setValue(TOKENS_PROPERTY, tokens.getText());
        propertiesComponent.setValue(TEMPERATURE_PROPERTY, temperature.getText());
    }

    @Override
    public void reset() {
        String secretKey = PropertiesComponent.getInstance().getValue(SECRET_KEY_PROPERTY);
        secretKeyField.setText(secretKey);
    }

    @Override
    public void disposeUIResources() {
        // Implement this method to dispose of any resources used by the UI
    }

    public void createUIComponents() {
        languageCombo = new JComboBox();
        languageCombo.addItem("English");
        languageCombo.addItem("Portuguese (Brasil)");
        languageCombo.addItem("Spanish");
    }

    public String getSecretToken() {
        if (!secretKeyField.getText().isEmpty()) {
            return secretKeyField.getText()
                    .replace("bearer ", "")
                    .replace("Bearer ", "");
        }
        return "";
    }

    public boolean validateTokensField() {
        String temperatureText = this.tokens.getText().trim();
        String errorMessage = Message.getMessage("error_token_invalid_value");
        if (temperatureText.isEmpty()) {
            Messages.showMessageDialog(errorMessage, "Error", Messages.getWarningIcon());
            return false;
        }

        try {
            double number = Integer.parseInt(temperatureText);
            if (number > 2048 || number < 0) {
                Messages.showMessageDialog(errorMessage, "Error", Messages.getWarningIcon());
                return false;
            }
        } catch (NumberFormatException e) {
            Messages.showMessageDialog(errorMessage, "Error", Messages.getWarningIcon());
            return false;
        }
        return true;
    }

    public boolean validateTemperatureField() {
        String temperatureText = this.temperature.getText().trim();
        String errorMessage = Message.getMessage("error_temperature_invalid_value");
        if (temperatureText.isEmpty()) {
            Messages.showMessageDialog(errorMessage, "Error", Messages.getWarningIcon());
            return false;
        }

        try {
            double number = Double.parseDouble(temperatureText);
            if (number > 1.0 || number < 0.1) {
                Messages.showMessageDialog(errorMessage, "Error", Messages.getWarningIcon());
                return false;
            }
        } catch (NumberFormatException e) {
            Messages.showMessageDialog(errorMessage, "Error", Messages.getWarningIcon());
            return false;
        }
        return true;
    }
}
