package scene.test;

import java.awt.Graphics2D;

import objectInterface.IDrawable;
import objectInterface.IWalkOnAble;

public abstract class TeleportGate implements IDrawable, IWalkOnAble {
	protected int x, y, z;
	protected boolean isActive, isAsserted;
	

	public TeleportGate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected abstract void teleport(Player p);

	// the progress checking is just a mock up. It should be set up later
	protected final int teleportProgressControl = 100 * 50;
	protected int teleportProgress = 0;

	public void update(int step) {
		
		if (isObjectAbove() && isActive) {
			teleportProgress += step;
		} else {
			teleportProgress = 0;
		}
		if (teleportProgress >= teleportProgressControl) {
			teleportProgress = 0;
			teleport(getPlayerAbove());
		}

	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isObjectAbove() {
		// teleportGate check only player above
		if (getPlayerAbove() != null) {
			return true;
		}
		return false;
	}

	public Player getPlayerAbove() {
		if (ObjectMap.drawableObjectHashMap.get(x + " " + y + " " + z + " Player" + util.Constants.PLAYER1_ID) != null)
			return (Player) (ObjectMap.drawableObjectHashMap
					.get(x + " " + y + " " + z + " Player" + util.Constants.PLAYER1_ID));
		else if (ObjectMap.drawableObjectHashMap
				.get(x + " " + y + " " + z + " Player" + util.Constants.PLAYER2_ID) != null)
			return (Player) (ObjectMap.drawableObjectHashMap
					.get(x + " " + y + " " + z + " Player" + util.Constants.PLAYER2_ID));
		return null;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isAsserted() {
		return isAsserted;
	}

	public void setAsserted(boolean isAsserted) {
		this.isAsserted = isAsserted;
	}
	
	@Override
	public int getCellX() {
		// TODO Auto-generated method stub
		return this.x;
	}

	@Override
	public int getCellY() {
		// TODO Auto-generated method stub
		return this.y;
	}

	@Override
	public int getCellZ() {
		// TODO Auto-generated method stub
		return this.z;
	}

}
