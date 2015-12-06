package scene.test;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import geom.Vector2;
import util.Constants.ColorSwatch;

class Block {

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

	protected void draw(Graphics2D g, Camera camera) {
		Vector2 a = camera.getDrawPosition(x - 0.5f, y - 0.5f, 0);
		Vector2 b = camera.getDrawPosition(x + 0.5f, y + 0.5f, 1);
		float height = camera.getDrawSizeZ(1);

		g.setStroke(new BasicStroke(3));
		g.setColor(ColorSwatch.FOREGROUND);
		g.draw(new Rectangle2D.Float(a.getX(), a.getY(), b.getX() - a.getX(), b.getY() - a.getY()));
		g.setStroke(new BasicStroke(1));
		g.draw(new Line2D.Float(a.getX(), b.getY() - height, b.getX(), b.getY() - height));
	}

}
