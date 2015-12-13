package core;

import java.awt.Graphics2D;

public abstract interface IScrollableListItem {
	public abstract int getHeight();
	public abstract void drawContent(Graphics2D g, int x, int y, int width, boolean isSelected);
}
