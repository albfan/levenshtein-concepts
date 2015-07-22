package org.albfan.leven;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

public class ComponentLinker {
    public static void main(String[] args) {
        setupLookAndFeel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setLocationRelativeTo(null);

        final LinkerGlassPane<JComponent> linker = new LinkerGlassPane<JComponent>() {

            @Override
            protected Point getPointFrom(JComponent t) {
                return getRectCenter(getBoundsInWindow(t));
            }

            private Point getRectCenter(Rectangle rect) {
                return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
            }

            private Rectangle getBoundsInWindow(JComponent component) {
                return getRelativeBounds(component, getRootPaneAncestor(component));
            }

            private Rectangle getRelativeBounds(JComponent component, JComponent relativeTo) {
                return new Rectangle(getRelativeLocation(component, relativeTo), component.getSize());
            }

            private Point getRelativeLocation(JComponent component, JComponent relativeTo) {
                Point los = component.getLocationOnScreen();
                Point rt = relativeTo.getLocationOnScreen();
                return new Point(los.x - rt.x, los.y - rt.y);
            }

            private JRootPane getRootPaneAncestor(JComponent c) {
                for (Container p = c.getParent(); p != null; p = p.getParent()) {
                    if ((p instanceof JRootPane)) {
                        return (JRootPane) p;
                    }
                }
                return null;
            }
        };
        frame.setGlassPane(linker);
        linker.setVisible(true);


        MouseInputAdapter m = new MouseInputAdapter() {
            JComponent last;

            public void mousePressed(MouseEvent event) {
                JComponent source = (JComponent) event.getSource();
                if (last != source) {
                    linker.link(source);
                }
                last = source;
                linker.setCurrent(source);
            }
        };

        JPanel content = new JPanel();
        int rows = 10;
        int cols = 5;
        content.setLayout(new GridLayout(rows, cols, 5, 5));
        content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < rows * cols; i++) {
            JLabel label = new JLabel("Label" + i);
            label.addMouseListener(m);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            content.add(label);
        }
        final JCheckBox check = new JCheckBox("Edit path");
        check.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                linker.setAllowEdit(check.isSelected());
            }
        });
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
