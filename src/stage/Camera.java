package stage;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.gameobj.IDrawable;
import stage.gameobj.Player;
import util.Constants;
import util.Helper;

public class Camera {
	private float zoomFactor = 50;

	private Vector3 centerPos;

	private int sceneWidth;
	private int sceneHeight;

	private IDrawable followObj;

	public float getZoomFactor() {
		return zoomFactor;
	}

	public Camera(IDrawable followObj) {
		this.followObj = followObj;
		this.centerPos = getPreferredCenterPos();
		if (this.followObj instanceof Player) {
			((Player) this.followObj).assignCamera(this);
		}
	}

	private boolean deformationChanged = true;

	public boolean isDeformationChanged() {
		return deformationChanged;
	}

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
