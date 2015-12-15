package scene.level;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;

import core.DrawManager;
import core.ScrollableUIList;
import core.geom.Vector2;
import util.Helper;
import util.InputManager;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Constants.PlayMode;
import util.Resource.FontWeight;

public final class LevelEditorPageLevelSelector {

	private static final int HEADER_TEXT_SIZE = 50;

	private PlayMode selectedPlayMode;
	private int selectedOptionChoice = 0;
	private boolean playModeSelected = false;
	private NewLevelPage newLevelPage = null;

	private ScrollableUIList levelList;

	public ILevelSelectCallback levelSelectCallbackObj = null;

	public String getCurrentModeSelectName() {
		return getModeSelectName(selectedOptionChoice);
	}

	protected String getModeSelectName(int buttonId) {
		switch (buttonId) {
			case 0:
				return "New";
			case 1:
				return "Edit";
			case 2:
				return "Import";
			case 3:
				return "Export";
			default:
				return null;
		}
	}

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

	public LevelEditorPageLevelSelector() {
		selectedPlayMode = PlayMode.SINGLE_PLAYER;
	}

	public void update(int step) {
		if (playModeSelected) {
			if (newLevelPage == null) {
				if (isEscapeKeyHandled()) { // ESC key is triggered
					playModeSelected = false;
					levelList.setFocusing(false);
				} else {
					if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
						if (selectedOptionChoice == 3) { // Export
							// TODO : Export level file
						} else if (selectedOptionChoice == 1) { // Edit
							if (levelSelectCallbackObj != null && levelList.getSelectedItem() != null) {
								this.levelSelectCallbackObj.onLevelSelect((LevelData) levelList.getSelectedItem());
							}
						}
					} else {
						if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN, false)) {
							levelList.selectNextItem();
						}
						if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP, false)) {
							levelList.selectPreviousItem();
						}
					}
				}
				levelList.update(step);
			} else {
				if (isEscapeKeyHandled()) { // ESC key is triggered
					newLevelPage = null;
					playModeSelected = false;
				} else {
					newLevelPage.update(step);
				}
			}
		} else {
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN)
					|| InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP)) {
				// Toggle
				selectedPlayMode = (selectedPlayMode == PlayMode.SINGLE_PLAYER) ? PlayMode.COOP_MODE
						: PlayMode.SINGLE_PLAYER;
				// selectedOptionChoice = 0;
			}
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_LEFT)) {
				selectedOptionChoice = (selectedOptionChoice + 3) % 4;
			}
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_RIGHT)) {
				selectedOptionChoice = (selectedOptionChoice + 1) % 4;
			}

			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					if (((selectedPlayMode == PlayMode.SINGLE_PLAYER) == (i == 0)) && j == selectedOptionChoice) {
						playModeAnimStep[i][j] -= step;
						if (playModeAnimStep[i][j] < 0) {
							playModeAnimStep[i][j] = 0;
						}
					} else {
						playModeAnimStep[i][j] += step;
						if (playModeAnimStep[i][j] > playModeAnimLength) {
							playModeAnimStep[i][j] = playModeAnimLength;
						}
					}
				}
			}

			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {

				switch (selectedOptionChoice) {
					case 0: // New
						newLevelPage = new NewLevelPage(selectedPlayMode, levelSelectCallbackObj);
						
						playModeSelected = true;
						break;
					case 2: // Import
						importLevelFile();
						break;
					case 1: // Edit
					case 3: // Export

						levelList = new ScrollableUIList();
						levelList.setFocusing(true);
						populateLevelList();

						playModeSelected = true;

						break;
				}

			}
		}
	}

	private void importLevelFile() {
		JFileChooser levelFileChooser = new JFileChooser();
		levelFileChooser.setDialogTitle("Import Level File");
		levelFileChooser.showOpenDialog(null);

	}

	private void populateLevelList() {
		Chapter chapter = LevelFileManager.getInstance().getUserChapter(selectedPlayMode);

		levelList.clear();
		levelList.addAll(chapter.getLevelDataList());
		levelList.resetSelectedItemIndex();
		LevelThumbnailRenderer.startRenderThumbnail(chapter);
	}

	public void draw(Graphics2D g, int x, int y, int width, int height) {
		if (newLevelPage != null) {
			newLevelPage.draw(g, x, y, width, height);
		} else if (!playModeSelected) {
			drawPlayModeSelector(g, x, y, width, height);
		} else {
			levelList.draw(g, x, y, width, height);
		}
	}

	private int[][] playModeAnimStep = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
	private final int playModeAnimLength = 100 * 10;

	private void drawPlayModeButton(Graphics2D g, String text, int x, int y, int width, int height, float animRatio) {
		g.setColor(Helper.blendColor(ColorSwatch.FOREGROUND, ColorSwatch.SHADOW, animRatio));

		Vector2 textPos = Helper.getCenteredTextPosition(text, g.getFont(), g, x, y, width, height);

		g.drawRect(x, y, width, height);
		g.drawString(text, textPos.getX(), textPos.getY());
	}

	private void drawPlayModeSelector(Graphics2D g, int x, int y, int width, int height) {

		int middleStartY = y + height / 2;
		int buttonMarginX = 50;
		int buttonMarginY = 20;

		g.setFont(Resource.getInstance().getDefaultFont(HEADER_TEXT_SIZE, FontWeight.REGULAR));
		g.drawString(PlayMode.SINGLE_PLAYER.getFullModeName(), x, y + g.getFontMetrics().getAscent());
		g.drawString(PlayMode.COOP_MODE.getFullModeName(), x, middleStartY + g.getFontMetrics().getAscent());

		int buttonWidth = (width - buttonMarginX * 3) / 4;
		int buttonHeight = height / 2 - HEADER_TEXT_SIZE - buttonMarginY * 2 - 20;

		g.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g.setFont(Resource.getInstance().getDefaultFont(75, FontWeight.BOLD));

		for (int i = 0; i < 4; i++) {
			drawPlayModeButton(g, getModeSelectName(i), x + (buttonWidth + buttonMarginX) * i,
					y + HEADER_TEXT_SIZE + buttonMarginY, buttonWidth, buttonHeight,
					playModeAnimStep[0][i] / (float) playModeAnimLength);

			drawPlayModeButton(g, getModeSelectName(i), x + (buttonWidth + buttonMarginX) * i,
					middleStartY + HEADER_TEXT_SIZE + buttonMarginY, buttonWidth, buttonHeight,
					playModeAnimStep[1][i] / (float) playModeAnimLength);
		}

	}

}
