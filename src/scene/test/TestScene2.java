package scene.test;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import geom.Vector2;
import objectInterface.IDrawable;
import scene.core.Scene;
import util.Resource;
import util.Constants.ColorSwatch;

public class TestScene2 extends Scene {

	private Camera camera;
	private Player player1;

	ArrayList<Block> blocks = new ArrayList<>();
	ArrayList<FloorSwitch> floorSwitches = new ArrayList<>();
	
	public TestScene2() {
		ObjectMap.drawableObjectHashMap = new HashMap<String , IDrawable>();
		player1 = new Player("Player1");
		camera = new Camera(player1);

		floorSwitches.add(new FloorSwitch(6, 1, 0, false, 5));
		floorSwitches.add(new FloorSwitch(6, 3, 0, false, 10));
		floorSwitches.add(new FloorSwitch(6, 5, 0, false, 20));
		floorSwitches.add(new FloorSwitch(6, 7, 0, true, 10));
		floorSwitches.add(new FloorSwitch(6, 9, 0, true, 20));
		
//		blocks.add(new Block(0, 2, 0));
//		blocks.add(new Block(2, 0, 0));	
//		blocks.add(new Block(-9, 2, 0, 110, true));
//		blocks.add(new Block(-10, 3, 0, 110, true));
//		
		blocks.add(new Block(-2, 3, 0, 20, false));
		blocks.add(new Block(-2, 2, 0, 20, true));
		blocks.add(new Block(-2, 1, 0, 20, true));
		blocks.add(new Block(-2, 0, 0, 20, true));
		blocks.add(new Block(-2, -1, 0, 20, true));
		blocks.add(new Block(-1, -2, 0, 20, true));
		blocks.add(new Block(0, -3 ,0, 20, true));
		blocks.add(new Block(1, -2, 0, 20, true));
		blocks.add(new Block(2, -1, 0, 20, true));
		blocks.add(new Block(2, 0, 0, 20, true));
		blocks.add(new Block(2, 1, 0, 20, true));
		blocks.add(new Block(2, 2, 0, 20, true));
		blocks.add(new Block(2, 3, 0, 20, true));
		
		blocks.add(new Block(-1, 1, 0, 110, true));
		blocks.add(new Block(0, 1, 0, 110, true));
		blocks.add(new Block(1, 1, 0, 110, true));
		
		for(Block eachBlock : blocks) {
			ObjectMap.drawableObjectHashMap.put(eachBlock.getX() +" "+eachBlock.getY() + " " + eachBlock.getZ(), eachBlock);
		}
		ObjectMap.drawableObjectHashMap.put(player1.getCellX()+ " " + player1.getCellY() + " " + player1.getCellZ() + " " + player1.getName(), player1);
	}

	@Override
	public void update(int step) {

		camera.update(step);
		player1.update(step, camera.getRotation());
		
		for (FloorSwitch fs : floorSwitches) {
			fs.update(step, player1);
		}

	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {

		camera.setSceneSize(sceneWidth, sceneHeight);

		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		// Draw class name
		g.setColor(ColorSwatch.SHADOW);
		g.setFont(Resource.getInstance().getDefaultFont(50));
		g.drawString(this.getClass().getName(), 20, 20 + g.getFontMetrics().getAscent());

		// Draw grid line

		g.setStroke(new BasicStroke(1));
		g.setColor(ColorSwatch.SHADOW);
		for (int i = -9; i <= 10; i++) {
			Vector2 startPos = camera.getDrawPosition(i - 0.5f, -10 - 0.5f, 0);
			Vector2 endPos = camera.getDrawPosition(i - 0.5f, 10 + 0.5f, 0);
			g.draw(new Line2D.Float(startPos.getX(), startPos.getY(), endPos.getX(), endPos.getY()));

			Vector2 startPos2 = camera.getDrawPosition(-10 - 0.5f, i - 0.5f, 0);
			Vector2 endPos2 = camera.getDrawPosition(10 + 0.5f, i - 0.5f, 0);
			g.draw(new Line2D.Float(startPos2.getX(), startPos2.getY(), endPos2.getX(), endPos2.getY()));
		}
		g.setStroke(new BasicStroke(3));
		// Draw floor border
		int[] x = new int[4];
		int[] y = new int[4];

		Vector2[] v = new Vector2[4];
		v[0] = camera.getDrawPosition(-10.5f, -10.5f, 0);
		v[1] = camera.getDrawPosition(-10.5f, 10.5f, 0);
		v[2] = camera.getDrawPosition(10.5f, 10.5f, 0);
		v[3] = camera.getDrawPosition(10.5f, -10.5f, 0);

		for (int k = 0; k < 4; k++) {
			x[k] = (int) v[k].getX();
			y[k] = (int) v[k].getY();
		}

		g.drawPolygon(x, y, 4);

		// Draw things

		player1.draw(g, camera);

		for (Block b : blocks) {
			b.draw(g, camera);
		}
		
		for (FloorSwitch fs : floorSwitches) {
			fs.draw(g, camera);
		}
	}

}
