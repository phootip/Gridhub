package stage;

import java.awt.Graphics2D;
import java.util.HashMap;

import stage.gameobj.IDrawable;

/**
 * This class represents the floorLevel (Z value) for each Scene or Stage
 * 
 * 
 * @author Thanat Jatuphattharachat
 *
 */

public class FloorLevel implements IDrawable {

	private int[][] floorLevelMap;
	private int sizeX;
	private int sizeY;

	/**
	 * Get size of map in x-axis.
	 * @return Integer representing size of map in x-axis.
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 * Get size of map in y-axis.
	 * @return Integer representing size of map in y-axis.
	 */
	public int getSizeY() {
		return sizeY;
	}

	/**
	 * Instantiate the new FloorLevel Object.
	 * 
	 * @param gridSizeX
	 *            the size of map in x-axis
	 * @param gridSizeY
	 *            the size of map in y-axis
	 */
	public FloorLevel(int gridSizeX, int gridSizeY) {
		floorLevelMap = new int[gridSizeX][gridSizeY];
		sizeX = gridSizeX;
		sizeY = gridSizeY;
	}

	public int[][] getFloorMap() {
		return floorLevelMap;
	}

	public int getZValueFromXY(int x, int y) {
		if (x >= floorLevelMap.length || x < 0 || y >= floorLevelMap[0].length || y < 0)
			return 0;
		return floorLevelMap[x][y];
	}

	public void setZValue(int x, int y, int z) {
		if (x >= sizeX || x < 0 || y >= sizeY || y < 0 || z < 0)
			return;
		else
			floorLevelMap[x][y] = z;
	}

	public boolean isOutOfMap(int x, int y) {
		return x >= sizeX || x < 0 || y > sizeY || y < 0;
	}

	/**
	 * draw floor grid line in the scene
	 */
	@Override
	public void draw(Graphics2D g, Camera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getDrawX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDrawY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDrawZ() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
