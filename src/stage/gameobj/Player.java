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
import util.Constants;
import util.Constants.ColorSwatch;
import util.Constants.PlayerHelper;
import util.Helper;
import util.InputManager;

public class Player implements IDrawable, ICameraAssignable {

	protected static float BALL_RADIUS = 0.4f;

	private static final BasicStroke BALL_MAIN_STROKE = new BasicStroke(3);
	private static final BasicStroke BALL_GLOW_STROKE = new BasicStroke(9);

	private PlayerTrail playerTrail;
	private Color baseColor;

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
	private final int weight = 50;
	private boolean isOnSlope;

	private FloorLevel floorLevelMap;
	private transient ObjectMap objectMap;
	
	
	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
	}

	public int getWeight() {
		return weight;
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

	public String getName() {
		return name;
	}

	private int playerId;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerPosition(int cellX, int cellY, int cellZ) {
		objectMap.drawableObjectHashMap.remove(new ObjectVector(this.cellX, this.cellY, this.cellZ, this.name));
		this.cellX = cellX;
		this.cellY = cellY;
		this.cellZ = cellZ;
		nextCellX = cellX;
		nextCellY = cellY;
		nextCellZ = cellZ;
		objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
		isMoving = true;
	}

	public void setCellX(int x) {
		objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		cellX = x;
		nextCellX = x;
		objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
	}

	public void setCellY(int y) {
		objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		cellY = y;
		nextCellY = y;
		objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
	}

	public void setCellZ(int z) {
		objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		cellZ = z;
		nextCellZ = z;
		objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);

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

		this.baseColor = Constants.PlayerHelper.getPlayerColor(playerId);
		this.playerTrail = new PlayerTrail(this, this.baseColor);
	}

	private boolean isMoving = false;
	private int walkStep = 0;
	private boolean isMoveFast = false;
	private final int walkDurationSlow = 100 * 10;
	private final int walkDurationFast = 100 * 5;

	private Camera assignedCamera;

	@Override
	public void assignCamera(Camera camera) {
		this.assignedCamera = camera;
	}

	public void update(int step) {
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
				if (assignedCamera != null) {
					switch (assignedCamera.getRotation()) {
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

		}
		// check for NextStepCell
		// first check floor level
		int floorLevelCellX = cellX;
		int floorLevelCellY = cellY;
		int floorLevelNextCellX = nextCellX;
		int floorLevelNextCellY = nextCellY;
		// System.out.println(isOnSlope);
		if (floorLevelMap.isOutOfMap(nextCellX, nextCellY) || isNextCellPlayer(nextCellX, nextCellY, nextCellZ)) {
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
					IDrawable nextCellBelow = objectMap.drawableObjectHashMap
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
						Slope slopeBelow = (Slope) objectMap.drawableObjectHashMap
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
								if (isNextCellPlayer(nextCellX, nextCellY, nextCellZ - 1)) {
									standStill();
								} else {
									setCellZ(cellZ - 1);
									boolean isLeavingSlope = tryMoveAndPushXDirection();
									if (isLeavingSlope) {
										isOnSlope = false;
										if(objectMap.drawableObjectHashMap.get(new ObjectVector(cellX, cellY, cellZ-1)) instanceof Slope) isOnSlope = true;
									} else {
										setCellZ(cellZ + 1);
										setCellX(cellX);
										setCellY(cellY);
									}
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
								if (isNextCellPlayer(nextCellX, nextCellY, nextCellZ - 1)) {
									standStill();
								} else {
									setCellZ(cellZ - 1);
								boolean isLeavingSlope = tryMoveAndPushYDirection();
								if (isLeavingSlope) {
									isOnSlope = false;
									if(objectMap.drawableObjectHashMap.get(new ObjectVector(cellX, cellY, cellZ-1)) instanceof Slope) isOnSlope = true;
								} else {
									setCellZ(cellZ + 1);
									setCellX(cellX);
									setCellY(cellY);
								}

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
							IDrawable nextXObstacle = objectMap.drawableObjectHashMap
									.get(new ObjectVector(nextCellX, cellY, nextCellZ));

							if (nextXObstacle != null) {
								if (nextXObstacle instanceof Block)
									tryMoveAndPushXDirection();
							} else {
								moveOnlyXandZ();
							}

						} else if ((nextCellY - cellY) != 0) {
							IDrawable nextYObstacle = objectMap.drawableObjectHashMap
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
				if (!(objectMap.drawableObjectHashMap
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
				} else if ((objectMap.drawableObjectHashMap
						.get(new ObjectVector(nextCellX, nextCellY, nextCellZ)) instanceof Slope)) {
					// when the floor is not equal but next move is slope this means player is on the
					// the higher side of the slope and prepare to move down (cellZ and nextZ of slope will match)
					Slope slopeNextCell = (Slope) objectMap.drawableObjectHashMap
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
				if (isOnSlope && (nextCellX - cellX) != 0 && nextCellY - cellY !=0) {
					// if player is on slope the only case player move at the same cellZ and floor is when player jump
					// back from slope entrance to the higher side
					standStill();
					isOnSlope = true;
				} else if(isOnSlope) {
					isOnSlope = false;
				}
				IDrawable nextCellObstacle = objectMap.drawableObjectHashMap
						.get(new ObjectVector(nextCellX, nextCellY, nextCellZ));

				if ((nextCellObstacle == null || nextCellObstacle instanceof IWalkOnAble)) {
					// Action when player move diagonal
					if ((nextCellX - cellX) != 0 && (nextCellY - cellY) != 0) {
						// for Z there might not cause any problem

						IDrawable nextXObstacle = objectMap.drawableObjectHashMap
								.get(new ObjectVector(nextCellX, cellY, nextCellZ));
						IDrawable nextYObstacle = objectMap.drawableObjectHashMap
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
						IDrawable nextXObstacle = objectMap.drawableObjectHashMap
								.get(new ObjectVector(nextCellX, cellY, nextCellZ));
						IDrawable nextYObstacle = objectMap.drawableObjectHashMap
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

							if (isNextCellEntranceOfSlope && nextCellSlope.isAlignX() && nextCellX - cellX != 0
									&& !isNextCellPlayer(nextCellX, nextCellY, nextCellZ + 1)) {

								setCellZ(cellZ + 1);
								moveOnlyXandZ();
								isOnSlope = true;
							} else if (isNextCellEntranceOfSlope && nextCellSlope.isAlignY() && nextCellY - cellY != 0
									&& !isNextCellPlayer(nextCellX, nextCellY, nextCellZ + 1)) {
								setCellZ(cellZ + 1);
								moveOnlyYandZ();
								isOnSlope = true;
							} else {
								standStill();
							}
						} else if (nextCellObstacle instanceof Gate) {
							standStill();
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
				IDrawable candidateObj = objectMap.drawableObjectHashMap.get(new ObjectVector(cellX, cellY, cellZ - 1));

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

				IDrawable candidateObj = objectMap.drawableObjectHashMap.get(candidateObjPosition);
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

		playerTrail.update(step,
				(float) Math.hypot(ballDiffX, ballDiffZ) * Math.signum(ballDiffX), (float) Math.hypot(ballDiffY, ballDiffZ) * Math.signum(ballDiffY));
	}

	public PlayerTrail getPlayerTrail() {
		return playerTrail;
	}

	public void drawOverlay(Graphics2D g, Camera camera) {
		float ballRadius = camera.getDrawSizeZ(BALL_RADIUS);
		Vector2 ballCenter = camera.getDrawPosition(drawX, drawY, drawZ).subtract(0, ballRadius);

		g.setColor(ColorSwatch.FOREGROUND);
		BasicStroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10,
				new float[] { 2f, 2f }, 0);
		g.setStroke(dashedStroke);
		g.drawOval(ballCenter.getIntX() - (int) ballRadius, ballCenter.getIntY() - (int) ballRadius,
				(int) (ballRadius * 2), (int) (ballRadius * 2));
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {

		float ballRadius = camera.getDrawSizeZ(BALL_RADIUS);
		Vector2 ballCenter = camera.getDrawPosition(drawX, drawY, drawZ).subtract(0, ballRadius);

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillOval(ballCenter.getIntX() - (int) ballRadius, ballCenter.getIntY() - (int) ballRadius,
				(int) (ballRadius * 2), (int) (ballRadius * 2));

		g.setStroke(BALL_GLOW_STROKE);
		g.setColor(Helper.getAlphaColor(baseColor, 128));
		g.drawOval(ballCenter.getIntX() - (int) ballRadius, ballCenter.getIntY() - (int) ballRadius,
				(int) (ballRadius * 2), (int) (ballRadius * 2));
		g.setStroke(BALL_MAIN_STROKE);
		g.setColor(new Color(0xFF, 0xFF, 0xFF));
		g.drawOval(ballCenter.getIntX() - (int) ballRadius, ballCenter.getIntY() - (int) ballRadius,
				(int) (ballRadius * 2), (int) (ballRadius * 2));
	}

	private void pushXOrPushY(IDrawable nextXObstacle, IDrawable nextYObstacle) {
		// move in XDirection First
		boolean isPushed = ((PushableObject) nextXObstacle).push(0, nextCellX - cellX, 0, 0);

		if (isPushed) {
			// Push X if player can push
			objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, cellY, cellZ, this.name), this);
			cellX = nextCellX;
			nextCellY = cellY;
			nextCellZ = cellZ;

		} else {
			// push y if it cannot pushX
			isPushed = ((PushableObject) nextYObstacle).push(0, 0, nextCellY - cellY, 0);
		}

		if (isPushed) {
			objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			objectMap.drawableObjectHashMap.put(new ObjectVector(cellX, nextCellY, cellZ, this.name), this);
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
			objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, cellY, cellZ, this.name), this);
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
			objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
			objectMap.drawableObjectHashMap.put(new ObjectVector(cellX, nextCellY, cellZ, this.name), this);
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

		objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		objectMap.drawableObjectHashMap.put(new ObjectVector(cellX, nextCellY, nextCellZ, this.name), this);
		nextCellX = cellX;
		cellY = nextCellY;
		cellZ = nextCellZ;
	}

	private void moveOnlyXandZ() {

		objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, cellY, nextCellZ, this.name), this);
		cellX = nextCellX;
		nextCellY = cellY;
		cellZ = nextCellZ;
	}

	private void moveAllDir() {

		objectMap.drawableObjectHashMap.remove(new ObjectVector(cellX, cellY, cellZ, this.name));
		objectMap.drawableObjectHashMap.put(new ObjectVector(nextCellX, nextCellY, nextCellZ, this.name), this);
		cellX = nextCellX;
		cellY = nextCellY;
		cellZ = nextCellZ;
	}

	private boolean tryMoveAndPushXDirection() {
		IDrawable nextObjectBelow = objectMap.drawableObjectHashMap
				.get(new ObjectVector(nextCellX, cellY, nextCellZ - 1));
		if (cellZ != floorLevelMap.getZValueFromXY(nextCellX, cellY) && !isOnSlope
				&& !(nextObjectBelow instanceof Block)) {
			standStill();
			return false;
		}
		IDrawable nextXObstacle = objectMap.drawableObjectHashMap.get(new ObjectVector(nextCellX, cellY, nextCellZ));

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
		IDrawable nextObjectBelow = objectMap.drawableObjectHashMap
				.get(new ObjectVector(cellX, nextCellY, nextCellZ - 1));
		if (cellZ != floorLevelMap.getZValueFromXY(cellX, nextCellY) && !isOnSlope
				&& !(nextObjectBelow instanceof Block)) {
			standStill();
			return false;
		}
		IDrawable nextYObstacle = objectMap.drawableObjectHashMap.get(new ObjectVector(cellX, nextCellY, nextCellZ));

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
		IDrawable nextXObstacle = objectMap.drawableObjectHashMap.get(new ObjectVector(nextCellX, cellY, nextCellZ));
		IDrawable nextYObstacle = objectMap.drawableObjectHashMap.get(new ObjectVector(cellX, nextCellY, nextCellZ));

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

	private boolean isNextCellPlayer(int x, int y, int z) {
		if (this.playerId == 1) {
			return objectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)) != null;
		} else {
			return objectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)) != null;
		}

	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(drawX, drawY, drawZ);
	}

}
