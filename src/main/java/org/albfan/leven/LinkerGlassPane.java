package org.albfan.leven;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JRootPane;

/**
 * GlassPane with links between underliying components
 * User: alberto
 * Date: 12/12/12
 * Time: 8:36
 */
public class LinkerGlassPane extends JComponent {
    private Vector<JComponent> linked;
    private int current=-1;

    boolean allowAdd;

    int SIZE = 10;

    int RADIUS = 6;

    public LinkerGlassPane() {
        linked = new Vector<JComponent>();
    }

    public void link(JComponent comp) {
        if (isAllowAdd()) {
            linked.add(comp);
            repaint();
        }
    }

    public boolean isAllowAdd() {
        return allowAdd;
    }

    public void setAllowAdd(boolean allowAdd) {
        this.allowAdd = allowAdd;
        current = -1;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(5.0F));

        if (linked.size() > 0) {
            JComponent c1 = linked.get(0);
            for (int i = 1; i < linked.size(); i++) {
                JComponent c2 = linked.get(i);
                Point p1 = getRectCenter(getBoundsInWindow(c1));
                Point p2 = getRectCenter(getBoundsInWindow(c2));
                g2d.setPaint(new Color(0, 0, 0, 0.3f));
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                int x = p2.x - RADIUS;
                int y = p2.y - RADIUS;
                int w = 2 * RADIUS;
                int h = w;
                g2d.setColor(new Color(0, 0, 1, 0.6f));
                Ellipse2D.Double shape = new Ellipse2D.Double(x, y, w, h);
                g2d.fill(shape);
                g2d.setColor(new Color(0, 0, 1, 0.3f).darker().darker());
                g2d.draw(shape);
                c1 = c2;
            }
        }
    }

    private Point getRectCenter(Rectangle rect) {
        return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
    }

    private Rectangle getBoundsInWindow(Component component) {
        return getRelativeBounds(component, getRootPaneAncestor(component));
    }

    private Rectangle getRelativeBounds(Component component, Component relativeTo) {
        return new Rectangle(getRelativeLocation(component, relativeTo), component.getSize());
    }

    private Point getRelativeLocation(Component component, Component relativeTo)
    {
        Point los = component.getLocationOnScreen();
        Point rt = relativeTo.getLocationOnScreen();
        return new Point(los.x - rt.x, los.y - rt.y);
    }

    private JRootPane getRootPaneAncestor(Component c) {
        for (Container p = c.getParent(); p != null; p = p.getParent()) {
            if ((p instanceof JRootPane)) {
                return (JRootPane)p;
            }
        }
        return null;
    }

    public boolean contains(int x, int y) {
        return false;
    }

    public ActionListener getLinkerListener() {
        return new ActionListener() {
            private JComponent last = null;
            public void actionPerformed(ActionEvent e) {
                JComponent source = (JComponent) e.getSource();
                if (last != source) {
                    link(source);
                }
                last = source;
            }
        };
    }

    public void setCurrent(MouseEvent event) {
        for (int i = 0; i < linked.size(); i++) {
            if (linked.get(i) == event.getSource()) {
                current = i;
                return;
            }
        }
        if (current != -1 && linked.get(current) != event.getSource()) {
            moveCurrent(event);
        }
    }

    public void moveCurrent(MouseEvent event) {
        if (current == -1) return;

        JComponent comp = (JComponent) event.getSource();
        if (comp != linked.get(current)) {
            linked.set(current, comp);
            repaint();
        }
    }
}
