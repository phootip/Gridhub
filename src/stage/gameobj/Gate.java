package stage.gameobj;

import java.awt.Graphics2D;

import core.geom.Vector3;
import stage.Camera;
import stage.ObjectMap;

/**
 * This Class represent Gate object in the game. Gate will be controlled by the switches. The player will not be able 
 * to move across the gate unless it is activated
 * 
 * @author Thanat
 *
 */
public class Gate implements IDrawable , IControlable {
	private boolean isAsserted;
	private int x, y, z;
	private transient ObjectMap objectMap;

	public Gate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.isAsserted = false;
	}
	
	protected final int gateProgressControl = 100 * 50;
	protected int gateActivationProgress = 0;
	/**
	 * set the {@link ObjectMap} above the player
	 * @param objectMap
	 */
	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
	}

	/**
	 * This method is called by {@link GameStage} to update the state of the switch.
	 * @param step
	 */
	public void update(int step) {
		
		if(isAsserted) {
			gateActivationProgress += step;
		} else {
			gateActivationProgress = 0;
			if(objectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z)) == null ) {
				objectMap.drawableObjectHashMap.put(new ObjectVector(x, y, z), this);
			}
		}
		
		if(gateActivationProgress >= gateProgressControl) {
			// TODO take an action when gate is asserted
			performAction();
			gateActivationProgress = 0;
		}
	}
	
	/**
	 * This method perform an action when the Gate is being activated
	 */
	public void performAction() {
		// do action
		if(isObjectAbove()) {
			return;
		}
		else objectMap.drawableObjectHashMap.remove(new ObjectVector(x, y, z));
		
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
	
	/**
	 * 
	 * @return boolean indicate whether there is an object above
	 */
	
	public boolean isObjectAbove() {
		if (getPlayerAbove() != null || getBlockAbove() != null) {
			return true;
		}

		return false;
	}
	
	/**
	 * The method is used to get the player standing above the gate when it is deactivated
	 * @return the player over the gate
	 */
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

	/**
	 * The method is used to get the block above the gate when it is deactivated
	 * @return the block over the gate
	 */
	private Block getBlockAbove() {
		IDrawable objectAbove = objectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z));
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
