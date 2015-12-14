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
	 * The speed of the game. Default is 100. The number should be divisible by 100, or 100 is divisible by it.
	 */
	public static int GAME_SPEED = 100;

	/**
	 * A unique ID for Player1
	 */
	public static final int PLAYER1_ID = 1;
	/**
	 * A unique ID for Player2
	 */
	public static final int PLAYER2_ID = 2;

	/**
	 * The minimum size of the game screen, including the window border. This is used when {@link #IS_FULLSCREEN} is
	 * false.
	 */
	public static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(1366, 768);

	/**
	 * The name of file collecting chapter list data.
	 */
	public static final String CHAPTER_LIST_FILE_NAME = "ChapterList";

	public final static class ColorSwatch {
		public static final Color BACKGROUND = new Color(25, 38, 69);
		public static final Color FOREGROUND = Color.WHITE;
		public static final Color SHADOW = new Color(50, 76, 138);
	}

	public final static class PlayerHelper {

		public static Color getPlayerColor(int playerId) {
			switch (playerId) {
				case PLAYER1_ID:
					return new Color(255, 0, 0);
				case PLAYER2_ID:
					return new Color(120, 83, 255);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);
			}
		}

		public static boolean isLeftKeyPressing(int playerId) {
			switch (playerId) {
				case PLAYER1_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_A)
							|| InputManager.getInstance().isKeyPressing(KeyEvent.VK_LEFT);
				case PLAYER2_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_L);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}

		public static boolean isRightKeyPressing(int playerId) {
			switch (playerId) {
				case PLAYER1_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_D)
							|| InputManager.getInstance().isKeyPressing(KeyEvent.VK_RIGHT);
				case PLAYER2_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_QUOTE)
							|| InputManager.getInstance().isKeyPressing(16780807);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}

		public static boolean isDownKeyPressing(int playerId) {
			switch (playerId) {
				case PLAYER1_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_S)
							|| InputManager.getInstance().isKeyPressing(KeyEvent.VK_DOWN);
				case PLAYER2_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_SEMICOLON)
							|| InputManager.getInstance().isKeyPressing(16780839);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}

		public static boolean isUpKeyPressing(int playerId) {
			switch (playerId) {
				case PLAYER1_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_W)
							|| InputManager.getInstance().isKeyPressing(KeyEvent.VK_UP);
				case PLAYER2_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_P);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);

			}
		}

		public static boolean isRotateCameraLeftPressing(int playerId) {
			switch (playerId) {
				case PLAYER1_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_Q);
				case PLAYER2_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_O);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);
			}
		}

		public static boolean isRotateCameraRightPressing(int playerId) {
			switch (playerId) {
				case PLAYER1_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_E);
				case PLAYER2_ID:
					return InputManager.getInstance().isKeyPressing(KeyEvent.VK_OPEN_BRACKET);
				default:
					throw new IllegalArgumentException("Invalid player id : " + playerId);
			}
		}

	}

	public enum PlayMode {
		SINGLE_PLAYER("Single Player", "singleplayer"), COOP_MODE("Co-op mode", "coop");

		private String folderName;
		private String fullModeName;

		/**
		 * @return The folder name corresponding to play mode.
		 */
		public String getLevelFolderName() {
			return folderName;
		}

		private PlayMode(String fullModeName, String folderName) {
			this.folderName = folderName;
			this.fullModeName = fullModeName;
		}

		public String getFullModeName() {
			return fullModeName;
		}

	}
}
