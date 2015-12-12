package core;

import java.awt.Graphics2D;

import scene.core.Scene;
import util.Constants;
import util.InputManager;

/**
 * This class manage all the game scenes and call the draw the appropriate one.
 * 
 * @author Kasidit Iamthong
 *
 */
public class SceneManager {
	private static SceneManager instance = new SceneManager();

	/**
	 * Get an instance of SceneManager.
	 * 
	 * @return An instance of SceneManager.
	 */
	public static SceneManager getInstance() {
		return instance;
	}

	private Scene nextScene;
	private Scene currentScene;

	/**
	 * Set the scene that will be switched to in the next frame.
	 * 
	 * @param scene
	 *            the scene that will be switch to in the next frame.
	 */
	public void setNextScene(Scene scene) {
		this.nextScene = scene;
	}

	private SceneManager() {
	}

	/**
	 * Perform an update to current scene, including the game logic and drawing.
	 * This function should be called every game frame.
	 */
	public void update() {
		// Logic section
		if (this.nextScene != null) {
			this.currentScene = nextScene;
			this.nextScene = null;
		}

		if (this.currentScene != null) {
			this.currentScene.update(Constants.GAME_SPEED);

			do {
				InputManager.getInstance().update();
				
				Graphics2D graphic = DrawManager.getInstance().getGraphic();
				this.currentScene.draw(graphic, DrawManager.getInstance().getCanvasWidth(),
						DrawManager.getInstance().getCanvasHeight());
				
				if (util.Constants.SHOW_FPS_MONITOR) {
					FPSCollector.drawMonitor(graphic, DrawManager.getInstance().getCanvasWidth());
				}
				
				// Retry drawing if updating canvas fail.
			} while (!DrawManager.getInstance().updateCanvas());
		}

	}
}
