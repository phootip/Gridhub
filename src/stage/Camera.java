package stage;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.gameobj.ICameraAssignable;
import stage.gameobj.IDrawable;
import stage.gameobj.Player;
import util.Constants;
import util.Helper;

/**
 * A camera for help rendering things in game. Useful for converting a game coordinate to screen coordinate.
 * 
 * @author Kasidit Iamthong
 *
 */
public class Camera {
	private float zoomFactor = 50;

	private Vector3 centerPos;

	private int sceneWidth;
	private int sceneHeight;

	private IDrawable followObj;

	/**
	 * Get the zoom factor of the camera. Multiplying vector size in game world with zoom factor yields a size in screen
	 * coordinate system (not considering direction of the vector).
	 * 
	 * @return The zoom factor of the camera.
	 */
	public float getZoomFactor() {
		return zoomFactor;
	}

	/**
	 * Create a camera that will slowly follow specified object, trying to place the object in the center of the screen.
	 * 
	 * @param followObj
	 *            the object that the camera will follow.
	 */
	public Camera(IDrawable followObj) {
		this.followObj = followObj;
		this.centerPos = getPreferredCenterPos();
		if (this.followObj instanceof ICameraAssignable) {
			((ICameraAssignable) this.followObj).assignCamera(this);
		}
	}

	private boolean deformationChanged = true;

	/**
	 * Whether the camera has some deformation changed, such as zoom factor and rotation. When the camera deformation
	 * has not changed, some type of the {@link IDrawable} will looks exactly the same except its position, so they can
	 * be cached for better drawing performance.
	 * 
	 * @return Whether the camera has deformed since last reset.
	 * @see Camera#resetDeformationChanged()
	 */
	public boolean isDeformationChanged() {
		return deformationChanged;
	}

	/**
	 * Mark the camera as if there is no deformation changed. This should be called every frame before any update
	 * calculation.
	 */
	protected void resetDeformationChanged() {
		this.deformationChanged = false;
	}

	private Vector3 getPreferredCenterPos() {
		return this.followObj.getDrawPosition();
	}

	private final float FOLLOW_SPEED = 30;

	private int rotation = 0;
	private int oldRotation = 0;
	private float shiftedAngle = 0.1f;
	private float rotationAngle = shiftedAngle;

	private boolean isRotating = false;
	private int rotationFrame = 0;
	private final int rotationDuration = 100 * 30;

	/**
	 * Get the current rotation value of the camera. There are 4 possible values: 0, 1, 2, and 3. Useful for determining
	 * what direction the camera is heading.
	 * 
	 * @return The rotation value of the camera.
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Get the camera rotation angle in radian.
	 * 
	 * @return A camera rotation angle, ranged between [0, 2PI)
	 */
	public float getRotationAngle() {
		float ans = rotationAngle;
		while (ans >= Math.PI * 2) {
			ans -= Math.PI * 2;
		}
		while (ans < 0) {
			ans += Math.PI * 2;
		}
		return rotationAngle;
	}

	/**
	 * Update the camera with the specified step.
	 * 
	 * @param step
	 *            number of game step.
	 */
	public void update(int step) {
		centerPos.add(getPreferredCenterPos().subtract(centerPos)
				.multiply((float) (1 / Math.pow(FOLLOW_SPEED, 100f / step))));

		if (!isRotating) {
			int direction = 0;

			int playerId = Constants.PLAYER1_ID;
			if (followObj instanceof Player) {
				playerId = ((Player) followObj).getPlayerId();
			}

			if (Constants.PlayerHelper.isRotateCameraLeftPressing(playerId)) {
				direction += 3;
			} else if (Constants.PlayerHelper.isRotateCameraRightPressing(playerId)) {
				direction += 1;
			}

			direction %= 4;
			if (direction != 0) {
				isRotating = true;
				rotation = (rotation + direction) % 4;
			}
		}
		if (isRotating) {
			rotationFrame += step;
			if (rotationFrame >= rotationDuration) {
				oldRotation = rotation;
				isRotating = false;
				rotationFrame = 0;
				rotationAngle = (float) (rotation * Math.PI / 2) + shiftedAngle;
			} else {
				if (rotation - oldRotation == 1 || rotation - oldRotation == -1) {
					rotationAngle = Helper.sineInterpolate((float) (oldRotation * Math.PI / 2),
							(float) (rotation * Math.PI / 2), (float) rotationFrame / rotationDuration) + shiftedAngle;
				} else {
					rotationAngle = Helper.sineInterpolate((float) ((oldRotation == 0 ? 4 : oldRotation) * Math.PI / 2),
							(float) ((rotation == 0 ? 4 : rotation) * Math.PI / 2),
							(float) rotationFrame / rotationDuration) + shiftedAngle;
				}
			}
			deformationChanged = true;
		}
	}

	private final float yFactor = 0.5f;
	private final float zFactor = 1.0f;

	/**
	 * Convert the specified position in game coordinate system into position in screen coordinate system using raw
	 * positioning mode (the position vector of the center of the screen is assumed to be zero vector).
	 * 
	 * @param x
	 *            the x-component of the position vector.
	 * @param y
	 *            the y-component of the position vector.
	 * @param z
	 *            the z-component of the position vector.
	 * @return The position of the point in screen coordinate system.
	 * @see #getDrawPosition(float, float, float, boolean)
	 * @see #getDrawPosition(float, float, float)
	 * @see #getDrawPosition(Vector3)
	 */
	public Vector2 getRawDrawPosition(float x, float y, float z) {
		return new Vector2(x, y).rotate(rotationAngle).multiply(zoomFactor, zoomFactor * yFactor).subtract(0,
				getDrawSizeZ(z));
	}

