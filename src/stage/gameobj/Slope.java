package stage.gameobj;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;

/**
 * This class represents slope Object.
 * 
 * @author Thanat Jatuphattharachat
 *
 */

public class Slope implements ILargeDrawable {
	private static final float START_Z_SHIFTER = 0.15f;
	private int startX;
	private int startY;
	private int startZ;
	private int endZ;
	private int endX;
	private int endY;
	private int drawX;
	private int drawY;
	private boolean isAlignX;
	private boolean isAlignY;

	/**
	 * Constant representing the slope going upstair in right direction (+x).
	 */
	public static final int ALIGNMENT_RIGHT = 1;
	/**
	 * Constant representing the slope going upstair in down direction (+y).
	 */
	public static final int ALIGNMENT_DOWN = 2;
	/**
	 * Constant representing the slope going upstair in left direction (-x).
	 */
	public static final int ALIGNMENT_LEFT = -1;
	/**
	 * Constant representing the slope going upstair in up direction (-y).
	 */
	public static final int ALIGNMENT_UP = -2;

	/**
	 * Create Slope with specified position and alignment.
	 * 
	 * @param startX
	 *            the starting x position of Slope
	 * @param startY
	 *            the starting y position of Slope
	 * @param alignment
	 *            Possible value are {@link #ALIGNMENT_RIGHT}, {@link #ALIGNMENT_DOWN}, {@link #ALIGNMENT_LEFT}, and
	 *            {@link #ALIGNMENT_UP}.
	 */
	public Slope(int startX, int startY, int startZ, int alignment) {
		super();
		this.startX = startX;
		this.startY = startY;
		switch (alignment) {
			case ALIGNMENT_RIGHT:
				endX = startX + 2;
				endY = startY;
				break;
			case ALIGNMENT_DOWN:
				endX = startX;
				endY = startY + 2;
				break;
			case ALIGNMENT_LEFT:
				endX = startX - 2;
				endY = startY;
				break;
			case ALIGNMENT_UP:
				endX = startX;
				endY = startY - 2;
				break;

			default:
				throw new IllegalArgumentException("Invalid Slope alignment : " + alignment);
		}
		this.startZ = startZ;
		endZ = startZ + 1;
		isAlignX = alignment == ALIGNMENT_RIGHT || alignment == ALIGNMENT_LEFT;
		isAlignY = alignment == ALIGNMENT_DOWN || alignment == ALIGNMENT_UP;
	}

	public void update(int step, Camera camera) {
		// Calculate drawX and drawY

		float cameraAngle = camera.getRotationAngle();
		if (cameraAngle < Math.PI * 1 / 2) {
			// System.out.println("1");
			drawX = Math.max(startX, endX);
			drawY = Math.max(startY, endY);
		} else if (cameraAngle < Math.PI * 2 / 2) {
			// System.out.println("2");
			drawX = Math.max(startX, endX);
			drawY = Math.min(startY, endY);
		} else if (cameraAngle < Math.PI * 3 / 2) {
			// System.out.println("3");
			drawX = Math.min(startX, endX);
			drawY = Math.min(startY, endY);
		} else {
			// System.out.println("4");
			drawX = Math.min(startX, endX);
			drawY = Math.max(startY, endY);
		}
	}

	/**
	 * This method check the x alignment of the Slope
	 * 
	 * @return whether the slope alignment is X Direction
	 */
	public boolean isAlignX() {
		return isAlignX;
	}

	/**
	 * This method check the Y alignment of the Slope
	 * 
	 * @return whether the slope alignment is Y Direction
	 */
	public boolean isAlignY() {
		return isAlignY;
	}

	/**
	 * This method check whether the x y postion is The entrance of the slope
	 * 
	 * @param x
	 *            x position needed to be checked
	 * @param y
	 *            y Position needed to be checked
	 * @return whether it is Slope Entrance
	 */
	public boolean isSlopeEntrance(int x, int y) {
		return x == startX && y == startY;
	}

	/**
	 * This method check whether the x y postion is The exit of the slope
	 * 
	 * @param x
	 *            x position needed to be checked
	 * @param y
	 *            y Position needed to be checked
	 * @return whether it is Slope Exit
	 */
	public boolean isSlopeExit(int x, int y) {
		return x == endX && y == endY;
	}

