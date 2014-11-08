package org.albfan.leven;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import model.LevenshteinTableModel;

public class LevenshteinGraphFrame
{
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel(new GridLayout(1, 2));
    final JTextArea textAreaOrigin = new JTextArea();
    final JTextArea textAreaDestiny = new JTextArea();
    textAreaOrigin.setPreferredSize(new Dimension(1, 200));
    textAreaDestiny.setPreferredSize(new Dimension(1, 200));
    JScrollPane jScrollPane = new JScrollPane(textAreaOrigin);
    jScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    panel.add(jScrollPane);
    panel.add(new JScrollPane(textAreaDestiny));
    JSplitPane splitPane = new JSplitPane(0);
    splitPane.setTopComponent(panel);
    splitPane.setContinuousLayout(true);
    splitPane.setDividerLocation(0.1D);
    final LevenshteinTableModel model = new LevenshteinTableModel(textAreaOrigin.getText(), textAreaDestiny.getText());
    DocumentListener listener = new DocumentListener()
    {
      public void insertUpdate(DocumentEvent e) {
        onChange(e);
      }

      public void removeUpdate(DocumentEvent e)
      {
        onChange(e);
      }

      public void changedUpdate(DocumentEvent e)
      {
        onChange(e);
      }

      private void onChange(DocumentEvent e) {
        model.setOrigin(textAreaOrigin.getText());
        model.setDestiny(textAreaDestiny.getText());
      }
    };
    textAreaOrigin.getDocument().addDocumentListener(listener);
    textAreaDestiny.getDocument().addDocumentListener(listener);

    final JSpinner spinner = new JSpinner(new SpinnerNumberModel(15, 5, 2147483647, 1));

    final JTable table = new JTable(model)
    {
      public boolean getScrollableTracksViewportWidth()
      {
        boolean ok = false;
        if ((autoResizeMode != 0) && 
          ((getParent() instanceof JViewport))) {
          int parentWidth = getParent().getWidth();
          int tableWidth = getPreferredSize().width;
          ok = parentWidth > tableWidth;
        }

        return ok;
      }

      public void addColumn(TableColumn aColumn)
      {
        aColumn.setPreferredWidth(((Integer)spinner.getValue()).intValue());
        super.addColumn(aColumn);
      }

      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    spinner.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e) {
        TableColumnModel cm = table.getColumnModel();
        Integer preferredWidth = (Integer)spinner.getValue();
        for (int i = 0; i < cm.getColumnCount(); i++)
          cm.getColumn(i).setPreferredWidth(preferredWidth.intValue());
      }
    });
    table.setTableHeader(null);
    table.setDefaultRenderer(Object.class, model.getCellRenderer());
    JPanel panelGraph = new JPanel(new BorderLayout());
    panelGraph.add(new JScrollPane(table));
    JPanel bPanel = new JPanel(new FlowLayout(3));
    bPanel.add(new JLabel("Min Column Width"));
    bPanel.add(spinner);
    panelGraph.add(bPanel, "South");
    splitPane.setBottomComponent(panelGraph);
    table.setFillsViewportHeight(true);
    frame.getContentPane().add(splitPane);
    frame.pack();
    frame.setVisible(true);
  }
}
