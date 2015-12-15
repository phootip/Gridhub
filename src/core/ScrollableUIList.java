package core;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;

public class ScrollableUIList extends ArrayList<IScrollableListItem> {
	private static final int INITIAL_SCROLL_POSITION = 20;
	private static final int VERTICAL_GAP = 20;
	private static final int SCROLL_LEFT_AREA = 100;
	private static final long serialVersionUID = 1L;

	public ScrollableUIList() {
	}

	private int getTotalHeight() {
		int totalHeight = 0;
		for (IScrollableListItem item : this) {
			totalHeight += item.getListItemHeight();
		}
		return totalHeight;
	}

	private int selectedItemIndex = 0;
	private float currentScrollPosition = INITIAL_SCROLL_POSITION;
	private float preferredPosition = 0;

	public IScrollableListItem getSelectedItem() {
		if (this.size() == 0) {
			return null;
		}
		return this.get(selectedItemIndex);
	}

	/**
	 * Select next item in list if exists.
	 * 
	 * @return Whether there is a next item to select.
	 */
	public boolean selectNextItem() {
		if (selectedItemIndex + 1 < this.size()) {
			selectedItemIndex++;
			return true;
		}
		return false;
	}

	/**
	 * Select previous item in list if exists.
	 * 
	 * @return Whether there is a previous item to select.
	 */
	public boolean selectPreviousItem() {
		if (selectedItemIndex > 0) {
			selectedItemIndex--;
			return true;
		}
		return false;
	}

	public void resetSelectedItemIndex() {
		this.selectedItemIndex = 0;
//		this.currentScrollPosition = INITIAL_SCROLL_POSITION;
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

		if (focusing) {
			focusingAnimStep += step;
			if (focusingAnimStep > focusingAnimLength) {
				focusingAnimStep = focusingAnimLength;
			}
		} else {
			focusingAnimStep -= step;
			if (focusingAnimStep < 0) {
				focusingAnimStep = 0;
			}
		}
	}

	private void calculatePreferredPosition(int height) {

		if (this.size() == 0) {
			preferredPosition = 0;
			return;
		}

		preferredPosition = currentScrollPosition;
		float upperY = 0, lowerY = 0;

		for (int i = 0; i < selectedItemIndex; i++) {
			upperY += this.get(i).getListItemHeight();
		}
		lowerY = upperY + this.get(selectedItemIndex).getListItemHeight();

		if (upperY - currentScrollPosition < 100) {
			preferredPosition = upperY - 100;
		}
		if (lowerY - currentScrollPosition > height - SCROLL_LEFT_AREA) {
			preferredPosition = lowerY + 100 - height;
		}
		preferredPosition = Math.max(-VERTICAL_GAP,
				Math.min(getTotalHeight() - height + VERTICAL_GAP, preferredPosition));
	}

	private int focusingAnimStep = 0;
	private final int focusingAnimLength = 100 * 5;

	public void draw(Graphics2D g, int x, int y, int width, int height) {
		calculatePreferredPosition(height);

		Rectangle oldClipBound = g.getClipBounds();
		g.setClip(x, y, width, height);

		int cumulativeY = -(int) currentScrollPosition;
		for (int i = 0; i < this.size(); i++) {
			IScrollableListItem item = this.get(i);

			if (i == selectedItemIndex) {
				g.setColor(ColorSwatch.FOREGROUND);
				g.fillRect(x, y + cumulativeY, width, item.getListItemHeight());
			}

			item.drawListItemContent(g, x, y + cumulativeY, width, i == selectedItemIndex);
			cumulativeY += item.getListItemHeight();
		}

		g.setClip(oldClipBound);

		g.setColor(Helper.blendColor(ColorSwatch.SHADOW, ColorSwatch.FOREGROUND,
				(float) focusingAnimStep / focusingAnimLength));
		g.setStroke(Resource.getGameObjectThickStroke());
		g.drawRect(x, y, width, height);
	}

}