	/**
	 * This method check whether the position x y is on the Slope
	 * 
	 * @param x
	 *            x position needed to be checked
	 * @param y
	 *            y position needed to be checked
	 * @return whether it is on the Slope
	 */
	public boolean isObjectOnSlope(int x, int y) {
		return x >= startX && x <= endX && y >= startY && y <= endY;
	}

	/**
	 * get the start position of x
	 * 
	 * @return initial x position of the Slope
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * get the start position of y
	 * 
	 * @return initial y position of the Slope
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * get the start position of z
	 * 
	 * @return initial z position of the Slope
	 */
	public int getStartZ() {
		return startZ;
	}

	/**
	 * get the end position of z
	 * 
	 * @return Endpoint of z position of the Slope
	 */
	public int getEndZ() {
		return endZ;
	}

	/**
	 * get the end position of x
	 * 
	 * @return Endpoint of x position of the Slope
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * get the end position of y
	 * 
	 * @return Endpoint of y position of the Slope
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * draw the object slope in the scene
	 */
	@Override
	public void draw(Graphics2D g, Camera camera) {
		throw new UnsupportedOperationException("Please call draw() method with position parameter instead.");
	}

	/**
	 * Calculate the z-position of the ball, assuming that the ball is on this slope.
	 * 
	 * @param drawX
	 *            current x-position of the ball
	 * @param drawY
	 *            current y-position of the ball
	 * @return Current z-position of the ball.
	 */
	public float getBallZ(float drawX, float drawY) {

		if (isAlignX()) {
			float lowX = startX;
			float highX = endX;
			boolean isReverse = false;

			if (lowX > highX) {
				isReverse = true;

				float temp = lowX;
				lowX = highX;
				highX = temp;
			}

			lowX -= 0.5f;
			highX += 0.5f;
			float ans = Helper.clamp(0, 1, Helper.interpolate(0, 1, (drawX - lowX) / (highX - lowX)));
			if (isReverse) {
				ans = 1 - ans;
			}

			return ans + startZ;
		} else if (isAlignY()) {
			float lowY = startY;
			float highY = endY;
			boolean isReverse = false;

			if (lowY > highY) {
				isReverse = true;

				float temp = lowY;
				lowY = highY;
				highY = temp;
			}

			lowY -= 0.5f;
			highY += 0.5f;
			float ans = Helper.clamp(0, 1, Helper.interpolate(0, 1, (drawY - lowY) / (highY - lowY)));
			if (isReverse) {
				ans = 1 - ans;
			}

			return ans + startZ;
		} else {
			throw new RuntimeException("WHATT!!?!?!??");
		}

	}

	@Override
	public Collection<Vector3> getDrawPositionList() {
		List<Vector3> positionList = new ArrayList<>();

		for (int i = Math.min(startX, endX); i <= Math.max(startX, endX); i++) {
			for (int j = Math.min(startY, endY); j <= Math.max(startY, endY); j++) {
				positionList.add(new Vector3(i, j, startZ + (i == startX && j == startY ? -START_Z_SHIFTER : 0)));
			}
		}

		return positionList;
	}

