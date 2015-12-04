package util;

import java.awt.Dimension;

public class Settings {
	public static final String PROGRAM_NAME = "Gridhub";

	/**
	 * This constant determines whether the game will be run in full screen mode
	 * or not.
	 */
	public static final boolean IS_FULLSCREEN = false;

	/**
	 * This constant set the size of the game screen, excluding the window
	 * border. This is used when {@code IS_FULLSCREEN} is false.
	 */
	public static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1600, 900);
}
