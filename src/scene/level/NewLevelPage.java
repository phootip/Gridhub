package scene.level;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import core.SceneManager;
import core.geom.Vector2;
import scene.leveleditor.LevelEditorScene;
import util.Constants.ColorSwatch;
import util.Constants.PlayMode;
import util.Resource.FontWeight;
import util.Helper;
import util.InputManager;
import util.Resource;

public class NewLevelPage {

	private static final int SPINNER_TOP_MARGIN = 80;
	private PlayMode playMode;
	private IntegerRangeSpinner widthSpinner;
	private IntegerRangeSpinner heightSpinner;
	private int selectedIndex = 0;
	private ILevelSelectCallback levelSelectCallbackObj;

	public NewLevelPage(PlayMode playMode, ILevelSelectCallback levelSelectCallbackObj) {
		this.levelSelectCallbackObj = levelSelectCallbackObj;
		this.playMode = playMode;
		widthSpinner = new IntegerRangeSpinner(8, 50);
		heightSpinner = new IntegerRangeSpinner(8, 50);
		widthSpinner.setFocusing(true);
	}

	public void update(int step) {
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_LEFT)) {
			selectedIndex = (selectedIndex + 2) % 3;
		}
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_RIGHT)) {
			selectedIndex = (selectedIndex + 1) % 3;
		}

		if (selectedIndex == 0) {
			widthSpinner.setFocusing(true);
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN, false)) {
				widthSpinner.selectNextItem();
			}
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP, false)) {
				widthSpinner.selectPreviousItem();
			}
		} else
			widthSpinner.setFocusing(false);

		if (selectedIndex == 1) {
			heightSpinner.setFocusing(true);
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN, false)) {
				heightSpinner.selectNextItem();
			}
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP, false)) {
				heightSpinner.selectPreviousItem();
			}
		} else
			heightSpinner.setFocusing(false);

		if (selectedIndex == 2) {
			createBtnAnimStep += step;
			if (createBtnAnimStep > createBtnAnimLength) {
				createBtnAnimStep = createBtnAnimLength;
			}
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
				String levelName = JOptionPane.showInputDialog("Please enter a non-empty level name:");
				if (levelName != null && levelName.length() > 0) {
					int levelWidth = ((IntegerListItem) (widthSpinner.getSelectedItem())).getNumber();
					int levelHeight = ((IntegerListItem) (widthSpinner.getSelectedItem())).getNumber();
					Chapter chapter = LevelFileManager.getInstance().getUserChapter(playMode);
					int playerCount = (playMode == PlayMode.SINGLE_PLAYER) ? 1 : 2;
					String fileName = chapter.getLevelDataList().size() + LevelFileManager.LEVEL_FILE_NAME_SUFFIX;

					// LevelData levelData = new ...;
					LevelData levelData = new LevelData(playerCount, levelName, levelWidth, levelHeight, chapter,fileName);
					try {
						LevelFileManager.getInstance().saveLevelData(levelData);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.levelSelectCallbackObj.onLevelSelect(levelData);
				}
			}
		} else {
			createBtnAnimStep -= step;
			if (createBtnAnimStep < 0) {
				createBtnAnimStep = 0;
			}
		}

		widthSpinner.update(step);
		heightSpinner.update(step);
	}

	private int createBtnAnimStep = 0;
	private final int createBtnAnimLength = 100 * 10;

	public void draw(Graphics2D g, int x, int y, int width, int height) {
		Font sizeHeaderFont = Resource.getInstance().getDefaultFont(50, FontWeight.BOLD);
		g.setFont(sizeHeaderFont);

		int marginX = 20;
		int spinnerWidth = width / 6;

		g.drawString("Width", x, y + g.getFontMetrics().getAscent());
		g.drawString("Height", x + spinnerWidth + marginX, y + g.getFontMetrics().getAscent());

		widthSpinner.draw(g, x, y + SPINNER_TOP_MARGIN, spinnerWidth, height - SPINNER_TOP_MARGIN);
		heightSpinner.draw(g, x + spinnerWidth + marginX, y + SPINNER_TOP_MARGIN, spinnerWidth,
				height - SPINNER_TOP_MARGIN);

		// Right button

		float createButtonAnimRatio = 1 - (float) createBtnAnimStep / createBtnAnimLength;

		int buttonWidth = width - spinnerWidth * 2 - marginX * 2;

		String createText = "Create Level";
		g.setFont(Resource.getInstance().getDefaultFont(100, FontWeight.BOLD));

		Vector2 createTextPos = Helper.getCenteredTextPosition(createText, g.getFont(), g, x + width - buttonWidth,
				y + SPINNER_TOP_MARGIN, buttonWidth, height - SPINNER_TOP_MARGIN);

		g.setColor(Helper.blendColor(ColorSwatch.FOREGROUND, ColorSwatch.SHADOW, createButtonAnimRatio));
		g.drawRect(x + width - buttonWidth, y + SPINNER_TOP_MARGIN, buttonWidth, height - SPINNER_TOP_MARGIN);
		g.drawString(createText, createTextPos.getX(), createTextPos.getY());

	}

}
