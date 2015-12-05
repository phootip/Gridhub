package scene.core;

import java.awt.Graphics2D;

/**
 * This represent a game scene that can be shown only one at a time, e.g. main
 * menu, main game, and other.
 * 
 * @author Kasidit Iamthong
 *
 */
public abstract class Scene {

	/**
	 * Update the scene with the specified step. This is part of the game logic
	 * section in the game looping.
	 * <p>
	 * The value of step determines how much the game should be advanced forward
	 * in time. The step is always in the range 1 to 100, and usually be 100.
	 * For example, if the step is 50, then the game should be 2 times slower.
	 * <p>
	 * It is guaranteed that if the current step is value {code v}, the
	 * cumulative step is always be 100, 200, 300, ...,
	 * {@code floor(v / 100) * 100}. In other word, if the code in the update
	 * function is as follows:
	 * <p>
	 * {@code int cumulativeStep += step; }
	 * <p>
	 * Then the {@code cumulativeStep} was, or will be, any values that is
	 * multiple of 100, except zero.
	 * 
	 * @param step
	 *            the number of step that the game is advanced.
	 */
	public abstract void update(int step);

	/**
	 * Draw the scene. This is part of the draw section in the game looping.
	 * Note that this might be called multiple time in the same frame, and every
	 * time that this function is called should cause the same result.
	 * 
	 * @param g
	 *            the Graphics2D object that can be used for painting operation.
	 * @param sceneWidth
	 *            the width of the scene
	 * @param sceneHeight
	 *            the width of the scene
	 */
	public abstract void draw(Graphics2D g, int sceneWidth, int sceneHeight);

}
