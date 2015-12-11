package stage.gameobj;

import java.util.ArrayList;

import stage.FloorSwitch;

public class GateController extends SwitchController {
	private Gate gate;
	public GateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray , Gate gate) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.gate = gate;
		
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
