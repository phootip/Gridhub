package stage;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import core.geom.Vector2;
import core.geom.Vector3;
import stage.renderer.LevelRenderer;
import scene.core.Scene;
import scene.level.Chapter;
import scene.level.LevelData;
import scene.level.LevelDataBuilder;
import stage.editor.EditorCursor.EditorCursorState;
import stage.editor.AddableObject;
import stage.editor.EditorCursor;
import stage.LevelEditorManager;
import stage.gameobj.Block;

import stage.gameobj.FloorPiece;
import stage.gameobj.FloorSwitch;

import stage.gameobj.BlockController;
import stage.gameobj.FinishArea;
import stage.gameobj.Gate;
import stage.gameobj.GateController;
import stage.gameobj.GateToGateTeleport;
import stage.gameobj.IControlable;
import stage.gameobj.IDrawable;
import stage.gameobj.ObjectVector;
import stage.gameobj.Player;
import stage.gameobj.Slope;
import stage.gameobj.SwitchController;
import stage.gameobj.TeleportDestionation;
import stage.gameobj.TeleportGate;
import stage.gameobj.TeleportGateController;
import stage.gameobj.TeleportToArea;
import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;

public class GameStage {

	private List<Camera> cameraList = new ArrayList<>();
	private Player player1 = null, player2 = null;
	private EditorCursor cursor = null;
	private FloorLevel floorLevelMap;

	protected FloorLevel getFloorLevelMap() {
		return dataSetFloorLevel;
	}

	private ArrayList<Block> blocks = new ArrayList<>();
	private ArrayList<FloorSwitch> floorSwitches = new ArrayList<>();
	private ArrayList<Slope> slopes = new ArrayList<>();

	private ArrayList<GateToGateTeleport> teleportGates = new ArrayList<>();
	// private ArrayList<GateToGateTeleport> teleportLinked = new ArrayList<>();
	private int[] telePortLink;
	private ArrayList<TeleportToArea> teleportToAreas = new ArrayList<>();
	private ArrayList<TeleportDestionation> teleportDests = new ArrayList<>();
	
	private ArrayList<GateController> gateControllers = new ArrayList<>();
	// private int[] gateControllerGateIndex;
	private ArrayList<Integer> gateControllerGateIndex = new ArrayList<>();
	private ArrayList<int[]> linkGate = new ArrayList<>();

	private ArrayList<BlockController> blockControllers = new ArrayList<>();
	private ArrayList<TeleportGateController> teleportGateControllers = new ArrayList<>();
	private ArrayList<Gate> gates = new ArrayList<>();
	private ArrayList<SwitchController> swControllers = new ArrayList<>();
	private ArrayList<Integer> finishX = new ArrayList<>();
	private ArrayList<Integer> finishY = new ArrayList<>();
	private int[] startX;
	private int[] startY;
	private int playerCount;
	
	
	// Testing DataSet
	private ArrayList<Block> dataSetBlock;
	private ArrayList<Slope> dataSetSlopes;
	private ArrayList<FloorSwitch> dataSetFloorSwitches;
	private ArrayList<GateToGateTeleport> dataSetTeleportToGates;
	private int[] dataSetLinkTeleport;
	private ArrayList<TeleportToArea> dataSetTeleportToArea;
	private ArrayList<TeleportDestionation> dataSetTeleportDests;
	private ArrayList<Gate> dataSetsGate;
	private ArrayList<SwitchController> dataSetSWControllers;
	private ArrayList<int[]> dataSetControlObjectIndex;
	private ArrayList<int[]> dataSetSwitchSetIndex;
	private ArrayList<BlockController> dataSetBlockController;
	private ArrayList<TeleportGateController> datasetTeleportGateController;
	private FloorLevel dataSetFloorLevel;
	private String dataLevelName;
	private ArrayList<Integer> dataSetFinishX;
	private ArrayList<Integer> dataSetFinishY;
	private int[] dataSetStartX;
	private int[] dataSetStartY;
	private ArrayList<FinishArea> dataSetfinishArea = new ArrayList<>();
	private int dataPlayerCount;

	
	private GameStageType gameStageType;
	private LevelData levelData;
	private LevelEditorManager editorManager = null;

