package com.crunchydata.gui;

import com.crunchydata.model.Document;
import com.crunchydata.service.QueryExecutor;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class LowerPanel extends JPanel implements UpdateAble{
    JTextArea jTextArea = null;
    private static final Logger logger = Logger.getLogger(LowerPanel.class.getName());
    JSplitPane jSplitPane;
    LoggerPanel loggerPanel;
    TextAreaLogger textAreaLogger;
    public LowerPanel(int width, int height) {

        jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane.setDividerLocation(height/3);
        loggerPanel = new LoggerPanel();
        loggerPanel.getLoggerText().setPreferredSize(new Dimension(width, height/3));
        textAreaLogger = new TextAreaLogger(loggerPanel.getLoggerText());

        jTextArea = new JTextArea();
        jTextArea.setPreferredSize(new Dimension(width, height/3));

        JScrollPane jScrollPane = new JScrollPane(jTextArea, VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setPreferredSize(new Dimension(width,height/2));
        jSplitPane.setTopComponent(jScrollPane);
        jSplitPane.setBottomComponent(loggerPanel);
        this.add(jSplitPane);
        Logger rootLogger = LogManager.getLogManager().getLogger("");

        rootLogger.addHandler(textAreaLogger);
        logger.log(Level.INFO, "Hello world");
    }

    @Override
    public void update(String text) {
        try {
            QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor("");
            InputStream stream = new FileInputStream(text);
            byte []bytes = new byte[stream.available()];
            stream.read(bytes);
            Document document = new Document();
            document.setName(text);
            document.setFileBytes(bytes);
            queryExecutor.updateDoc(document);
        } catch (Exception e) {
            logger.fine("Error getting query Executor: " + e.getMessage() );
        }
        jTextArea.setText(text);
    }
}
