package scene.test;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import com.sun.xml.internal.bind.v2.TODO;

import core.geom.Vector2;
import objectInterface.IDrawable;
import objectInterface.PushableObject;
import objectInterface.WalkThroughable;
import util.Constants;
import util.Helper;
import util.Constants.ColorSwatch;

class Block implements PushableObject, WalkThroughable {

	protected static final float BLOCK_HEIGHT = 1.0f;
	private int x, y, z, nextX, nextY, nextZ, weight;
	private boolean isWalkThroughable;
	private int[][] floorLevelMap = FloorLevel.getInstance().getFloorMap();
	private int mapXRangeShift = floorLevelMap.length / 2;
	private int mapYRangeShift = floorLevelMap[0].length / 2;
	// private boolean isObjectAbove;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public int getCellX() {
		return x;
	}

	@Override
	public int getCellY() {
		return y;
	}

	@Override
	public int getCellZ() {
		return z;
	}

	protected int getWeight() {
		return weight;
	}

	public Block(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.nextX = x;
		this.nextY = y;
		this.nextZ = z;
	}

	public Block(int x, int y, int z, int weight, boolean isWalkThroughable) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.nextX = x;
		this.nextY = y;
		this.nextZ = z;
		this.weight = weight;
		this.isWalkThroughable = isWalkThroughable;
	}

	public boolean isPushable() {

		if (weight >= 100 || ObjectMap.drawableObjectHashMap.get(x + " " + y + " " + (z + 1)) != null)
			return false;
		if (ObjectMap.drawableObjectHashMap.get(x + " " + y + " " + (z + 1) + " Player" + util.Constants.PLAYER1_ID) != null
				|| ObjectMap.drawableObjectHashMap
						.get(x + " " + y + " " + (z + 1) + " Player" + util.Constants.PLAYER2_ID) != null)
			return false;
		return true;
	}

	public boolean push(int previousWeight, int diffX, int diffY, int diffZ) {
		if(FloorLevel.getInstance().isOutOfMap(x+diffX, y+diffY)) return false;
		if (this.weight + previousWeight > 100 || !isPushable())
			return false;
		if (ObjectMap.drawableObjectHashMap
				.get((x + diffX) + " " + (y + diffY) + " " + (z + diffZ) + " Player" + util.Constants.PLAYER1_ID) != null
				|| ObjectMap.drawableObjectHashMap.get(
						(x + diffX) + " " + (y + diffY) + " " + (z + diffZ) + " Player" + util.Constants.PLAYER2_ID) != null)
			return false;
		if(z != floorLevelMap[x+diffX +mapXRangeShift][y+diffY + mapYRangeShift]) return false;
		IDrawable nextObjectObstacles = ObjectMap.drawableObjectHashMap
				.get((x + diffX) + " " + (y + diffY) + " " + (z + diffZ));
		if (nextObjectObstacles == null) {
			ObjectMap.drawableObjectHashMap.put((x + diffX) + " " + (y + diffY) + " " + (z + diffZ), this);
			ObjectMap.drawableObjectHashMap.remove(x + " " + y + " " + z);
			this.x += diffX;
			this.y += diffY;
			this.z += diffZ;
			return true;

		} else {
			if (nextObjectObstacles instanceof PushableObject) {
				boolean isPushed = ((PushableObject) nextObjectObstacles).push(previousWeight + this.weight, diffX,
						diffY, diffZ);
				if (isPushed) {
					ObjectMap.drawableObjectHashMap.put((x + diffX) + " " + (y + diffY) + " " + (z + diffZ), this);
					ObjectMap.drawableObjectHashMap.remove(x + " " + y + " " + z);
					this.x += diffX;
					this.y += diffY;
					this.z += diffZ;
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isWalkThroughable() {
		return isWalkThroughable;
	}

	private final float[][] cornerShifter = new float[][] { { -0.5f, -0.5f }, { +0.5f, -0.5f }, { +0.5f, +0.5f },
			{ -0.5f, +0.5f } };

	public void draw(Graphics2D g, Camera camera) {

		float z = 0;
		// draw when on slope
		if (y == 10) {
			if (x < 10 - 0.5)
				z = 0;
			else if (x > 12.5f)
				z = 1;
			else
				z = Helper.interpolate(0, 1, (x - 9.5f) / 3f);
		}

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
				y + cornerShifter[(furthestBaseId + 3) % 4][1], z + BLOCK_HEIGHT);
		outerBorder[3] = camera.getDrawPosition(x + cornerShifter[furthestBaseId][0],
				y + cornerShifter[furthestBaseId][1], z + BLOCK_HEIGHT);
		outerBorder[4] = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 1) % 4][0],
				y + cornerShifter[(furthestBaseId + 1) % 4][1], z + BLOCK_HEIGHT);
		outerBorder[5] = basis[(furthestBaseId + 1) % 4];

		innerPoint = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 2) % 4][0],
				y + cornerShifter[(furthestBaseId + 2) % 4][1], z + BLOCK_HEIGHT);

		int[] outerBorderCoordX = new int[6];
		int[] outerBorderCoordY = new int[6];
		for (int i = 0; i < 6; i++) {
			outerBorderCoordX[i] = (int) outerBorder[i].getX();
			outerBorderCoordY[i] = (int) outerBorder[i].getY();
		}

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		g.setStroke(new BasicStroke(3));
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		g.setStroke(new BasicStroke(1));
		g.draw(new Line2D.Float(innerPoint.getX(), innerPoint.getY(), outerBorder[0].getX(), outerBorder[0].getY()));
		g.draw(new Line2D.Float(innerPoint.getX(), innerPoint.getY(), outerBorder[2].getX(), outerBorder[2].getY()));
		g.draw(new Line2D.Float(innerPoint.getX(), innerPoint.getY(), outerBorder[4].getX(), outerBorder[4].getY()));
	}

}