	private ObjectMap objectMap = new ObjectMap();

	public GameStage(LevelData levelData, GameStageType gameStageType) {
		this(levelData, gameStageType, true);
	}

	public GameStage(LevelData lvlData, GameStageType gameStageType, boolean showLevelName) {
		this.gameStageType = gameStageType;
		this.levelData = lvlData;
		objectMap.drawableObjectHashMap = new HashMap<ObjectVector, IDrawable>();

//		floorLevelMap = new FloorLevel(25, 25);
//		int floorMapXSize = floorLevelMap.getSizeX();
//		int floorMapYSize = floorLevelMap.getSizeY();
//
//		for (int i = 0; i < floorMapXSize; i++) {
//			for (int j = 0; j < floorMapYSize; j++) {
//				if (i > 11) {
//					floorLevelMap.setZValue(i, j, 2);
//				} else if (i > 8) {
//					floorLevelMap.setZValue(i, j, 1);
//				} else {
//					floorLevelMap.setZValue(i, j, 0);
//				}
//			}
//		}


		/////////////////////////Starting to Serialize ///////////////////////////////////

//		blocks.add(new Block(6, 11, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(7, 11, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(8, 11, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(6, 12, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(7, 12, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(8, 12, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(6, 13, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(7, 13, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(8, 13, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(16, 4, 2, 20, true, floorLevelMap));
//		blocks.add(new Block(9, 6, 1, 20, true, floorLevelMap));
//		blocks.add(new Block(2, 3, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(2, 4, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(2, 5, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(2, 7, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(2, 8, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(2, 9, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(2, 10, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(1, 4, 1, 150, true, floorLevelMap));
//		blocks.add(new Block(2, 3, 1, 20, true, floorLevelMap));
//		blocks.add(new Block(3, 3, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(4, 3, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(5, 3, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(6, 3, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(7, 3, 0, 20, true, floorLevelMap));
//		blocks.add(new Block(8, 3, 0, 20, true, floorLevelMap));
//		
//		slopes.add(new Slope(6, 10, 0, Slope.ALIGNMENT_RIGHT));
//		slopes.add(new Slope(6, 5, 0, Slope.ALIGNMENT_RIGHT));
//		slopes.add(new Slope(9, 10, 1, Slope.ALIGNMENT_RIGHT));
//		slopes.add(new Slope(-2, 3, 0, Slope.ALIGNMENT_RIGHT));
//		slopes.add(new Slope(13, 4, 2, Slope.ALIGNMENT_RIGHT));
//		slopes.add(new Slope(16, 1, 2, Slope.ALIGNMENT_DOWN));
//		slopes.add(new Slope(16, 7, 2, Slope.ALIGNMENT_UP));
//		slopes.add(new Slope(19, 4, 2, Slope.ALIGNMENT_LEFT));
//		
//		floorSwitches.add(new FloorSwitch(1, 0, 0, false, 20));
//		floorSwitches.add(new FloorSwitch(4, 0, 0, false, 10));
//		floorSwitches.add(new FloorSwitch(6, 0, 0, false, 10));
//
//		GateToGateTeleport gateTele0 = new GateToGateTeleport(0, 1, 0);
//		GateToGateTeleport gateTele1 = new GateToGateTeleport(5, 0, 0);
//		GateToGateTeleport gateTele2 = new GateToGateTeleport(16, 4, 3);
//		gateTele0.setDestinationGate(gateTele2);
//		gateTele1.setDestinationGate(gateTele0);
//		gateTele2.setDestinationGate(gateTele0);
//		teleportGates.add(gateTele0);
//		teleportGates.add(gateTele1);
//		teleportGates.add(gateTele2);
//		
//		teleportToAreas.add(new TeleportToArea(10, 4, 1, 0, 2, 0));
//		for(int i =  0 ; i < teleportToAreas.size() ; i++) {
//			teleportDests.add(teleportToAreas.get(i).getTeleportDestination());
//		}
//
//		Gate gate1 = new Gate(1, 3, 0);
//		Gate gate2 = new Gate(1, 4, 0);
//		Gate gate3 = new Gate(1, 5, 0);
//		gates.add(gate1);
//		gates.add(gate2);
//		gates.add(gate3);
//		
//		ArrayList<FloorSwitch> fsArray = new ArrayList<>();
//		fsArray.add(floorSwitches.get(0));
//		fsArray.add(floorSwitches.get(1));
//		SwitchController swControl0 = new SwitchController(fsArray, new int[] { 0, 1, 0, 0 }, gate1);
//		SwitchController swControl1 = new SwitchController(fsArray, new int[] { 0, 1, 0, 0 }, gate2);
//		fsArray = new ArrayList<>();
//		fsArray.add(floorSwitches.get(1));
//		fsArray.add(floorSwitches.get(2));
//		SwitchController swControl2 = new SwitchController(fsArray, new int[] { 0, 0, 1, 0 }, gate3);
//		SwitchController swControl3 = new SwitchController(fsArray, new int[] { 0, 0, 0, 1 }, gateTele2);
//		swControllers.add(swControl0);
//		swControllers.add(swControl1);
//		swControllers.add(swControl2);
//		swControllers.add(swControl3);
//		
//		String levelName = "Chenlong Test 01";
//		finishX.add(0);
//		finishY.add(24);
//		startX = new int []{9,4};
//		startY = new int []{0,0};
//		playerCount = 1;
//		
//		LevelDataBuilder lvlBuilder = new LevelDataBuilder();
//		lvlBuilder.addBlocks(blocks);
//		lvlBuilder.addSlopes(slopes);
//		lvlBuilder.addFloorSwitches(floorSwitches);
//		lvlBuilder.addGateTogateTeles(teleportGates);
//		lvlBuilder.addTeleportToArea(teleportToAreas);
//		lvlBuilder.addTelportDests(teleportDests);
//		lvlBuilder.addGates(gates);
//		lvlBuilder.addSwControllers(swControllers);
//		lvlBuilder.addFloorLevel(floorLevelMap);
//		lvlBuilder.addLevelName(levelName);
//		lvlBuilder.addFinishX(finishX);
//		lvlBuilder.addFinishY(finishY);
//		lvlBuilder.addStartX(startX);
//		lvlBuilder.addStartY(startY);
//		lvlBuilder.addPlayerCount(playerCount);
//		//lvlBuilder.build();
//		//System.out.println("FuckYou");
//		String outPutJSON = lvlBuilder.createLevelDataAsJSONString();
//		System.out.println(outPutJSON);
//		try{
//			FileWriter writer = new FileWriter("E:\\0.json");
//			writer.write(outPutJSON);
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		///////////////////////// End of File Creating ///////////////////////////////////
		
		/////////////////////////Begin to Deserialize ///////////////////////////////////
		//levelData = LevelData.parse(1,outPutJSON);
		dataSetBlock = levelData.getBlocks();
		dataSetSlopes = levelData.getSlopes();
		dataSetFloorSwitches = levelData.getFloorSwitches();
		dataSetTeleportToGates = levelData.getGateTogateTeles();
		dataSetLinkTeleport = levelData.getGateLink();
		dataSetTeleportToArea = levelData.getTeleportToArea();
		dataSetTeleportDests = levelData.getTeleportDests();
		dataSetsGate = levelData.getGates();
		dataSetSWControllers = levelData.getSwControllers();
		dataSetControlObjectIndex = levelData.getControlObjectIndex();
		dataSetSwitchSetIndex = levelData.getSwitchSetIndex();
		dataSetFloorLevel = levelData.getFloorLevel();
		dataLevelName = levelData.getLevelName();
		dataSetFinishX = levelData.getFinishX();
		dataSetFinishY = levelData.getFinishY();
		dataSetStartX = levelData.getStartX();
		dataSetStartY = levelData.getStartY();
		playerCount = levelData.getPlayerCount();
		
		if (gameStageType == GameStageType.PLAY) {
			player1 = new Player(util.Constants.PLAYER1_ID, dataSetFloorLevel, 9, 4, 1);
			this.cameraList.add(new Camera(player1));

			if (levelData.getPlayerCount() == 2) {
				player2 = new Player(util.Constants.PLAYER2_ID, dataSetFloorLevel, 0, 0, 0);
				this.cameraList.add(new Camera(player2));
			}
		} else if (gameStageType == GameStageType.LEVEL_EDITOR) {

			cursor = new EditorCursor(dataSetFloorLevel);
			this.cameraList.add(new Camera(cursor));

			this.editorManager = new LevelEditorManager(cursor, this);

		} else if (gameStageType == GameStageType.THUMBNAIL) {

			this.cameraList.add(new Camera(new IDrawable() {
				@Override
				public Vector3 getDrawPosition() {
					return new Vector3(dataSetFloorLevel.getSizeX() / 2, dataSetFloorLevel.getSizeY() / 2, 0);
				}

				@Override
				public void draw(Graphics2D g, Camera camera) {
				}
			}));

		}
		
		for (Block eachBlock : dataSetBlock) {
			eachBlock.setObjectMap(objectMap);
			eachBlock.setFloorLevelMap(dataSetFloorLevel);
			objectMap.drawableObjectHashMap.put(eachBlock.getObjectVector(), eachBlock);
		}
		
		for (Slope eachSlope : dataSetSlopes) {

			int slopeStartX = eachSlope.getStartX();
			int slopeStartY = eachSlope.getStartY();
			int slopeStartZ = eachSlope.getStartZ();
			int slopeEndX = eachSlope.getEndX();
			int slopeEndY = eachSlope.getEndY();
			int xBar = (slopeStartX + slopeEndX) / 2;
			int yBar = (slopeStartY + slopeEndY) / 2;

			objectMap.drawableObjectHashMap.put(new ObjectVector(slopeStartX, slopeStartY, slopeStartZ), eachSlope);
			objectMap.drawableObjectHashMap.put(new ObjectVector(xBar, yBar, slopeStartZ), eachSlope);
			objectMap.drawableObjectHashMap.put(new ObjectVector(slopeEndX, slopeEndY, slopeStartZ), eachSlope);

		}
		
		for (FloorSwitch eachSwitch : dataSetFloorSwitches) {
			eachSwitch.setObjectMap(objectMap);
			objectMap.drawableObjectHashMap.put(eachSwitch.getObjectVectorWithName(), eachSwitch);
		}
		
		
		for (int i = 0; i < dataSetTeleportToGates.size(); i++) {
			GateToGateTeleport mainGate = dataSetTeleportToGates.get(i);
			mainGate.setDestinationTelelportGate(dataSetTeleportToGates.get(dataSetLinkTeleport[i]));
			mainGate.setObjectMap(objectMap);
		}
		
		for (GateToGateTeleport eachTeleport : dataSetTeleportToGates) {
			objectMap.drawableObjectHashMap
					.put(new ObjectVector(eachTeleport.getX(), eachTeleport.getY(), eachTeleport.getZ()), eachTeleport);
		}
		
		for (TeleportToArea eachTeleport : dataSetTeleportToArea) {
			eachTeleport.setObjectMap(objectMap);
			objectMap.drawableObjectHashMap
					.put(new ObjectVector(eachTeleport.getX(), eachTeleport.getY(), eachTeleport.getZ()), eachTeleport);
		}
		
		for (TeleportDestionation eachTeleportDest : dataSetTeleportDests) {
			eachTeleportDest.setObjectMap(objectMap);
			objectMap.drawableObjectHashMap.put(new ObjectVector(eachTeleportDest.getX(), eachTeleportDest.getY(), eachTeleportDest.getZ()), eachTeleportDest);
		}
		
		for (Gate eachGate : dataSetsGate) {
			eachGate.setObjectMap(objectMap);
			objectMap.drawableObjectHashMap.put(new ObjectVector(eachGate.getX(), eachGate.getY(), eachGate.getZ()),
					eachGate);
		}
		
		for (int i = 0 ; i < dataSetSWControllers.size() ; i++) {
			SwitchController switchCtrl = dataSetSWControllers.get(i);
			int[] switchIndex = dataSetSwitchSetIndex.get(i);
			ArrayList<FloorSwitch> switchSet = new ArrayList<>();
			for(int j = 0 ; j < switchIndex.length ; j++) {
				switchSet.add(dataSetFloorSwitches.get(switchIndex[j]));
			}
			switchCtrl.setFloorSwitchesControllerSet(switchSet);
		}
		for(int i = 0 ; i < dataSetSWControllers.size() ; i++) {
			SwitchController switchCtrl = dataSetSWControllers.get(i);
			int[] controlObjectIndex = dataSetControlObjectIndex.get(i);
			for(int j = 0 ; j < controlObjectIndex.length ; j++) {
				if(controlObjectIndex[j] >= 0) {
					if(j == 0) {
						//Gate Type
						switchCtrl.setControlObject(dataSetsGate.get(controlObjectIndex[j]));
					}
					if(j == 1) {
						//GateToGateType
						switchCtrl.setControlObject(dataSetTeleportToGates.get(controlObjectIndex[j]));
					}
					if(j == 2) {
						//ToAreaType
						switchCtrl.setControlObject(dataSetTeleportToArea.get(controlObjectIndex[j]));
					}
				}
			}
		}
		
		System.out.println("Level : "+ dataLevelName);
		System.out.println(playerCount);
		
		for(int i = 0 ; i < dataSetFinishX.size() ; i++) {
			int finX = dataSetFinishX.get(i);
			int finY = dataSetFinishY.get(i);
			dataSetfinishArea.add(new FinishArea(finX, finY,dataSetFloorLevel.getZValueFromXY(finX, finY) ));
		}
		
		for(FinishArea eachFinArea : dataSetfinishArea) {
			eachFinArea.setObjectMap(objectMap);
			objectMap.drawableObjectHashMap.put(new ObjectVector(eachFinArea.getX(), eachFinArea.getY(), eachFinArea.getZ()), eachFinArea);
		}
		
		if (player1 != null) {
			player1.setObjectMap(objectMap);
			objectMap.drawableObjectHashMap.put(
					new ObjectVector(player1.getCellX(), player1.getCellY(), player1.getCellZ(), player1.getName()),
					player1);
			objectMap.drawableObjectHashMap.put(new ObjectVector(-1, -1, -1, "PlayerTrail : " + player1.getName()),
					player1.getPlayerTrail());
		}
		if (player2 != null) {
			player2.setObjectMap(objectMap);
			objectMap.drawableObjectHashMap.put(
					new ObjectVector(player2.getCellX(), player2.getCellY(), player2.getCellZ(), player2.getName()),
					player2);
			objectMap.drawableObjectHashMap.put(new ObjectVector(-1, -1, -1, "PlayerTrail : " + player2.getName()),
					player2.getPlayerTrail());
		}
		
		if (cursor != null) {
			objectMap.drawableObjectHashMap.put(new ObjectVector(-1, -1, -1, "EditorCursor"), cursor);
		}
		// ObjectMap.drawableObjectHashMap.put(
		// player1.getCellX() + " " + player1.getCellY() + " " + player1.getCellZ() + " " + player1.getName(),
		// player1);

		constructFloorPieces(false);
//		for (FloorPiece floor : dataSetFloorLevel.getFloorPieces()) {
//			objectMap.drawableObjectHashMap.put(floor.getObjectVector(), floor);
//		}

		if (!showLevelName) {
			levelNameShowTimer = levelNameShowDuration;
		}
		/////////////////////////////////////// End of Deserialize //////////////////////////////////////////////////
		
		

	}

