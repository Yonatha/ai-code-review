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
import com.yth.ai.codereview.client.GoogleAI.GeminiService;
import com.yth.ai.codereview.client.OpenAI.ChatGPTService;
import com.yth.ai.codereview.configuration.CodeReviewSettings;
import com.yth.ai.codereview.configuration.Message;
import com.yth.ai.codereview.configuration.PluginPropertiesEnum;

import java.awt.datatransfer.StringSelection;

public class CodeReviewAction extends AnAction {
    private Editor editor;
    private String engine;
    private String secretKey;
    private String model;
    private String temperature;
    private String maxTokens;
    
    private CodeReviewAction(){
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        engine = propertiesComponent.getValue(PluginPropertiesEnum.ENGINE.getPropertyName());
        secretKey = propertiesComponent.getValue(PluginPropertiesEnum.SECRET_KEY_PROPERTY.getPropertyName());
        model = propertiesComponent.getValue(PluginPropertiesEnum.MODEL_PROPERTY.getPropertyName());
        temperature = propertiesComponent.getValue(PluginPropertiesEnum.TEMPERATURE_PROPERTY.getPropertyName());
        maxTokens = propertiesComponent.getValue(PluginPropertiesEnum.TOKENS_PROPERTY.getPropertyName());
    }
    @Override
    public void actionPerformed(AnActionEvent e) {
        editor = e.getData(CommonDataKeys.EDITOR);

        if (editor == null)
            return;

        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        if (selectedText == null || selectedText.isEmpty())
            return;

        if (!isComplianceSecret())
            return;

        String suggestion = getSuggestion(selectedText);

        if (suggestion != null) {
            CopyPasteManager.getInstance().setContents(new StringSelection(suggestion));
            displayClipBoardAISugestion();
        }
    }

    public boolean isComplianceSecret(){
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
            return false;
        }

        return true;
    }

    public String getSuggestion(String selectedText) {
        if (engine.equals("ChatGPT")) {
            ChatGPTService chatGPTService = new ChatGPTService();
            return chatGPTService.openAiMountRequest(model, temperature, selectedText);
        }

        GeminiService geminiService = new GeminiService();
        return geminiService.geminiMoutRequest(selectedText);
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
}
