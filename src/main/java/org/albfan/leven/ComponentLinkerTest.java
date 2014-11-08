package org.albfan.leven;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ComponentLinkerTest
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

    JPanel content = new JPanel();
    int rows = 10;
    int cols = 5;
    content.setLayout(new GridLayout(rows, cols, 5, 5));
    content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    
    ActionListener actionListener = new ActionListener() {
  	private JButton last = null;
      public void actionPerformed(ActionEvent e) {
    	JButton source = (JButton) e.getSource();
        if (last != null) {
		linker.link(last, source);
        }
        last = source;
      }
    };

    for (int i = 0; i < rows * cols; i++) {
      JButton button = new JButton("Button" + i);
      button.addActionListener(actionListener);
      content.add(button);
    }
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
