package com.crunchydata.model;

import com.crunchydata.postgres.PGServiceFile;
import com.crunchydata.service.QueryExecutor;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCTableModel extends AbstractTableModel {
    QueryExecutor queryExecutor;
    List<Document> documents;

    public JDBCTableModel() {
        try {
            PGServiceFile pgServiceFile = PGServiceFile.load();
            pgServiceFile.getSections();
            setService(pgServiceFile.getService("mlstest"));
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setService(Map<String, String> service) throws SQLException{
        queryExecutor = QueryExecutor.getQueryExecutor(service);
        documents = queryExecutor.getDocuments();
    }

    void updateModel() {

    }
    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return documents.size();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Document document = documents.get(rowIndex);
        if (columnIndex == 0 ){
            return document.getId();
        }else {
            return document.getName();
        }
    }

    /**
     * Returns a default name for the column using spreadsheet conventions:
     * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     * returns an empty string.
     *
     * @param column the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column) {
        if (column == 0 ) return "ID";
        if (column == 1 ) return "Name";
        return super.getColumnName(column);
    }

    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);
    }

    public Document getDocument(int row) {
        return documents.get(row);
    }
}
