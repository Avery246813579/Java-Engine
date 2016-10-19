package me.averydurrant.javaengine;

import me.averydurrant.javaengine.graphics.Renderer;

public class MainThread implements Runnable {
	private Renderer renderer;
	private boolean running = true;

	public MainThread(Renderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void run() {
		long lastTick = System.nanoTime(), tick = System.currentTimeMillis();
		double delta = 0, targetUpdates = 1000000000.0 / 60.0;
		int updates = 0, frames = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTick) / targetUpdates;
			lastTick = now;
			if (delta >= 1) {
//				update();
				updates++;
				delta--;
			}

			renderer.render();
			frames++;
			if (System.currentTimeMillis() - tick > 1000) {
				tick += 1000;
				System.out.println("Frames: " + frames + " Updates: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

}
