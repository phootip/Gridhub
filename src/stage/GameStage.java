package stage;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.Helper;
import util.Resource;

public class GameStage extends Scene {

	private Camera camera1;
	private Camera camera2;
	private Player player1;
	private Player player2;
	private FloorLevel floorLevelMap;
	private ArrayList<Block> blocks = new ArrayList<>();
	private ArrayList<FloorSwitch> floorSwitches = new ArrayList<>();
	private ArrayList<Slope> slopes = new ArrayList<>();
	ArrayList<TeleportGate> teleportGates = new ArrayList<>();
	ArrayList<SwitchController> switchController = new ArrayList<>();
	ArrayList<Gate> gates = new ArrayList<>();

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
		// map floor checking
		for (int j = 0; j < floorMapYSize; j++) {
			for (int i = 0; i < floorMapXSize; i++) {
				System.out.print(floorLevelMap.getZValueFromXY(i, j));
				if (i == floorMapXSize - 1)
					System.out.println();

			}
		}
		player1 = new Player(util.Constants.PLAYER1_ID, floorLevelMap, 9, 4, 1);
		player2 = new Player(util.Constants.PLAYER2_ID, floorLevelMap, 0, 0, 0);

		camera1 = new Camera(player1);
		camera2 = new Camera(player2);

		// floorSwitches.add(new FloorSwitch(6, 3, 0, false, 10));
		// floorSwitches.add(new FloorSwitch(6, 5, 0, false, 20));
		// floorSwitches.add(new FloorSwitch(6, 7, 0, true, 10));
		// floorSwitches.add(new FloorSwitch(6, 9, 0, true, 20));

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

		// slopes.add(new Slope(5, 5, 2));
		// slopes.add(new Slope(-10, -10, -1));
		// slopes.add(new Slope(-5 , -5, -2));

		// blocks.add(new Block(0, 2, 0));
		// blocks.add(new Block(2, 0, 0));
		// blocks.add(new Block(-9, 2, 0, 110, true));
		// blocks.add(new Block(-10, 3, 0, 110, true));
		//
		// blocks.add(new Block(-2, 3, 0, 20, false));
		// blocks.add(new Block(-2, 2, 1, 20, true));
		// blocks.add(new Block(-2, 1, 0, 20, true));
		// blocks.add(new Block(-2, 0, 2, 20, true));
		// blocks.add(new Block(-2, -1, 0, 20, true));
		// blocks.add(new Block(-1, -2, 1, 20, true));
		// blocks.add(new Block(0, -3 ,0, 20, true));
		// blocks.add(new Block(1, -2, 2, 20, true));
		// blocks.add(new Block(2, -1, 0, 20, true));
		// blocks.add(new Block(2, 0, 1, 20, true));
		// blocks.add(new Block(2, 1, 0, 20, true));
		// blocks.add(new Block(2, 2, 3, 20, true));
		blocks.add(new Block(16, 4, 2, 20, true, floorLevelMap));
		blocks.add(new Block(2, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 4, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 5, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 7, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 8, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 9, 0, 20, true, floorLevelMap));
		blocks.add(new Block(2, 10, 0, 20, true, floorLevelMap));

		// blocks.add(new Block(1, 3, 0, 20, true, floorLevelMap));

		blocks.add(new Block(1, 4, 1, 150, true, floorLevelMap));

