package me.averydurrant.javaengine.graphics;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import me.averydurrant.javaengine.MainThread;

public class Renderer extends Canvas {
	private BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private static final long serialVersionUID = 1L;
	private JEFrame frame;

	public Renderer(JEFrame frame) {
		this.frame = frame;
		
		frame.add(this);
		new Thread(new MainThread(this)).start();
	}

	public void render() {
		BufferStrategy bufferStrategy = frame.getBufferStrategy();

		if (bufferStrategy == null) {
			frame.createBufferStrategy(3);;
			
			return;
		}
		
		clear();
		draw();
		
		Graphics graphics = bufferStrategy.getDrawGraphics();
		graphics.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), null);
		graphics.dispose();
		
		bufferStrategy.show();
	}
	
	public void draw(){
		
	}
	
	public void clear(){
		for(int i = 0; i < frame.getWidth() * frame.getHeight(); i++){
			pixels[i] = 0xFFFFFF;
		}
	}
}
