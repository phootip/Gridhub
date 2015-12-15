package scene.level;

import java.awt.Graphics2D;

import core.IScrollableListItem;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Resource.FontWeight;

class IntegerListItem implements IScrollableListItem {

	private int number;

	public IntegerListItem(int number) {
		this.number = number;
	}

	protected int getNumber() {
		return number;
	}

	@Override
	public int getListItemHeight() {
		return 50;
	}

	@Override
	public void drawListItemContent(Graphics2D g, int x, int y, int width, boolean isSelected) {
		g.setFont(Resource.getInstance().getDefaultFont(50, isSelected ? FontWeight.BOLD : FontWeight.REGULAR));
		g.setColor(isSelected ? ColorSwatch.BACKGROUND : ColorSwatch.FOREGROUND);
		g.drawString(Integer.toString(number), x + 50, y + g.getFontMetrics().getAscent());
	}

}