	private void drawStartPiece(Graphics2D g, Camera camera) {
		Vector3 startP1 = new Vector3(startX, startY, startZ);
		Vector3 endP1 = new Vector3(startX, startY, (startZ * 2 + endZ) / 3f);
		Vector3 midP1 = new Vector3(startX, startY, startZ);
		Vector3 startP2 = new Vector3(startP1);
		Vector3 endP2 = new Vector3(endP1);
		Vector3 midP2 = new Vector3(midP1);

		if (isAlignX) {
			float shifter = (startX < endX) ? -0.5f : 0.5f;

			startP1.add(shifter, 0.5f, 0);
			endP1.add(-shifter, 0.5f, 0);
			midP1.add(-shifter, 0.5f, 0);

			startP2.add(shifter, -0.5f, 0);
			endP2.add(-shifter, -0.5f, 0);
			midP2.add(-shifter, -0.5f, 0);

		} else if (isAlignY) {
			float shifter = (startY < endY) ? -0.5f : 0.5f;

			startP1.add(0.5f, shifter, 0);
			endP1.add(0.5f, -shifter, 0);
			midP1.add(0.5f, -shifter, 0);

			startP2.add(-0.5f, shifter, 0);
			endP2.add(-0.5f, -shifter, 0);
			midP2.add(-0.5f, -shifter, 0);
		}

		Vector2 startV1 = camera.getDrawPosition(startP1);
		Vector2 endV1 = camera.getDrawPosition(endP1);
		Vector2 midV1 = camera.getDrawPosition(midP1);
		Vector2 startV2 = camera.getDrawPosition(startP2);
		Vector2 endV2 = camera.getDrawPosition(endP2);
		Vector2 midV2 = camera.getDrawPosition(midP2);

		if (midV1.getY() < midV2.getY()) {
			Vector2 temp;

			temp = startV1;
			startV1 = startV2;
			startV2 = temp;

			temp = midV1;
			midV1 = midV2;
			midV2 = temp;

			temp = endV1;
			endV1 = endV2;
			endV2 = temp;
		}
		// Now, 1 is always nearer than 2.

		// There are 2 types of drawing Slope: 1 inner line and 3 inner lines
		boolean is3InnerLines = (midV1.getX() < startV1.getX()) != (midV1.getX() < midV2.getX());

		int[] outerPolygonX = new int[5];
		int[] outerPolygonY = new int[5];

		outerPolygonX[0] = startV2.getIntX();
		outerPolygonX[1] = startV1.getIntX();
		outerPolygonX[2] = midV1.getIntX();
		outerPolygonX[4] = endV2.getIntX();

		outerPolygonY[0] = startV2.getIntY();
		outerPolygonY[1] = startV1.getIntY();
		outerPolygonY[2] = midV1.getIntY();
		outerPolygonY[4] = endV2.getIntY();

		if (is3InnerLines) {
			outerPolygonX[3] = midV2.getIntX();
			outerPolygonY[3] = midV2.getIntY();
		} else {
			outerPolygonX[3] = endV1.getIntX();
			outerPolygonY[3] = endV1.getIntY();
		}

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillPolygon(outerPolygonX, outerPolygonY, 5);

		int[] outerPolylineX = new int[4];
		int[] outerPolylineY = new int[4];

		outerPolylineX[0] = endV2.getIntX();
		outerPolylineX[1] = startV2.getIntX();
		outerPolylineX[2] = startV1.getIntX();
		outerPolylineX[3] = midV1.getIntX();

		outerPolylineY[0] = endV2.getIntY();
		outerPolylineY[1] = startV2.getIntY();
		outerPolylineY[2] = startV1.getIntY();
		outerPolylineY[3] = midV1.getIntY();

		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(Resource.getGameObjectThickStroke());
		g.drawPolyline(outerPolylineX, outerPolylineY, 4);

		// Inner lines
		g.setStroke(Resource.getGameObjectThinStroke());
		g.drawLine(endV1.getIntX(), endV1.getIntY(), startV1.getIntX(), startV1.getIntY());
	}

