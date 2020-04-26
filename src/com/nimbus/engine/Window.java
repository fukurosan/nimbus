package com.nimbus.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window {
	
	private JFrame frame;
	private BufferedImage image;
	private Canvas canvas;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private NimbusSettings settings = NimbusSettings.INSTANCE;
	
	public Window() {
		image = new BufferedImage(settings.WIDTH, settings.HEIGHT, BufferedImage.TYPE_INT_RGB);
		canvas = new Canvas();
		Dimension dimensions = new Dimension((int) (settings.WIDTH * settings.SCALE), (int) (settings.HEIGHT * settings.SCALE));
		canvas.setPreferredSize(dimensions);
		canvas.setMaximumSize(dimensions);
		canvas.setMinimumSize(dimensions);
		
		frame = new JFrame(settings.TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		graphics = bufferStrategy.getDrawGraphics();
	}
	
	public void update() {
		graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bufferStrategy.show();
	}

	public BufferedImage getImage() {
		return image;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return frame;
	}

}
