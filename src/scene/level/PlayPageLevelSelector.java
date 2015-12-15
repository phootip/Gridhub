package scene.level;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.ScrollableUIList;
import core.geom.Vector2;
import util.Helper;
import util.InputManager;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Constants.PlayMode;
import util.Resource.FontWeight;

public final class PlayPageLevelSelector {

	private PlayMode selectedPlayMode;
	private boolean playModeSelected = false;
	private ScrollableUIList chapterList;
	private ScrollableUIList levelList;
	private ScrollableUIList focusingList;

	public ILevelSelectCallback levelSelectCallbackObj = null;

	public void setLevelSelectCallback(ILevelSelectCallback levelSelectCallbackObj) {
		this.levelSelectCallbackObj = levelSelectCallbackObj;
	}

	public PlayMode getSelectedPlayMode() {
		return selectedPlayMode;
	}

	public boolean isPlayModeSelected() {
		return playModeSelected;
	}

	public boolean isEscapeKeyHandled() {
		return playModeSelected && InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ESCAPE);
	}

	public PlayPageLevelSelector() {
		selectedPlayMode = PlayMode.SINGLE_PLAYER;
	}

	public void update(int step) {
		if (playModeSelected) {
			if (isEscapeKeyHandled()) { // ESC key is triggered
				playModeSelected = false;
				chapterList.setFocusing(false);
				levelList.setFocusing(false);
			} else {
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
					if (focusingList == chapterList) {
						chapterList.setFocusing(false);
						levelList.setFocusing(true);
						focusingList = levelList;
					} else {
						if (levelSelectCallbackObj != null) {
							if (levelList.getSelectedItem() != null) {
								this.levelSelectCallbackObj.onLevelSelect((LevelData) levelList.getSelectedItem());
							}
						}
					}
				} else if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_RIGHT)
						|| InputManager.getInstance().isKeyTriggering(KeyEvent.VK_LEFT)) {
					if (focusingList == chapterList) {
						chapterList.setFocusing(false);
						levelList.setFocusing(true);
						focusingList = levelList;
					} else {
						chapterList.setFocusing(true);
						levelList.setFocusing(false);
						focusingList = chapterList;
					}
				} else {
					if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN, false)) {
						if (focusingList.selectNextItem() && focusingList == chapterList) {
							populateLevelList((Chapter) chapterList.getSelectedItem());
						}
					}
					if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP, false)) {
						if (focusingList.selectPreviousItem() && focusingList == chapterList) {
							populateLevelList((Chapter) chapterList.getSelectedItem());
						}
					}
				}
			}
			chapterList.update(step);
			levelList.update(step);
		} else {
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_RIGHT)
					|| InputManager.getInstance().isKeyTriggering(KeyEvent.VK_LEFT)) {
				// Toggle
				selectedPlayMode = (selectedPlayMode == PlayMode.SINGLE_PLAYER) ? PlayMode.COOP_MODE
						: PlayMode.SINGLE_PLAYER;
			}

			if (selectedPlayMode == PlayMode.SINGLE_PLAYER) {
				playModeAnimStep -= step;
				if (playModeAnimStep < 0) {
					playModeAnimStep = 0;
				}
			} else {
				playModeAnimStep += step;
				if (playModeAnimStep > playModeAnimLength) {
					playModeAnimStep = playModeAnimLength;
				}
			}

			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
				chapterList = new ScrollableUIList();
				chapterList.addAll(LevelFetcher.getInstance().getChapterList(selectedPlayMode));
				chapterList.setFocusing(true);

				levelList = new ScrollableUIList();
				populateLevelList((Chapter) chapterList.getSelectedItem());

				focusingList = chapterList;
				playModeSelected = true;

			}
		}
	}

	private void populateLevelList(Chapter chapter) {
		levelList.clear();
		levelList.addAll(chapter.getLevelDataList());
		levelList.resetSelectedItemIndex();
		LevelThumbnailRenderer.startRenderThumbnail(chapter);
	}

	public void draw(Graphics2D g, int x, int y, int width, int height) {
		if (!playModeSelected) {
			drawPlayModeSelector(g, x, y, width, height);
		} else {
			int marginSize = 50;
			int chapterSelectSize = 500;
			chapterList.draw(g, x, y, chapterSelectSize, height);
			int levelSelectSize = width - chapterSelectSize - marginSize;
			levelList.draw(g, x + width - levelSelectSize, y, levelSelectSize, height);
		}
	}

	private int playModeAnimStep = 0;
	private final int playModeAnimLength = 100 * 10;

	private void drawPlayModeSelector(Graphics2D g, int x, int y, int width, int height) {
		int modeSelectMargin = 50;
		float modeSelectAnimRatio = (float) playModeAnimStep / playModeAnimLength;
		Font modeSelectFont = Resource.getInstance().getDefaultFont(100, FontWeight.BOLD);

		g.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g.setFont(modeSelectFont);

		int coopModeRectLeft = x + (width + modeSelectMargin) / 2;
		int singlePlayerRectLeft = x;
		int modesRectTop = y;
		int modesRectWidth = (width - modeSelectMargin) / 2;
		int modesRectHeight = height;

		// Single player
		String singlePlayerText = PlayMode.SINGLE_PLAYER.getFullModeName();

		Vector2 singlePlayerTextPos = Helper.getCenteredTextPosition(singlePlayerText, modeSelectFont, g,
				singlePlayerRectLeft, modesRectTop, modesRectWidth, modesRectHeight);

		g.setColor(Helper.blendColor(ColorSwatch.FOREGROUND, ColorSwatch.SHADOW, modeSelectAnimRatio));
		g.drawRect(singlePlayerRectLeft, modesRectTop, modesRectWidth, modesRectHeight);
		g.drawString(singlePlayerText, singlePlayerTextPos.getX(), singlePlayerTextPos.getY());

		// Co-op
		String coopModeText = PlayMode.COOP_MODE.getFullModeName();

		Vector2 coopModeTextPos = Helper.getCenteredTextPosition(coopModeText, modeSelectFont, g, coopModeRectLeft,
				modesRectTop, modesRectWidth, modesRectHeight);

		g.setColor(Helper.blendColor(ColorSwatch.FOREGROUND, ColorSwatch.SHADOW, 1 - modeSelectAnimRatio));
		g.drawRect(coopModeRectLeft, modesRectTop, modesRectWidth, modesRectHeight);
		g.drawString(coopModeText, coopModeTextPos.getX(), coopModeTextPos.getY());
	}

}
