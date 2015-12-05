package scene.test;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import com.sun.glass.events.KeyEvent;

import geom.Vector2;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;

class Player {

	public static int BALL_SIZE = 50;

	private float x;
	private float y;
	private float z;

	private int oldCellX;
	private int oldCellY;
	private int oldCellZ;

	private int cellX;
	private int cellY;
	private int cellZ;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public int getCellX() {
		return cellX;
	}

	public int getCellY() {
		return cellY;
	}

	public int getCellZ() {
		return cellZ;
	}

	public Player() {
		x = y = z = cellX = cellY = cellZ = oldCellX = oldCellY = oldCellZ = 0;
	}

	private boolean isMoving = false;
	int walkStep = 0;
	final int walkDuration = 1000;

	public void update(int step) {
		if (!isMoving) {
			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_UP)) {
				isMoving = true;
				cellY--;
			}
			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_DOWN)) {
				isMoving = true;
				cellY++;
			}
			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_LEFT)) {
				isMoving = true;
				cellX--;
			}
			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_RIGHT)) {
				isMoving = true;
				cellX++;
			}
		}
		if (isMoving) {
			walkStep += step;

			if (walkStep >= walkDuration) {
				walkStep = 0;
				isMoving = false;

				oldCellX = cellX;
				oldCellY = cellY;
				oldCellZ = cellZ;
				x = cellX;
				y = cellY;
				z = cellZ;
			} else {
				float ratio = (float) walkStep / walkDuration;
				x = Helper.interpolate(oldCellX, cellX, ratio);
				y = Helper.interpolate(oldCellY, cellY, ratio);
				z = Helper.interpolate(oldCellZ, cellZ, ratio);
			}
		}
	}

	public void draw(Graphics2D g, Camera camera) {
		g.setStroke(new BasicStroke(3));
		g.setColor(ColorSwatch.FOREGROUND);

		float ballRadius = camera.getDrawSizeZ(0.5f);
		Vector2 ballCenter = camera.getDrawPosition(x, y, z).subtract(0, ballRadius);

		g.draw(new Ellipse2D.Float(ballCenter.getX() - ballRadius, ballCenter.getY() - ballRadius, ballRadius * 2,
				ballRadius * 2));
	}

}
