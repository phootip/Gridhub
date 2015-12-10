package scene.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.org.apache.bcel.internal.Constants;

import core.geom.Vector2;
import core.renderer.LevelRenderer;
import objectInterface.IDrawable;
import scene.core.Scene;
import util.Resource;
import util.Constants.ColorSwatch;

public class TestScene2 extends Scene {

	private Camera camera;
	private Player player1;
	private Player player2;
	private int gridSize;
	private FloorLevel floorLevelMap;
	ArrayList<Block> blocks = new ArrayList<>();
	ArrayList<FloorSwitch> floorSwitches = new ArrayList<>();
	ArrayList<Slope> slopes = new ArrayList<>();

	public TestScene2(int gridSize) {
		ObjectMap.drawableObjectHashMap = new HashMap<String, IDrawable>();
		this.gridSize = gridSize;

		FloorLevel.getInstance().newFloorLevelMap(gridSize);
		int floorMapXSize = FloorLevel.getInstance().getFloorMap().length;
		int floorMapYSize = FloorLevel.getInstance().getFloorMap()[0].length;

		for (int i = 0; i < floorMapXSize; i++) {
			for (int j = 0; j < floorMapYSize; j++) {
				if (i > 23) {
					FloorLevel.getInstance().setZValue(i, j, 2);
				} else if (i > 20) {
					FloorLevel.getInstance().setZValue(i, j, 1);
				} else {
					FloorLevel.getInstance().setZValue(i, j, 0);
				}
			}
		}
//		map floor checking
//		for (int j = 0; j < floorMapYSize; j++) {
//			for (int i = 0; i < floorMapXSize; i++) {
//				System.out.print(FloorLevel.getInstance().getZValueFromXY(i, j));
//				if(i == floorMapXSize - 1) System.out.println();
//				
//			}
//		}
		player1 = new Player(util.Constants.PLAYER1_ID);
		player2 = new Player(util.Constants.PLAYER2_ID);
		camera = new Camera(player1);

		floorSwitches.add(new FloorSwitch(6, 1, 0, false, 5));
		floorSwitches.add(new FloorSwitch(6, 3, 0, false, 10));
		floorSwitches.add(new FloorSwitch(6, 5, 0, false, 20));
		floorSwitches.add(new FloorSwitch(6, 7, 0, true, 10));
		floorSwitches.add(new FloorSwitch(6, 9, 0, true, 20));

		slopes.add(new Slope(6, 10, 0, 1));
		
		
		slopes.add(new Slope(6, 5, 0, 1));
		slopes.add(new Slope(9,10,1,1));
		slopes.add(new Slope(-2, 3, 0, 1));
		
		
		for (Slope eachSlope : slopes) {

			int slopeStartX = eachSlope.getStartX();
			int slopeStartY = eachSlope.getStartY();
			int slopeStartZ = eachSlope.getStartZ();
			int slopeEndZ = eachSlope.getEndZ();
			int slopeEndX = eachSlope.getEndX();
			int slopeEndY = eachSlope.getEndY();
			int xBar = (slopeStartX + slopeEndX) / 2;
			int yBar = (slopeStartY + slopeEndY) / 2;

			ObjectMap.drawableObjectHashMap.put(slopeStartX + " " + slopeStartY + " " + slopeStartZ, eachSlope);
			ObjectMap.drawableObjectHashMap.put(xBar + " " + yBar + " " + slopeStartZ, eachSlope);
			ObjectMap.drawableObjectHashMap.put(slopeEndX + " " + slopeEndY + " " + slopeEndZ, eachSlope);

		}
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
		
		blocks.add(new Block(2, 3, 0, 20, true));
		blocks.add(new Block(2, 4, 0, 20, true));
		blocks.add(new Block(2, 5, 0, 20, true));
		blocks.add(new Block(2, 7, 0, 20, true));
		blocks.add(new Block(2, 8, 0, 20, true));
		blocks.add(new Block(2, 9, 0, 20, true));
		blocks.add(new Block(2, 10, 0, 20, true));
		
		blocks.add(new Block(1, 3, 0, 20, true));
		blocks.add(new Block(2, 3, 1, 20, true));
		blocks.add(new Block(3, 3, 0, 20, true));
		blocks.add(new Block(4, 3, 0, 20, true));
		blocks.add(new Block(5, 3, 0, 20, true));
		blocks.add(new Block(6, 3, 0, 20, true));
		blocks.add(new Block(7, 3, 0, 20, true));
		blocks.add(new Block(8, 3, 0, 20, true));

		// blocks.add(new Block(-1, 1, 0, 110, true));
		// blocks.add(new Block(0, 1, 0, 110, true));
		// blocks.add(new Block(1, 1, 0, 110, true));

//		for (int i = -15; i <= 15; i++) {
//			for (int j = -15; j <= 15; j++) {
//				if (i >= -2 && j >= -2 && i <= 2 && j <= 2)
//					continue;
//				blocks.add(new Block(i, j + 1, 0, 1, true));
//			}
//		}

		for (Block eachBlock : blocks) {
			ObjectMap.drawableObjectHashMap.put(eachBlock.getX() + " " + eachBlock.getY() + " " + eachBlock.getZ(),
					eachBlock);
		}
		// ObjectMap.drawableObjectHashMap.put(
		// player1.getCellX() + " " + player1.getCellY() + " " + player1.getCellZ() + " " + player1.getName(),
		// player1);
	}

