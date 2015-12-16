package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.ObjectMap;
import util.Helper;
import util.Resource;
import util.Constants.ColorSwatch;

public class FinishArea implements IDrawable, IWalkOnAble {
	private int x, y, z;
	private transient boolean isPlayerAbove;
	private transient ObjectMap objectMap;

	public FinishArea(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.isPlayerAbove = false;
	}

	public void update() {
		if (isObjectAbove()) {
			isPlayerAbove = true;
		} else {
			isPlayerAbove = false;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
	}

	public boolean isPlayerAbove() {
		return isPlayerAbove;
	}

	public void perFormFinish() {
		System.out.println("You won");
	}

	@Override
	public boolean isObjectAbove() {
		if (getPlayerAbove() != null) {
			return true;
		}

		return false;
	}

	public Player getPlayerAbove() {
		if (objectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)) != null) {
			return (Player) (objectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)));

		} else if (objectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)) != null) {
			return (Player) (objectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)));
		}

		return null;
	}

	@Override
	public Vector3 getDrawPosition() {
		// TODO Auto-generated method stub
		return new Vector3(x, y, z - 0.45f);
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		Vector2 bottom = camera.getDrawPosition(x, y, z);
		Vector2 top = camera.getDrawPosition(x, y, z + 0.7f);
		Vector2 middle = camera.getDrawPosition(x, y, z + 0.35f);
		Vector2 flagEnd = new Vector2(top).add(middle).multiply(0.5f).add(camera.getDrawSizeX(0.5f), 0);

		g.setStroke(Resource.getGameObjectThickStroke());
		g.setColor(ColorSwatch.FOREGROUND);

		g.drawLine(bottom.getIntX(), bottom.getIntY(), top.getIntX(), top.getIntY());
		g.drawLine(flagEnd.getIntX(), flagEnd.getIntY(), top.getIntX(), top.getIntY());
		g.drawLine(flagEnd.getIntX(), flagEnd.getIntY(), middle.getIntX(), middle.getIntY());

		if (getPlayerAbove() != null) {
			final float innerCellShift = 0.4f;

			Vector2 cornerA = camera.getDrawPosition(x + innerCellShift, y + innerCellShift, z);
			Vector2 cornerB = camera.getDrawPosition(x + innerCellShift, y - innerCellShift, z);
			Vector2 cornerC = camera.getDrawPosition(x - innerCellShift, y - innerCellShift, z);
			Vector2 cornerD = camera.getDrawPosition(x - innerCellShift, y + innerCellShift, z);

			Path2D.Float innerBorderPath = new Path2D.Float();
			innerBorderPath.moveTo(cornerA.getX(), cornerA.getY());
			innerBorderPath.lineTo(cornerB.getX(), cornerB.getY());
			innerBorderPath.lineTo(cornerC.getX(), cornerC.getY());
			innerBorderPath.lineTo(cornerD.getX(), cornerD.getY());
			innerBorderPath.closePath();

//			g.setStroke(new BasicStroke(2));
			g.setColor(Helper.getAlphaColor(Color.GREEN, 150));
			g.fill(innerBorderPath);
		}

	}
}
