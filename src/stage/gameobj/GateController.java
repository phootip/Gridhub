package stage.gameobj;

import java.util.ArrayList;

import stage.gameobj.FloorSwitch;

public class GateController extends SwitchController {
	private Gate gate;
	public GateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray , Gate gate) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.gate = gate;
		
	}
	
	public GateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray ) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.gate = null;
		
	}
	
	
	
	public void setControlGate(Gate g) {
		gate = g;
	}
	
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
