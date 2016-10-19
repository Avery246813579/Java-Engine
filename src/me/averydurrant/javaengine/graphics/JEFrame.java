package me.averydurrant.javaengine.graphics;

import java.awt.Dimension;

import javax.swing.JFrame;

public class JEFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private Renderer renderer;
	
	public JEFrame(String name){
		setTitle(name);
		
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);

		renderer = new Renderer(this);
	}
}	
