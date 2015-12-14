package scene.level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import core.IScrollableListItem;
import core.geom.Vector2;
import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;
import util.Resource.FontWeight;

public final class LevelData implements IScrollableListItem {

	/**
	 * Get the thumbnail image of this level data.
	 * 
	 * @return Thumbnail image object, or {@code null} if the thumbnail has not been created.
	 */
	protected BufferedImage getThumbnail() {
		return thumbnail;
	}

	/**
	 * Set the thumbnail image of this level data. This should be called by {@link LevelThumbnailRenderer}.
	 * 
	 * @param thumbnail
	 *            a {@link BufferedImage} object of thumbnail image.
	 */
	protected void setThumbnail(BufferedImage thumbnail) {
		this.thumbnail = thumbnail;
	}

	private transient BufferedImage thumbnail = null;
	private transient Chapter chapter;

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	private static final int CONTENT_MARGIN_LEFT = 50;
	private static final int IMAGE_WIDTH = 180;
	private static final int IMAGE_HEIGHT = 90;
	private static final int TEXT_MARGIN = 50;
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
		return 100;
	}

	@Override
	public void drawListItemContent(Graphics2D g, int x, int y, int width, boolean isSelected) {

		int imgLeft = x + CONTENT_MARGIN_LEFT;
		int imgTop = y + (getListItemHeight() - IMAGE_HEIGHT) / 2;

		if (this.getThumbnail() != null) {
			AffineTransform thumbnailTransform = new AffineTransform();
			thumbnailTransform.scale((double) IMAGE_WIDTH / thumbnail.getWidth(),
					(double) IMAGE_HEIGHT / thumbnail.getHeight());
			g.drawImage(thumbnail, new AffineTransformOp(thumbnailTransform, AffineTransformOp.TYPE_BICUBIC),
					imgLeft, imgTop);
		} else {
			g.setColor(ColorSwatch.SHADOW);
			g.setStroke(Resource.getGameObjectThinStroke());
			g.drawRect(imgLeft, imgTop, IMAGE_WIDTH, IMAGE_HEIGHT);

			g.setColor(ColorSwatch.SHADOW);
			String loadingText = "Generating Preview...";
			Font loadingTextFont = Resource.getInstance().getDefaultFont(20, FontWeight.BOLD);
			Vector2 drawPosition = Helper.getCenteredTextPosition(loadingText, loadingTextFont, g, imgLeft, imgTop,
					IMAGE_WIDTH, IMAGE_HEIGHT);
			
			g.setFont(loadingTextFont);
			g.drawString(loadingText, drawPosition.getX(), drawPosition.getY());
		}

		Font levelNameFont = Resource.getInstance().getDefaultFont(50);
		Vector2 levelNameTextPos = Helper.getCenteredTextPosition(mapName, levelNameFont, g, x, y, width,
				getListItemHeight());
		g.setFont(levelNameFont);
		g.setColor(isSelected ? ColorSwatch.BACKGROUND : ColorSwatch.FOREGROUND);
		g.drawString(mapName, x + IMAGE_WIDTH + CONTENT_MARGIN_LEFT + TEXT_MARGIN, levelNameTextPos.getY());

	}

}
