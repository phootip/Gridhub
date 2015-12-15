package scene.level;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import core.IScrollableListItem;
import util.Helper;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Constants.PlayMode;
import util.Resource.FontWeight;

/**
 * This class represents a chapter of the game. One chapter may contains multiple {@link LevelData}s.
 * 
 * @author Kasidit Iamthong
 *
 */
public class Chapter implements IScrollableListItem {

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
	private transient List<LevelData> levelDataList;
	private transient boolean isRenderingJobStarted;
	private transient PlayMode playMode;

	/**
	 * Get the play mode of the chapter.
	 * 
	 * @return The play mode.
	 */
	protected PlayMode getPlayMode() {
		return playMode;
	}

	/**
	 * Set the play mode of the chapter
	 * 
	 * @param playMode
	 *            the play mode to set.
	 */
	protected void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}

	/**
	 * Determines whether the thumbnail image rendering job has started. Useful for preventing two rendering jobs
	 * running at the same {@link Chapter} and preventing unnecessary rendering job.
	 * 
	 * @return Whether or not the rendering job has started.
	 */
	protected boolean isRenderingJobStarted() {
		return isRenderingJobStarted;
	}

	/**
	 * Set the is-rendering-job-started flag to specified value.
	 * 
	 * @param isRenderingJobStarted
	 *            the specified value to set.
	 */
	protected void setRenderingJobStarted(boolean isRenderingJobStarted) {
		this.isRenderingJobStarted = isRenderingJobStarted;
	}

	/**
	 * Get the level data list that belongs to this {@link Chapter}.
	 * 
	 * @return The {@link List} of the {@link LevelData}.
	 */
	protected List<LevelData> getLevelDataList() {
		return levelDataList;
	}

	/**
	 * Add new level to this chapter.
	 * 
	 * @param levelFileContent
	 *            a content of the level file as String.
	 * @param levelFileName
	 *            a filename of the level file, including the extension.
	 */
	protected void addLevel(String levelFileContent, String levelFileName) {
		// LevelData levelData = LevelData.parse((playMode == PlayMode.SINGLE_PLAYER) ? 1 : 2, levelFileContent);=
		LevelData levelData = LevelData.parse(levelFileContent);
		levelData.setLevelFileName(levelFileName);
		levelData.setChapter(this);
		levelDataList.add(levelData);
	}

	/**
	 * Get the order number of this {@link Chapter}.
	 * 
	 * @return The chapter order number.
	 */
	protected int getChapterOrder() {
		return chapterOrder;
	}

	/**
	 * Set the order number of this {@link Chapter}.
	 * 
	 * @param chapterOrder
	 *            the chapter order number to set.
	 */
	protected void setChapterOrder(int chapterOrder) {
		this.chapterOrder = chapterOrder;
	}

	/**
	 * Get this chapter name.
	 * 
	 * @return The chapter name.
	 */
	public String getChapterName() {
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
		levelDataList = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "Chapter " + chapterOrder + " : " + chapterName;
	}

	@Override
	public int getListItemHeight() {
		return (int) Math.ceil(TOP_MARGIN + SUBTEXT_SIZE + MIDDLE_MARGIN + MAINTEXT_SIZE + BOTTOM_MARGIN);
	}

	@Override
	public void drawListItemContent(Graphics2D g, int x, int y, int width, boolean isSelected) {
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
