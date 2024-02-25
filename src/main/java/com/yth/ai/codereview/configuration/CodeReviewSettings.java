package com.yth.ai.codereview.configuration;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import com.yth.ai.codereview.client.OpenAI.OpenAIClient;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@State(
        name = "CodeReviewSettings",
        storages = {
                @Storage("CodeReviewSettings.form")
        }
)
public class CodeReviewSettings implements Configurable {

    private Editor editor;

    private static final String ENGINE = "com.yth.ai.codereview.engine";
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
    private JRadioButton aiEngineGemini;
    private JLabel linkGetOpenAISecretKey;
    private JButton testConnectionButton;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "My Plugin Settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        linkGetOpenAISecretKey.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://platform.openai.com/account/api-keys"));
                    } catch (IOException | URISyntaxException ex) {
                        try {
                            throw new ConfigurationException("Could not open link, please access https://platform.openai.com/account/api-keys");
                        } catch (ConfigurationException exc) {
                            throw new RuntimeException(exc);
                        }
                    }
                }
            }
        });

        testConnectionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    try {
                        apply();

                        boolean isConnected = OpenAIClient.testConnection();
                        Notification notification;
                        if (isConnected) {
                            notification = new Notification(
                                    "notification.codereview",
                                    "AI Code Review",
                                    Message.getMessage("success_test_connection"),
                                    NotificationType.INFORMATION);
                        } else {
                            notification = new Notification(
                                    "notification.codereview",
                                    "AI Code Review",
                                    Message.getMessage("error_test_connection"),
                                    NotificationType.ERROR);
                        }
                        Notifications.Bus.notify(notification);
                    } catch (ConfigurationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
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

        ValidateSettings.validateTemperatureInput(tokens);
        ValidateSettings.validateTemperatureField(temperature);

        String engineSelected = aiEngineChatGpt.getText();
        if (aiEngineGemini.isSelected())
            engineSelected = aiEngineGemini.getText();

        propertiesComponent.setValue(ENGINE, engineSelected);
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
