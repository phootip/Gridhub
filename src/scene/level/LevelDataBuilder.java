package scene.level;

import java.util.ArrayList;

import stage.FloorLevel;
import stage.gameobj.Block;
import stage.gameobj.FloorSwitch;
import stage.gameobj.Gate;
import stage.gameobj.GateToGateTeleport;
import stage.gameobj.Slope;
import stage.gameobj.SwitchController;
import stage.gameobj.TeleportDestionation;
import stage.gameobj.TeleportToArea;

public class LevelDataBuilder {
	private ArrayList<Block> blocks;
	private ArrayList<Slope> slopes;
	private ArrayList<FloorSwitch> floorSwitches;
	private ArrayList<GateToGateTeleport> gateTogateTeles;
	private ArrayList<TeleportToArea> teleportToArea;
	private ArrayList<TeleportDestionation> telportDests;
	private ArrayList<Gate> gates;
	private ArrayList<SwitchController> swControllers;
	private FloorLevel floorLevel;
	private String levelName;
	private ArrayList<Integer> finishX;
	private ArrayList<Integer> finishY;
	private int [] startX;
	private int [] startY;
	private int playerCount;
	private String fileName;
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LevelDataBuilder() {
		
	}
	
	public int getPlayerCount() {
		return playerCount;
	}
	
	public LevelData build() {
		return LevelData.createLevelDataAsJSON(this);
	}

	public String createLevelDataAsJSONString() {
		LevelData levelData = LevelData.createLevelDataAsJSON(this);
		return levelData.getLevelDataJSON();
	}
	
	public int[] getStartX() {
		return startX;
	}

	public int[] getStartY() {
		return startY;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}
	public void addBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	public ArrayList<Slope> getSlopes() {
		return slopes;
	}
	public void addSlopes(ArrayList<Slope> slopes) {
		this.slopes = slopes;
	}
	public ArrayList<FloorSwitch> getFloorSwitches() {
		return floorSwitches;
	}
	public void addFloorSwitches(ArrayList<FloorSwitch> floorSwitches) {
		this.floorSwitches = floorSwitches;
	}
	public ArrayList<GateToGateTeleport> getGateTogateTeles() {
		return gateTogateTeles;
	}
	public void addGateTogateTeles(ArrayList<GateToGateTeleport> gateTogateTeles) {
		this.gateTogateTeles = gateTogateTeles;
	}
	public ArrayList<TeleportToArea> getTeleportToArea() {
		return teleportToArea;
	}
	public void addTeleportToArea(ArrayList<TeleportToArea> teleportToArea) {
		this.teleportToArea = teleportToArea;
	}
	public ArrayList<TeleportDestionation> getTelportDests() {
		return telportDests;
	}
	public void addTelportDests(ArrayList<TeleportDestionation> telportDests) {
		this.telportDests = telportDests;
	}
	public ArrayList<Gate> getGates() {
		return gates;
	}
	public void addGates(ArrayList<Gate> gates) {
		this.gates = gates;
	}
	public ArrayList<SwitchController> getSwControllers() {
		return swControllers;
	}
	public void addSwControllers(ArrayList<SwitchController> swControllers) {
		this.swControllers = swControllers;
	}
	public FloorLevel getFloorLevel() {
		return floorLevel;
	}
	public void addFloorLevel(FloorLevel floorLevel) {
		this.floorLevel = floorLevel;
	}
	public String getLevelName() {
		return levelName;
	}
	public void addLevelName(String levelName) {
		this.levelName = levelName;
	}
	public ArrayList<Integer> getFinishX() {
		return finishX;
	}
	public void addFinishX(ArrayList<Integer> finishX) {
		this.finishX = finishX;
	}
	public ArrayList<Integer> getFinishY() {
		return finishY;
	}
	public void addFinishY(ArrayList<Integer> finishY) {
		this.finishY = finishY;
	}

	public void addStartX(int[] startX) {
		this.startX = startX;
	}

	public void addStartY(int[] startY) {
		this.startY = startY;
	}

	public void addPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}
	
	
	
	
	
	
}
