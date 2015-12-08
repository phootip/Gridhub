package util;

import java.awt.Color;
import java.awt.Dimension;

public final class Constants {
	public static final String PROGRAM_NAME = "Gridhub";

	/**
	 * Determines whether the game will be run in full screen mode or not.
	 */
	public static final boolean IS_FULLSCREEN = false;

	/**
	 * The size of the game screen, excluding the window border. This is used
	 * when {@link #IS_FULLSCREEN} is false.
	 */
	public static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1600, 900);

	/**
	 * Determines whether the FPS monitor should be shown.
	 */
	public static final boolean SHOW_FPS_MONITOR = true;

	/**
	 * The maximum frame per second of game looping.
	 */
	public static int MAX_FRAME_PER_SECOND = 60;

	/**
	 * The minimum size of the game screen, including the window border. This is
	 * used when {@link #IS_FULLSCREEN} is false.
	 */
	public static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(1366, 768);

	public final static class ColorSwatch {
		public static final Color BACKGROUND = new Color(25, 38, 69);
		public static final Color FOREGROUND = Color.WHITE;
		public static final Color SHADOW = new Color(50, 76, 138);
	}
}
