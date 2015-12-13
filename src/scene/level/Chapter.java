package scene.level;

import java.awt.Font;
import java.awt.Graphics2D;

import core.IScrollableListItem;
import core.geom.Vector2;
import util.Helper;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Resource.FontWeight;

class Chapter implements IScrollableListItem {

	private static final int BOTTOM_MARGIN = 10;
	private static final int MAINTEXT_SIZE = 60;
	private static final int SUBTEXT_SIZE = 30;
	private static final float MIDDLE_MARGIN = 0.5f;
	private static final int LEFT_MARGIN = 30;
	private static final int TOP_MARGIN = 10;
	private String chapterName;
	private String folderName;
	private boolean userFolder;
	private transient int chapterOrder;

	/**
	 * @return the chapterOrder
	 */
	protected int getChapterOrder() {
		return chapterOrder;
	}

	/**
	 * @param chapterOrder
	 *            the chapterOrder to set
	 */
	protected void setChapterOrder(int chapterOrder) {
		this.chapterOrder = chapterOrder;
	}

	/**
	 * Get this chapter name.
	 * 
	 * @return The chapter name.
	 */
	protected String getChapterName() {
		return chapterName;
	}

	/**
	 * Get the folder name that is used for collecting this chapter's level files.
	 * 
	 * @return Name of folder containing level files of this chapter.
	 */
	protected String getFolderName() {
		return folderName;
	}

	/**
	 * Get the boolean value determining whether or not this chapter is used as a user folder. In other words, the
	 * imported/exported levels should be in this chapter. There should be exactly one chapter that is marked as user
	 * folder.
	 * 
	 * @return Whether or not this chapter is used as a user folder.
	 */
	protected boolean isUserFolder() {
		return userFolder;
	}

	private Chapter() {
	}

	@Override
	public String toString() {
		return "Chapter " + chapterOrder + " : " + chapterName;
	}

	@Override
	public int getHeight() {
		return (int) Math.ceil(TOP_MARGIN + SUBTEXT_SIZE + MIDDLE_MARGIN + MAINTEXT_SIZE + BOTTOM_MARGIN);
	}

	@Override
	public void drawContent(Graphics2D g, int x, int y, int width, boolean isSelected) {
		String subText = "Chapter " + chapterOrder;
		Font subTextFont = Resource.getInstance().getDefaultFont(SUBTEXT_SIZE, FontWeight.BOLD);
		g.setColor(Helper.blendColor(ColorSwatch.FOREGROUND, ColorSwatch.BACKGROUND, isSelected ? 0.6f : 0.4f));
		g.setFont(subTextFont);
		g.drawString(subText, x + LEFT_MARGIN, y + g.getFontMetrics().getAscent() + TOP_MARGIN);

		String mainText = chapterName;
		Font mainTextFont = Resource.getInstance().getDefaultFont(MAINTEXT_SIZE, FontWeight.REGULAR);
		g.setColor(isSelected ? ColorSwatch.BACKGROUND : ColorSwatch.FOREGROUND);
		g.setFont(mainTextFont);
		g.drawString(mainText, x + LEFT_MARGIN,
				y + g.getFontMetrics().getAscent() + TOP_MARGIN + MIDDLE_MARGIN + SUBTEXT_SIZE);
	}

}
