package stage;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Type;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import stage.renderer.LevelRenderer;
import scene.core.Scene;
import stage.gameobj.Block;

import stage.gameobj.FloorPiece;
import stage.gameobj.FloorSwitch;

import stage.gameobj.BlockController;

import stage.gameobj.Gate;
import stage.gameobj.GateController;
import stage.gameobj.GateToGateTeleport;
import stage.gameobj.IDrawable;
import stage.gameobj.ObjectVector;
import stage.gameobj.Player;
import stage.gameobj.Slope;
import stage.gameobj.SwitchController;
import stage.gameobj.TeleportGate;
import stage.gameobj.TeleportGateController;
import stage.gameobj.TeleportToArea;
import util.Constants.ColorSwatch;

public class GameStage extends Scene {

	private Camera camera;
	private Player player1;
	private Player player2;
	private FloorLevel floorLevelMap;
	private ArrayList<Block> blocks = new ArrayList<>();
	private ArrayList<FloorSwitch> floorSwitches = new ArrayList<>();
	private ArrayList<Slope> slopes = new ArrayList<>();
	
	private ArrayList<GateToGateTeleport> teleportGates = new ArrayList<>();
	//private ArrayList<GateToGateTeleport> teleportLinked = new ArrayList<>();
	private int[] telePortLink;
	private ArrayList<TeleportToArea> teleportToAreas = new ArrayList<>();
	
	private ArrayList<GateController> gateControllers = new ArrayList<>();
	//private int[] gateControllerGateIndex; 
	private ArrayList<Integer> gateControllerGateIndex;
	private ArrayList<int[]> linkGate = new ArrayList<>();
	
	private ArrayList<BlockController> blockControllers = new ArrayList<>();
	private ArrayList<TeleportGateController> teleportGateControllers = new ArrayList<>();
	private ArrayList<Gate> gates = new ArrayList<>();
	
	//for testing
	private ArrayList<Block>datasetGsonBlock;
	private ArrayList<FloorSwitch> dataSetFloorSwitches;
	private ArrayList<Slope> dataSetSlopes;
	
	private ArrayList<GateToGateTeleport> dataSetTeleportToGates;
	//private ArrayList<GateToGateTeleport> dataSetTeleportLink;
	private int[] dataSetLinkTeleport;
 	private ArrayList<TeleportToArea> dataSetTeleportToArea;
	
	private ArrayList<GateController> dataSetsGateController;
	private ArrayList<BlockController> dataSetBlockController;
	private ArrayList<TeleportGateController> datasetTeleportGateController;
	private ArrayList<Gate> dataSetsGate;
	
	private ArrayList<int[]> dataSetLinkGate;
	

