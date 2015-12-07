package scene.test;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;

import com.sun.xml.internal.bind.v2.TODO;

import geom.Vector2;
import objectInterface.IDrawable;
import objectInterface.PushableObject;
import objectInterface.WalkThroughable;
import util.Constants.ColorSwatch;

class Block implements PushableObject, WalkThroughable {

	protected static final float BLOCK_HEIGHT = 0.75f;

	private int x, y, z, nextX, nextY, nextZ, weight;
	private boolean isWalkThroughable;

	protected int getX() {
		return x;
	}

	protected int getY() {
		return y;
	}

	protected int getZ() {
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
		if (weight >= 100)
			return false;
		return true;
	}
	
	public boolean push(int previousWeight, int diffX, int diffY, int diffZ) {
		
		if (this.weight + previousWeight > 100)
			return false;
		IDrawable nextObjectObstacles = ObjectMap.drawableObjectHashMap.get((x+diffX) + " " + (y+diffY) + " " + (z+diffZ));
		if (nextObjectObstacles == null) {
			ObjectMap.drawableObjectHashMap.put((x+diffX) + " " + (y+diffY) + " " + (z+diffZ), this);
			ObjectMap.drawableObjectHashMap.remove(x + " " + y + " " + z);
			this.x += diffX;
			this.y += diffY;
			this.z += diffZ;
			return true;

		} else {
			if (nextObjectObstacles instanceof PushableObject) {
				boolean isPushed = ((PushableObject) nextObjectObstacles).push(previousWeight + this.weight, diffX, diffY, diffZ);
				if(isPushed) {
					ObjectMap.drawableObjectHashMap.put((x+diffX) + " " + (y+diffY) + " " + (z+diffZ), this);
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

	protected void draw(Graphics2D g, Camera camera) {
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

		g.setStroke(new BasicStroke(3));
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));
		g.setStroke(new BasicStroke(1));
		g.draw(new Line2D.Float(innerPoint.getX(), innerPoint.getY(), outerBorder[0].getX(), outerBorder[0].getY()));
		g.draw(new Line2D.Float(innerPoint.getX(), innerPoint.getY(), outerBorder[2].getX(), outerBorder[2].getY()));
		g.draw(new Line2D.Float(innerPoint.getX(), innerPoint.getY(), outerBorder[4].getX(), outerBorder[4].getY()));
	}

}
