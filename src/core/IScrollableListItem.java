package core;

import java.awt.Graphics2D;

/**
 * An interface to specified the item of {@link ScrollableUIList} that it can be drawn onto screen.
 * 
 * @author Kasidit Iamthong
 *
 */
public abstract interface IScrollableListItem {

	/**
	 * Get the height of the list item.
	 * 
	 * @return the height of the list item.
	 */
	public abstract int getListItemHeight();

	/**
	 * Draw the item content onto the specified {@link Graphics2D}. Usually called by {@link ScrollableUIList} when it
	 * needs drawing.
	 * 
	 * @param g
	 *            the specified {@link Graphics2D} object.
	 * @param x
	 *            the left value of the paintable area.
	 * @param y
	 *            the top value of the paintable area.
	 * @param width
	 *            the width of the paintable area.
	 * @param isSelected
	 *            determines whether current item is selected.
	 */
	public abstract void drawListItemContent(Graphics2D g, int x, int y, int width, boolean isSelected);
}
