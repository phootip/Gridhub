package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for collecting FPS data, record only last constant data.
 * 
 * @author Kasidit Iamthong
 *
 */
class FPSCollector {

	/**
	 * The maximum data size of the list.
	 */
	protected static final int MAX_DATA_SIZE = 100;

	/**
	 * The height of the drawn FPS monitor.
	 */
	protected static final int MONITOR_HEIGHT = 60;

	private static ArrayList<Integer> framePerSeconds = new ArrayList<>();

	/**
	 * Add recorded value into list, also remove the excess data.
	 * 
	 * @param fps
	 *            the value to be added.
	 */
	protected static void add(int fps) {
		framePerSeconds.add(fps);
		if (framePerSeconds.size() > MAX_DATA_SIZE) {
			framePerSeconds.remove(0);
		}
	}

	/**
	 * Get the data list of the recorded data.
	 * 
	 * @return The {@code List} of recorded data.
	 */
	protected static List<Integer> getFramePerSeconds() {
		return framePerSeconds;
	}

	/**
	 * Draw an FPS monitor at a top-right position.
	 * 
	 * @param g
	 *            the {@code Graphics2D} object which will be drawn onto.
	 * @param canvasWidth
	 *            the width of the canvas
	 */
	protected static void drawMonitor(Graphics2D g, int canvasWidth) {
		g.setColor(new Color(0, 0, 0, 0x33));
		g.fillRect(canvasWidth - MAX_DATA_SIZE, 0, MAX_DATA_SIZE, MONITOR_HEIGHT);

		g.setColor(Color.WHITE);
		for (int i = 1; i < framePerSeconds.size(); i++) {
			g.drawLine(canvasWidth - MAX_DATA_SIZE + i - 1,
					framePerSeconds.get(i - 1) * MONITOR_HEIGHT / util.Constants.MAX_FRAME_PER_SECOND,
					canvasWidth - MAX_DATA_SIZE + i,
					framePerSeconds.get(i) * MONITOR_HEIGHT / util.Constants.MAX_FRAME_PER_SECOND);
		}
	}

}