	public void update(int step) {

		for (Camera camera : cameraList) {
			camera.update(step);
		}
		if (player1 != null) {
			player1.update(step);
		}
		if (player2 != null) {
			player2.update(step);
		}
		if (cursor != null) {
			cursor.setState(EditorCursorState.NORMAL);
			cursor.update(step);
		}

		for (FloorSwitch fs : dataSetFloorSwitches) {
			fs.update(step);
		}

		for (GateToGateTeleport teleGate : dataSetTeleportToGates) {
			teleGate.update(step);
		}
		for (TeleportToArea teleGate : dataSetTeleportToArea) {
			teleGate.update(step);
		}
		for (Block each : dataSetBlock) {
			each.update(step);
		}
		for (SwitchController eachController : dataSetSWControllers) {
			eachController.update();
		}
		for (Gate each : dataSetsGate) {
			each.update(step);
		}
		for (FinishArea each : dataSetfinishArea) {
			each.update();
		}

		if (this.editorManager != null) {
			this.editorManager.update(step);
		}

		if (floorPieceList != null) {
			for (FloorPiece piece : floorPieceList) {
				piece.update(step);
			}
		}

		levelNameShowTimer += step;
		if (levelNameShowTimer > levelNameShowDuration) {
			levelNameShowTimer = levelNameShowDuration;
		}

	}

