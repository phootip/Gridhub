package stage;

import java.util.ArrayList;
import java.util.List;

import stage.gameobj.FloorPiece;

/**
 * This class represents the floor level (Z value) for each {@link GameStage}.
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

	/**
	 * Create the list of {@link FloorPiece}s of this {@link FloorLevel} object.
	 * 
	 * @return List of {@link FloorPiece}s.
	 */
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

	/**
	 * Get the internal floor map. Note that editing this array may cause a bug, so edit with caution. Using method
	 * {@link #getZValueFromXY(int, int)} and {@link #setZValue(int, int, int)} is more preferable.
	 * 
	 * @return A 2-dimensionally array representing a floor map, with the first index as x-axis and second index as
	 *         y-axis.
	 * @see FloorLevel#getZValueFromXY(int, int)
	 * @see FloorLevel#setZValue(int, int, int)
	 */
	public int[][] getFloorMap() {
		return floorLevelMap;
	}

	/**
	 * Get the z value of floor in the specified position.
	 * 
	 * @param x
	 *            the x position of the floor
	 * @param y
	 *            the y position of the floor
	 * @return The z value of the floor at the specified position, or return {@code 0} if the requested position is
	 *         invalid, e.g. outside the map.
	 * @see #setZValue(int, int, int)
	 */
	public int getZValueFromXY(int x, int y) {
		if (x >= floorLevelMap.length || x < 0 || y >= floorLevelMap[0].length || y < 0)
			return 0;
		return floorLevelMap[x][y];
	}

	/**
	 * Set the z value of floor in the specified position.
	 * 
	 * @param x
	 *            the x position of the floor
	 * @param y
	 *            the y position of the floor
	 * @param z
	 *            the z value of the floor to set
	 * @see #getZValue(int, int)
	 */
	public void setZValue(int x, int y, int z) {
		if (x > sizeX - 1 || x < 0 || y > sizeY - 1 || y < 0 || z < 0)
			return;
		else
			floorLevelMap[x][y] = z;
	}

	/**
	 * Determines whether the specified position is outside the map or not.
	 * 
	 * @param x
	 *            the x position of the floor
	 * @param y
	 *            the y position of the floor
	 * @return Whether or not the specified position is outside the level map.
	 */
	public boolean isOutOfMap(int x, int y) {
		return x > sizeX - 1 || x < 0 || y > sizeY - 1 || y < 0;
	}
	
	

}
