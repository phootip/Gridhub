package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.FloorLevel;
import stage.ObjectMap;
import util.Constants.ColorSwatch;
import util.Constants.PlayerHelper;
import util.Helper;
import util.InputManager;

public class Player implements IDrawable {

	protected static float BALL_RADIUS = 0.4f;
	private static float BALL_TRAIL_RADIUS = 0.4f;
	private static int MAX_TRAIL_LENGTH = 50;

	private static final BasicStroke BALL_GLOW_STROKE = new BasicStroke(3);
	private static final BasicStroke BALL_MAIN_STROKE = new BasicStroke(9);

	private Color baseColor;
	private Stroke mainTrailStroke;
	private Color mainTrailColor;
	private Stroke glowTrailStroke;
	private Color glowTrailColor;

	private float drawX;
	private float drawY;
	private float drawZ;

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
	private boolean isOnSlope;

	private ArrayList<ArrayList<Vector3>> trailPosition;
	private ArrayList<ArrayList<Vector3>> shiftedTrailPosition;

	private FloorLevel floorLevelMap;

	public int getCellX() {
		return cellX;
	}

	public int getCellY() {
		return cellY;
	}

	public int getCellZ() {
		return cellZ;
	}

	public String getName() {
		return name;
	}

	private int playerId;

	protected int getPlayerId() {
		return playerId;
	}

	public void setPlayerPosition(int cellX, int cellY, int cellZ) {
		ObjectMap.drawableObjectHashMap.remove(new ObjectVector(this.cellX, this.cellY, this.cellZ, this.name));
		this.cellX = cellX;
		this.cellY = cellY;
		this.cellZ = cellZ;
		nextCellX = cellX;
		nextCellY = cellY;
		nextCellZ = cellZ;
		ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
		isMoving = true;
	}

	public void setCellX(int x) {
		ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		cellX = x;
		nextCellX = x;
		ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
	}

	public void setCellY(int y) {
		ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		cellY = y;
		nextCellY = y;
		ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
	}