		blocks.add(new Block(2, 3, 1, 20, true, floorLevelMap));
		blocks.add(new Block(3, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(4, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(5, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(6, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(7, 3, 0, 20, true, floorLevelMap));
		blocks.add(new Block(8, 3, 0, 20, true, floorLevelMap));

		// blocks.add(new Block(-1, 1, 0, 110, true));
		// blocks.add(new Block(0, 1, 0, 110, true));
		// blocks.add(new Block(1, 1, 0, 110, true));

		// for (int i = -15; i <= 15; i++) {
		// for (int j = -15; j <= 15; j++) {
		// if (i >= -2 && j >= -2 && i <= 2 && j <= 2)
		// continue;
		// blocks.add(new Block(i, j + 1, 0, 1, true));
		// }
		// }

		GateToGateTeleport gateTele1 = new GateToGateTeleport(0, 1, 0);
		GateToGateTeleport gateTele2 = new GateToGateTeleport(9, 0, 1, gateTele1);
		GateToGateTeleport gateTele3 = new GateToGateTeleport(16, 4, 3, gateTele1);
		gateTele1.setDestinationTelelportGate(gateTele3);
		teleportGates.add(gateTele1);
		teleportGates.add(gateTele2);
		teleportGates.add(gateTele3);
		teleportGates.add(new TeleportToArea(10, 4, 1, 0, 2, 0));

		floorSwitches.add(new FloorSwitch(1, 0, 0, false, 20));
		floorSwitches.add(new FloorSwitch(4, 0, 0, false, 10));

		Gate gate1 = new Gate(1, 3, 0);
		GateController gateController = new GateController(floorSwitches, new int[] { 0, 1, 0, 1 }, gate1);
		TeleportGateController teleController = new TeleportGateController(floorSwitches, new int[] { 0, 1, 0, 1 },
				gateTele3);
		BlockController bController = new BlockController(floorSwitches, new int[] { 0, 1, 0, 1 }, blocks.get(8),
				BlockController.MOVE_DOWN_TYPE, 1);
		switchController.add((SwitchController) gateController);
		switchController.add((SwitchController) teleController);
		switchController.add((SwitchController) bController);
		gates.add(gate1);

		for (Slope eachSlope : slopes) {

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

		for (TeleportGate eachTeleport : teleportGates) {
			ObjectMap.drawableObjectHashMap
					.put(new ObjectVector(eachTeleport.getX(), eachTeleport.getY(), eachTeleport.getZ()), eachTeleport);
		}

		for (Block eachBlock : blocks) {
			ObjectMap.drawableObjectHashMap.put(new ObjectVector(eachBlock.getX(), eachBlock.getY(), eachBlock.getZ()),
					eachBlock);
		}

		for (FloorSwitch eachSwitch : floorSwitches) {
			ObjectMap.drawableObjectHashMap.put(eachSwitch.getObjectVectorWithName(), eachSwitch);
		}

		for (Gate eachGate : gates) {
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

		camera1.update(step);
		camera2.update(step);
		player1.update(step, camera1.getRotation());
		player2.update(step, camera2.getRotation());

		for (FloorSwitch fs : floorSwitches) {
			fs.update(step);
		}
		for (TeleportGate teleGate : teleportGates) {
			teleGate.update(step);
		}
		for (Slope slope : slopes) {
			slope.update(step, camera1);
		}
		for (SwitchController eachController : switchController) {
			eachController.update();
		}
		for (Gate each : gates) {
			each.update(step);
		}
		for (Block each : blocks) {
			each.update(step);
		}

	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {

		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		Camera[] cameraList = new Camera[] { camera1, camera2 };

		for (int i = 0; i < cameraList.length; i++) {
			Camera camera = cameraList[i];

			camera.setSceneSize(sceneWidth / cameraList.length, sceneHeight);

			Block.refreshDrawCache(camera);
			FloorPiece.refreshDrawCache(camera);

			AffineTransform oldTransform = g.getTransform();
			Rectangle oldClip = g.getClipBounds();

			g.setClip(sceneWidth * i / cameraList.length, 0, sceneWidth / cameraList.length, sceneHeight);
			g.translate(sceneWidth * i / cameraList.length, 0);

			LevelRenderer.draw(ObjectMap.drawableObjectHashMap.values(), g, camera);

			g.setTransform(oldTransform);
			g.setClip(oldClip);

			if (i > 0) {
				g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.BACKGROUND, 1));
				g.setStroke(new BasicStroke(9));
				g.drawLine(sceneWidth * i / cameraList.length, 0, sceneWidth * i / cameraList.length, sceneHeight);
				
				g.setColor(ColorSwatch.SHADOW);
				g.setStroke(new BasicStroke(5));
				g.drawLine(sceneWidth * i / cameraList.length, 0, sceneWidth * i / cameraList.length, sceneHeight);
			}

		}

	}

}
