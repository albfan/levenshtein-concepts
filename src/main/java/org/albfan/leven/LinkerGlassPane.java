package org.albfan.leven;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.Vector;
import javax.swing.JComponent;

/**
 * GlassPane with links between underliying components
 * User: alberto
 * Date: 12/12/12
 * Time: 8:36
 */
public abstract class LinkerGlassPane<T> extends JComponent {
    private Vector<T> linked;
    private int current=-1;

    boolean allowEdit;

    int SIZE = 10;

    int RADIUS = 6;
    private Color LINE_COLOR = new Color(0, 0, 0, 0.3f);
    private Color POINT_COLOR = new Color(0, 0, 1, 0.6f);
    private Color LINE_POINT_COLOR = new Color(0, 0, 1, 0.3f).darker().darker();
    private Color POINT_CURRENT_COLOR = new Color(1, 0, 0, 0.6f);
    private Color LINE_POINT_CURRENT_COLOR = new Color(1, 0, 0, 0.3f).darker().darker();
    private int NO_SELECTION = -1;

    public LinkerGlassPane() {
        linked = new Vector<T>();
    }

    public void link(T comp) {
        if (!isAllowEdit()) {
            linked.add(comp);
            repaint();
        }
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
        current = NO_SELECTION;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(5.0F));

        if (linked.size() > 0) {
            T c1 = linked.get(0);
            for (int i = 1; i < linked.size(); i++) {
                T c2 = linked.get(i);
                Point p1 = getPointFrom(c1);
                Point p2 = getPointFrom(c2);
                g2d.setPaint(LINE_COLOR);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                int x = p2.x - RADIUS;
                int y = p2.y - RADIUS;
                int w = 2 * RADIUS;
                int h = w;
                Color point_color;
                Color line_point_color;
                if (i == current) {
                    point_color = POINT_CURRENT_COLOR;
                    line_point_color = LINE_POINT_CURRENT_COLOR;
                } else {
                    point_color = POINT_COLOR;
                    line_point_color = LINE_POINT_COLOR;
                }
                g2d.setColor(point_color);
                Ellipse2D.Double shape = new Ellipse2D.Double(x, y, w, h);
                g2d.fill(shape);
                g2d.setColor(line_point_color);
                g2d.draw(shape);
                c1 = c2;
            }
        }
    }

    protected abstract Point getPointFrom(T t);

    public boolean contains(int x, int y) {
        return false;
    }

    public void setCurrent(T t) {
        if (!isAllowEdit()) return;

        for (int i = 0; i < linked.size(); i++) {
            if (linked.get(i) == t) {
                current = i;
                break;
            }
        }
        if (current != NO_SELECTION && linked.get(current) != t) {
            moveCurrent(t);
        } else {
            repaint();
        }
    }

    public void moveCurrent(T t) {
        if (current == NO_SELECTION) return;

        if (!t.equals(linked.get(current))) {
            linked.set(current, t);
            repaint();
        }
    }
}