	/**
	 * Convert the specified position in game coordinate system into position in screen coordinate system using
	 * specified mode.
	 * 
	 * @param x
	 *            the x-component of the position vector.
	 * @param y
	 *            the y-component of the position vector.
	 * @param z
	 *            the z-component of the position vector.
	 * @param isRawDrawPosition
	 *            specified whether or not the result should be the raw draw position.
	 * @return The position of the point in screen coordinate system.
	 * @see Camera#setSceneSize(int, int)
	 * @see #getDrawPosition(float, float, float)
	 * @see #getDrawPosition(Vector3)
	 */
	public Vector2 getDrawPosition(float x, float y, float z, boolean isRawDrawPosition) {
		return isRawDrawPosition ? getRawDrawPosition(x, y, z) : getDrawPosition(x, y, z);
	}

	/**
	 * Convert the specified position in game coordinate system into position in screen coordinate system using current
	 * {@code screenWidth}, and {@code screenHeight}.
	 * 
	 * @param x
	 *            the x-component of the position vector.
	 * @param y
	 *            the y-component of the position vector.
	 * @param z
	 *            the z-component of the position vector.
	 * @return The position of the point in screen coordinate system.
	 * @see Camera#setSceneSize(int, int)
	 * @see #getDrawPosition(float, float, float, boolean)
	 * @see #getDrawPosition(Vector3)
	 */
	public Vector2 getDrawPosition(float x, float y, float z) {
		return new Vector2(x, y).subtract(centerPos.getX(), centerPos.getY()).rotate(rotationAngle)
				.multiply(zoomFactor, zoomFactor * yFactor).add(0, getDrawSizeZ(0.5f + centerPos.getZ()))
				.add(sceneWidth / 2f, sceneHeight / 2f - getDrawSizeZ(z));
	}

	/**
	 * Convert the specified position in game coordinate system into position in screen coordinate system using current
	 * {@code screenWidth}, and {@code screenHeight}.
	 * 
	 * @param v
	 *            the position vector.
	 * @return The position of the point in screen coordinate system.
	 * @see Camera#setSceneSize(int, int)
	 * @see #getDrawPosition(float, float, float, boolean)
	 * @see #getDrawPosition(Vector3)
	 */
	public Vector2 getDrawPosition(Vector3 v) {
		return getDrawPosition(v.getX(), v.getY(), v.getZ());
	}

	/**
	 * A shorthand method for getting only x value from {@link #getDrawPosition(float, float, float)} with arbitrary z
	 * value.
	 * 
	 * @param x
	 *            the x-component of the position vector.
	 * @param y
	 *            the y-component of the position vector.
	 * @return The x-component of the position vector of the point in screen coordinate system.
	 * @see #getYPosition(float, float, float)
	 */
	public float getXPosition(float x, float y) {
		return getDrawPosition(x, y, 0).getX();
	}

	/**
	 * A shorthand method for getting only y value from {@link #getDrawPosition(float, float, float)}.
	 * 
	 * @param x
	 *            the x-component of the position vector.
	 * @param y
	 *            the y-component of the position vector.
	 * @param z
	 *            the z-component of the position vector.
	 * @return The y-component of the position vector of the point in screen coordinate system.
	 * @see #getXPosition(float, float)
	 */
	public float getYPosition(float x, float y, float z) {
		return getDrawPosition(x, y, z).getY();
	}

	/**
	 * Convert the size of a vector that lies along the x axis in the game coordinate system into screen coordinate.
	 * Assume that the x-axis in game world is parallel to the x axis of the screen.
	 * 
	 * @param size
	 *            the size of a vector in the game coordinate system.
	 * @return The size of a vector in the screen coordinate system.
	 */
	public float getDrawSizeX(float size) {
		return size * zoomFactor;
	}

	/**
	 * Convert the size of a vector that lies along the y axis in the game coordinate system into screen coordinate.
	 * Assume that the y-axis in game world is parallel to the y axis of the screen.
	 * 
	 * @param size
	 *            the size of a vector in the game coordinate system.
	 * @return The size of a vector in the screen coordinate system.
	 */
	public float getDrawSizeY(float size) {
		return size * zoomFactor * yFactor;
	}

	/**
	 * Convert the size of a vector that lies along the z axis in the game coordinate system into screen coordinate.
	 * Assume that the z-axis in game world is parallel to the y axis of the screen.
	 * 
	 * @param size
	 *            the size of a vector in the game coordinate system.
	 * @return The size of a vector in the screen coordinate system.
	 */
	public float getDrawSizeZ(float size) {
		return size * zoomFactor * zFactor;
	}

	/**
	 * Set the screen size. Used for calculating vector position.
	 * 
	 * @param width
	 *            the screen width.
	 * @param height
	 *            the screen height.
	 * @see #getDrawPosition(Vector3)
	 * @see #getDrawPosition(float, float, float)
	 * @see #getDrawPosition(float, float, float, boolean)
	 */
	public void setSceneSize(int width, int height) {
		sceneWidth = width;
		sceneHeight = height;
	}

}
