package scene.play;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import core.DrawManager;
import core.SceneManager;
import core.geom.Vector2;
import scene.core.Scene;
import scene.level.LevelData;
import scene.mainmenu.MainMenuScene;
import stage.GameStage;
import stage.GameStageType;
import util.Constants.ColorSwatch;
import util.Resource.FontWeight;
import util.Helper;
import util.InputManager;
import util.Resource;

public class PlayScene extends Scene {

	private static final int MAX_MENU_WIDTH = 1000;
	private static final int MENU_ITEM_TEXT_HEIGHT = 60;
	private static final int MENU_ITEM_HEIGHT = 80;
	private static final int MENU_LEVEL_NAME_TEXT_SIZE = 80;
	private static final int MENU_CHAPTER_NAME_TEXT_SIZE = 40;
	private static final int MENU_TOP_MARGIN = 50;
	protected LevelData levelData;
	protected GameStage gameStage;

	protected boolean isPause = false;
	private boolean isGameRunning = true;
	private int pauseDelayTimer = 0;
	private final int pauseDelayMaxValue = 100 * 20;

	private BufferedImage pausedGameScreenImg = null;

	public PlayScene(LevelData levelData) {
		this.levelData = levelData;
		createGameStage();
	}

	protected void createGameStage() {
		this.gameStage = new GameStage(this.levelData, GameStageType.PLAY);
	}

