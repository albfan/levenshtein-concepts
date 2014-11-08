package org.albfan.leven;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: alberto
 * Date: 29/01/13
 * Time: 21:28
 * To change this template use File | Settings | File Templates.
 */
public class ColorMix {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JPanel colorPanel = new JPanel();
        panel.add(buildColorPanel(colorPanel));
        panel.add(buildColorPanel(colorPanel));
        panel.add(colorPanel);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static JComponent buildColorPanel(JPanel colorPanel) {
        final JPanel colorSelectedPanel = new JPanel();
        JPanel selectColorPanel = new JPanel(new GridLayout(4,1));
        final JSlider slidRed = new JSlider(JSlider.VERTICAL, 0, 255, 0);
        selectColorPanel.add(slidRed);
        final JSlider slidGreen = new JSlider(JSlider.VERTICAL, 0, 255, 0);
        selectColorPanel.add(slidGreen);
        final JSlider slidBlue = new JSlider(JSlider.VERTICAL, 0, 255, 0);
        selectColorPanel.add(slidBlue);
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                colorSelectedPanel.setBackground(new Color(slidRed.getValue(), slidGreen.getValue(), slidBlue.getValue()));
            }
        };
        slidRed.addChangeListener(changeListener);
        slidGreen.addChangeListener(changeListener);
        slidBlue.addChangeListener(changeListener);
        selectColorPanel.add(slidRed);
        selectColorPanel.add(slidGreen);
        selectColorPanel.add(slidBlue);
        selectColorPanel.add(colorSelectedPanel);
        return selectColorPanel;
    }
}
