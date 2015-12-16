package stage.gameobj;

import java.util.ArrayList;

import stage.gameobj.FloorSwitch;

/**
 * This method inherit SwitchController and is the controller of the teleportGate. It will activate the TeleportGate
 * when the preset logic is hit as {@SwitchController} does.
 * 
 * @author Thanat Jatuphattharachat
 *
 */
public class TeleportGateController extends SwitchController {
	private TeleportGate teleportGate;

	public TeleportGateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray,
			TeleportGate teleportGate) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.teleportGate = teleportGate;
		this.teleportGate.setAsserted(false);
	}

	public TeleportGateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.teleportGate = null;
		this.teleportGate.setAsserted(false);
	}
	/**
	 * set the gate which will be controlled by this controller
	 * @param g
	 */
	public void setControlGate(TeleportGate g) {
		teleportGate = g;
	}

	@Override
	public void performHitAction() {
		// TODO Auto-generated method stub
		teleportGate.setAsserted(true);
	}

	@Override
	public void reverseAction() {
		// TODO Auto-generated method stub
		teleportGate.setAsserted(false);
	}

}
