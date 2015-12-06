package scene.test;

import geom.Vector2;
import geom.Vector3;

class Camera {
	private float zoomFactor = 50;

	private float centerX;
	private float centerY;

	private int sceneWidth;
	private int sceneHeight;

	private Player player;

	public float getZoomFactor() {
		return zoomFactor;
	}

	public Camera(Player player) {
		centerX = player.getX();
		centerY = player.getY();
		this.player = player;
	}

	private final float FOLLOW_SPEED = (float) Math.pow(5, 1.0 / 60);

	public void update(int step) {
		centerX += (player.getX() - centerX) / Math.pow(FOLLOW_SPEED, step);
		centerY += (player.getY() - centerY) / Math.pow(FOLLOW_SPEED, step);
	}

	public Vector2 getDrawPosition(float x, float y, float z) {
		return new Vector2(getXPosition(x), getYPosition(y, z));
	}

	public Vector2 getDrawPosition(Vector3 v) {
		return getDrawPosition(v.getX(), v.getY(), v.getZ());
	}

	public float getXPosition(float x) {
		return sceneWidth / 2f + getDrawSizeX(x - centerX);
	}

	public float getYPosition(float y, float z) {
		return sceneHeight / 2f + getDrawSizeY(y - centerY) + getDrawSizeZ(z);
	}

	public float getDrawSizeX(float size) {
		return size * zoomFactor;
	}

	public float getDrawSizeY(float size) {
		return size * zoomFactor / 2f;
	}

	public float getDrawSizeZ(float size) {
		return size * zoomFactor;
	}

	public void setSceneSize(int width, int height) {
		sceneWidth = width;
		sceneHeight = height;
	}

}
