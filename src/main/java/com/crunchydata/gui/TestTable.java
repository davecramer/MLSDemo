package com.crunchydata.gui;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class TestTable extends JFrame {
  private JTable m_simpleTable;

  private SimpleTableModel m_simpleTableModel;

  public TestTable() {
    Vector dummyMacData = new Vector(10, 10);
    dummyMacData.addElement(new Data(new Integer(100), "A", "1","C", "E"));
    dummyMacData.addElement(new Data(new Integer(105), "R", "2","S", "E"));
    m_simpleTableModel = new SimpleTableModel(dummyMacData);
    m_simpleTable = new JTable(m_simpleTableModel);
    JScrollPane scrollPane = new JScrollPane(m_simpleTable);
    getContentPane().add(scrollPane);
  }

  public static void main(String[] arg) {
    TestTable m = new TestTable();

    m.setVisible(true);
    m.setSize(new Dimension(600, 300));
    m.validate();
  }

  class SimpleTableModel extends AbstractTableModel {
    public String[] m_colNames = { "A", "B", "C","D", "E" };

    public Class[] m_colTypes = { Integer.class, String.class, String.class, String.class,
        String.class };

    Vector m_macDataVector;

    public SimpleTableModel(Vector macDataVector) {
      super();
      m_macDataVector = macDataVector;
    }
    public int getColumnCount() {
      return m_colNames.length;
    }
    public int getRowCount() {
      return m_macDataVector.size();
    }
    public void setValueAt(Object value, int row, int col) {
      Data macData = (Data) (m_macDataVector.elementAt(row));

      switch (col) {
        case 0:
          macData.setA((Integer) value);
          break;
        case 1:
          macData.setB((String) value);
          break;
        case 2:
          macData.setC((String) value);
          break;
        case 3:
          macData.setD((String) value);
          break;
        case 4:
          macData.setE((String) value);
          break;
      }
    }

    public String getColumnName(int col) {
      return m_colNames[col];
    }

    public Class getColumnClass(int col) {
      return m_colTypes[col];
    }
    public Object getValueAt(int row, int col) {
      Data macData = (Data) (m_macDataVector.elementAt(row));

      switch (col) {
        case 0:
          return macData.getA();
        case 1:
          return macData.getB();
        case 2:
          return macData.getC();
        case 3:
          return macData.getD();
        case 4:
          return macData.getE();
      }

      return new String();
    }
  }

}

class Data {
  private Integer a;

  private String b;

  private String c;

  private String d;

  private String e;

  public Data() {
  }

  public Data(Integer aa, String bb, String cc, String dd, String ee) {
    a = aa;
    b = bb;
    c = cc;
    d = dd;
    e = ee;
  }

  public Integer getA() {
    return a;
  }

  public String getB() {
    return b;
  }

  public String getC() {
    return c;
  }

  public String getD() {
    return d;
  }

  public String getE() {
    return e;
  }

  public void setA(Integer aa) {
    a = aa;
  }

  public void setB(String macName) {
    b = macName;
  }

  public void setC(String cc) {
    c = cc;
  }

  public void setD(String dd) {
    d = dd;
  }

  public void setE(String ee) {
    e = ee;
  }
}
