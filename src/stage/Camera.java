package stage;

import java.awt.event.KeyEvent;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.gameobj.Player;
import util.Constants;
import util.Helper;
import util.InputManager;

public class Camera {
	private float zoomFactor = 50;

	private Vector3 centerPos;

	private int sceneWidth;
	private int sceneHeight;

	private Player player;

	public float getZoomFactor() {
		return zoomFactor;
	}

	public Camera(Player player) {
		this.player = player;
		this.centerPos = getPreferredCenterPos();
	}

	private Vector3 getPreferredCenterPos() {
		return this.player.getDrawPosition();
	}

	private final float FOLLOW_SPEED = (float) Math.pow(5, 1.0 / 60);

	private int rotation = 0;
	private int oldRotation = 0;
	private float shiftedAngle = 0.1f;
	private float rotationAngle = shiftedAngle;

	private boolean isRotating = false;
	private int rotationFrame = 0;
	private final int rotationDuration = 100 * 30;

	protected int getRotation() {
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

	public void update(int step) {
		centerPos.add(getPreferredCenterPos().subtract(centerPos).multiply(1 / (float) Math.pow(FOLLOW_SPEED, step)));

		if (!isRotating) {
			int direction = 0;

			if (Constants.PlayerHelper.isRotateCameraLeftPressing(player.getPlayerId())) {
				direction += 3;
			} else if (Constants.PlayerHelper.isRotateCameraRightPressing(player.getPlayerId())) {
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
		}
	}

	private final float yFactor = 0.5f;
	private final float zFactor = 1.0f;

	public Vector2 getRawDrawPosition(float x, float y, float z) {
		return new Vector2(x, y).rotate(rotationAngle).multiply(zoomFactor, zoomFactor * yFactor).subtract(0,
				getDrawSizeZ(z));
	}

	public Vector2 getDrawPosition(float x, float y, float z, boolean isRawDrawPosition) {
		return isRawDrawPosition ? getRawDrawPosition(x, y, z) : getDrawPosition(x, y, z);
	}

	public Vector2 getDrawPosition(float x, float y, float z) {
		return new Vector2(x, y).subtract(centerPos.getX(), centerPos.getY()).rotate(rotationAngle)
				.add(0, centerPos.getZ() + 0.5f).multiply(zoomFactor, zoomFactor * yFactor)
				.add(sceneWidth / 2f, sceneHeight / 2f - getDrawSizeZ(z));
	}

	public Vector2 getDrawPosition(Vector3 v) {
		return getDrawPosition(v.getX(), v.getY(), v.getZ());
	}

	public float getXPosition(float x, float y) {
		return getDrawPosition(x, y, 0).getX();
	}

	public float getYPosition(float x, float y, float z) {
		return getDrawPosition(x, y, z).getY();
	}

	public float getDrawSizeX(float size) {
		return size * zoomFactor;
	}

	public float getDrawSizeY(float size) {
		return size * zoomFactor * yFactor;
	}

	public float getDrawSizeZ(float size) {
		return size * zoomFactor * zFactor;
	}

	public void setSceneSize(int width, int height) {
		sceneWidth = width;
		sceneHeight = height;
	}

}