	private int levelNameShowTimer = 0;
	private int levelNameShowDuration = 100 * 120;
	private static final int OVERLAY_LEVEL_NAME_TEXT_SIZE = 50;
	private static final int OVERLAY_CHAPTER_NAME_TEXT_SIZE = 30;
	private static final int OVERLAY_BOX_VERTICAL_MARGIN = 20;

	private void drawLevelName(Graphics2D g, int x, int y, int width, int height) {
		if (gameStageType == GameStageType.PLAY && levelNameShowTimer < levelNameShowDuration) {
			Rectangle oldClip = g.getClipBounds();

			Font chapterNameFont = Resource.getInstance().getDefaultFont(OVERLAY_CHAPTER_NAME_TEXT_SIZE);
			Font levelNameFont = Resource.getInstance().getDefaultFont(OVERLAY_LEVEL_NAME_TEXT_SIZE,
					Resource.FontWeight.BOLD);
			String chapterName = levelData.getChapter().getChapterName();
			String levelName = levelData.getLevelName();

			final int boxWidth = 100 + g.getFontMetrics(levelNameFont).stringWidth(levelName);
			final int boxHeight = OVERLAY_BOX_VERTICAL_MARGIN * 2 + OVERLAY_LEVEL_NAME_TEXT_SIZE
					+ OVERLAY_CHAPTER_NAME_TEXT_SIZE;
			int boxLeft = x + (width - boxWidth) / 2;
			int boxTop = y + 100;

			int clipperHeight = boxHeight;
			if (levelNameShowTimer < 100 * 20) {
				clipperHeight = (int) Helper.sineInterpolate(0, clipperHeight, levelNameShowTimer / (100 * 20f), false,
						true);
			} else if (levelNameShowTimer > 100 * 100) {
				clipperHeight = (int) Helper.sineInterpolate(clipperHeight, 0,
						(levelNameShowTimer - 100 * 100) / (100 * 20f), true, false);
			}
			int clipperTop = boxTop + (boxHeight - clipperHeight) / 2;
			g.setClip(boxLeft, clipperTop, boxWidth, clipperHeight);

			g.setColor(ColorSwatch.SHADOW);
			g.fillRect(boxLeft, boxTop, boxWidth, boxHeight);

			boxTop = clipperTop;

			// Chapter Name

			Vector2 chapterNameCenterPos = Helper.getCenteredTextPosition(chapterName, chapterNameFont, g, boxLeft, 0,
					boxWidth, 0);

			g.setFont(chapterNameFont);
			g.setColor(ColorSwatch.FOREGROUND);
			g.drawString(chapterName, chapterNameCenterPos.getX(),
					boxTop + OVERLAY_BOX_VERTICAL_MARGIN + g.getFontMetrics().getAscent());

			// Level Name

			Vector2 levelNameCenterPos = Helper.getCenteredTextPosition(levelName, levelNameFont, g, boxLeft, 0,
					boxWidth, 0);

			g.setFont(levelNameFont);
			g.setColor(ColorSwatch.FOREGROUND);
			g.drawString(levelName, levelNameCenterPos.getX(), boxTop + OVERLAY_BOX_VERTICAL_MARGIN
					+ OVERLAY_CHAPTER_NAME_TEXT_SIZE + g.getFontMetrics().getAscent());

			g.setClip(oldClip);
		}
	}

