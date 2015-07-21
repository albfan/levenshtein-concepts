package org.albfan.leven;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

public class ComponentLinker
{
  public static void main(String[] args)
  {
    setupLookAndFeel();

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(3);
    frame.setLocationRelativeTo(null);

    final LinkerGlassPane linker = new LinkerGlassPane();
    frame.setGlassPane(linker);
    linker.setVisible(true);


    MouseInputAdapter m = new MouseInputAdapter() {

      public void mousePressed(MouseEvent event) {
        linker.setCurrent(event);
      }
    };

    JPanel content = new JPanel();
    int rows = 10;
    int cols = 5;
    content.setLayout(new GridLayout(rows, cols, 5, 5));
    content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    ActionListener actionListener = linker.getLinkerListener();

    for (int i = 0; i < rows * cols; i++) {
      JButton button = new JButton("Button" + i);
      button.addActionListener(actionListener);
      button.addMouseListener(m);
      content.add(button);
    }
    final JCheckBox check = new JCheckBox("allow add");
    check.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        linker.setAllowAdd(check.isSelected());
      }
    });
    check.setSelected(true);
    content.add(check);
    frame.add(content);

    frame.pack();
    frame.setVisible(true);
  }

  private static void setupLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }
}
