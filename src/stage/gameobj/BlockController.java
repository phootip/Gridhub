package stage.gameobj;

import java.util.ArrayList;

import stage.gameobj.FloorSwitch;

/**
 * This method is used together with @{link FloorSwitch} in order to control the block movement in specific situation
 * 
 * @author Thanat Jatuphattharachat
 *
 */
public class BlockController extends SwitchController {
	private Block block;
	private int controllType;
	private int moveStep;
	private boolean hasPerfomrmHit, hasPerformReverse;

	public BlockController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray, Block block,
			int controlType, int moveStep) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.block = block;
		this.controllType = controlType;
		this.moveStep = moveStep;
		this.hasPerfomrmHit = false;
		this.hasPerformReverse = true;
	}

	public BlockController(ArrayList<FloorSwitch> floorSwitchesControllerSet, int[] logicLookUpArray, int controlType,
			int moveStep) {
		super(floorSwitchesControllerSet, logicLookUpArray);
		this.block = null;
		this.controllType = controlType;
		this.moveStep = moveStep;
		this.hasPerfomrmHit = false;
		this.hasPerformReverse = true;
	}

	public static final int MOVE_UP_TYPE = 1;
	public static final int MOVE_DOWN_TYPE = 2;
	
	/**
	 * This method is called to set @{link Block} under this controller.
	 * @param b
	 */
	public void setControlBlock(Block b) {
		block = b;
	}
	
	/**
	 * When the preset logic is hit {@link SwitchController} the controller perform the action
	 */
	@Override
	public void performHitAction() {
		// TODO Auto-generated method stub
		if (controllType == 1 && !hasPerfomrmHit) {
			hasPerfomrmHit = true;
			hasPerformReverse = false;
			block.setZ(moveStep);

		} else if (controllType == 2 && !hasPerfomrmHit) {
			System.out.println("Hello");
			hasPerfomrmHit = true;
			hasPerformReverse = false;
			block.setZ(-moveStep);
		}
	}
	/**
	 *  When the preset logic is not hit {@link SwitchController} the controller perform the action
	 */
	@Override
	public void reverseAction() {
		// TODO Auto-generated method stub
		if (controllType == 1 && !hasPerformReverse) {
			hasPerfomrmHit = false;
			hasPerformReverse = true;
			block.setZ(-moveStep);
		} else if (controllType == 2 && !hasPerformReverse) {
			hasPerfomrmHit = false;
			hasPerformReverse = true;
			block.setZ(moveStep);
		}
	}

}