	public boolean isEscapeKeyHandled() {
		return (editorManager != null) && editorManager.isEscapeKeyHandled();
	}

	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {

		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		int cameraCount = cameraList.size();

		int shifter = 0;
		if (editorManager != null) {
			shifter = editorManager.drawPane(g, 0, 0, sceneWidth, sceneHeight);
		}
		int totalViewportWidth = sceneWidth - shifter;

		for (int i = 0; i < cameraCount; i++) {
			Camera camera = cameraList.get(i);

			camera.setSceneSize(totalViewportWidth / cameraCount, sceneHeight);

			Block.refreshDrawCache(camera);
			FloorPiece.refreshDrawCache(camera);

			AffineTransform oldTransform = g.getTransform();
			Rectangle oldClip = g.getClipBounds();

			g.setClip(shifter + totalViewportWidth * i / cameraCount, 0, totalViewportWidth / cameraCount, sceneHeight);
			g.translate(shifter + totalViewportWidth * i / cameraCount, 0);

			LevelRenderer.draw(objectMap.drawableObjectHashMap.values(), g, camera);
			// Cursor and player(s) overlay is exception; it is on everything
			drawOverlays(g, camera);

			g.setTransform(oldTransform);
			g.setClip(oldClip);

			drawLevelName(g, shifter + totalViewportWidth * i / cameraCount, 0, totalViewportWidth / cameraCount,
					sceneHeight);

			if (i > 0) {
				g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.BACKGROUND, 1));
				g.setStroke(new BasicStroke(9));
				g.drawLine(shifter + totalViewportWidth * i / cameraCount, 0, totalViewportWidth * i / cameraCount,
						sceneHeight);

				g.setColor(ColorSwatch.SHADOW);
				g.setStroke(new BasicStroke(5));
				g.drawLine(shifter + totalViewportWidth * i / cameraCount, 0, totalViewportWidth * i / cameraCount,
						sceneHeight);
			}

