package com.yth.ai.codereview.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CodeReviewConfiguration implements Configurable {
    private JPanel panel;
    private JTextField secretKeyField;
    private JRadioButton radioButtonIngles;
    private JRadioButton radioButtonPortugues;
    private JPanel panel1;
    private JLabel mainPanelTitleLbl;
    private JLabel secretKeyLbl;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Configurações do Meu Plugin";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        panel = new JPanel(new GridLayout(0, 1));
        secretKeyField = new JPasswordField();
        secretKeyField.setSize(300,20);
        panel.add(new JLabel("Secret Key:"));
        panel.add(new JLabel("Token bearer provided by your OpenAI account. Ex: xxxxxxxxxxxxxxxmxxxxxxxxxxxxxxxxxxxx"));
        panel.add(secretKeyField);
        panel.add(panel1);

        ButtonGroup buttonGroup = new ButtonGroup();
        radioButtonIngles = new JRadioButton("English");
        radioButtonPortugues = new JRadioButton("Português (Brasil)");
        buttonGroup.add(radioButtonIngles);
        buttonGroup.add(radioButtonPortugues);
        panel.add(new JLabel("AI Interaction Language"));
        panel.add(radioButtonIngles);
        panel.add(radioButtonPortugues);

        return panel;
    }

    @Override
    public boolean isModified() {
        // Lógica para determinar se os valores foram modificados desde a última vez que as configurações foram salvas
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        // Lógica para aplicar as configurações ao plugin
    }

    @Override
    public void reset() {
        // Lógica para redefinir os valores das configurações para o padrão
    }

    @Override
    public void disposeUIResources() {
        panel = null;
        secretKeyField = null;
        radioButtonIngles = null;
        radioButtonPortugues = null;
    }
}

