package scene.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import core.geom.Vector2;
import core.geom.Vector3;
import objectInterface.IDrawable;
import objectInterface.PushableObject;
import objectInterface.WalkThroughable;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;

class Player implements IDrawable {

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

	private int nextCellX;
	private int nextCellY;
	private int nextCellZ;

	private String name;

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

	public String getName() {
		return name;
	}

	public Player(String name) {
		x = y = z = cellX = cellY = cellZ = oldCellX = oldCellY = oldCellZ = nextCellX = nextCellY = nextCellZ = 0;
		this.name = name;
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
	private int walkStep = 0;
	private boolean isMoveFast = false;
	final int walkDurationSlow = 100 * 10;
	final int walkDurationFast = 100 * 5;

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
				isMoveFast = InputManager.getInstance().isKeyPressing(KeyEvent.VK_SHIFT);
				switch (cameraDirection) {
				case 0:
					nextCellX += xDir;
					nextCellY += yDir;
					break;
				case 1:
					nextCellX += yDir;
					nextCellY -= xDir;
					break;
				case 2:
					nextCellX -= xDir;
					nextCellY -= yDir;
					break;
				case 3:
					nextCellX -= yDir;
					nextCellY += xDir;
					break;
				}
			}

		}
		// check for NextStepCell
		IDrawable nextCellObstacle = ObjectMap.drawableObjectHashMap.get(nextCellX + " " + nextCellY + " " + nextCellZ);
		if (nextCellObstacle == null) {
			// Action when player move diagonal
			if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
				// for Z there might not cause any problem
				IDrawable nextXObstacle = ObjectMap.drawableObjectHashMap
						.get(nextCellX + " " + cellY + " " + nextCellZ);
				IDrawable nextYObstacle = ObjectMap.drawableObjectHashMap
						.get(cellX + " " + nextCellY + " " + nextCellZ);

				if (nextXObstacle != null || nextYObstacle != null) {
					// if Obstacle is Pushable Object
					if (nextXObstacle != null && nextYObstacle != null) {
						if (nextXObstacle instanceof PushableObject && nextYObstacle instanceof PushableObject) {
							// if both xObstacle and yObstacle pushable object
							// move in XDirection First
							pushXOrPushY(nextXObstacle, nextYObstacle);

						} else if (nextXObstacle instanceof PushableObject) {
							// if only xObstacle is pushable object, then move
							// XDirection
							pushX(nextXObstacle);

						} else if (nextYObstacle instanceof PushableObject) {
							// if only yObstacle is pushable object, then move
							// YDirection
							pushY(nextYObstacle);
						} else {
							// if both Obstacles is not pushable object -- Do
							// not move
							standStill();
						}

					} else if (nextXObstacle != null) {
						// if there is xObstacle but no yObstacle then move
						// YDirection
						moveOnlyYandZ();
					} else if (nextYObstacle != null) {
						// if there is yObstacle but no xObstacle then move
						// XDirection
						moveOnlyXandZ();
					}

				} else {
					// If there is no obstacles for both y and x, then player
					// can move freely
					moveAllDir();
				}
			} else {
				// for object moving in only y or x axis (and no obstacles at
				// all) it can move freely
				moveAllDir();
			}

		} else if (nextCellObstacle != null && nextCellObstacle != this) {

			if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
				// in case of moving in both y and x if there is an
				// obstacles and it's pushable object
				// player must not be able to push it
				IDrawable nextXObstacle = ObjectMap.drawableObjectHashMap
						.get(nextCellX + " " + cellY + " " + nextCellZ);
				IDrawable nextYObstacle = ObjectMap.drawableObjectHashMap
						.get(cellX + " " + nextCellY + " " + nextCellZ);

				if (nextXObstacle != null && nextYObstacle != null) {
					// if both are pushable Object, consider x first. If x
					// cannot be pushed then consider y
					if (nextXObstacle instanceof PushableObject && nextYObstacle instanceof PushableObject) {
						// PushX Then Push Y
						pushXOrPushY(nextXObstacle, nextYObstacle);

					} else if (nextXObstacle instanceof PushableObject) {
						// if it can push x then push x first
						pushX(nextXObstacle);

					} else if (nextYObstacle instanceof PushableObject) {
						// if it cannot push x then try push y
						pushY(nextYObstacle);
					} else {
						// if it cannot push both then stand still
						standStill();
					}

				} else if (nextXObstacle != null) {
					// if there is nextXObstacle then move Y instead and push
					// nothing
					moveOnlyYandZ();
				} else {
					// if there is nextYObstacle or there is no Obstacle for
					// both nextY and nextX then move X Direction and push
					// nothing
					moveOnlyXandZ();
				}

			} else {
				// player push along y or x axis only
				boolean isPushed = ((PushableObject) nextCellObstacle).push(0, nextCellX - cellX, nextCellY - cellY,
						nextCellZ - cellZ);

				if (isPushed) {
					moveAllDir();

				} else {
					standStill();
				}
			}

		}

		if (isMoving)

		{
			walkStep += step;
			int walkDuration = isMoveFast ? walkDurationFast : walkDurationSlow;

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

	private void pushXOrPushY(IDrawable nextXObstacle, IDrawable nextYObstacle) {
		// move in XDirection First
		boolean isPushed = ((PushableObject) nextXObstacle).push(0, nextCellX - cellX, 0, 0);

		if (isPushed) {
			// Push X if player can push
			ObjectMap.drawableObjectHashMap.remove(cellX + " " + cellY + " " + cellZ + " " + this.name);
			ObjectMap.drawableObjectHashMap.put(nextCellX + " " + cellY + " " + cellZ + " " + this.name, this);
			cellX = nextCellX;
			nextCellY = cellY;
			nextCellZ = cellZ;

		} else {
			// push y if it cannot pushX
			isPushed = ((PushableObject) nextYObstacle).push(0, 0, nextCellY - cellY, 0);
		}

		if (isPushed) {
			ObjectMap.drawableObjectHashMap.remove(cellX + " " + cellY + " " + cellZ + " " + this.name);
			ObjectMap.drawableObjectHashMap.put(cellX + " " + nextCellY + " " + cellZ + " " + this.name, this);
			nextCellX = cellX;
			cellY = nextCellY;
			nextCellZ = cellZ;
		} else {
			// if it cannot push both y and x then stand still
			standStill();
		}
	}

	private void pushX(IDrawable nextXObstacle) {
		boolean isPushed = ((PushableObject) nextXObstacle).push(0, nextCellX - cellX, 0, 0);

		if (isPushed) {
			ObjectMap.drawableObjectHashMap.remove(cellX + " " + cellY + " " + cellZ + " " + this.name);
			ObjectMap.drawableObjectHashMap.put(nextCellX + " " + cellY + " " + cellZ + " " + this.name, this);
			cellX = nextCellX;
			nextCellY = cellY;
			nextCellZ = cellZ;

		} else {
			standStill();
		}
	}

	private void pushY(IDrawable nextYObstacle) {
		boolean isPushed = ((PushableObject) nextYObstacle).push(0, 0, nextCellY - cellY, 0);

		if (isPushed) {
			ObjectMap.drawableObjectHashMap.remove(cellX + " " + cellY + " " + cellZ + " " + this.name);
			ObjectMap.drawableObjectHashMap.put(cellX + " " + nextCellY + " " + cellZ + " " + this.name, this);
			nextCellX = cellX;
			cellY = nextCellY;
			nextCellZ = cellZ;

		} else {
			standStill();
		}
	}

	private void standStill() {
		nextCellX = cellX;
		nextCellY = cellY;
		nextCellZ = cellZ;
	}

	private void moveOnlyYandZ() {
		ObjectMap.drawableObjectHashMap.remove(cellX + " " + cellY + " " + cellZ + " " + this.name);
		ObjectMap.drawableObjectHashMap.put(cellX + " " + nextCellY + " " + nextCellZ + " " + this.name, this);
		nextCellX = cellX;
		cellY = nextCellY;
		cellZ = nextCellZ;
	}

	private void moveOnlyXandZ() {
		ObjectMap.drawableObjectHashMap.remove(cellX + " " + cellY + " " + cellZ + " " + this.name);
		ObjectMap.drawableObjectHashMap.put(nextCellX + " " + cellY + " " + nextCellZ + " " + this.name, this);
		cellX = nextCellX;
		nextCellY = cellY;
		cellZ = nextCellZ;
	}

	private void moveAllDir() {
		ObjectMap.drawableObjectHashMap.remove(cellX + " " + cellY + " " + cellZ + " " + this.name);
		ObjectMap.drawableObjectHashMap.put(nextCellX + " " + nextCellY + " " + nextCellZ + " " + this.name, this);
		cellX = nextCellX;
		cellY = nextCellY;
		cellZ = nextCellZ;
	}

}
