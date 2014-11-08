package org.albfan.leven;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Main {  
	public static void main(String[] args) {
		JFrame frame = new JFrame();

		final JButton activate = new JButton("Show");
		frame.getContentPane().add(activate, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);

		final GlassPane glass = new GlassPane();
		frame.setGlassPane(glass);

		activate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				glass.setVisible(!glass.isVisible());
			}
		});
		activate.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent evt) {
				glass.setPoint(evt.getPoint());
				glass.repaint();
			}
		});
	}
}

class GlassPane extends JComponent {

	private Point point;

	public GlassPane() { 
	}

	public void setPoint(Point point) {
		this.point = point;	
	}

	public void paint(Graphics g) {
		g.translate((int)point.getX()+20,(int)point.getY()+20);
		g.setColor(Color.black);
		g.drawString("test", 0, 10);
	}
}