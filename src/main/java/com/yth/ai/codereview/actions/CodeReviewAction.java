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
import com.yth.ai.codereview.client.OpenAIClient;
import com.yth.ai.codereview.client.OpenAIRequest;
import com.yth.ai.codereview.client.OpenAIResponse;
import com.yth.ai.codereview.configuration.CodeReviewSettings;
import com.yth.ai.codereview.configuration.Message;
import com.yth.ai.codereview.configuration.PluginPropertiesEnum;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        List<OpenAIRequest.Message> messages = new ArrayList<>();
        messages.add(new OpenAIRequest.Message("system", Message.getMessage("reviewCodeMessage")));
        messages.add(new OpenAIRequest.Message("user", selectedText));
//        messages.add(new OpenAIRequest.Message("assistant", Message.getMessage("role.assistant.codereviewresponse")));
        OpenAIRequest request = new OpenAIRequest(
                model.toLowerCase(),
                messages,
                Double.parseDouble(temperature)
        );

        OpenAIClient cliente = new OpenAIClient();
        OpenAIResponse response = cliente.request(request);
        if (response.choices != null) {
            String suggestion = this.mountAiSuggestions(response);
            CopyPasteManager.getInstance().setContents(new StringSelection(suggestion));
            displayClipBoardAISugestion();
        }
    }

    public String mountAiSuggestions(OpenAIResponse response) {
        StringBuilder sb = new StringBuilder();
        for (OpenAIResponse.Choice choice : response.getChoices()) {
            sb.append(choice.message.content);
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
}
