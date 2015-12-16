package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.ObjectMap;
import util.Helper;
import util.Resource;

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

	protected final int gateProgressControl = 100 * 20;
	protected int gateActivationProgress = 0;

	/**
	 * set the {@link ObjectMap} above the player
	 * @param objectMap
	 */

	protected int gateActivationAnim = 0;


	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
	}

	/**
	 * This method is called by {@link GameStage} to update the state of the switch.
	 * @param step
	 */
	public void update(int step) {

		gateActivationAnim -= step;

		if (isAsserted) {
			gateActivationProgress += step;
		} else {
			gateActivationProgress = 0;
			if (objectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z)) == null) {
				objectMap.drawableObjectHashMap.put(new ObjectVector(x, y, z), this);
			}
		}
		if (gateActivationAnim < gateActivationProgress) {
			gateActivationAnim = gateActivationProgress;
		}

		if (gateActivationProgress >= gateProgressControl) {
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
		if (isObjectAbove()) {
			return;
		} else
			objectMap.drawableObjectHashMap.remove(new ObjectVector(x, y, z));

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
		float ratio = 1 - Helper.sineInterpolate((float) gateActivationAnim / gateProgressControl, false, true);

		float crossShifter = ratio * 0.3f;

		Vector2 cornerA = camera.getDrawPosition(x + crossShifter, y + crossShifter, z);
		Vector2 cornerB = camera.getDrawPosition(x - crossShifter, y - crossShifter, z);
		Vector2 cornerC = camera.getDrawPosition(x - crossShifter, y + crossShifter, z);
		Vector2 cornerD = camera.getDrawPosition(x + crossShifter, y - crossShifter, z);

		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(cornerA.getIntX(), cornerA.getIntY(), cornerB.getIntX(), cornerB.getIntY());
		g.drawLine(cornerC.getIntX(), cornerC.getIntY(), cornerD.getIntX(), cornerD.getIntY());

		g.setColor(Helper.getAlphaColorPercentage(Color.RED, 0.5f));
		g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawLine(cornerA.getIntX(), cornerA.getIntY(), cornerB.getIntX(), cornerB.getIntY());
		g.drawLine(cornerC.getIntX(), cornerC.getIntY(), cornerD.getIntX(), cornerD.getIntY());
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
