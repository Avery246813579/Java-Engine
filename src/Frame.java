import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Frame extends Canvas implements Runnable, MouseListener, KeyListener {
	JFrame frame;
	boolean running = true;
	public static final int WIDTH = 800, HEIGHT = 600;
	public static final double SCALE = 1;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	List<Point> clicks = new ArrayList<Point>();
	
	private int[] steve;
	private int sW, sH;

	public static void main(String[] args) {
		new Frame().start();
	}

	public Frame() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		addKeyListener(this);
		
		try {
			BufferedImage image = ImageIO.read(getClass().getResourceAsStream("TEST.png"));

			int width = sW = image.getWidth();
			int height = sH = image.getHeight();
			steve = new int[width * height];
			image.getRGB(0, 0, width, height, steve, 0, width);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void pack() {
		frame = new JFrame("Test Frame");
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void run() {
		pack();

		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				frames = 0;
				updates = 0;
			}
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		clear();
		draw();

		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, (int) (WIDTH * SCALE), (int) (HEIGHT * SCALE), null);

		g.dispose();
		bs.show();
	}

	public void clear() {
		for (int x = 0; x < WIDTH * HEIGHT; x++) {
			pixels[x] = 0x000000;
		}
	}

	public void update() {

	}

	public void draw() {
//		for (int w = 0; w < WIDTH; w++) {
//			pixels[w + 10 * WIDTH] = 16720639;
//		}

		for (int x = 0; x < sW; x++) {
			if (x < 0 || x > WIDTH)
				continue;

			for (int y = 0; y < sH; y++) {
				if (y < 0 || y > HEIGHT)
					continue;

				try {
					pixels[x + y * WIDTH] = steve[x + y * sW];
				} catch (Exception e) {
				}
			}
		}

		for(int x = 0; x < WIDTH; x++){
			for(int y = 0; y < HEIGHT; y++){
				int color = pixels[x + y * WIDTH];
				int r = (color & 0xff0000) >> 16;
				int g = (color & 0xff00) >> 8;
				int b = (color & 0xff);
				
				r *= .3;
				g *= .3;
				b *= .3;
				
				if (r > 255)
					r = 255;
				if (g > 255)
					g = 255;
				if (b > 255)
					b = 255;
				
				pixels[x + y * WIDTH] = r << 16 | g << 8 | b;
			}
		}
		
		Map<Integer, Integer> lights = new HashMap<Integer, Integer>();
		for(Point point : clicks){
			for(int x = point.getX() - 75; x < point.getX() + 75; x++){
				for(int y = point.getY() - 75; y < point.getY() + 75; y++){
					if(lights.get(x + y * WIDTH) != null){
						lights.put(x + y * WIDTH, lights.get(x + y * WIDTH) + 1);
					}else{
						lights.put(x + y * WIDTH, 1);
					}
				}
			}
		}
		
		for(int keys : lights.keySet()){
			int color = pixels[keys];

			int r = (color & 0xff0000) >> 16;
			int g = (color & 0xff00) >> 8;
			int b = (color & 0xff);
			
			int a = (2 * lights.get(keys));
			r *= a;
			g *= a;
			b *= a;
			
			if (r > 255)
				r = 255;
			if (g > 255)
				g = 255;
			if (b > 255)
				b = 255;
			
			pixels[keys] = r << 16 | g << 8 | b;
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		clicks.add(new Point(event.getX(), event.getY()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_Z){
			if(clicks.size() > 0){
				clicks.remove(clicks.size() - 1);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
