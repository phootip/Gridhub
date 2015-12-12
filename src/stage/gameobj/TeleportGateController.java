package stage.gameobj;

import java.util.ArrayList;

import stage.FloorSwitch;

public class TeleportGateController extends SwitchController {
	private TeleportGate teleportGate;
	public TeleportGateController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray , TeleportGate teleportGate) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.teleportGate = teleportGate;
		this.teleportGate.setAsserted(false);
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
