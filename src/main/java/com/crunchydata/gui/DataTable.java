package com.crunchydata.gui;

import com.crunchydata.model.Document;
import com.crunchydata.model.JDBCTableModel;
import com.crunchydata.postgres.PGServiceFile;
import com.crunchydata.service.QueryExecutor;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/*
BodyContentHandler handler = new ParseDoc().parseDoc(text);
import com.crunchydata.service.ParseDoc;
import org.apache.tika.sax.BodyContentHandler;


 */
public class DataTable extends JPanel {
    private static final Logger logger = Logger.getLogger(DataTable.class.getName());
    JDBCTableModel model = new JDBCTableModel();
    JTable table = new JTable();
    QueryExecutor queryExecutor;

    public DataTable(Dimension dimension) {
        setPreferredSize(dimension);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMinWidth(dimension.width/10);
        table.getColumnModel().getColumn(1).setMinWidth(dimension.width*8/10);
        add(table);
    }

    public void addRowSelectedListener(ListSelectionListener listSelectionListener) {
        table.getSelectionModel().addListSelectionListener(listSelectionListener);
    }
    public void setService(String service) {
        try {
            queryExecutor = QueryExecutor.getQueryExecutor(service);
            loadData();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    public void loadData() {
        List <Document>list = queryExecutor.getDocuments();
        Vector<Object> row = new Vector<Object>();
        final int[] rowNumber = {0};
        list.forEach(doc->{
            model.setValueAt(doc.getId(), rowNumber[0],0);
            model.setValueAt(doc.getName(), rowNumber[0]++,1);
        });
    }

 }
