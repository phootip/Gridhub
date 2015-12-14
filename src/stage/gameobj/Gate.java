package stage.gameobj;

import java.awt.Graphics2D;

import core.geom.Vector3;
import stage.Camera;
import stage.ObjectMap;

public class Gate implements IDrawable , IControlable {
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
		
		if(isAsserted) {
			gateActivationProgress += step;
		} else {
			gateActivationProgress = 0;
			if(ObjectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z)) == null ) {
				ObjectMap.drawableObjectHashMap.put(new ObjectVector(x, y, z), this);
			}
		}
		
		if(gateActivationProgress >= gateProgressControl) {
			// TODO take an action when gate is asserted
			performAction();
			gateActivationProgress = 0;
		}
	}
	
	public void performAction() {
		// do action
		if(isObjectAbove()) {
			return;
		}
		else ObjectMap.drawableObjectHashMap.remove(new ObjectVector(x, y, z));
		
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

	
	public boolean isObjectAbove() {
		if (getPlayerAbove() != null || getBlockAbove() != null) {
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


	private Block getBlockAbove() {
		IDrawable objectAbove = ObjectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z));
		if (objectAbove != null && objectAbove instanceof Block) {
			return (Block) objectAbove;
		} else
			return null;
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(x, y, z);
	}

	@Override
	public void activate() {
		this.isAsserted = true;
		
	}

	@Override
	public void deActivate() {
		this.isAsserted = false;
		
	}
	
}