	public void setCellZ(int z) {
		ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		cellZ = z;
		nextCellZ = z;
		ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);

	}

	/**
	 * @deprecated Use {@link #Player(int,FloorLevel,int,int,int)} instead
	 */
	public Player(int playerId, FloorLevel floorLevelMap) {
		this(playerId, floorLevelMap, 0, 0, 0);
	}

	public Player(int playerId, FloorLevel floorLevelMap, int initialX, int initialY, int initialZ) {
		this.floorLevelMap = floorLevelMap;

		drawX = cellX = oldCellX = nextCellX = initialX;
		drawY = cellY = oldCellY = nextCellY = initialY;
		drawZ = cellZ = oldCellZ = nextCellZ = initialZ;
		this.playerId = playerId;

		this.name = "Player" + playerId;
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
			shiftedTrailPosition.get(i)
					.add(new Vector3(trailDots[i]).multiply(BALL_TRAIL_RADIUS).add(drawX, drawY, drawZ));
		}

		// Set player drawing assets
		baseColor = PlayerHelper.getPlayerColor(playerId);
		mainTrailStroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		mainTrailColor = Helper.getAlphaColor(baseColor, 150);
		glowTrailStroke = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		glowTrailColor = Helper.getAlphaColor(baseColor, 75);
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
			shiftedTrailPosition.get(i)
					.add(new Vector3(newTrailDot).multiply(BALL_TRAIL_RADIUS).add(drawX, drawY, drawZ));

			if (trail.size() > MAX_TRAIL_LENGTH) {
				trail.remove(0);
				shiftedTrailPosition.get(i).remove(0);
			}
		}
	}

	public void update(int step, int cameraDirection) {
		float ballDiffX = 0;
		float ballDiffY = 0;
		float ballDiffZ = 0;

		if (!isMoving) {
			// Not correlated with x-y axis of grid, just the keyboard.
			int yDir = 0;
			int xDir = 0;

			if (PlayerHelper.isUpKeyPressing(playerId)) {
				yDir--;
			}
			if (PlayerHelper.isDownKeyPressing(playerId)) {
				yDir++;
			}
			if (PlayerHelper.isLeftKeyPressing(playerId)) {
				xDir--;
			}
			if (PlayerHelper.isRightKeyPressing(playerId)) {
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
		// first check floor level
		int floorLevelCellX = cellX;
		int floorLevelCellY = cellY;
		int floorLevelNextCellX = nextCellX;
		int floorLevelNextCellY = nextCellY;
		// System.out.println(isOnSlope);
		if (floorLevelMap.isOutOfMap(nextCellX, nextCellY)) {
			if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
				// if move diagonal then it can move either y or x
				boolean isNextX_OutOfMap = floorLevelMap.isOutOfMap(nextCellX, cellY);
				boolean isNextY_OutOfMap = floorLevelMap.isOutOfMap(cellX, nextCellY);
				if (isNextX_OutOfMap && isNextY_OutOfMap) {
					standStill();
				} else if (isNextY_OutOfMap) {
					// moveXAndTryPushX

					tryMoveAndPushXDirection();
				} else if (isNextX_OutOfMap) {
					tryMoveAndPushYDirection();
				} else {
					standStill();
				}

			} else if (nextCellX - cellX != 0) {
				standStill();
			} else if (nextCellY - cellY != 0) {
				standStill();
			}
		} else if (!(floorLevelMap.isOutOfMap(nextCellX, nextCellY))) {
			// not out of Map
			// In case floor is not equals
			if (cellZ != floorLevelMap.getZValueFromXY(floorLevelNextCellX, floorLevelNextCellY)) {
				// if floor is not equal to current Z
				if (cellZ > floorLevelMap.getZValueFromXY(floorLevelNextCellX, floorLevelNextCellY)) {
					// if player is on a higher floor then check for nextCell below
					IDrawable nextCellBelow = ObjectMap.drawableObjectHashMap
							.get(new ObjectVector(nextCellX, nextCellY, nextCellZ - 1));

					if (nextCellBelow instanceof Slope) {

						Slope slopeNextCell = (Slope) nextCellBelow;
						boolean isNextX_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelNextCellX,
								floorLevelCellY);
						boolean isNextY_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelCellX,
								floorLevelNextCellY);
						if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
							// if it moves diag it cannot get in to the slope
							if (isNextX_ZValueEqual) {
								// moveXAndTryPushX
								tryMoveAndPushXDirection();
							} else if (isNextY_ZValueEqual) {
								tryMoveAndPushYDirection();
							} else {
								standStill();
							}
						} else if ((nextCellX - cellX) != 0) {
							if (slopeNextCell.isAlignX()) {
								// will change to try push and move
								moveOnlyXandZ();
								isOnSlope = true;
							} else {
								standStill();
							}
						} else if ((nextCellY - cellY) != 0) {
							if (slopeNextCell.isAlignY()) {
								moveOnlyYandZ();
								isOnSlope = true;
							} else {
								standStill();
							}
						}
					} else if ((nextCellBelow == null || nextCellBelow instanceof Block
							|| nextCellBelow instanceof Slope) && isOnSlope) {
						// exit the slope from both direction when there is the box waiting
						Slope slopeBelow = (Slope) ObjectMap.drawableObjectHashMap
								.get(new ObjectVector(cellX, cellY, cellZ - 1));

						if (nextCellX - cellX != 0 && slopeBelow.isAlignX() && nextCellY - cellY == 0) {
							if (slopeBelow.isSlopeExit(cellX, cellY)) {
								if (nextCellBelow == null) {
									standStill();
									// for falling down the slope if there is nothing at the exit
									// setCellZ(cellZ-1);
									// moveOnlyXandZ();
									// isOnSlope = false;
								} else {
									tryMoveAndPushXDirection();
									isOnSlope = false;
								}

							} else {
								setCellZ(cellZ - 1);
								boolean isLeavingSlope = tryMoveAndPushXDirection();
								if (isLeavingSlope) {
									isOnSlope = false;
								} else {
									setCellZ(cellZ + 1);
									setCellX(cellX);
									setCellY(cellY);
								}
							}

						} else if (nextCellX - cellX == 0 && slopeBelow.isAlignY() && nextCellY - cellY != 0) {
							if (slopeBelow.isSlopeExit(cellX, cellY)) {
								if (nextCellBelow == null) {
									standStill();
								} else {
									tryMoveAndPushYDirection();
									isOnSlope = false;
								}

							} else {
								setCellZ(cellZ - 1);
								boolean isLeavingSlope = tryMoveAndPushYDirection();
								if (isLeavingSlope) {
									isOnSlope = false;
								} else {
									setCellZ(cellZ + 1);
									setCellX(cellX);
									setCellY(cellY);
								}
							}

						}
					} else if (nextCellBelow instanceof Block) {
						boolean isNextX_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelNextCellX,
								floorLevelCellY);
						boolean isNextY_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelCellX,
								floorLevelNextCellY);
						if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
							// if it moves diag it cannot get in to the slope
							if (isNextX_ZValueEqual) {
								// moveXAndTryPushX
								tryMoveAndPushXDirection();
							} else if (isNextY_ZValueEqual) {
								tryMoveAndPushYDirection();
							} else {
								standStill();
							}
						} else if ((nextCellX - cellX) != 0) {
							IDrawable nextXObstacle = ObjectMap.drawableObjectHashMap
									.get(new ObjectVector(nextCellX, cellY, nextCellZ));

							if (nextXObstacle != null) {
								if (nextXObstacle instanceof Block)
									tryMoveAndPushXDirection();
							} else {
								moveOnlyXandZ();
							}

						} else if ((nextCellY - cellY) != 0) {
							IDrawable nextYObstacle = ObjectMap.drawableObjectHashMap
									.get(new ObjectVector(cellX, nextCellY, nextCellZ));
							if (nextYObstacle != null) {
								if (nextYObstacle instanceof Block)
									tryMoveAndPushYDirection();
							} else {
								moveOnlyYandZ();
							}

						}

					}
				}
				if (!(ObjectMap.drawableObjectHashMap
						.get(new ObjectVector(nextCellX, nextCellY, nextCellZ)) instanceof Slope)) {
					// if the floor is not equal then it can't move
					boolean isNextX_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelNextCellX,
							floorLevelCellY);
					boolean isNextY_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelCellX,
							floorLevelNextCellY);
					if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
						// if move diagonal then it can move either y or x
						if (isNextX_ZValueEqual) {
							// moveXAndTryPushX
							tryMoveAndPushXDirection();
						} else if (isNextY_ZValueEqual) {
							tryMoveAndPushYDirection();
						} else {
							standStill();
						}

					} else if (nextCellX - cellX != 0) {
						standStill();
					} else if (nextCellY - cellY != 0) {
						standStill();
					}
				} else if ((ObjectMap.drawableObjectHashMap
						.get(new ObjectVector(nextCellX, nextCellY, nextCellZ)) instanceof Slope)) {
					// when the floor is not equal but next move is slope this means player is on the
					// the higher side of the slope and prepare to move down (cellZ and nextZ of slope will match)
					Slope slopeNextCell = (Slope) ObjectMap.drawableObjectHashMap
							.get(new ObjectVector(nextCellX, nextCellY, nextCellZ));

					boolean isNextX_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelNextCellX,
							floorLevelCellY);
					boolean isNextY_ZValueEqual = cellZ == floorLevelMap.getZValueFromXY(floorLevelCellX,
							floorLevelNextCellY);
					if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
						// if it moves diag it cannot get in to the slope
						if (isNextX_ZValueEqual) {
							// moveXAndTryPushX
							tryMoveAndPushXDirection();
						} else if (isNextY_ZValueEqual) {
							tryMoveAndPushYDirection();
						} else {
							standStill();
						}
					} else if ((nextCellX - cellX) != 0) {
						if (slopeNextCell.isAlignX()) {
							moveOnlyXandZ();

						} else {
							standStill();
						}
					} else if ((nextCellY - cellY) != 0) {
						if (slopeNextCell.isAlignY()) {
							moveOnlyYandZ();
						} else {
							standStill();
						}
					}
				}
			} else if (cellZ == floorLevelMap.getZValueFromXY(floorLevelNextCellX, floorLevelNextCellY)) {
				// if the Floorlevel is equal to Z
				if (isOnSlope) {
					// if player is on slope the only case player move at the same cellZ and floor is when player jump
					// back from slope entrance to the higher side
					isOnSlope = false;
				}
				IDrawable nextCellObstacle = ObjectMap.drawableObjectHashMap
						.get(new ObjectVector(nextCellX, nextCellY, nextCellZ));

				if (nextCellObstacle == null || nextCellObstacle instanceof IWalkOnAble) {
					// Action when player move diagonal
					if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
						// for Z there might not cause any problem

						IDrawable nextXObstacle = ObjectMap.drawableObjectHashMap
								.get(new ObjectVector(nextCellX, cellY, nextCellZ));
						IDrawable nextYObstacle = ObjectMap.drawableObjectHashMap
								.get(new ObjectVector(cellX, nextCellY, nextCellZ));

						if (nextXObstacle != null || nextYObstacle != null) {
							// if Obstacle is Pushable Object
							if (nextXObstacle != null && nextYObstacle != null) {
								if (nextXObstacle instanceof PushableObject
										&& nextYObstacle instanceof PushableObject) {
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

				} else if (nextCellObstacle != null && !(nextCellObstacle instanceof IWalkOnAble)) {

					if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
						// in case of moving in both y and x if there is an
						// obstacles and it's pushable object
						// player must not be able to push it
						IDrawable nextXObstacle = ObjectMap.drawableObjectHashMap
								.get(new ObjectVector(nextCellX, cellY, nextCellZ));
						IDrawable nextYObstacle = ObjectMap.drawableObjectHashMap
								.get(new ObjectVector(cellX, nextCellY, nextCellZ));

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
						if (nextCellObstacle instanceof PushableObject) {
							// player push along y or x axis only
							boolean isPushed = ((PushableObject) nextCellObstacle).push(0, nextCellX - cellX,
									nextCellY - cellY, nextCellZ - cellZ);

							if (isPushed) {
								moveAllDir();

							} else {
								standStill();
							}
						} else if (nextCellObstacle instanceof Slope) {
							// check if isSlopeEntrace
							Slope nextCellSlope = (Slope) nextCellObstacle;

							boolean isNextCellEntranceOfSlope = nextCellSlope.isSlopeEntrance(nextCellX, nextCellY);

							if (isNextCellEntranceOfSlope && nextCellSlope.isAlignX() && nextCellX - cellX != 0) {

								setCellZ(cellZ + 1);
								moveOnlyXandZ();
								isOnSlope = true;
							} else if (isNextCellEntranceOfSlope && nextCellSlope.isAlignY()
									&& nextCellY - cellY != 0) {
								setCellZ(cellZ + 1);
								moveOnlyYandZ();
								isOnSlope = true;
							} else {
								standStill();
							}
						}

					}

				}
			}

		}

		if (isMoving) {
			walkStep += step;
			int walkDuration = isMoveFast ? walkDurationFast : walkDurationSlow;

			if (walkStep >= walkDuration) {
				walkStep = 0;
				isMoving = false;

				oldCellX = cellX;
				oldCellY = cellY;
				oldCellZ = cellZ;
				drawX = cellX;
				drawY = cellY;

				Slope currentOnSlope = null;
				IDrawable candidateObj = ObjectMap.drawableObjectHashMap.get(new ObjectVector(cellX, cellY, cellZ - 1));

				if (candidateObj instanceof Slope) {
					currentOnSlope = (Slope) candidateObj;
				}

				if (currentOnSlope != null) {
					drawZ = currentOnSlope.getBallZ(drawX, drawY);
				} else {
					drawZ = cellZ;
				}
			} else {
				float ratio = (float) walkStep / walkDuration;
				ballDiffX = drawX;
				ballDiffY = drawY;
				ballDiffZ = drawZ;

				drawX = Helper.interpolate(oldCellX, cellX, ratio);
				drawY = Helper.interpolate(oldCellY, cellY, ratio);

				Slope currentOnSlope = null;
				ObjectVector candidateObjPosition;

				if (ratio <= 0.5f) {
					candidateObjPosition = new ObjectVector(oldCellX, oldCellY, (oldCellZ - 1));
				} else {
					candidateObjPosition = new ObjectVector(cellX, cellY, (cellZ - 1));
				}

				IDrawable candidateObj = ObjectMap.drawableObjectHashMap.get(candidateObjPosition);
				if (candidateObj instanceof Slope) {
					currentOnSlope = (Slope) candidateObj;
				}

				if (currentOnSlope != null) {
					drawZ = currentOnSlope.getBallZ(drawX, drawY);
				} else {
					drawZ = ratio <= 0.5f ? oldCellZ : cellZ;
				}

				ballDiffX = drawX - ballDiffX;
				ballDiffY = drawY - ballDiffY;
				ballDiffZ = drawZ - ballDiffZ;
			}
		}

		// drawZ = isOnSlope ? 1 : 0;
		// move on slope cheating
		// if (y == 10) {
		// if (x < 10 - 0.5)
		// z = 0;
		// else if (x > 12.5f)
		// z = 1;
		// else
		// z = Helper.interpolate(0, 1, (x - 9.5f) / 3f);
		// }

		updateTrail((float) Math.hypot(ballDiffX, ballDiffZ) * Math.signum(ballDiffX),
				(float) Math.hypot(ballDiffY, ballDiffZ) * Math.signum(ballDiffY));
	}

	public void draw(Graphics2D g, Camera camera) {

		float ballRadius = camera.getDrawSizeZ(BALL_RADIUS);
		Vector2 ballCenter = camera.getDrawPosition(drawX, drawY, drawZ).subtract(0, ballRadius);

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillOval(ballCenter.getIntX() - (int) ballRadius, ballCenter.getIntY() - (int) ballRadius,
				(int) (ballRadius * 2), (int) (ballRadius * 2));

		// Draw trail
		for (int i = 0; i < shiftedTrailPosition.size(); i++) {

			int n = shiftedTrailPosition.get(i).size();
			int[] xPos = new int[n];
			int[] yPos = new int[n];

			boolean isStill = true;

			for (int j = 0; j < shiftedTrailPosition.get(i).size(); j++) {

				Vector3 trail = new Vector3(shiftedTrailPosition.get(i).get(j)).add(0, 0, BALL_RADIUS);
				Vector2 pos = camera.getDrawPosition(trail);
				xPos[j] = (int) pos.getX();
				yPos[j] = (int) pos.getY();

				if (j > 0 && (xPos[j] != xPos[j - 1] || yPos[j] != yPos[j - 1]))
					isStill = false;
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

		g.setStroke(BALL_MAIN_STROKE);
		g.setColor(Helper.getAlphaColor(baseColor, 128));
		g.drawOval(ballCenter.getIntX() - (int) ballRadius, ballCenter.getIntY() - (int) ballRadius,
				(int) (ballRadius * 2), (int) (ballRadius * 2));
		g.setStroke(BALL_GLOW_STROKE);
		g.setColor(new Color(0xFF, 0xFF, 0xFF));
		g.drawOval(ballCenter.getIntX() - (int) ballRadius, ballCenter.getIntY() - (int) ballRadius,
				(int) (ballRadius * 2), (int) (ballRadius * 2));
	}

	private void pushXOrPushY(IDrawable nextXObstacle, IDrawable nextYObstacle) {
		// move in XDirection First
		boolean isPushed = ((PushableObject) nextXObstacle).push(0, nextCellX - cellX, 0, 0);

		if (isPushed) {
			// Push X if player can push
			ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, cellY, cellZ, this.name), this);
			cellX = nextCellX;
			nextCellY = cellY;
			nextCellZ = cellZ;

		} else {
			// push y if it cannot pushX
			isPushed = ((PushableObject) nextYObstacle).push(0, 0, nextCellY - cellY, 0);
		}

		if (isPushed) {
			ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(cellX, nextCellY, cellZ, this.name), this);
			nextCellX = cellX;
			cellY = nextCellY;
			nextCellZ = cellZ;
		} else {
			// if it cannot push both y and x then stand still
			standStill();
		}
	}

	private boolean pushX(IDrawable nextXObstacle) {
		boolean isPushed = ((PushableObject) nextXObstacle).push(0, nextCellX - cellX, 0, 0);

		if (isPushed) {
			ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, cellY, cellZ, this.name), this);
			cellX = nextCellX;
			nextCellY = cellY;
			nextCellZ = cellZ;
			return true;

		} else {
			standStill();
			return false;
		}
	}

	private boolean pushY(IDrawable nextYObstacle) {
		boolean isPushed = ((PushableObject) nextYObstacle).push(0, 0, nextCellY - cellY, 0);

		if (isPushed) {
			ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(cellX, nextCellY, cellZ, this.name), this);
			nextCellX = cellX;
			cellY = nextCellY;
			nextCellZ = cellZ;
			return true;

		} else {
			standStill();
			return false;
		}
	}

	private void standStill() {
		nextCellX = cellX;
		nextCellY = cellY;
		nextCellZ = cellZ;
	}

	private void moveOnlyYandZ() {
		ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		ObjectMap.drawableObjectHashMap.put(new ObjectVector(cellX, nextCellY, nextCellZ, this.name), this);
		nextCellX = cellX;
		cellY = nextCellY;
		cellZ = nextCellZ;
	}

	private void moveOnlyXandZ() {
		ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, cellY, nextCellZ, this.name), this);
		cellX = nextCellX;
		nextCellY = cellY;
		cellZ = nextCellZ;
	}

	private void moveAllDir() {
		ObjectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		ObjectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
		cellX = nextCellX;
		cellY = nextCellY;
		cellZ = nextCellZ;
	}

	private boolean tryMoveAndPushXDirection() {
		IDrawable nextObjectBelow = ObjectMap.drawableObjectHashMap
				.get(new ObjectVector(nextCellX, cellY, nextCellZ - 1));
		if (cellZ != floorLevelMap.getZValueFromXY(nextCellX, cellY) && !isOnSlope
				&& !(nextObjectBelow instanceof Block)) {
			standStill();
			return false;
		}
		IDrawable nextXObstacle = ObjectMap.drawableObjectHashMap.get(new ObjectVector(nextCellX, cellY, nextCellZ));

		if (nextXObstacle != null) {
			if (nextXObstacle instanceof PushableObject) {
				return pushX(nextXObstacle);

			} else if (nextXObstacle instanceof IWalkOnAble) {
				moveOnlyXandZ();
				return true;
			} else {
				standStill();
				return false;
			}
		} else {
			moveOnlyXandZ();
			return true;
		}
	}

	private boolean tryMoveAndPushYDirection() {
		IDrawable nextObjectBelow = ObjectMap.drawableObjectHashMap
				.get(new ObjectVector(cellX, nextCellY, nextCellZ - 1));
		if (cellZ != floorLevelMap.getZValueFromXY(cellX, nextCellY) && !isOnSlope
				&& !(nextObjectBelow instanceof Block)) {
			standStill();
			return false;
		}
		IDrawable nextYObstacle = ObjectMap.drawableObjectHashMap.get(new ObjectVector(cellX, nextCellY, nextCellZ));

		if (nextYObstacle != null) {
			if (nextYObstacle instanceof PushableObject) {
				return pushY(nextYObstacle);
			} else if (nextYObstacle instanceof IWalkOnAble) {
				moveOnlyYandZ();
				return true;
			} else {
				standStill();
				return false;
			}
		} else {
			moveOnlyYandZ();
			return true;
		}
	}

	private void tryMoveAndPushXYDirection() {
		IDrawable nextXObstacle = ObjectMap.drawableObjectHashMap.get(new ObjectVector(nextCellX, cellY, nextCellZ));
		IDrawable nextYObstacle = ObjectMap.drawableObjectHashMap.get(new ObjectVector(cellX, nextCellY, nextCellZ));

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
	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(drawX, drawY, drawZ);
	}

}