	private void drawMiddlePiece(Graphics2D g, Camera camera) {

		int x = (startX + endX) / 2;
		int y = (startY + endY) / 2;
		int z = startZ;

		final float[][] cornerShifter = new float[][] { { -0.5f, -0.5f }, { +0.5f, -0.5f }, { +0.5f, +0.5f },
				{ -0.5f, +0.5f } };
		float[] cornerHeight = new float[] { endZ - startZ, endZ - startZ, endZ - startZ, endZ - startZ };
		cornerHeight[0] *= (x - 1 == startX || y - 1 == startY) ? 1 / 3f : 2 / 3f;
		cornerHeight[1] *= (x + 1 == startX || y - 1 == startY) ? 1 / 3f : 2 / 3f;
		cornerHeight[2] *= (x + 1 == startX || y + 1 == startY) ? 1 / 3f : 2 / 3f;
		cornerHeight[3] *= (x - 1 == startX || y + 1 == startY) ? 1 / 3f : 2 / 3f;

		Vector2[] basis = new Vector2[4];
		for (int i = 0; i < 4; i++) {
			basis[i] = camera.getDrawPosition(x + cornerShifter[i][0], y + cornerShifter[i][1], z);
		}

		int furthestBaseId = 0;
		Vector2 furthestBase = basis[0];

		for (int i = 1; i < 4; i++) {
			if (basis[i].getY() < furthestBase.getY()) {
				furthestBaseId = i;
				furthestBase = basis[i];
			}
		}

		Vector2[] outerBorder = new Vector2[6];
		Vector2 innerPoint;

		outerBorder[0] = basis[(furthestBaseId + 2) % 4];
		outerBorder[1] = basis[(furthestBaseId + 3) % 4];
		outerBorder[2] = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 3) % 4][0],
				y + cornerShifter[(furthestBaseId + 3) % 4][1], z + cornerHeight[(furthestBaseId + 3) % 4]);
		outerBorder[3] = camera.getDrawPosition(x + cornerShifter[furthestBaseId][0],
				y + cornerShifter[furthestBaseId][1], z + cornerHeight[furthestBaseId]);
		outerBorder[4] = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 1) % 4][0],
				y + cornerShifter[(furthestBaseId + 1) % 4][1], z + cornerHeight[(furthestBaseId + 1) % 4]);
		outerBorder[5] = basis[(furthestBaseId + 1) % 4];

		innerPoint = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 2) % 4][0],
				y + cornerShifter[(furthestBaseId + 2) % 4][1], z + cornerHeight[(furthestBaseId + 2) % 4]);

		int[] outerBorderCoordX = new int[6];
		int[] outerBorderCoordY = new int[6];
		for (int i = 0; i < 6; i++) {
			outerBorderCoordX[i] = (int) outerBorder[i].getX();
			outerBorderCoordY[i] = (int) outerBorder[i].getY();
		}

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		// g.setStroke(Resource.getGameObjectThickStroke());
		g.setColor(ColorSwatch.FOREGROUND);
		// g.drawPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		g.setStroke(Resource.getGameObjectThinStroke());
		if ((cornerShifter[(furthestBaseId + 2) % 4][1] == cornerShifter[(furthestBaseId + 3) % 4][1]) == isAlignX) {
			g.drawLine(innerPoint.getIntX(), innerPoint.getIntY(), outerBorder[2].getIntX(), outerBorder[2].getIntY());
			g.setStroke(Resource.getGameObjectThickStroke());
			g.drawLine(outerBorderCoordX[0], outerBorderCoordY[0], outerBorderCoordX[1], outerBorderCoordY[1]);
			g.drawLine(outerBorderCoordX[3], outerBorderCoordY[3], outerBorderCoordX[4], outerBorderCoordY[4]);
		} else {
			g.drawLine(innerPoint.getIntX(), innerPoint.getIntY(), outerBorder[4].getIntX(), outerBorder[4].getIntY());
			g.setStroke(Resource.getGameObjectThickStroke());
			g.drawLine(outerBorderCoordX[0], outerBorderCoordY[0], outerBorderCoordX[5], outerBorderCoordY[5]);
			g.drawLine(outerBorderCoordX[3], outerBorderCoordY[3], outerBorderCoordX[2], outerBorderCoordY[2]);
		}
	}

	private void drawEndPiece(Graphics2D g, Camera camera) {

		int x = endX;
		int y = endY;
		int z = startZ;

		float[][] cornerShifter = new float[][] { { -0.5f, -0.5f }, { +0.5f, -0.5f }, { +0.5f, +0.5f },
				{ -0.5f, +0.5f } };
		float[] cornerHeight = new float[] { endZ - startZ, endZ - startZ, endZ - startZ, endZ - startZ };
		int centerX = (startX + endX) / 2;
		int centerY = (startY + endY) / 2;
		cornerHeight[0] *= (centerX - 1 == startX || centerY - 1 == startY) ? 2 / 3f : 1;
		cornerHeight[1] *= (centerX + 1 == startX || centerY - 1 == startY) ? 2 / 3f : 1;
		cornerHeight[2] *= (centerX + 1 == startX || centerY + 1 == startY) ? 2 / 3f : 1;
		cornerHeight[3] *= (centerX - 1 == startX || centerY + 1 == startY) ? 2 / 3f : 1;

		Vector2[] basis = new Vector2[4];
		for (int i = 0; i < 4; i++) {
			basis[i] = camera.getDrawPosition(x + cornerShifter[i][0], y + cornerShifter[i][1], z);
		}

		int furthestBaseId = 0;
		Vector2 furthestBase = basis[0];

		for (int i = 1; i < 4; i++) {
			if (basis[i].getY() < furthestBase.getY()) {
				furthestBaseId = i;
				furthestBase = basis[i];
			}
		}

		Vector2[] outerBorder = new Vector2[6];
		Vector2 innerPoint;

		outerBorder[0] = basis[(furthestBaseId + 2) % 4];
		outerBorder[1] = basis[(furthestBaseId + 3) % 4];
		outerBorder[2] = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 3) % 4][0],
				y + cornerShifter[(furthestBaseId + 3) % 4][1], z + cornerHeight[(furthestBaseId + 3) % 4]);
		outerBorder[3] = camera.getDrawPosition(x + cornerShifter[furthestBaseId][0],
				y + cornerShifter[furthestBaseId][1], z + cornerHeight[furthestBaseId]);
		outerBorder[4] = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 1) % 4][0],
				y + cornerShifter[(furthestBaseId + 1) % 4][1], z + cornerHeight[(furthestBaseId + 1) % 4]);
		outerBorder[5] = basis[(furthestBaseId + 1) % 4];

		innerPoint = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 2) % 4][0],
				y + cornerShifter[(furthestBaseId + 2) % 4][1], z + cornerHeight[(furthestBaseId + 2) % 4]);

		int[] outerBorderCoordX = new int[6];
		int[] outerBorderCoordY = new int[6];
		for (int i = 0; i < 6; i++) {
			outerBorderCoordX[i] = (int) outerBorder[i].getX();
			outerBorderCoordY[i] = (int) outerBorder[i].getY();
		}

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		g.setStroke(Resource.getGameObjectThickStroke());
		g.setColor(ColorSwatch.FOREGROUND);
		// g.drawPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		int[] shiftDir = new int[] { (furthestBaseId + 2) % 4, (furthestBaseId + 3) % 4, (furthestBaseId + 3) % 4,
				furthestBaseId, (furthestBaseId + 1) % 4, (furthestBaseId + 1) % 4 };
		if (isAlignX) {
			for (int i = 0; i < 6; i++) {
				if (cornerShifter[shiftDir[i]][0] != (startX - endX) / 4f
						|| cornerShifter[shiftDir[(i + 1) % 6]][0] != (startX - endX) / 4f) {
					g.drawLine(outerBorderCoordX[i], outerBorderCoordY[i], outerBorderCoordX[(i + 1) % 6],
							outerBorderCoordY[(i + 1) % 6]);
				}
			}
		} else {
			for (int i = 0; i < 6; i++) {
				if (cornerShifter[shiftDir[i]][1] != (startY - endY) / 4f
						|| cornerShifter[shiftDir[(i + 1) % 6]][1] != (startY - endY) / 4f) {
					g.drawLine(outerBorderCoordX[i], outerBorderCoordY[i], outerBorderCoordX[(i + 1) % 6],
							outerBorderCoordY[(i + 1) % 6]);
				}
			}
		}

		g.setStroke(Resource.getGameObjectThinStroke());

		if (isAlignX) {
			for (int i = 0; i < 6; i += 2) {
				if (cornerShifter[(furthestBaseId + 2) % 4][0] != (startX - endX) / 4f
						|| cornerShifter[shiftDir[i]][0] != (startX - endX) / 4f) {
					g.drawLine(innerPoint.getIntX(), innerPoint.getIntY(), outerBorder[i].getIntX(),
							outerBorder[i].getIntY());
				}
			}
		} else {
			for (int i = 0; i < 6; i += 2) {
				if (cornerShifter[(furthestBaseId + 2) % 4][1] != (startY - endY) / 4f
						|| cornerShifter[shiftDir[i]][1] != (startY - endY) / 4f) {
					g.drawLine(innerPoint.getIntX(), innerPoint.getIntY(), outerBorder[i].getIntX(),
							outerBorder[i].getIntY());
				}
			}
		}

		/*Vector3 startP1 = new Vector3(startX, startY, startZ);
		Vector3 endP1 = new Vector3(endX, endY, endZ);
		Vector3 midP1 = new Vector3(endX, endY, startZ);
		Vector3 startP2 = new Vector3(startP1);
		Vector3 endP2 = new Vector3(endP1);
		Vector3 midP2 = new Vector3(midP1);
		
		if (isAlignX) {
			float shifter = (startX < endX) ? -0.5f : 0.5f;
		
			startP1.add(shifter, 0.5f, 0);
			endP1.add(-shifter, 0.5f, 0);
			midP1.add(-shifter, 0.5f, 0);
		
			startP2.add(shifter, -0.5f, 0);
			endP2.add(-shifter, -0.5f, 0);
			midP2.add(-shifter, -0.5f, 0);
		
		} else if (isAlignY) {
			float shifter = (startY < endY) ? -0.5f : 0.5f;
		
			startP1.add(0.5f, shifter, 0);
			endP1.add(0.5f, -shifter, 0);
			midP1.add(0.5f, -shifter, 0);
		
			startP2.add(-0.5f, shifter, 0);
			endP2.add(-0.5f, -shifter, 0);
			midP2.add(-0.5f, -shifter, 0);
		}
		
		Vector2 startV1 = camera.getDrawPosition(startP1);
		Vector2 endV1 = camera.getDrawPosition(endP1);
		Vector2 midV1 = camera.getDrawPosition(midP1);
		Vector2 startV2 = camera.getDrawPosition(startP2);
		Vector2 endV2 = camera.getDrawPosition(endP2);
		Vector2 midV2 = camera.getDrawPosition(midP2);
		
		if (midV1.getY() < midV2.getY()) {
			Vector2 temp;
		
			temp = startV1;
			startV1 = startV2;
			startV2 = temp;
		
			temp = midV1;
			midV1 = midV2;
			midV2 = temp;
		
			temp = endV1;
			endV1 = endV2;
			endV2 = temp;
		}
		// Now, 1 is always nearer than 2.
		
		// There are 2 types of drawing Slope: 1 inner line and 3 inner lines
		boolean is3InnerLines = (midV1.getX() < startV1.getX()) != (midV1.getX() < midV2.getX());
		
		int[] outerPolygonX = new int[5];
		int[] outerPolygonY = new int[5];
		
		outerPolygonX[0] = startV2.getIntX();
		outerPolygonX[1] = startV1.getIntX();
		outerPolygonX[2] = midV1.getIntX();
		outerPolygonX[4] = endV2.getIntX();
		
		outerPolygonY[0] = startV2.getIntY();
		outerPolygonY[1] = startV1.getIntY();
		outerPolygonY[2] = midV1.getIntY();
		outerPolygonY[4] = endV2.getIntY();
		
		if (is3InnerLines) {
			outerPolygonX[3] = midV2.getIntX();
			outerPolygonY[3] = midV2.getIntY();
		} else {
			outerPolygonX[3] = endV1.getIntX();
			outerPolygonY[3] = endV1.getIntY();
		}
		
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillPolygon(outerPolygonX, outerPolygonY, 5);
		
		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(Resource.getGameObjectThickStroke());
		g.drawPolygon(outerPolygonX, outerPolygonY, 5);
		
		// Inner lines
		g.setStroke(Resource.getGameObjectThinStroke());
		if (is3InnerLines) {
			g.drawLine(endV1.getIntX(), endV1.getIntY(), startV1.getIntX(), startV1.getIntY());
			g.drawLine(endV1.getIntX(), endV1.getIntY(), midV1.getIntX(), midV1.getIntY());
			g.drawLine(endV1.getIntX(), endV1.getIntY(), endV2.getIntX(), endV2.getIntY());
		} else {
			g.drawLine(startV1.getIntX(), startV1.getIntY(), endV1.getIntX(), endV1.getIntY());
		}*/

	}

	@Override
	public void draw(Graphics2D g, Camera camera, Vector3 position) {
		if (position.equals(new Vector3(startX, startY, startZ - START_Z_SHIFTER))) {
			 drawStartPiece(g, camera);
		} else if (position.equals(new Vector3(endX, endY, startZ))) {
			drawEndPiece(g, camera);
		} else {
			 drawMiddlePiece(g, camera);
		}
	}

	@Override
	public Vector3 getDrawPosition() {
		throw new UnsupportedOperationException("Please call getDrawPositionList() method instead.");
	}

}