			camera.resetDeformationChanged();

		}

	}

	public String buildLevelDataAsString() {
		
		LevelDataBuilder lvlBuilder = new LevelDataBuilder();
		lvlBuilder.addBlocks(dataSetBlock);
		lvlBuilder.addSlopes(dataSetSlopes);
		lvlBuilder.addFloorSwitches(dataSetFloorSwitches);
		lvlBuilder.addGateTogateTeles(dataSetTeleportToGates);
		lvlBuilder.addTeleportToArea(dataSetTeleportToArea);
		lvlBuilder.addTelportDests(dataSetTeleportDests);
		lvlBuilder.addGates(dataSetsGate);
		lvlBuilder.addSwControllers(dataSetSWControllers);
		lvlBuilder.addFloorLevel(dataSetFloorLevel);
		lvlBuilder.addLevelName(dataLevelName);
		lvlBuilder.addFinishX(dataSetFinishX);
		lvlBuilder.addFinishY(dataSetFinishY);
		lvlBuilder.addStartX(dataSetStartX);
		lvlBuilder.addStartY(dataSetStartY);
		lvlBuilder.addPlayerCount(dataPlayerCount);
		String outPutJSON = lvlBuilder.createLevelDataAsJSONString();
		lvlBuilder.setFileName(levelData.getLevelFileName());
		return outPutJSON;
	}
	
	public LevelData buildLevelData() {
		String outPutJSON = buildLevelDataAsString();
		Chapter chapter = levelData.getChapter();
		levelData = LevelData.parse(outPutJSON);
		levelData.setChapter(chapter);
		return levelData;
		
	}
	private void drawOverlays(Graphics2D g, Camera camera) {
		for (FloorSwitch fs : dataSetFloorSwitches) {
			fs.drawOverlay(g, camera);
		}
		if (cursor != null) {
			cursor.drawOverlay(g, camera);
		}
		if (player1 != null) {
			player1.drawOverlay(g, camera);
		}
		if (player2 != null) {
			player2.drawOverlay(g, camera);
		}
		if (editorManager != null) {
			editorManager.drawOverlay(g, camera);
		}
	}

	protected ObjectVector getPlacingObjectPositionAtCursor() {
		int x = cursor.getCurrentX();
		int y = cursor.getCurrentY();
		ObjectVector ans = new ObjectVector(x, y, dataSetFloorLevel.getZValueFromXY(x, y));
		while (objectMap.drawableObjectHashMap.get(ans) != null) {
			ans.addVector(0, 0, 1);
		}
		return ans;
	}

	protected boolean isAbleToPlaceObjectAtCursor(AddableObject objectType) {
		int x = cursor.getCurrentX();
		int y = cursor.getCurrentY();
		if (objectType == AddableObject.BOX) {
			Object obj = objectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, dataSetFloorLevel.getZValueFromXY(x, y)));
			return (obj == null) || (obj instanceof Block);
		}
		return false;
	}

	protected boolean isAbleToPlaceSlopeAtCursor(int alignment) {
		ObjectVector middlePos = getPlacingObjectPositionAtCursor();
		ObjectVector startPos = Slope.getSlopeStartPosition(middlePos, alignment);
		ObjectVector endPos = Slope.getSlopeEndPosition(middlePos, alignment);

		// Check level
		if (dataSetFloorLevel.getZValueFromXY(startPos.getX(), startPos.getY()) != startPos.getZ())
			return false;
		if (dataSetFloorLevel.getZValueFromXY(middlePos.getX(), middlePos.getY()) != middlePos.getZ())
			return false;
		if (dataSetFloorLevel.getZValueFromXY(endPos.getX(), endPos.getY()) != endPos.getZ() - 1)
			return false;

		endPos.addVector(0, 0, -1);

		return (objectMap.drawableObjectHashMap.get(startPos) == null)
				&& (objectMap.drawableObjectHashMap.get(middlePos) == null)
				&& (objectMap.drawableObjectHashMap.get(endPos) == null);
	}

	protected void addObjectAtCursor(Object object) {
		// ObjectVector placePosition = getPlacingObjectPositionAtCursor();
		if (object instanceof Block) {
			Block obj = (Block) object;
			obj.setObjectMap(objectMap);
			dataSetBlock.add(obj);
			objectMap.drawableObjectHashMap.put(obj.getObjectVector(), obj);
		}
	}

	private List<FloorPiece> floorPieceList;

	protected List<FloorPiece> getFloorPieceList() {
		return floorPieceList;
	}

	protected void constructFloorPieces(boolean checkForExistingFloorPiece) {
		if (checkForExistingFloorPiece) {
			Iterator<ObjectVector> it = objectMap.drawableObjectHashMap.keySet().iterator();
			while (it.hasNext()) {
				ObjectVector ov = it.next();
				if (ov.getName().equals("FloorPiece")) {
					it.remove();
				}
			}
		}

		floorPieceList = getFloorLevelMap().getFloorPieces();
		for (FloorPiece floor : floorPieceList) {
			objectMap.drawableObjectHashMap.put(floor.getObjectVector(), floor);
		}
	}

	protected void addSlopeAtCursor(int alignment) {
		ObjectVector middlePos = getPlacingObjectPositionAtCursor();
		ObjectVector startPos = Slope.getSlopeStartPosition(middlePos, alignment);

		Slope slope = new Slope(startPos.getX(), startPos.getY(), startPos.getZ(), alignment);

		int slopeStartX = slope.getStartX();
		int slopeStartY = slope.getStartY();
		int slopeStartZ = slope.getStartZ();
		int slopeEndX = slope.getEndX();
		int slopeEndY = slope.getEndY();
		int xBar = (slopeStartX + slopeEndX) / 2;
		int yBar = (slopeStartY + slopeEndY) / 2;

		objectMap.drawableObjectHashMap.put(new ObjectVector(slopeStartX, slopeStartY, slopeStartZ), slope);
		objectMap.drawableObjectHashMap.put(new ObjectVector(xBar, yBar, slopeStartZ), slope);
		objectMap.drawableObjectHashMap.put(new ObjectVector(slopeEndX, slopeEndY, slopeStartZ), slope);
	}

}
