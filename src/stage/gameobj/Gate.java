package stage.gameobj;

import java.awt.Graphics2D;

import stage.Camera;
import stage.ObjectMap;

public class Gate implements IDrawable, IWalkOnAble {
	private boolean isAsserted;
	private int x, y, z;

	public Gate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.isAsserted = false;
	}
	
	protected final int gateProgressControl = 100 * 50;
	protected int gateActivationProgress = 0;
	
	public void update(int step) {
		
		if(isAsserted && isObjectAbove()) {
			gateActivationProgress += step;
		}
		
		if(gateActivationProgress >= gateProgressControl) {
			// TODO take an action when gate is asserted
			performActionToPlayer(getPlayerAbove());
			gateActivationProgress = 0;
		}
	}
	
	public void performActionToPlayer(Player p) {
		// do action
	}
	
	
	
	public boolean isAsserted() {
		return isAsserted;
	}

	public void setAsserted(boolean isAsserted) {
		this.isAsserted = isAsserted;
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
	public boolean isObjectAbove() {
		// teleportGate check only player above
		if (getPlayerAbove() != null) {
			return true;
		}
		return false;
	}

	public Player getPlayerAbove() {
		if (ObjectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)) != null) {
			return (Player) (ObjectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)));

		} else if (ObjectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)) != null) {
			return (Player) (ObjectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)));
		}

		return null;
	}
	@Override
	public float getDrawX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDrawY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDrawZ() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		// TODO Auto-generated method stub

	}

}
