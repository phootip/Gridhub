package scene.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import geom.Vector2;
import geom.Vector3;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;

class Player {

	protected static float BALL_RADIUS = 0.5f;
	private static float BALL_TRAIL_RADIUS = 0.5f;
	private static int MAX_TRAIL_LENGTH = 50;

	private float x;
	private float y;
	private float z;

	private int oldCellX;
	private int oldCellY;
	private int oldCellZ;

	private int cellX;
	private int cellY;
	private int cellZ;

	private ArrayList<ArrayList<Vector3>> trailPosition;
	private ArrayList<ArrayList<Vector3>> shiftedTrailPosition;

	protected float getX() {
		return x;
	}

	protected float getY() {
		return y;
	}

	protected float getZ() {
		return z;
	}

	protected int getCellX() {
		return cellX;
	}

	protected int getCellY() {
		return cellY;
	}

	protected int getCellZ() {
		return cellZ;
	}

	public Player() {
		x = y = z = cellX = cellY = cellZ = oldCellX = oldCellY = oldCellZ = 0;

		// Create initial trail dots
		float rotationX = 1.234f;
		float rotationY = 2.345f;
		float rotationZ = 3.456f;

		Vector3[] trailDots = new Vector3[6];

		trailDots[0] = new Vector3(1, 0, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[1] = new Vector3(-1, 0, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[2] = new Vector3(0, 1, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[3] = new Vector3(0, -1, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[4] = new Vector3(0, 0, 1).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[5] = new Vector3(0, 0, -1).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);

		trailPosition = new ArrayList<>();
		shiftedTrailPosition = new ArrayList<>();
		for (int i = 0; i < trailDots.length; i++) {
			trailPosition.add(new ArrayList<>());
			shiftedTrailPosition.add(new ArrayList<>());

			trailPosition.get(i).add(trailDots[i]);
			shiftedTrailPosition.get(i).add(new Vector3(trailDots[i]).multiply(BALL_TRAIL_RADIUS).add(x, y, z));
		}
	}

	private boolean isMoving = false;
	int walkStep = 0;
	final int walkDuration = 1000;

	private void updateTrail(float diffX, float diffY) {

		float angleXZ = -diffX / BALL_RADIUS;
		float angleYZ = -diffY / BALL_RADIUS;

		for (int i = 0; i < trailPosition.size(); i++) {
			ArrayList<Vector3> trail = trailPosition.get(i);

			Vector3 newTrailDot = new Vector3(trail.get(trail.size() - 1));
			newTrailDot.rotateXZ(angleXZ).rotateYZ(angleYZ);

			trail.add(newTrailDot);
			shiftedTrailPosition.get(i).add(new Vector3(newTrailDot).multiply(BALL_TRAIL_RADIUS).add(x, y, z));

			if (trail.size() > MAX_TRAIL_LENGTH) {
				trail.remove(0);
				shiftedTrailPosition.get(i).remove(0);
			}
		}
	}

	public void update(int step, int cameraDirection) {
		float ballDiffX = 0;
		float ballDiffY = 0;

		if (!isMoving) {
			// Not correlated with x-y axis of grid, just the keyboard.
			int yDir = 0;
			int xDir = 0;

			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_UP)) {
				yDir--;
			}
			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_DOWN)) {
				yDir++;
			}
			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_LEFT)) {
				xDir--;
			}
			if (InputManager.getInstance().isKeyPressing(KeyEvent.VK_RIGHT)) {
				xDir++;
			}

			if (xDir != 0 || yDir != 0) {
				isMoving = true;
				switch (cameraDirection) {
				case 0:
					cellX += xDir;
					cellY += yDir;
					break;
				case 1:
					cellX += yDir;
					cellY -= xDir;
					break;
				case 2:
					cellX -= xDir;
					cellY -= yDir;
					break;
				case 3:
					cellX -= yDir;
					cellY += xDir;
					break;
				}
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
				ballDiffX = x;
				ballDiffY = y;

				// Try changing to sineInterpolate for weird effect!
				x = Helper.interpolate(oldCellX, cellX, ratio);
				y = Helper.interpolate(oldCellY, cellY, ratio);
				z = Helper.interpolate(oldCellZ, cellZ, ratio);

				ballDiffX = x - ballDiffX;
				ballDiffY = y - ballDiffY;
			}
		}

		updateTrail(ballDiffX, ballDiffY);
	}

	public void draw(Graphics2D g, Camera camera) {

		float ballRadius = camera.getDrawSizeZ(BALL_RADIUS);
		Vector2 ballCenter = camera.getDrawPosition(x, y, z).subtract(0, ballRadius);

		g.setColor(ColorSwatch.BACKGROUND);
		g.fill(new Ellipse2D.Float(ballCenter.getX() - ballRadius, ballCenter.getY() - ballRadius, ballRadius * 2,
				ballRadius * 2));

		// Draw trail
		Stroke mainTrailStroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		Color mainTrailColor = new Color(0xFF, 0x00, 0x00, 150);
		Stroke glowTrailStroke = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		Color glowTrailColor = new Color(0xFF, 0x00, 0x00, 75);
		for (int i = 0; i < shiftedTrailPosition.size(); i++) {

			int n = shiftedTrailPosition.get(i).size();
			int[] xPos = new int[n];
			int[] yPos = new int[n];

			boolean isStill = true;

			for (int j = 0; j < shiftedTrailPosition.get(i).size(); j++) {

				// if (trailPosition.get(i).get(j - 1).getY() > 0 ||
				// trailPosition.get(i).get(j).getY() > 0) {
				// continue;

				// Vector3 startTrail = new
				// Vector3(shiftedTrailPosition.get(i).get(j - 1)).subtract(0,
				// 0, BALL_RADIUS);
				// Vector3 endTrail = new
				// Vector3(shiftedTrailPosition.get(i).get(j)).subtract(0, 0,
				// BALL_RADIUS);
				// Vector2 startPoint = camera.getDrawPosition(startTrail);
				// Vector2 endPoint = camera.getDrawPosition(endTrail);

				Vector3 trail = new Vector3(shiftedTrailPosition.get(i).get(j)).add(0, 0, BALL_RADIUS);
				Vector2 pos = camera.getDrawPosition(trail);
				xPos[j] = (int) pos.getX();
				yPos[j] = (int) pos.getY();

				if (j > 0 && (xPos[j] != xPos[j - 1] || yPos[j] != yPos[j - 1]))
					isStill = false;

				// g.setStroke(mainTrailStroke);
				// g.setColor(mainTrailColor);
				// g.draw(new Line2D.Float(startPoint.getX(), startPoint.getY(),
				// endPoint.getX(), endPoint.getY()));
				//
				// g.setStroke(glowTrailStroke);
				// g.setColor(glowTrailColor);
				// g.draw(new Line2D.Float(startPoint.getX(), startPoint.getY(),
				// endPoint.getX(), endPoint.getY()));
			}

			if (!isStill) {
				g.setStroke(mainTrailStroke);
				g.setColor(mainTrailColor);
				g.drawPolyline(xPos, yPos, n);

				g.setStroke(glowTrailStroke);
				g.setColor(glowTrailColor);
				g.drawPolyline(xPos, yPos, n);
			}

		}

		// Draw a ball

		g.setStroke(new BasicStroke(9));
		g.setColor(new Color(0xFF, 0, 0, 128));
		g.draw(new Ellipse2D.Float(ballCenter.getX() - ballRadius, ballCenter.getY() - ballRadius, ballRadius * 2,
				ballRadius * 2));
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(0xFF, 0xFF, 0xFF));
		g.draw(new Ellipse2D.Float(ballCenter.getX() - ballRadius, ballCenter.getY() - ballRadius, ballRadius * 2,
				ballRadius * 2));
	}

}