	@Override
	public void update(int step) {

		camera.update(step);
		player1.update(step, camera.getRotation());
		player2.update(step, camera.getRotation());

		for (FloorSwitch fs : floorSwitches) {
			fs.update(step, player1);
		}

	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {

		camera.setSceneSize(sceneWidth, sceneHeight);

		Block.refreshDrawCache(camera);

		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		// Draw grid line

		g.setStroke(new BasicStroke(1));
		g.setColor(ColorSwatch.SHADOW);
		for (int i = -gridSize + 1; i <= gridSize; i++) {
			Vector2 startPos = camera.getDrawPosition(i - 0.5f, -gridSize - 0.5f, 0);
			Vector2 endPos = camera.getDrawPosition(i - 0.5f, gridSize + 0.5f, 0);
			g.drawLine(startPos.getIntX(), startPos.getIntY(), endPos.getIntX(), endPos.getIntY());

			Vector2 startPos2 = camera.getDrawPosition(-gridSize - 0.5f, i - 0.5f, 0);
			Vector2 endPos2 = camera.getDrawPosition(gridSize + 0.5f, i - 0.5f, 0);
			g.drawLine(startPos2.getIntX(), startPos2.getIntY(), endPos2.getIntX(), endPos2.getIntY());
		}
		g.setStroke(new BasicStroke(3));
		// Draw floor border
		int[] x = new int[4];
		int[] y = new int[4];

		Vector2[] v = new Vector2[4];
		v[0] = camera.getDrawPosition(-gridSize - 0.5f, -gridSize - 0.5f, 0);
		v[1] = camera.getDrawPosition(-gridSize - 0.5f, gridSize + 0.5f, 0);
		v[2] = camera.getDrawPosition(gridSize + 0.5f, gridSize + 0.5f, 0);
		v[3] = camera.getDrawPosition(gridSize + 0.5f, -gridSize - 0.5f, 0);

		for (int k = 0; k < 4; k++) {
			x[k] = (int) v[k].getX();
			y[k] = (int) v[k].getY();
		}

		g.drawPolygon(x, y, 4);

		// Draw things

		/*
		 * player1.draw(g, camera); player2.draw(g, camera);
		 * 
		 * for (Block b : blocks) { b.draw(g, camera); }
		 * 
		 * for (FloorSwitch fs : floorSwitches) { fs.draw(g, camera); }
		 */

		LevelRenderer.draw(ObjectMap.drawableObjectHashMap.values(), g, camera);

		// Drawing slope
		for (int i = 0; i < slopes.size(); i++) {
			slopes.get(i).draw(g, camera);
		}

<<<<<<< HEAD
		// int slopeStartX = 10;
		// int slopeStartY = 10;
		// int slopeStartZ = 0;
=======
		// int slopeStartX = slopes.get(0).getStartX();
		// int slopeStartY = slopes.get(0).getStartY();
		// int slopeStartZ = slopes.get(0).getStartZ();
>>>>>>> origin/PlayerMovement
		//
		// Vector2 startV1 = camera.getDrawPosition(slopeStartX - 0.5f, slopeStartY + 0.5f, slopeStartZ);
		// Vector2 endV1 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY + 0.5f, slopeStartZ + 1);
		// Vector2 startV2 = camera.getDrawPosition(slopeStartX - 0.5f, slopeStartY - 0.5f, slopeStartZ);
		// Vector2 endV2 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY - 0.5f, slopeStartZ + 1);
		//
		// Vector2 mid1 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY + 0.5f, slopeStartZ);
		// Vector2 mid2 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY - 0.5f, slopeStartZ);
		//
		// g.setStroke(new BasicStroke(3));
		// g.setColor(ColorSwatch.FOREGROUND);
		// g.drawLine((int) startV1.getX(), (int) startV1.getY(), (int) endV1.getX(), (int) endV1.getY());
		// g.drawLine((int) startV2.getX(), (int) startV2.getY(), (int) endV2.getX(), (int) endV2.getY());
		// g.drawLine((int) startV1.getX(), (int) startV1.getY(), (int) startV2.getX(), (int) startV2.getY());
		// g.drawLine((int) endV1.getX(), (int) endV1.getY(), (int) endV2.getX(), (int) endV2.getY());
		//
		// g.drawLine((int) startV1.getX(), (int) startV1.getY(), (int) mid1.getX(), (int) mid1.getY());
		// g.drawLine((int) startV2.getX(), (int) startV2.getY(), (int) mid2.getX(), (int) mid2.getY());
		// g.drawLine((int) mid1.getX(), (int) mid1.getY(), (int) endV1.getX(), (int) endV1.getY());
		// g.drawLine((int) mid2.getX(), (int) mid2.getY(), (int) endV2.getX(), (int) endV2.getY());
		// g.drawLine((int) mid1.getX(), (int) mid1.getY(), (int) mid2.getX(), (int) mid2.getY());
	}

}
