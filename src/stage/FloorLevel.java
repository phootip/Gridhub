package stage;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stage.gameobj.FloorPiece;
import stage.gameobj.IDrawable;

/**
 * This class represents the floorLevel (Z value) for each Scene or Stage
 * 
 * 
 * @author Thanat Jatuphattharachat
 *
 */

public class FloorLevel {

	private int[][] floorLevelMap;
	private int sizeX;
	private int sizeY;

	/**
	 * Get size of map in x-axis.
	 * 
	 * @return Integer representing size of map in x-axis.
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 * Get size of map in y-axis.
	 * 
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

	public List<FloorPiece> getFloorPieces() {
		ArrayList<FloorPiece> pieceList = new ArrayList<>();

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				int z = getZValueFromXY(i, j);
				boolean showPXBorder = isOutOfMap(i + 1, j) || (getZValueFromXY(i + 1, j) != z);
				boolean showPYBorder = isOutOfMap(i, j + 1) || (getZValueFromXY(i, j + 1) != z);
				boolean showNXBorder = isOutOfMap(i - 1, j) || (getZValueFromXY(i - 1, j) != z);
				boolean showNYBorder = isOutOfMap(i, j - 1) || (getZValueFromXY(i, j - 1) != z);
				pieceList.add(new FloorPiece(i, j, z, showPXBorder, showPYBorder, showNXBorder, showNYBorder));
			}
		}

		return pieceList;
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

}
