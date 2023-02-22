package com.yth.ai.codereview.actions;

import com.intellij.diff.actions.CompareClipboardWithSelectionAction;
import com.intellij.ide.DataManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.yth.ai.codereview.PluginNotifier;
import com.yth.ai.codereview.client.OpenAIClient;
import com.yth.ai.codereview.client.OpenAIRequest;
import com.yth.ai.codereview.client.OpenAIResponse;
import com.yth.ai.codereview.configuration.CodeReviewSettings;
import com.yth.ai.codereview.configuration.Message;
import com.yth.ai.codereview.configuration.PluginPropertiesEnum;

import java.awt.datatransfer.StringSelection;
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
        String model = Objects.requireNonNull(propertiesComponent.getValue(PluginPropertiesEnum.MODEL_PROPERTY.getPropertyName())).toLowerCase();
        double temperature = Double.parseDouble(propertiesComponent.getValue(PluginPropertiesEnum.TEMPERATURE_PROPERTY.getPropertyName()));
        int maxTokens = Integer.parseInt(propertiesComponent.getValue(PluginPropertiesEnum.TOKENS_PROPERTY.getPropertyName()));

        if (secretKey == null || secretKey.isEmpty()) {
            PluginNotifier.notifyError(editor.getProject(), Message.getMessage("error_secrekey_notfound"));
            return;
        }

        String prompt = String.format("%s %s",
                Message.getMessage("reviewCodeMessage"),
                selectedText
        );

        OpenAIRequest request = new OpenAIRequest(
                model,
                prompt,
                temperature,
                maxTokens,
                1,
                0,
                0
        );

        OpenAIClient cliente = new OpenAIClient();
        OpenAIResponse responce = cliente.request(request);
        if (responce.choices != null) {
            String sugestion = this.mountAiSugestions(responce);
            CopyPasteManager.getInstance().setContents(new StringSelection(sugestion));
            displayClipBoardAISugestion();
        }
    }

    public String mountAiSugestions(OpenAIResponse response) {
        StringBuilder sb = new StringBuilder();
        for (OpenAIResponse.Choice choice : response.getChoices()) {
            sb.append(choice.text);
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
