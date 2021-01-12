package com.crunchydata.gui;

import javax.swing.*;

public class LoggerPanel extends JPanel {
    JTextArea loggerText;
    public LoggerPanel() {
        loggerText = new JTextArea();
        add(new JScrollPane(loggerText));
    }
    public JTextArea getLoggerText() {
        return loggerText;
    }
}
