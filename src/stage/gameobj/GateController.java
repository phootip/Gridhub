package stage.gameobj;

import java.util.ArrayList;

import stage.gameobj.FloorSwitch;

/**
 * 
 * This Class represented the Gate Controller which is used to check whether the gate will be activated or not.
 * 
 * @author Thanat
 *
 */
public class GateController extends SwitchController {
	private Gate gate;

	public GateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray, Gate gate) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.gate = gate;

	}

	public GateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.gate = null;

	}
	/**
	 * set the gate being controlled by this controller
	 * @param g
	 */
	public void setControlGate(Gate g) {
		gate = g;
	}
	/**
	 * set the gate being controlled by this controller
	 * @return the gate under this controller control
	 */
	public Gate getControlGate() {
		return gate;
	}

	@Override
	public void performHitAction() {
		gate.setAsserted(true);

	}

	@Override
	public void reverseAction() {
		gate.setAsserted(false);

	}

}
