package stage.gameobj;

import java.util.ArrayList;

import stage.gameobj.FloorSwitch;

public class BlockController extends SwitchController {
	private Block block;
	private int controllType;
	private int moveStep;
	private boolean hasPerfomrmHit , hasPerformReverse;

	public BlockController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray, Block block,
			int controlType, int moveStep) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.block = block;
		this.controllType = controlType;
		this.moveStep = moveStep;
		this.hasPerfomrmHit = false;
		this.hasPerformReverse = false;
	}

	public static final int MOVE_UP_TYPE = 1;
	public static final int MOVE_DOWN_TYPE = 2;

	@Override
	public void performHitAction() {
		// TODO Auto-generated method stub
		if (controllType == 1 && !hasPerfomrmHit) {
			hasPerfomrmHit = true;
			hasPerformReverse = false;
			block.push(0, 0, 0, moveStep);
			
		} else if (controllType == 2 && !hasPerfomrmHit) {
			hasPerfomrmHit = true;
			hasPerformReverse = false;
			block.push(0, 0, 0, -moveStep);
		} 
	}

	@Override
	public void reverseAction() {
		// TODO Auto-generated method stub
		if (controllType == 1) {
			hasPerfomrmHit = false;
			hasPerformReverse = true;
			block.push(0, 0, 0, -moveStep);
		} else if (controllType == 2) {
			hasPerfomrmHit = false;
			hasPerformReverse = true;
			block.push(0, 0, 0, moveStep);
		} 
	}

}
