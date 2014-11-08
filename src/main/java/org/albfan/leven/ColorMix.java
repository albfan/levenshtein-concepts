package org.albfan.leven;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: alberto
 * Date: 29/01/13
 * Time: 21:28
 * To change this template use File | Settings | File Templates.
 */
public class ColorMix {

    Vector<JPanel> colores;
    JComboBox comboBox;
    JPanel colorPanel;

    public ColorMix() {
        this.colores = new Vector<JPanel>();
        new Vector<String>();
        this.comboBox = new JComboBox(new DefaultComboBoxModel());
        this.colorPanel = new JPanel();
        buildGUI();
    }

    public void buildGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.fill = GridBagConstraints.BOTH;
        cc.insets = new Insets(5, 5, 5, 5);
        cc.weightx = .2;
        cc.weighty = 1;
        colorPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(buildColorPanel(), cc);
        frame.getContentPane().add(buildColorPanel(), cc);
        cc.weightx = .6;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints ccColor = new GridBagConstraints();
        ccColor.fill = GridBagConstraints.BOTH;
        ccColor.insets = new Insets(5, 5, 5, 5);
        ccColor.weightx = 1;
        ccColor.weighty = 1;
        panel.add(colorPanel, ccColor);
        ccColor.gridy = 1;
        ccColor.weighty = 0;
        panel.add(comboBox, ccColor);
        frame.getContentPane().add(panel, cc);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ColorMix();
    }

    private JComponent buildColorPanel() {
        final JPanel pColor = new JPanel();
        colores.add(pColor);
        pColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel pSelectColor = new JPanel(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.fill = GridBagConstraints.BOTH;
        cc.insets = new Insets(5, 5, 5, 5);
        cc.weightx = 1;
        cc.weighty = 1;
        final JSlider slidRed = buildSlider(pSelectColor, cc);
        final JSlider slidGreen = buildSlider(pSelectColor, cc);
        final JSlider slidBlue = buildSlider(pSelectColor, cc);
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pColor.setBackground(new Color(slidRed.getValue(), slidGreen.getValue(), slidBlue.getValue()));
                Color bg = new DefaultMixer().calculateMix(colores);
                colorPanel.setBackground(bg);
            }
        };
        slidRed.addChangeListener(changeListener);
        slidGreen.addChangeListener(changeListener);
        slidBlue.addChangeListener(changeListener);
        pSelectColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pSelectColor.add(pColor, cc);
        changeListener.stateChanged(null);
        return pSelectColor;
    }

    static class DefaultMixer {
        public Color calculateMix(Vector<JPanel> colores) {
            int red = 0;
            int green = 0;
            int blue = 0;
            for (int i = 0; i < colores.size(); i++) {
                Color background = colores.get(i).getBackground();
                red += background.getRed();
                green += background.getGreen();
                blue += background.getBlue();
            }
            return new Color(Math.min(255, red), Math.min(255, green), Math.min(255, blue));
        }
    }

    private JSlider buildSlider(JPanel container, GridBagConstraints upperCC) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.fill = GridBagConstraints.BOTH;
        cc.insets = new Insets(5, 5, 5, 5);
        cc.weightx = 1;
        cc.weighty = 0.7;

        final JSlider slider = new JSlider(JSlider.VERTICAL, 0, 255, 0);
        slider.setFont(new Font("Serif", Font.PLAIN, 4));

        Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
        labels.put(0, new JLabel("0"));
        labels.put(128, new JLabel("128"));
        labels.put(255, new JLabel("255"));
        panel.add(slider, cc);
        final JTextField field = new JTextField();
        field.setEditable(false);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                field.setText(String.valueOf(slider.getValue()));
            }
        });
        cc.gridx = 0;
        cc.gridy = 1;
        cc.weighty = 0;

        panel.add(field, cc);
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);

        container.add(panel, upperCC);

        return slider;
    }

    static class TertiaryMixer implements ColorMix.Mixer {
        public Color calculateMix(Vector<JLabel> colores) {
            Color background1 = ((JLabel) colores.get(0)).getBackground();
            int red = background1.getRed();
            int green = background1.getGreen();
            int blue = background1.getBlue();
            Color background2 = ((JLabel) colores.get(1)).getBackground();
            red -= background2.getRed();
            green -= background2.getGreen();
            blue -= background2.getBlue();
            return new Color(Math.min(255, background1.getRed() - red / 2), Math.min(255, background1.getGreen() - green / 2), background1.getBlue() - blue / 2);
        }

        public String toString() {
            return "Tertiary";
        }
    }

    static class DilutingSustractiveMixer implements ColorMix.Mixer {
        public Color calculateMix(Vector<JLabel> colores) {
            int red = 0;
            int green = 0;
            int blue = 0;
            for (int i = 0; i < colores.size(); i++) {
                Color background = ((JLabel) colores.get(i)).getBackground();
                red = (int) (red + Math.pow(255 - background.getRed(), 2.0D));
                green = (int) (green + Math.pow(255 - background.getGreen(), 2.0D));
                blue = (int) (blue + Math.pow(255 - background.getBlue(), 2.0D));
            }
            return new Color(Math.min(255, (int) Math.sqrt(red / colores.size())), Math.min(255, (int) Math.sqrt(green / colores.size())), Math.min(255, (int) Math.sqrt(blue / colores.size())));
        }

        public String toString() {
            return "Diluting/Sustractive";
        }
    }

    static class SustractiveMixer implements ColorMix.Mixer {
        public Color calculateMix(Vector<JLabel> colores) {
            int red = 1;
            int green = 1;
            int blue = 1;
            for (int i = 0; i < colores.size(); i++) {
                Color background = ((JLabel) colores.get(i)).getBackground();
                red *= background.getRed();
                green *= background.getGreen();
                blue *= background.getBlue();
            }
            return new Color(Math.min(255, red / 255), Math.min(255, green / 255), Math.min(255, blue / 255));
        }

        public String toString() {
            return "Sustractive";
        }
    }

    static class AdditiveMixer implements ColorMix.Mixer {
        public Color calculateMix(Vector<JLabel> colores) {
            int red = 0;
            int green = 0;
            int blue = 0;
            for (int i = 0; i < colores.size(); i++) {
                Color background = ((JLabel) colores.get(i)).getBackground();
                red += background.getRed();
                green += background.getGreen();
                blue += background.getBlue();
            }
            return new Color(Math.min(255, red), Math.min(255, green), Math.min(255, blue));
        }

        public String toString() {
            return "Additive";
        }
    }

    static abstract interface Mixer {
        public abstract Color calculateMix(Vector<JLabel> paramVector);
    }

    class NamedColor extends Color {
        private String name;

        NamedColor(Color color, String name) {
            super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}

