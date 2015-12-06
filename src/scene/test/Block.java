package scene.test;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;

import geom.Vector2;
import util.Constants.ColorSwatch;

class Block {

	protected static final float BLOCK_HEIGHT = 0.75f;

	private int x, y, z;

	protected int getX() {
		return x;
	}

	protected int getY() {
		return y;
	}

	protected int getZ() {
		return z;
	}

	public Block(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
