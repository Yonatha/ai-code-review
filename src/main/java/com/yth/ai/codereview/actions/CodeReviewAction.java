package com.yth.ai.codereview.actions;

import com.intellij.diff.actions.CompareClipboardWithSelectionAction;
import com.intellij.ide.DataManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.yth.ai.codereview.client.GoogleAI.Candidate;
import com.yth.ai.codereview.client.GoogleAI.Dto.Content;
import com.yth.ai.codereview.client.GoogleAI.Dto.SafetySetting;
import com.yth.ai.codereview.client.GoogleAI.GeminiAIClient;
import com.yth.ai.codereview.client.GoogleAI.GeminiAIRequest;
import com.yth.ai.codereview.client.GoogleAI.GeminiAIResponse;
import com.yth.ai.codereview.client.GoogleAI.Dto.*;
import com.yth.ai.codereview.client.OpenAI.OpenAIClient;
import com.yth.ai.codereview.client.OpenAI.OpenAIRequest;
import com.yth.ai.codereview.client.OpenAI.OpenAIResponse;
import com.yth.ai.codereview.configuration.CodeReviewSettings;
import com.yth.ai.codereview.configuration.Message;
import com.yth.ai.codereview.configuration.PluginPropertiesEnum;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class CodeReviewAction extends AnAction {

    private Editor editor;

    @Override
    public void actionPerformed(AnActionEvent e) {
        editor = e.getData(CommonDataKeys.EDITOR);

        if (editor == null)
            return;

        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        if (selectedText == null || selectedText.isEmpty())
            return;

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        String engine = propertiesComponent.getValue(PluginPropertiesEnum.ENGINE.getPropertyName());
        String secretKey = propertiesComponent.getValue(PluginPropertiesEnum.SECRET_KEY_PROPERTY.getPropertyName());
        String model = propertiesComponent.getValue(PluginPropertiesEnum.MODEL_PROPERTY.getPropertyName());
        String temperature = propertiesComponent.getValue(PluginPropertiesEnum.TEMPERATURE_PROPERTY.getPropertyName());
        String maxTokens = propertiesComponent.getValue(PluginPropertiesEnum.TOKENS_PROPERTY.getPropertyName());

        if (secretKey == null || secretKey.isEmpty()) {
            Notification notification = new Notification(
                    "notification.codereview",
                    "AI Code Review",
                    Message.getMessage("error_secrekey_notfound"),
                    NotificationType.INFORMATION);

            notification.addAction(new NotificationAction("Configure") {
                @Override
                public void actionPerformed(AnActionEvent e, Notification notification) {
                    ShowSettingsUtil.getInstance().showSettingsDialog(editor.getProject(), CodeReviewSettings.class);
                }
            });

            Notifications.Bus.notify(notification, editor.getProject());
            return;
        }

        if (engine.equals("ChatGPT")) {
            openAiMountRequest(model, temperature, selectedText);
        } else if (engine.equals("Gemini")) {
            geminiMoutRequest(selectedText);
        }
    }

    public String mountAiSuggestions(OpenAIResponse response) {
        StringBuilder sb = new StringBuilder();
        for (OpenAIResponse.Choice choice : response.getChoices()) {
            sb.append(choice.message.content);
        }
        return sb.toString();
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

    private void displayClipBoardAISugestion() {
        CompareClipboardWithSelectionAction compareAction = new CompareClipboardWithSelectionAction();
        DataContext dataContext = DataManager.getInstance().getDataContext(editor.getComponent());
        AnActionEvent compareEvent = AnActionEvent.createFromAnAction(
                compareAction,
                null,
                ActionPlaces.UNKNOWN,
                dataContext
        );

        compareAction.actionPerformed(compareEvent);
    }

    public void openAiMountRequest(String model, String temperature, String selectedText) {
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
        if (response.choices != null) {
            String suggestion = this.mountAiSuggestions(response);
            CopyPasteManager.getInstance().setContents(new StringSelection(suggestion));
            displayClipBoardAISugestion();
        }
    }

    public void geminiMoutRequest(String selectedText) {

        GeminiAIClient client = new GeminiAIClient();

        GeminiAIRequest request = new GeminiAIRequest();
        Content content = new Content();
        Part part = new Part();

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
        System.out.println(response);

        if (!response.getCandidates().isEmpty()) {
            String suggestion = this.mountAiSuggestions(response.getCandidates());
            System.out.println(suggestion);
            CopyPasteManager.getInstance().setContents(new StringSelection(suggestion));
            displayClipBoardAISugestion();
        }
    }
}
