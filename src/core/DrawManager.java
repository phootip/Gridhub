package core;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * This class manage all the core drawing logic. Most code is adapted from
 * http://stackoverflow.com/a/1963684.
 * 
 * @author Kasidit Iamthong
 *
 */
final class DrawManager {

	private static DrawManager instance = new DrawManager();

	/**
	 * Get an instance of {@link DrawManager}.
	 * 
	 * @return an instance of {@link DrawManager}.
	 */
	public static DrawManager getInstance() {
		return instance;
	}

	private Canvas canvas;
	private BufferStrategy strategy;
	private Graphics2D graphic;
	private GraphicsConfiguration gConfig;

	private boolean isCanvasSizeSet = false;
	private int canvasWidth;
	private int canvasHeight;

	/**
	 * Set the canvas size.
	 * 
	 * @param width
	 *            the width of the canvas.
	 * @param height
	 *            the height of the canvas.
	 */
	public void setCanvasSize(final int width, final int height) {
		isCanvasSizeSet = true;

		this.canvasWidth = width;
		this.canvasHeight = height;
	}

	/**
	 * Return true if {@code setCanvanSize()} has been called.
	 * 
	 * @return Whether or not the canvas size has been set.
	 */
	public boolean isCanvasSizeSet() {
		return isCanvasSizeSet;
	}

	/**
	 * Get the width of the canvas.
	 * 
	 * @return An integer representing the width of the canvas, or null if the
	 *         size has not been set.
	 */
	public int getCanvasWidth() {
		return canvasWidth;
	}

	/**
	 * Get the height of the canvas.
	 * 
	 * @return An integer representing the width of the canvas, or null if the
	 *         size has not been set.
	 */
	public int getCanvasHeight() {
		return canvasHeight;
	}

	public BufferedImage createBlankBufferedImage(final int width, final int height, final boolean alpha) {
		return gConfig.createCompatibleImage(width, height, alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
	}

	/**
	 * Add the canvas component into a specified container.
	 * 
	 * @param container
	 *            the container which the canvas will be added into.
	 */
	public void addCanvasInto(Container container) {
		container.add(canvas);

		// Set a graphic to null, because the graphic must get from strategy.
		graphic = null;

		// Create a buffer strategy
		canvas.createBufferStrategy(2);
		do {
			strategy = canvas.getBufferStrategy();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (strategy == null);
	}

	private DrawManager() {
		gConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		canvas = new Canvas(gConfig);
		canvas.setFocusable(false);
	}

	/**
	 * Get the current {@code Graphics2D} object which can be used for painting.
	 * 
	 * @return The {@code Graphics2D} object.
	 */
	protected Graphics2D getGraphic() {
		if (graphic == null) {
			graphic = (Graphics2D) strategy.getDrawGraphics();
			graphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		graphic.setClip(0, 0, this.canvasWidth, this.canvasHeight);
		return graphic;
	}

	/**
	 * Update the canvas to show all the paint operation performed on
	 * {@code Graphics2D}. Note that updating might be failed, and, in that
	 * case, user must perform drawing operation then call this function again.
	 * 
	 * @return Whether or not canvas updating is successful.
	 */
	public boolean updateCanvas() {
		graphic.dispose();
		graphic = null;

		try {
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
			return !strategy.contentsLost();

		} catch (NullPointerException e) {
			return true;

		} catch (IllegalStateException e) {
			return true;
		}
	}

}
