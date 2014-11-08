package org.albfan.leven;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ColorComboRenderer extends JPanel
  implements ListCellRenderer
{
  protected Color m_c = Color.black;

  public ColorComboRenderer()
  {
    setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10, Color.white), new LineBorder(Color.black)));
  }

  public Component getListCellRendererComponent(JList list, Object obj, int row, boolean sel, boolean hasFocus)
  {
    if ((obj instanceof Color))
      m_c = ((Color)obj);
    return this;
  }

  public void paint(Graphics g) {
    setBackground(m_c);
    super.paint(g);
  }

  public static void main(String[] a) {
    JComboBox cbColor = new JComboBox();
    int[] values = { 0, 128, 192, 255 };
    for (int r = 0; r < values.length; r++)
      for (int g = 0; g < values.length; g++)
        for (int b = 0; b < values.length; b++) {
          Color c = new Color(values[r], values[g], values[b]);
          cbColor.addItem(c);
        }
    cbColor.setRenderer(new ColorComboRenderer());

    JFrame f = new JFrame();
    f.setDefaultCloseOperation(3);
    f.getContentPane().add(cbColor);
    f.pack();
    f.setSize(new Dimension(300, 80));
    f.setVisible(true);
  }
}