	public GameStage() {
		ObjectMap.drawableObjectHashMap = new HashMap<ObjectVector, IDrawable>();

		floorLevelMap = new FloorLevel(25, 25);
		int floorMapXSize = floorLevelMap.getSizeX();
		int floorMapYSize = floorLevelMap.getSizeY();

		for (int i = 0; i < floorMapXSize; i++) {
			for (int j = 0; j < floorMapYSize; j++) {
				if (i > 11) {
					floorLevelMap.setZValue(i, j, 2);
				} else if (i > 8) {
					floorLevelMap.setZValue(i, j, 1);
				} else {
					floorLevelMap.setZValue(i, j, 0);
				}
			}
		}

		player1 = new Player(util.Constants.PLAYER1_ID, floorLevelMap, 9, 4, 1);
		player2 = new Player(util.Constants.PLAYER2_ID, floorLevelMap, 0, 0, 0);

		camera = new Camera(player1);
		
		slopes.add(new Slope(6, 10, 0, Slope.ALIGNMENT_RIGHT));
		slopes.add(new Slope(6, 5, 0, Slope.ALIGNMENT_RIGHT));
		slopes.add(new Slope(9, 10, 1, Slope.ALIGNMENT_RIGHT));
		slopes.add(new Slope(-2, 3, 0, Slope.ALIGNMENT_RIGHT));

		blocks.add(new Block(6, 11, 0, 20, true, floorLevelMap));
		blocks.add(new Block(7, 11, 0, 20, true, floorLevelMap));
		blocks.add(new Block(8, 11, 0, 20, true, floorLevelMap));
		blocks.add(new Block(6, 12, 0, 20, true, floorLevelMap));
		blocks.add(new Block(7, 12, 0, 20, true, floorLevelMap));
		blocks.add(new Block(8, 12, 0, 20, true, floorLevelMap));
		blocks.add(new Block(6, 13, 0, 20, true, floorLevelMap));
		blocks.add(new Block(7, 13, 0, 20, true, floorLevelMap));
		blocks.add(new Block(8, 13, 0, 20, true, floorLevelMap));

		slopes.add(new Slope(13, 4, 2, Slope.ALIGNMENT_RIGHT));
		slopes.add(new Slope(16, 1, 2, Slope.ALIGNMENT_DOWN));
		slopes.add(new Slope(16, 7, 2, Slope.ALIGNMENT_UP));
		slopes.add(new Slope(19, 4, 2, Slope.ALIGNMENT_LEFT));

		blocks.add(new Block(16, 4, 2, 20, true, floorLevelMap));
		blocks.add(new Block(2, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 4, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 5, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 7, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 8, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 9, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 10, 0, 20, true, floorLevelMap));
		blocks.add(new Block(1, 4, 1, 150, true, floorLevelMap));
		blocks.add(new Block(2, 3, 1, 20, true, floorLevelMap));
		blocks.add(new Block(3, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(4, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(5, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(6, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(7, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(8, 3, 0, 20, true, floorLevelMap));
				

		GateToGateTeleport gateTele0 = new GateToGateTeleport(0, 1, 0);
		GateToGateTeleport gateTele1 = new GateToGateTeleport(5, 0, 0);
		GateToGateTeleport gateTele2 = new GateToGateTeleport(16, 4, 3);
		
		teleportGates.add(gateTele0);
		teleportGates.add(gateTele1);
		teleportGates.add(gateTele2);
		teleportToAreas.add(new TeleportToArea(10, 4, 1, 0, 2, 0));
		
		telePortLink = new int[]{2,0,0};
		
//		teleportLinked.add(gateTele2);
//		teleportLinked.add(gateTele1);
//		teleportLinked.add(gateTele0);
		

		floorSwitches.add(new FloorSwitch(1, 0, 0, false, 20));
		floorSwitches.add(new FloorSwitch(4, 0, 0, false, 10));
		floorSwitches.add(new FloorSwitch(6, 0, 0, false, 10));

		
        Gate gate1 = new Gate(1, 3, 0);
        Gate gate2 = new Gate(1 ,4 ,0);
        Gate gate3 = new Gate(1,5,0);
        ArrayList<FloorSwitch> fsArray = new ArrayList<>();
        
        int [] fsArr = new int[floorSwitches.size()];
        fsArr[0] =1;
        fsArr[1] =1;
        linkGate.add(fsArr);
        linkGate.add(fsArr);
        GateController gateController = new GateController(null, new int[] {0,1,0,0});
        GateController gateController2 = new GateController(null, new int[] {0,0,0,1});
        fsArr = new int[floorSwitches.size()];
        fsArr[1] = 1;
        fsArr[2] = 1;
        linkGate.add(fsArr);
        
//        fsArray.add(floorSwitches.get(1));
//        fsArray.add(floorSwitches.get(2));     
        GateController gateController3 = new GateController(fsArray , new int[] {0,0,1,0});
        
        //TeleportGateController teleController = new TeleportGateController(floorSwitches, new int[] {0,1,0,1}, gateTele3);
        BlockController bController = new BlockController(floorSwitches, new int[] {0,1,0,1}, blocks.get(8), BlockController.MOVE_DOWN_TYPE, 1);
        //teleportGateControllers.add(teleController);
        blockControllers.add(bController);
        
        gates.add(gate1);
        gates.add(gate2);
        gates.add(gate3);
        gateControllers.add(gateController);
        gateControllers.add(gateController2);
        gateControllers.add(gateController3);
        
        
//        gateControllerGateIndex.add(new Integer(0)); //gates.get(index 0)
//        gateControllerGateIndex.add(new Integer(1));
        
        
        HashMap <String, String> levelObject = new HashMap<String,String>();
		Gson gson = new Gson();
		
		String blockJson = gson.toJson(blocks);
		String floorSwitchJson = gson.toJson(floorSwitches);
		String slopeJson = gson.toJson(slopes);
		String switchSetSelected = gson.toJson(linkGate);
		
		String teleportGateJson = gson.toJson(teleportGates);
//		String teleportGateLinkJson = gson.toJson(teleportLinked);
		String teleportGateLinkJson = gson.toJson(telePortLink);
		String teleportGateToAreaJson = gson.toJson(teleportToAreas);
	
		
//		String swControllerJson = gson.toJson(switchController);
		String gateJson = gson.toJson(gates);
		String gateControllerJson = gson.toJson(gateControllers);
//		String gateControllerGateIndexJson = gson.toJson(gateControllerGateIndex);
		
		levelObject.put("block", blockJson);
		levelObject.put("teleport", teleportGateJson);
		String hashmapJson = gson.toJson(levelObject);
		Type hashType = new TypeToken<HashMap<String,String>>(){}.getType();
		
		HashMap<String,String> a = gson.fromJson(hashmapJson, hashType);
		System.out.println(a.get("block"));
		
		Type blockType = new TypeToken<ArrayList<Block>>(){}.getType();
		Type floorSwitchType = new TypeToken<ArrayList<FloorSwitch>>(){}.getType();
		Type slopeType = new TypeToken<ArrayList<Slope>>(){}.getType();
		
//		Type teleportType = new TypeToken<ArrayList<TeleportGate>>(){}.getType();
		Type teleportToGateType = new TypeToken<ArrayList<GateToGateTeleport>>(){}.getType();
		Type teleportToAreaType = new TypeToken<ArrayList<TeleportToArea>>(){}.getType();
		
//		Type switchControllerType = new TypeToken<ArrayList<SwitchController>>(){}.getType();
		Type gateType = new TypeToken<ArrayList<Gate>>(){}.getType();	
		Type gateControllerType = new TypeToken<ArrayList<GateController>>(){}.getType();
		Type gateLinkType = new TypeToken<ArrayList<int[]>>(){}.getType();
		
		
		
		
		datasetGsonBlock = gson.fromJson(blockJson,blockType);
		dataSetFloorSwitches = gson.fromJson(floorSwitchJson,floorSwitchType);
		dataSetSlopes = gson.fromJson(slopeJson,slopeType);
		
		dataSetTeleportToGates = gson.fromJson(teleportGateJson,teleportToGateType);
		dataSetLinkTeleport = gson.fromJson(teleportGateLinkJson , int[].class);
		dataSetTeleportToArea = gson.fromJson(teleportGateToAreaJson, teleportToAreaType);
		

//		dataSetswitchController = gson.fromJson(swControllerJson,switchControllerType);
		dataSetsGate = gson.fromJson(gateJson,gateType);
		dataSetsGateController = gson.fromJson(gateControllerJson, gateControllerType);
		dataSetLinkGate = gson.fromJson(switchSetSelected, gateLinkType);
		
		
		for(int i = 0 ; i < dataSetTeleportToGates.size() ; i++) {
			GateToGateTeleport mainGate = dataSetTeleportToGates.get(i);
			mainGate.setDestinationTelelportGate(dataSetTeleportToGates.get(dataSetLinkTeleport[i]));
		}
		
		for(int i = 0 ; i < dataSetsGateController.size() ; i++) {
			GateController g = dataSetsGateController.get(i);
			g.setControlGate(dataSetsGate.get(i));
			int [] n = dataSetLinkGate.get(i);
			ArrayList<FloorSwitch> fsArrayList = new ArrayList<>();
			for(int j =0 ; j < n.length ; j++) {
				if(n[j] == 1) {
					fsArrayList.add(dataSetFloorSwitches.get(j));
				}
			}
			g.setFloorSwitchesControllerSet(fsArrayList);
		}
		
		
		
		
        for (Slope eachSlope : dataSetSlopes) {

			int slopeStartX = eachSlope.getStartX();
			int slopeStartY = eachSlope.getStartY();
			int slopeStartZ = eachSlope.getStartZ();
			int slopeEndX = eachSlope.getEndX();
			int slopeEndY = eachSlope.getEndY();
			int xBar = (slopeStartX + slopeEndX) / 2;
			int yBar = (slopeStartY + slopeEndY) / 2;

			ObjectMap.drawableObjectHashMap.put(new ObjectVector(slopeStartX, slopeStartY, slopeStartZ), eachSlope);
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(xBar, yBar, slopeStartZ), eachSlope);
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(slopeEndX, slopeEndY, slopeStartZ), eachSlope);

		}

		for (GateToGateTeleport eachTeleport : dataSetTeleportToGates) {
			ObjectMap.drawableObjectHashMap
					.put(new ObjectVector(eachTeleport.getX(), eachTeleport.getY(), eachTeleport.getZ()), eachTeleport);
		}
		
		
		for (TeleportToArea eachTeleport : dataSetTeleportToArea) {
			ObjectMap.drawableObjectHashMap
					.put(new ObjectVector(eachTeleport.getX(), eachTeleport.getY(), eachTeleport.getZ()), eachTeleport);
		}

		for (Block eachBlock : datasetGsonBlock) {
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(eachBlock.getX(), eachBlock.getY(), eachBlock.getZ()),
					eachBlock);
		}

		for (FloorSwitch eachSwitch : dataSetFloorSwitches) {
			ObjectMap.drawableObjectHashMap.put(eachSwitch.getObjectVectorWithName(), eachSwitch);
		}

		for (Gate eachGate : dataSetsGate) {
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(eachGate.getX(), eachGate.getY(), eachGate.getZ()),
					eachGate);
		}

		ObjectMap.drawableObjectHashMap.put(
				new ObjectVector(player1.getCellX(), player1.getCellY(), player1.getCellZ(), player1.getName()),
				player1);
		ObjectMap.drawableObjectHashMap.put(
				new ObjectVector(player2.getCellX(), player2.getCellY(), player2.getCellZ(), player2.getName()),
				player2);
		// ObjectMap.drawableObjectHashMap.put(
		// player1.getCellX() + " " + player1.getCellY() + " " + player1.getCellZ() + " " + player1.getName(),
		// player1);

		for (FloorPiece floor : floorLevelMap.getFloorPieces()) {
			ObjectMap.drawableObjectHashMap.put(floor.getObjectVector(), floor);
		}

	}

	@Override
	public void update(int step) {

		camera.update(step);
		player1.update(step, camera.getRotation());
		player2.update(step, camera.getRotation());

		for (FloorSwitch fs : dataSetFloorSwitches) {
			fs.update(step);
		}
		
		
		for (GateToGateTeleport teleGate : dataSetTeleportToGates) {
			teleGate.update(step);
		}
		for (TeleportToArea teleGate : dataSetTeleportToArea) {
			teleGate.update(step);
		}
		for (Slope slope : dataSetSlopes) {
			slope.update(step, camera);
		}
		for (GateController eachController : dataSetsGateController) {
			eachController.update();
		}
		for (Gate each : dataSetsGate) {
			each.update(step);
		}

	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {

		camera.setSceneSize(sceneWidth, sceneHeight);

		Block.refreshDrawCache(camera);
		FloorPiece.refreshDrawCache(camera);

		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		// Draw grid line

		// int gridSizeX = floorLevelMap.getSizeX();
		// int gridSizeY = floorLevelMap.getSizeY();
		//
		// g.setStroke(new BasicStroke(1));
		// g.setColor(ColorSwatch.SHADOW);
		// for (int i = 0; i < gridSizeX; i++) {
		// Vector2 startPos = camera.getDrawPosition(i + 0.5f, -gridSizeY - 0.5f, 0);
		// Vector2 endPos = camera.getDrawPosition(i + 0.5f, gridSizeY + 0.5f, 0);
		// g.drawLine(startPos.getIntX(), startPos.getIntY(), endPos.getIntX(), endPos.getIntY());
		// }
		// for (int i = 0; i < gridSizeY; i++) {
		// Vector2 startPos2 = camera.getDrawPosition(-gridSizeX - 0.5f, i + 0.5f, 0);
		// Vector2 endPos2 = camera.getDrawPosition(gridSizeX + 0.5f, i + 0.5f, 0);
		// g.drawLine(startPos2.getIntX(), startPos2.getIntY(), endPos2.getIntX(), endPos2.getIntY());
		// }
		// g.setStroke(new BasicStroke(3));
		// // Draw floor border
		// int[] x = new int[4];
		// int[] y = new int[4];
		//
		// Vector2[] v = new Vector2[4];
		// v[0] = camera.getDrawPosition(-0.5f, -0.5f, 0);
		// v[1] = camera.getDrawPosition(-0.5f, gridSizeY + 0.5f, 0);
		// v[2] = camera.getDrawPosition(gridSizeX + 0.5f, gridSizeY + 0.5f, 0);
		// v[3] = camera.getDrawPosition(gridSizeX + 0.5f, -0.5f, 0);
		//
		// for (int k = 0; k < 4; k++) {
		// x[k] = (int) v[k].getX();
		// y[k] = (int) v[k].getY();
		// }
		//
		// g.drawPolygon(x, y, 4);

		// Draw things

		/*
		 * player1.draw(g, camera); player2.draw(g, camera);
		 * 
		 * for (Block b : blocks) { b.draw(g, camera); }
		 * 
		 * for (FloorSwitch fs : floorSwitches) { fs.draw(g, camera); }
		 */

		LevelRenderer.draw(ObjectMap.drawableObjectHashMap.values(), g, camera);

	}

}
