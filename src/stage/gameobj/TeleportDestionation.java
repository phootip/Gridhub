package stage.gameobj;

import java.awt.Graphics2D;

import core.geom.Vector3;
import stage.Camera;
import stage.ObjectMap;

public class TeleportDestionation implements IDrawable, IWalkOnAble {
	private int x, y, z;
	private transient ObjectMap objectMap;

	public TeleportDestionation(int x, int y, int z) {
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
		return new Vector3(x, y, z);
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		// TODO Auto-generated method stub

	}

}
