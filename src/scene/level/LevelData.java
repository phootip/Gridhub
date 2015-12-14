package scene.level;

import java.awt.Font;
import java.awt.Graphics2D;

import core.IScrollableListItem;
import core.geom.Vector2;
import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;

public final class LevelData implements IScrollableListItem {

	private static final int MARGIN_LEFT = 50;
	private int playerCount;
	private String mapName;

	public int getPlayerCount() {
		return playerCount;
	}

	public String getMapName() {
		return mapName;
	}

	private LevelData(int playerCount, String mapName) {
		this.playerCount = playerCount;
		this.mapName = mapName;
	}

	public static LevelData parse(String jsonContent) {
		return new LevelData(1, jsonContent);
	}

	@Override
	public int getListItemHeight() {
		return 50;
	}

	@Override
	public void drawListItemContent(Graphics2D g, int x, int y, int width, boolean isSelected) {
		Font levelNameFont = Resource.getInstance().getDefaultFont(50);
		Vector2 levelNameTextPos = Helper.getCenteredTextPosition(mapName, levelNameFont, g, x, y, width,
				getListItemHeight());
		g.setFont(levelNameFont);
		g.setColor(isSelected ? ColorSwatch.BACKGROUND : ColorSwatch.FOREGROUND);
		g.drawString(mapName, x + MARGIN_LEFT, levelNameTextPos.getY());
	}

}