	@Override
	public void update(int step) {
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ESCAPE) && !this.gameStage.isEscapeKeyHandled()) {
			isPause = !isPause;
			if (isPause && pauseDelayTimer == 0)
				selectedMenuItemIndex = 0;
		}

		if (isPause) {
			pauseDelayTimer += step;
			if (pauseDelayTimer > pauseDelayMaxValue) {
				pauseDelayTimer = pauseDelayMaxValue;
			}
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
				performMenuAction(selectedMenuItemIndex);
			} else {
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP)) {
					selectedMenuItemIndex = (selectedMenuItemIndex + getMenuItems().length - 1) % getMenuItems().length;
				}
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN)) {
					selectedMenuItemIndex = (selectedMenuItemIndex + 1) % getMenuItems().length;
				}
			}
		} else {
			pauseDelayTimer -= step;
			if (pauseDelayTimer < 0) {
				pauseDelayTimer = 0;
			}
		}
		isGameRunning = pauseDelayTimer == 0;

		if (isGameRunning) {
			gameStage.update(step);
		}

		if (isSceneFadingOut) {
			sceneFadeProgress -= step;
			if (sceneFadeProgress <= 0) {
				sceneFadeProgress = 0;
				SceneManager.getInstance().setNextScene(nextScene);
			}
		} else if (isSceneFadingIn) {
			sceneFadeProgress += step;
			if (sceneFadeProgress >= sceneFadeDuration) {
				sceneFadeProgress = sceneFadeDuration;
				isSceneFadingIn = false;
			}
		}
	}

	protected void performMenuAction(int selectedMenuItem) {
		switch (selectedMenuItem) {
			case 0: // Restart
				isSceneFadingOut = true;
				nextScene = new PlayScene(levelData);
				break;
			case 1: // Exit
				isSceneFadingOut = true;
				nextScene = new MainMenuScene(false);
				break;
		}
	}

	private boolean isSceneFadingIn = true;
	private int sceneFadeProgress = 0;
	private final int sceneFadeDuration = 100 * 10;
	protected boolean isSceneFadingOut = false;
	protected Scene nextScene;

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {

		if (!isGameRunning) {
			if (pausedGameScreenImg == null || pausedGameScreenImg.getWidth() != sceneWidth
					|| pausedGameScreenImg.getHeight() != sceneHeight) {
				pausedGameScreenImg = DrawManager.getInstance().createBlankBufferedImage(sceneWidth, sceneHeight,
						false);
				Graphics2D subG = pausedGameScreenImg.createGraphics();

				gameStage.draw(subG, sceneWidth, sceneHeight);
				subG.setColor(Helper.getAlphaColorPercentage(ColorSwatch.BACKGROUND, 0.8));
				subG.fillRect(0, 0, sceneWidth, sceneHeight);
			}
			g.drawImage(pausedGameScreenImg,
					new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_NEAREST_NEIGHBOR), 0, 0);

			drawPauseMenu(g, sceneWidth, sceneHeight);
		} else {
			pausedGameScreenImg = null;
			gameStage.draw(g, sceneWidth, sceneHeight);
		}

		if (isSceneFadingIn || isSceneFadingOut) {
			g.setColor(Helper.getAlphaColorPercentage(Color.BLACK, 1 - (float) sceneFadeProgress / sceneFadeDuration));
			g.fillRect(0, 0, sceneWidth, sceneHeight);
		}
	}

	private String[] menuItems = new String[] { "Restart", "Exit" };
	protected String[] getMenuItems() {
		return menuItems;
	}
	private int selectedMenuItemIndex;

	private void drawMenuContent(Graphics2D g, int x, int y, int width, int height) {
		Font menuItemFont = Resource.getInstance().getDefaultFont(MENU_ITEM_TEXT_HEIGHT);
		Font selectedMenuItemFont = Resource.getInstance().getDefaultFont(MENU_ITEM_TEXT_HEIGHT, FontWeight.BOLD);

		int startY = y + (height - MENU_ITEM_HEIGHT * getMenuItems().length) / 2;

		for (int i = 0; i < getMenuItems().length; i++) {
			String menuItemText = getMenuItems()[i];

			if (i == selectedMenuItemIndex) {
				g.setColor(ColorSwatch.BACKGROUND);
				g.fillRect(x + 100, startY + MENU_ITEM_HEIGHT * i, width - 200, MENU_ITEM_HEIGHT);
				g.setColor(ColorSwatch.FOREGROUND);
				g.setFont(selectedMenuItemFont);
			} else {
				g.setColor(ColorSwatch.BACKGROUND);
				g.setFont(menuItemFont);
			}

			Vector2 textPos = Helper.getCenteredTextPosition(menuItemText, g.getFont(), g, x,
					startY + MENU_ITEM_HEIGHT * i, width, MENU_ITEM_HEIGHT);
			g.drawString(menuItemText, textPos.getX(), textPos.getY());
		}
	}

	private void drawPauseMenu(Graphics2D g, int sceneWidth, int sceneHeight) {
		int menuWidth = (int) Helper.sineInterpolate(0, MAX_MENU_WIDTH, (float) pauseDelayTimer / pauseDelayMaxValue,
				false, true);
		Rectangle oldClip = g.getClipBounds();

		g.setColor(ColorSwatch.FOREGROUND);
		g.fillRect((sceneWidth - menuWidth) / 2, 0, menuWidth, sceneHeight);
		g.setClip((sceneWidth - menuWidth) / 2, 0, menuWidth, sceneHeight);

		// Chapter Name

		String chapterName = levelData.getChapter().getChapterName();
		Font chapterNameFont = Resource.getInstance().getDefaultFont(MENU_CHAPTER_NAME_TEXT_SIZE);
		Vector2 chapterNameCenterPos = Helper.getCenteredTextPosition(chapterName, chapterNameFont, g,
				(sceneWidth - menuWidth) / 2, 0, MAX_MENU_WIDTH, sceneHeight);

		g.setFont(chapterNameFont);
		g.setColor(ColorSwatch.BACKGROUND);
		g.drawString(chapterName, chapterNameCenterPos.getX(), MENU_TOP_MARGIN + g.getFontMetrics().getAscent());

		// Level Name

		String levelName = levelData.getMapName();
		Font levelNameFont = Resource.getInstance().getDefaultFont(MENU_LEVEL_NAME_TEXT_SIZE, Resource.FontWeight.BOLD);
		Vector2 levelNameCenterPos = Helper.getCenteredTextPosition(levelName, levelNameFont, g,
				(sceneWidth - menuWidth) / 2, 0, MAX_MENU_WIDTH, sceneHeight);

		g.setFont(levelNameFont);
		g.setColor(ColorSwatch.BACKGROUND);
		g.drawString(levelName, levelNameCenterPos.getX(),
				MENU_TOP_MARGIN + MENU_CHAPTER_NAME_TEXT_SIZE + g.getFontMetrics().getAscent());

		// Line Separator

		int lineY = 200;
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		g.setColor(ColorSwatch.BACKGROUND);
		int lineWidth = (int) (menuWidth * 0.8);
		g.drawLine((sceneWidth - lineWidth) / 2, lineY, (sceneWidth - lineWidth) / 2 + lineWidth, lineY);

		drawMenuContent(g, (sceneWidth - menuWidth) / 2, 215, MAX_MENU_WIDTH, sceneHeight - 315);

		// Menu content fader

		if (pauseDelayTimer < pauseDelayMaxValue) {
			g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.FOREGROUND,
					1 - (float) pauseDelayTimer / pauseDelayMaxValue));
			g.fillRect((sceneWidth - menuWidth) / 2, 0, menuWidth, sceneHeight);
		}

		// Reset Clip

		g.setClip(oldClip);
	}

}
