package com.crunchydata.gui;

import com.crunchydata.FileChosen;
import com.crunchydata.model.Document;
import com.crunchydata.postgres.PGServiceFile;
import com.crunchydata.service.DocumentService;
import com.crunchydata.service.ParseDoc;
import com.crunchydata.service.QueryExecutor;
import org.apache.tika.sax.BodyContentHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UpperPanel extends JSplitPane {
    private static final Logger logger = Logger.getLogger(UpperPanel.class.getName());
    PGServiceFile pgServiceFile;
    DataTable dataTable;
    UpdateAble updateAble;
    String service = "mlstest";

    public UpperPanel(int width, int height, UpdateAble updateAble) {
        DocumentService documentService = new DocumentService();
        this.updateAble = updateAble;
        orientation = VERTICAL_SPLIT;
        setOneTouchExpandable(true);
        setDividerLocation(height/2);

        setLayout(new FlowLayout());
        JPanel upper = new JPanel();
        upper.setPreferredSize(new Dimension(width,height/4));
        JPanel lower = new JPanel();
        lower.setPreferredSize(new Dimension(width,height/2 ));
        dataTable = new DataTable(lower.getPreferredSize());
        JScrollPane dataScrollPane = new JScrollPane(dataTable);
        dataScrollPane.setPreferredSize(lower.getPreferredSize());


        try {
            pgServiceFile = PGServiceFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFilePicker filePicker =  new JFilePicker("File", "Browse");
        // access JFileChooser class directly
        JFileChooser fileChooser = filePicker.getFileChooser();
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".pdf", "PDF files");
        String homeDirectory = System.getProperty("user.home");
        fileChooser.setCurrentDirectory(new File(homeDirectory + "/Downloads"));
        filePicker.addActionListener(new FileChosen() {
             @Override
             public void save(String fileName) {
                 try {
                     Document document = documentService.insertDocument(fileName, service);
                     documentService.updateTsVector(document, service);
                     BodyContentHandler handler = new ParseDoc().parseDoc(document);
                     updateAble.update(handler.toString());

                 } catch (Exception e) {
                     logger.info(e.getMessage());
                 }
             }
         });
        // Upper contains the file picker and the service picker
        // as well as the data scroll pane, which is under it
        upper.add(filePicker);
        setPreferredSize(new Dimension(width, height/2));
        JComboBox comboBox = new JComboBox();
        Map<String, Map<String, String>>services = pgServiceFile.getSections();
        services.keySet().forEach(key-> {
            comboBox.addItem(key);
        });
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if ( e.getStateChange() == ItemEvent.SELECTED ) {
                    setService(e.getItem().toString());
                }
            }
        });


        dataTable.addRowSelectedListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    try {
                        int index = dataTable.table.getSelectedRow();
                        Document document = dataTable.model.getDocument(index);
                        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(service);
                        BodyContentHandler handler = new ParseDoc().parseDoc(queryExecutor, document.getId());
                        updateAble.update(handler.toString());
                    } catch(Exception ex ){
                        logger.log(Level.INFO, ex.getMessage());
                    }
                }

            }
        });
        // add the combo box
        upper.add(comboBox);
        // add the data table
        upper.add(dataScrollPane);
        setTopComponent(upper);
        setBottomComponent(lower);
    }
    void setService(String service) {
        logger.log(Level.INFO, "Service: {0}", service);
        this.service = service;
        dataTable.setService(service);
    }
}
