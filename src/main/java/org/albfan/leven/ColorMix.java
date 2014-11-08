package org.albfan.leven;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    Vector<JLabel> firstMixColors;
    Vector<JLabel> secondMixColors;
    JComboBox comboBox;
    JLabel firstMixColor;
    JLabel firstSel;
    JLabel secondSel;
    JLabel finalColor;

    public ColorMix() {
        firstMixColors = new Vector();
        Vector mixers = new Vector();
        mixers.add(new ColorMix.AdditiveMixer());
        mixers.add(new ColorMix.SustractiveMixer());
        mixers.add(new ColorMix.TertiaryMixer());
        mixers.add(new ColorMix.DilutingSustractiveMixer());

        comboBox = new JComboBox(new DefaultComboBoxModel(mixers));
        firstMixColor = buildColorLabel();
        firstSel = buildColorLabel();
        secondSel = buildColorLabel();
        secondMixColors = new Vector();
        secondMixColors.add(firstSel);
        secondMixColors.add(secondSel);
        finalColor = buildColorLabel();
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ColorMix.this.calculateMixes();
            }
        });
        buildGUI();
    }

    private JLabel buildColorLabel() {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setHorizontalAlignment(0);
        label.setHorizontalTextPosition(0);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setPreferredSize(new Dimension(100, 25));
        return label;
    }

    public void buildGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setTitle("Mixing colors");

        frame.setLayout(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.fill = 1;
        cc.insets = new Insets(5, 5, 5, 5);
        cc.weightx = 0.2D;
        cc.weighty = 1.0D;
        frame.getContentPane().add(buildColorPanel(0), cc);
        frame.getContentPane().add(buildColorPanel(1), cc);
        cc.gridy = 1;
        JPanel firstMix = new JPanel(new GridBagLayout());
        GridBagConstraints ccCol = new GridBagConstraints();
        ccCol.fill = 1;
        ccCol.insets = new Insets(5, 5, 5, 5);
        ccCol.weightx = 1.0D;
        ccCol.weighty = 1.0D;

        ccCol.gridx = 0;
        ccCol.gridy = 0;
        ccCol.gridheight = 2;
        firstMix.add(firstMixColor, ccCol);
        ccCol.fill = 2;
        ccCol.weightx = 0.2D;
        ccCol.weighty = 0.5D;
        ccCol.gridx = 1;
        ccCol.gridy = 0;
        ccCol.gridheight = 1;
        ccCol.gridwidth = 1;
        firstMix.add(new JButton(new AbstractAction("Set First") {
            public void actionPerformed(ActionEvent e) {
                ColorMix.this.setBackgroundToLabel(firstSel, firstMixColor.getBackground());
                ColorMix.this.calculateMixes();
            }
        }), ccCol);

        ccCol.gridx = 1;
        ccCol.gridy = 1;
        firstMix.add(new JButton(new AbstractAction("Set Second") {
            public void actionPerformed(ActionEvent e) {
                ColorMix.this.setBackgroundToLabel(secondSel, firstMixColor.getBackground());
                ColorMix.this.calculateMixes();
            }
        }), ccCol);

        firstMix.setBorder(BorderFactory.createTitledBorder("Secondary Colors"));
        frame.getContentPane().add(firstMix, cc);
        cc.weightx = 0.6D;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints ccColor = new GridBagConstraints();
        ccColor.fill = 1;
        ccColor.insets = new Insets(5, 5, 5, 5);
        ccColor.weightx = 1.0D;
        ccColor.weighty = 1.0D;
        panel.add(firstSel, ccColor);
        ccColor.gridx = 1;
        panel.add(secondSel, ccColor);
        ccColor.gridx = 0;
        ccColor.gridy = 1;
        ccColor.weighty = 0.0D;
        ccColor.gridwidth = 2;
        panel.add(finalColor, ccColor);
        ccColor.gridy = 2;
        panel.add(comboBox, ccColor);
        panel.setBorder(BorderFactory.createTitledBorder("Tertiary Colors"));
        frame.getContentPane().add(panel, cc);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ColorMix();
    }

    private JComponent buildColorPanel(int selectedIndex) {
        final JLabel pColor = buildColorLabel();
        firstMixColors.add(pColor);
        JPanel pSelectColor = new JPanel(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.fill = 1;
        cc.insets = new Insets(5, 5, 5, 5);
        cc.weightx = 1.0D;
        cc.weighty = 1.0D;
        final JSlider slidRed = buildSlider(pSelectColor, cc);
        final JSlider slidGreen = buildSlider(pSelectColor, cc);
        final JSlider slidBlue = buildSlider(pSelectColor, cc);
        pSelectColor.add(pColor, cc);
        final JComboBox comboColores = buildColorCombo();
        comboColores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = (Color) comboColores.getSelectedItem();
                slidRed.setValue(color.getRed());
                slidGreen.setValue(color.getGreen());
                slidBlue.setValue(color.getBlue());
            }
        });
        comboColores.setSelectedIndex(selectedIndex);
        cc.gridy = 1;
        cc.gridwidth = 4;
        cc.weighty = 0.0D;
        pSelectColor.add(comboColores, cc);
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ColorMix.this.setBackgroundToLabel(pColor, new Color(slidRed.getValue(), slidGreen.getValue(), slidBlue.getValue()));
                ColorMix.this.calculateMixes();
            }
        };
        slidRed.addChangeListener(changeListener);
        slidGreen.addChangeListener(changeListener);
        slidBlue.addChangeListener(changeListener);
        pSelectColor.setBorder(BorderFactory.createBevelBorder(1));
        changeListener.stateChanged(null);
        return pSelectColor;
    }

    private JComboBox buildColorCombo() {
        Color TRANSPARENT = new Color(0, 0, 0, 0);

        Vector colors = new Vector();

        colors.add(new ColorMix.NamedColor(Color.RED, "Red"));
        colors.add(new ColorMix.NamedColor(Color.GREEN, "Green"));
        colors.add(new ColorMix.NamedColor(Color.BLUE, "Blue"));

        colors.add(new ColorMix.NamedColor(Color.YELLOW, "Yellow"));
        colors.add(new ColorMix.NamedColor(Color.MAGENTA, "Magenta"));
        colors.add(new ColorMix.NamedColor(Color.CYAN, "Cyan"));

        colors.add(new ColorMix.NamedColor(Color.WHITE, "White"));
        colors.add(new ColorMix.NamedColor(Color.LIGHT_GRAY, "Light Gray"));
        colors.add(new ColorMix.NamedColor(Color.GRAY, "Gray"));
        colors.add(new ColorMix.NamedColor(Color.DARK_GRAY, "Dark Gray"));
        colors.add(new ColorMix.NamedColor(Color.BLACK, "Black"));
        colors.add(new ColorMix.NamedColor(Color.PINK, "Pink"));
        colors.add(new ColorMix.NamedColor(Color.ORANGE, "Orange"));

        colors.add(new ColorMix.NamedColor(TRANSPARENT, "transparent"));

        colors.add(new ColorMix.NamedColor(new Color(15792383), "aliceblue"));
        colors.add(new ColorMix.NamedColor(new Color(16444375), "antiquewhite"));
        colors.add(new ColorMix.NamedColor(new Color(65535), "aqua"));
        colors.add(new ColorMix.NamedColor(new Color(8388564), "aquamarine"));
        colors.add(new ColorMix.NamedColor(new Color(15794175), "azure"));
        colors.add(new ColorMix.NamedColor(new Color(16119260), "beige"));
        colors.add(new ColorMix.NamedColor(new Color(16770244), "bisque"));
        colors.add(new ColorMix.NamedColor(new Color(0), "black"));
        colors.add(new ColorMix.NamedColor(new Color(16772045), "blanchedalmond"));
        colors.add(new ColorMix.NamedColor(new Color(255), "blue"));
        colors.add(new ColorMix.NamedColor(new Color(9055202), "blueviolet"));
        colors.add(new ColorMix.NamedColor(new Color(10824234), "brown"));
        colors.add(new ColorMix.NamedColor(new Color(14596231), "burlywood"));
        colors.add(new ColorMix.NamedColor(new Color(6266528), "cadetblue"));
        colors.add(new ColorMix.NamedColor(new Color(8388352), "chartreuse"));
        colors.add(new ColorMix.NamedColor(new Color(13789470), "chocolate"));
        colors.add(new ColorMix.NamedColor(new Color(16744272), "coral"));
        colors.add(new ColorMix.NamedColor(new Color(6591981), "cornflowerblue"));
        colors.add(new ColorMix.NamedColor(new Color(16775388), "cornsilk"));
        colors.add(new ColorMix.NamedColor(new Color(14423100), "crimson"));
        colors.add(new ColorMix.NamedColor(new Color(65535), "cyan"));
        colors.add(new ColorMix.NamedColor(new Color(139), "darkblue"));
        colors.add(new ColorMix.NamedColor(new Color(35723), "darkcyan"));
        colors.add(new ColorMix.NamedColor(new Color(12092939), "darkgoldenrod"));
        colors.add(new ColorMix.NamedColor(new Color(11119017), "darkgray"));
        colors.add(new ColorMix.NamedColor(new Color(11119017), "darkgrey"));
        colors.add(new ColorMix.NamedColor(new Color(25600), "darkgreen"));
        colors.add(new ColorMix.NamedColor(new Color(12433259), "darkkhaki"));
        colors.add(new ColorMix.NamedColor(new Color(9109643), "darkmagenta"));
        colors.add(new ColorMix.NamedColor(new Color(5597999), "darkolivegreen"));
        colors.add(new ColorMix.NamedColor(new Color(16747520), "darkorange"));
        colors.add(new ColorMix.NamedColor(new Color(10040012), "darkorchid"));
        colors.add(new ColorMix.NamedColor(new Color(9109504), "darkred"));
        colors.add(new ColorMix.NamedColor(new Color(15308410), "darksalmon"));
        colors.add(new ColorMix.NamedColor(new Color(9419919), "darkseagreen"));
        colors.add(new ColorMix.NamedColor(new Color(4734347), "darkslateblue"));
        colors.add(new ColorMix.NamedColor(new Color(3100495), "darkslategray"));
        colors.add(new ColorMix.NamedColor(new Color(3100495), "darkslategrey"));
        colors.add(new ColorMix.NamedColor(new Color(52945), "darkturquoise"));
        colors.add(new ColorMix.NamedColor(new Color(9699539), "darkviolet"));
        colors.add(new ColorMix.NamedColor(new Color(16716947), "deeppink"));
        colors.add(new ColorMix.NamedColor(new Color(49151), "deepskyblue"));
        colors.add(new ColorMix.NamedColor(new Color(6908265), "dimgray"));
        colors.add(new ColorMix.NamedColor(new Color(6908265), "dimgrey"));
        colors.add(new ColorMix.NamedColor(new Color(2003199), "dodgerblue"));
        colors.add(new ColorMix.NamedColor(new Color(11674146), "firebrick"));
        colors.add(new ColorMix.NamedColor(new Color(16775920), "floralwhite"));
        colors.add(new ColorMix.NamedColor(new Color(2263842), "forestgreen"));
        colors.add(new ColorMix.NamedColor(new Color(16711935), "fuchsia"));
        colors.add(new ColorMix.NamedColor(new Color(14474460), "gainsboro"));
        colors.add(new ColorMix.NamedColor(new Color(16316671), "ghostwhite"));
        colors.add(new ColorMix.NamedColor(new Color(16766720), "gold"));
        colors.add(new ColorMix.NamedColor(new Color(14329120), "goldenrod"));
        colors.add(new ColorMix.NamedColor(new Color(8421504), "gray"));
        colors.add(new ColorMix.NamedColor(new Color(8421504), "grey"));
        colors.add(new ColorMix.NamedColor(new Color(32768), "green"));
        colors.add(new ColorMix.NamedColor(new Color(11403055), "greenyellow"));
        colors.add(new ColorMix.NamedColor(new Color(15794160), "honeydew"));
        colors.add(new ColorMix.NamedColor(new Color(16738740), "hotpink"));
        colors.add(new ColorMix.NamedColor(new Color(13458524), "indianred"));
        colors.add(new ColorMix.NamedColor(new Color(4915330), "indigo"));
        colors.add(new ColorMix.NamedColor(new Color(16777200), "ivory"));
        colors.add(new ColorMix.NamedColor(new Color(15787660), "khaki"));
        colors.add(new ColorMix.NamedColor(new Color(15132410), "lavender"));
        colors.add(new ColorMix.NamedColor(new Color(16773365), "lavenderblush"));
        colors.add(new ColorMix.NamedColor(new Color(8190976), "lawngreen"));
        colors.add(new ColorMix.NamedColor(new Color(16775885), "lemonchiffon"));
        colors.add(new ColorMix.NamedColor(new Color(11393254), "lightblue"));
        colors.add(new ColorMix.NamedColor(new Color(15761536), "lightcoral"));
        colors.add(new ColorMix.NamedColor(new Color(14745599), "lightcyan"));
        colors.add(new ColorMix.NamedColor(new Color(16448210), "lightgoldenrodyellow"));
        colors.add(new ColorMix.NamedColor(new Color(13882323), "lightgray"));
        colors.add(new ColorMix.NamedColor(new Color(13882323), "lightgrey"));
        colors.add(new ColorMix.NamedColor(new Color(9498256), "lightgreen"));
        colors.add(new ColorMix.NamedColor(new Color(16758465), "lightpink"));
        colors.add(new ColorMix.NamedColor(new Color(16752762), "lightsalmon"));
        colors.add(new ColorMix.NamedColor(new Color(2142890), "lightseagreen"));
        colors.add(new ColorMix.NamedColor(new Color(8900346), "lightskyblue"));
        colors.add(new ColorMix.NamedColor(new Color(7833753), "lightslategray"));
        colors.add(new ColorMix.NamedColor(new Color(7833753), "lightslategrey"));
        colors.add(new ColorMix.NamedColor(new Color(11584734), "lightsteelblue"));
        colors.add(new ColorMix.NamedColor(new Color(16777184), "lightyellow"));
        colors.add(new ColorMix.NamedColor(new Color(65280), "lime"));
        colors.add(new ColorMix.NamedColor(new Color(3329330), "limegreen"));
        colors.add(new ColorMix.NamedColor(new Color(16445670), "linen"));
        colors.add(new ColorMix.NamedColor(new Color(16711935), "magenta"));
        colors.add(new ColorMix.NamedColor(new Color(8388608), "maroon"));
        colors.add(new ColorMix.NamedColor(new Color(6737322), "mediumaquamarine"));
        colors.add(new ColorMix.NamedColor(new Color(205), "mediumblue"));
        colors.add(new ColorMix.NamedColor(new Color(12211667), "mediumorchid"));
        colors.add(new ColorMix.NamedColor(new Color(9662680), "mediumpurple"));
        colors.add(new ColorMix.NamedColor(new Color(3978097), "mediumseagreen"));
        colors.add(new ColorMix.NamedColor(new Color(8087790), "mediumslateblue"));
        colors.add(new ColorMix.NamedColor(new Color(64154), "mediumspringgreen"));
        colors.add(new ColorMix.NamedColor(new Color(4772300), "mediumturquoise"));
        colors.add(new ColorMix.NamedColor(new Color(13047173), "mediumvioletred"));
        colors.add(new ColorMix.NamedColor(new Color(1644912), "midnightblue"));
        colors.add(new ColorMix.NamedColor(new Color(16121850), "mintcream"));
        colors.add(new ColorMix.NamedColor(new Color(16770273), "mistyrose"));
        colors.add(new ColorMix.NamedColor(new Color(16770229), "moccasin"));
        colors.add(new ColorMix.NamedColor(new Color(16768685), "navajowhite"));
        colors.add(new ColorMix.NamedColor(new Color(128), "navy"));
        colors.add(new ColorMix.NamedColor(new Color(16643558), "oldlace"));
        colors.add(new ColorMix.NamedColor(new Color(8421376), "olive"));
        colors.add(new ColorMix.NamedColor(new Color(7048739), "olivedrab"));
        colors.add(new ColorMix.NamedColor(new Color(16753920), "orange"));
        colors.add(new ColorMix.NamedColor(new Color(16729344), "orangered"));
        colors.add(new ColorMix.NamedColor(new Color(14315734), "orchid"));
        colors.add(new ColorMix.NamedColor(new Color(15657130), "palegoldenrod"));
        colors.add(new ColorMix.NamedColor(new Color(10025880), "palegreen"));
        colors.add(new ColorMix.NamedColor(new Color(11529966), "paleturquoise"));
        colors.add(new ColorMix.NamedColor(new Color(14184595), "palevioletred"));
        colors.add(new ColorMix.NamedColor(new Color(16773077), "papayawhip"));
        colors.add(new ColorMix.NamedColor(new Color(16767673), "peachpuff"));
        colors.add(new ColorMix.NamedColor(new Color(13468991), "peru"));
        colors.add(new ColorMix.NamedColor(new Color(16761035), "pink"));
        colors.add(new ColorMix.NamedColor(new Color(14524637), "plum"));
        colors.add(new ColorMix.NamedColor(new Color(11591910), "powderblue"));
        colors.add(new ColorMix.NamedColor(new Color(8388736), "purple"));
        colors.add(new ColorMix.NamedColor(new Color(16711680), "red"));
        colors.add(new ColorMix.NamedColor(new Color(12357519), "rosybrown"));
        colors.add(new ColorMix.NamedColor(new Color(4286945), "royalblue"));
        colors.add(new ColorMix.NamedColor(new Color(9127187), "saddlebrown"));
        colors.add(new ColorMix.NamedColor(new Color(16416882), "salmon"));
        colors.add(new ColorMix.NamedColor(new Color(16032864), "sandybrown"));
        colors.add(new ColorMix.NamedColor(new Color(3050327), "seagreen"));
        colors.add(new ColorMix.NamedColor(new Color(16774638), "seashell"));
        colors.add(new ColorMix.NamedColor(new Color(10506797), "sienna"));
        colors.add(new ColorMix.NamedColor(new Color(12632256), "silver"));
        colors.add(new ColorMix.NamedColor(new Color(8900331), "skyblue"));
        colors.add(new ColorMix.NamedColor(new Color(6970061), "slateblue"));
        colors.add(new ColorMix.NamedColor(new Color(7372944), "slategray"));
        colors.add(new ColorMix.NamedColor(new Color(7372944), "slategrey"));
        colors.add(new ColorMix.NamedColor(new Color(16775930), "snow"));
        colors.add(new ColorMix.NamedColor(new Color(65407), "springgreen"));
        colors.add(new ColorMix.NamedColor(new Color(4620980), "steelblue"));
        colors.add(new ColorMix.NamedColor(new Color(13808780), "tan"));
        colors.add(new ColorMix.NamedColor(new Color(32896), "teal"));
        colors.add(new ColorMix.NamedColor(new Color(14204888), "thistle"));
        colors.add(new ColorMix.NamedColor(new Color(16737095), "tomato"));
        colors.add(new ColorMix.NamedColor(new Color(4251856), "turquoise"));
        colors.add(new ColorMix.NamedColor(new Color(15631086), "violet"));
        colors.add(new ColorMix.NamedColor(new Color(16113331), "wheat"));
        colors.add(new ColorMix.NamedColor(new Color(16777215), "white"));
        colors.add(new ColorMix.NamedColor(new Color(16119285), "whitesmoke"));
        colors.add(new ColorMix.NamedColor(new Color(16776960), "yellow"));
        colors.add(new ColorMix.NamedColor(new Color(10145074), "yellowgreen"));

        JComboBox comboBox = new JComboBox(new DefaultComboBoxModel(colors));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            protected Color backgroundColor;

            public Component getListCellRendererComponent(JList list, Object obj, int row, boolean sel, boolean hasFocus) {
                if ((obj instanceof Color))
                    backgroundColor = ((Color) obj);
                setText(obj.toString());
                return this;
            }

            public void paint(Graphics g) {
                setBackground(backgroundColor);
                super.paint(g);
            }
        });
        return comboBox;
    }

    private void calculateMixes() {
        calculateFirstMix();
        calculateSecondMix();
    }

    private void calculateFirstMix() {
        calculateMix(firstMixColors, firstMixColor);
    }

    private void calculateSecondMix() {
        calculateMix(secondMixColors, finalColor);
    }

    private void calculateMix(Vector<JLabel> mixColors, JLabel finalColor) {
        Color bg = ((ColorMix.Mixer) comboBox.getSelectedItem()).calculateMix(mixColors);
        setBackgroundToLabel(finalColor, bg);
    }

    private void setBackgroundToLabel(JLabel label, Color color) {
        label.setBackground(color);
        label.setText(color.getRed() + "," + color.getGreen() + "," + color.getBlue());
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

    static class NamedColor extends Color {
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

