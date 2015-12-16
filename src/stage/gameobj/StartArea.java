package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.ObjectMap;
import util.Resource;
import util.Constants.ColorSwatch;

public class StartArea implements IDrawable, IWalkOnAble {
	private int x, y, z;
	private transient ObjectMap objectMap;

	public StartArea(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean isObjectAbove() {
		if (getPlayerAbove() != null) {
			return true;
		}

		return false;
	}

	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
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
	public Vector3 getDrawPosition() {
		// TODO Auto-generated method stub
		return new Vector3(x, y, z - 0.48f);
	}

	public static void draw(Graphics2D g, Camera camera, ObjectVector position) {
		int x = position.getX();
		int y = position.getY();
		int z = position.getZ();

		Vector2 bottom = camera.getDrawPosition(x, y, z);
		Vector2 top = camera.getDrawPosition(x, y, z + 0.6f);
		Vector2 left = camera.getDrawPosition(x, y, z + 0.25f).add(camera.getDrawSizeX(0.25f), 0);
		Vector2 right = camera.getDrawPosition(x, y, z + 0.25f).add(camera.getDrawSizeX(-0.25f), 0);

		g.setStroke(Resource.getGameObjectThickStroke());
		g.setColor(Color.PINK);

		g.drawLine(bottom.getIntX(), bottom.getIntY(), top.getIntX(), top.getIntY());
		g.drawLine(bottom.getIntX(), bottom.getIntY(), left.getIntX(), left.getIntY());
		g.drawLine(bottom.getIntX(), bottom.getIntY(), right.getIntX(), right.getIntY());
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		draw(g, camera, new ObjectVector(x, y, z));
	}

}
