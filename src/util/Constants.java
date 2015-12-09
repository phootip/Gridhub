package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

public final class Constants {
	public static final String PROGRAM_NAME = "Gridhub";

	/**
	 * Determines whether the game will be run in full screen mode or not.
	 */
	public static final boolean IS_FULLSCREEN = false;

	/**
	 * The size of the game screen, excluding the window border. This is used when {@link #IS_FULLSCREEN} is false.
	 */
	public static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1600, 900);

	/**
	 * Determines whether the FPS monitor should be shown.
	 */
	public static final boolean SHOW_FPS_MONITOR = true;
	
	/**
	 * Whether or not to cache the drawable things to improve performance.
	 */
	public static final boolean CACHE_DRAWABLE = true;

	/**
	 * The maximum frame per second of game looping.
	 */
	public static int MAX_FRAME_PER_SECOND = 60;

	/**
	 * The minimum size of the game screen, including the window border. This is used when {@link #IS_FULLSCREEN} is
	 * false.
	 */
	public static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(1366, 768);

	public final static class ColorSwatch {
		public static final Color BACKGROUND = new Color(25, 38, 69);
		public static final Color FOREGROUND = Color.WHITE;
		public static final Color SHADOW = new Color(50, 76, 138);
	}

	public final static class PlayerSettings {

		public static Color getPlayerColor(int playerId) {
			switch (playerId) {
				case 1:
					return new Color(255, 0, 0);
				case 2:
					return new Color(120, 83, 255);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);
			}
		}

		public static int getLeftKey(int playerId) {
			switch (playerId) {
				case 1:
					return KeyEvent.VK_A;
				case 2:
					return KeyEvent.VK_L;
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}

		public static int getRightKey(int playerId) {
			switch (playerId) {
				case 1:
					return KeyEvent.VK_D;
				case 2:
					return KeyEvent.VK_QUOTE;
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}

		public static int getDownKey(int playerId) {
			switch (playerId) {
				case 1:
					return KeyEvent.VK_S;
				case 2:
					return KeyEvent.VK_SEMICOLON;
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}

		public static int getUpKey(int playerId) {
			switch (playerId) {
				case 1:
					return KeyEvent.VK_W;
				case 2:
					return KeyEvent.VK_P;
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}
	}
}
