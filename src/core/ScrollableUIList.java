package core;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;

public class ScrollableUIList extends ArrayList<IScrollableListItem> {
	private static final int SCROLL_LEFT_AREA = 100;
	private static final long serialVersionUID = 1L;

	public ScrollableUIList() {
	}

	private int getTotalHeight() {
		int totalHeight = 0;
		for (IScrollableListItem item : this) {
			totalHeight += item.getHeight();
		}
		return totalHeight;
	}

	private int selectedItemIndex = 0;
	private float currentScrollPosition = 0;
	private float preferredPosition = 0;

	public IScrollableListItem getSelectedItem() {
		return this.get(selectedItemIndex);
	}

	/**
	 * Select next item in list if exists.
	 */
	public void selectNextItem() {
		if (selectedItemIndex + 1 < this.size()) {
			selectedItemIndex++;
		}
	}

	/**
	 * Select previous item in list if exists.
	 */
	public void selectPreviousItem() {
		if (selectedItemIndex > 0) {
			selectedItemIndex--;
		}
	}

	private boolean focusing = false;

	/**
	 * @return Whether this list is focusing or not.
	 */
	public boolean isFocusing() {
		return focusing;
	}

	/**
	 * Set the value whether or not this list is focusing.
	 * 
	 * @param focusing
	 *            the focusing to set
	 */
	public void setFocusing(boolean focusing) {
		this.focusing = focusing;
	}

	public void update(int step) {
		currentScrollPosition += (preferredPosition - currentScrollPosition) / Math.pow(4, 100f / step);
	}

	private void calculatePreferredPosition(int height) {

		preferredPosition = currentScrollPosition;
		float upperY = 0, lowerY = 0;

		for (int i = 0; i < selectedItemIndex; i++) {
			upperY += this.get(i).getHeight();
		}
		lowerY = upperY + this.get(selectedItemIndex).getHeight();

		if (upperY - currentScrollPosition < 100) {
			preferredPosition = upperY - 100;
		}
		if (lowerY - currentScrollPosition > height - SCROLL_LEFT_AREA) {
			preferredPosition = lowerY + 100 - height;
		}
		preferredPosition = Math.max(0, Math.min(getTotalHeight() - height, preferredPosition));
		
	}

	public void draw(Graphics2D g, int x, int y, int width, int height) {
		calculatePreferredPosition(height);

		Rectangle oldClipBound = g.getClipBounds();
		g.setClip(x, y, width, height);

		int cumulativeY = -(int) currentScrollPosition;
		for (int i = 0; i < this.size(); i++) {
			IScrollableListItem item = this.get(i);

			if (i == selectedItemIndex) {
				g.setColor(ColorSwatch.FOREGROUND);
				g.fillRect(x, y + cumulativeY, width, item.getHeight());
			}

			item.drawContent(g, x, y + cumulativeY, width, i == selectedItemIndex);
			cumulativeY += item.getHeight();
		}

		g.setClip(oldClipBound);

		g.setStroke(Resource.getGameObjectThinStroke());
		g.setColor(ColorSwatch.FOREGROUND);
		// g.drawRect(x, y, width, height);
	}

}
