package stage.gameobj;

import java.util.ArrayList;

import stage.gameobj.FloorSwitch;

/**
 * @author Thanat
 *
 */
public abstract class SwitchController {
	private ArrayList<FloorSwitch> floorSwitchesControllerSet = new ArrayList<>();
	private int [] logicLookUpArray;
	private boolean isLogicCombinationHit;
	private IControlable controlObject;

	public SwitchController(ArrayList<FloorSwitch> floorSwitchesControllerSet , int [] logicLookUpArray) {
		super();
		this.floorSwitchesControllerSet = floorSwitchesControllerSet;
		isLogicCombinationHit = false;
		this.logicLookUpArray = logicLookUpArray;
		this.controlObject = null;
		
	}
	
	public SwitchController(ArrayList<FloorSwitch> floorSwitchesControllerSet , int [] logicLookUpArray, IControlable controlObject) {
		super();
		this.floorSwitchesControllerSet = floorSwitchesControllerSet;
		isLogicCombinationHit = false;
		this.logicLookUpArray = logicLookUpArray;
		this.controlObject = controlObject;
		
	}
	
	
	public void setFloorSwitchesControllerSet(ArrayList<FloorSwitch> floorSwitchesControllerSet) {
		this.floorSwitchesControllerSet = floorSwitchesControllerSet;
	}


	public ArrayList<FloorSwitch> getFloorSwitchesControllerSet() {
		return floorSwitchesControllerSet; 
	}

	public void update() {
		
		int rowLookUp = 0;
		for(int i = 0 ; i < floorSwitchesControllerSet.size() ; i++) {
			if(floorSwitchesControllerSet.get(i).isAsserting()) {
				rowLookUp += (int) Math.pow(2, i);
			}
		}
		
		//System.out.println(rowLookUp);
		if(rowLookUp < 0) return;
		//System.out.println(isLogicCombinationHit);
		try {
			isLogicCombinationHit = logicLookUpArray[rowLookUp] == 1;
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			System.out.println("Look Up Table does not match switches controller set");
			isLogicCombinationHit = false;
		}
		
		if(isLogicCombinationHit) performHitAction();
		else reverseAction(); 
		
	}
	
	public void setResultTable(int [] logicLookUp) {
		if(logicLookUp.length < (int) Math.pow(2, floorSwitchesControllerSet.size())) {
			logicLookUpArray = new int[(int) Math.pow(2, floorSwitchesControllerSet.size())];
		} else {
			logicLookUpArray = logicLookUp;
		}
	}
	
	
	public IControlable getControlObject() {
		return controlObject;
	}


	public void setControlObject(IControlable controlObject) {
		this.controlObject = controlObject;
		this.controlObject.deActivate();
	}

	
	public void performHitAction() {
		controlObject.activate();
	}
	public void reverseAction(){
		controlObject.deActivate();
	}
	
	
	
	
	
}
