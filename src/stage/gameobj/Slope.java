package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import core.geom.Vector2;
import stage.Camera;
import util.Constants.ColorSwatch;

/**
 * This class represents slope Object.
 * 
 * @author Thanat Jatuphattharachat
 *
 */

public class Slope implements IDrawable {
	private int startX;
	private int startY;
	private int startZ;
	private int endZ;
	private int endX;
	private int endY;
	private boolean isAlignX;
	private boolean isAlignY;

	/**
	 * Constant representing the slope going toward right direction (+x).
	 */
	public static final int ALIGNMENT_RIGHT = 1;
	/**
	 * Constant representing the slope going toward down direction (+y).
	 */
	public static final int ALIGNMENT_DOWN = 2;
	/**
	 * Constant representing the slope going toward left direction (-x).
	 */
	public static final int ALIGNMENT_LEFT = -1;
	/**
	 * Constant representing the slope going toward up direction (-y).
	 */
	public static final int ALIGNMENT_UP = -2;

	/**
	 * Create Slope with specified position and alignment.
	 * 
	 * @param startX
	 *            the starting x position of Slope
	 * @param startY
	 *            the starting y position of Slope
	 * @param alignment
	 *            Possible value are {@link #ALIGNMENT_RIGHT}, {@link #ALIGNMENT_DOWN}, {@link #ALIGNMENT_LEFT}, and
	 *            {@link #ALIGNMENT_UP}.
	 */
	public Slope(int startX, int startY, int startZ, int alignment) {
		super();
		this.startX = startX;
		this.startY = startY;
		switch (alignment) {
			case ALIGNMENT_RIGHT:
				endX = startX + 2;
				endY = startY;
				break;
			case ALIGNMENT_DOWN:
				endX = startX;
				endY = startY + 2;
			case ALIGNMENT_LEFT:
				endX = startX - 2;
				endY = startY;
			case ALIGNMENT_UP:
				endX = startX;
				endY = startY - 2;

			default:
				throw new IllegalArgumentException("Invalid Slope alignment : " + alignment);
		}
		this.startZ = startZ;
		endZ = startZ;
		isAlignX = alignment == ALIGNMENT_RIGHT || alignment == ALIGNMENT_LEFT;
		isAlignY = alignment == ALIGNMENT_DOWN || alignment == ALIGNMENT_UP;
	}

	/**
	 * This method check the x alignment of the Slope
	 * 
	 * @return whether the slope alignment is X Direction
	 */
	public boolean isAlignX() {
		return isAlignX;
	}

	/**
	 * This method check the Y alignment of the Slope
	 * 
	 * @return whether the slope alignment is Y Direction
	 */
	public boolean isAlignY() {
		return isAlignY;
	}

	/**
	 * This method check whether the x y postion is The entrance of the slope
	 * 
	 * @param x
	 *            x position needed to be checked
	 * @param y
	 *            y Position needed to be checked
	 * @return whether it is Slope Entrance
	 */
	public boolean isSlopeEntrance(int x, int y) {
		return x == startX && y == startY;
	}

	/**
	 * This method check whether the x y postion is The exit of the slope
	 * 
	 * @param x
	 *            x position needed to be checked
	 * @param y
	 *            y Position needed to be checked
	 * @return whether it is Slope Exit
	 */
	public boolean isSlopeExit(int x, int y) {
		return x == endX && y == startY;
	}

	/**
	 * This method check whether the position x y is on the Slope
	 * 
	 * @param x
	 *            x position needed to be checked
	 * @param y
	 *            y position needed to be checked
	 * @return whether it is on the Slope
	 */
	public boolean isObjectOnSlope(int x, int y) {
		return x >= startX && x <= endX && y >= startY && y <= endY;
	}

	/**
	 * get the start position of x
	 * 
	 * @return initial x position of the Slope
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * get the start position of y
	 * 
	 * @return initial y position of the Slope
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * get the start position of z
	 * 
	 * @return initial z position of the Slope
	 */
	public int getStartZ() {
		return startZ;
	}

	/**
	 * get the end position of z
	 * 
	 * @return Endpoint of z position of the Slope
	 */
	public int getEndZ() {
		return endZ;
	}

	/**
	 * get the end position of x
	 * 
	 * @return Endpoint of x position of the Slope
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * get the end position of y
	 * 
	 * @return Endpoint of y position of the Slope
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * draw the object slope in the scene
	 */
	@Override
	public void draw(Graphics2D g, Camera camera) {
		// TODO Auto-generated method stub
		int slopeStartX = startX;
		int slopeStartY = startY;
		int slopeStartZ = startZ;

		Vector2 startV1 = camera.getDrawPosition(slopeStartX - 0.5f, slopeStartY + 0.5f, slopeStartZ);
		Vector2 endV1 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY + 0.5f, slopeStartZ + 1);
		Vector2 startV2 = camera.getDrawPosition(slopeStartX - 0.5f, slopeStartY - 0.5f, slopeStartZ);
		Vector2 endV2 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY - 0.5f, slopeStartZ + 1);

		Vector2 mid1 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY + 0.5f, slopeStartZ);
		Vector2 mid2 = camera.getDrawPosition(slopeStartX + 2.5f, slopeStartY - 0.5f, slopeStartZ);

		g.setStroke(new BasicStroke(3));
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawLine((int) startV1.getX(), (int) startV1.getY(), (int) endV1.getX(), (int) endV1.getY());
		g.drawLine((int) startV2.getX(), (int) startV2.getY(), (int) endV2.getX(), (int) endV2.getY());
		g.drawLine((int) startV1.getX(), (int) startV1.getY(), (int) startV2.getX(), (int) startV2.getY());
		g.drawLine((int) endV1.getX(), (int) endV1.getY(), (int) endV2.getX(), (int) endV2.getY());

		g.drawLine((int) startV1.getX(), (int) startV1.getY(), (int) mid1.getX(), (int) mid1.getY());
		g.drawLine((int) startV2.getX(), (int) startV2.getY(), (int) mid2.getX(), (int) mid2.getY());
		g.drawLine((int) mid1.getX(), (int) mid1.getY(), (int) endV1.getX(), (int) endV1.getY());
		g.drawLine((int) mid2.getX(), (int) mid2.getY(), (int) endV2.getX(), (int) endV2.getY());
		g.drawLine((int) mid1.getX(), (int) mid1.getY(), (int) mid2.getX(), (int) mid2.getY());
	}

	@Override
	public float getDrawX() {
		return startX;
	}

	@Override
	public float getDrawY() {
		return startY;
	}

	@Override
	public float getDrawZ() {
		return startZ;
	}

}
