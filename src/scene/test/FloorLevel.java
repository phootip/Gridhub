package scene.test;

import java.awt.Graphics2D;
import java.util.HashMap;

import objectInterface.IDrawable;
import sun.security.jca.GetInstance.Instance;

/**
 * This class represents the floorLevel(Z value) for each Scene or Stage
 * 
 * 
 * @author Thanat Jatuphattharachat
 *
 */

public class FloorLevel implements IDrawable {

	private static int[][] floorLevelMap;
	private int maxX;
	private int maxY;

	/**
	 * instantiate the FloorLevel Object.Please note that the maximum x value and maximum y value is 2* gridSize +1 because it must conatain
	 * both negative and non-negative value of x y and 0 in the middle
	 * 
	 * @param gridSize
	 */
	public FloorLevel(int gridSize) {
		floorLevelMap = new int[2 * gridSize+1][2 * gridSize+1];
	}

	public static int[][] getFloorMap() {
		return floorLevelMap;
	}

	public int getZValueFromXY(int x, int y) {
		if (x >= floorLevelMap.length || x < 0 || y >= floorLevelMap[0].length || y < 0)
			return 0;
		return floorLevelMap[x][y];
	}

	public void setZValue(int x, int y, int z) {
		if (x >= floorLevelMap.length || x < 0 || y >= floorLevelMap[0].length || y < 0 || z < 0)
			return;
		else
			floorLevelMap[x][y] = z;
	}

	/**
	 * this object does not contain individual x y z cell. Therefore getCell{@Value} will return 0 instead
	 * 
	 */
	@Override
	public int getCellX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCellY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCellZ() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * draw floor grid line in the scene
	 */
	@Override
	public void draw(Graphics2D g, Camera camera) {
		// TODO Auto-generated method stub

	}
}
