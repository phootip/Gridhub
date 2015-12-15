package scene.mainmenu;

import java.awt.Color;
import java.awt.Graphics2D;

import core.SceneManager;
import scene.level.ILevelSelectCallback;
import scene.level.LevelData;
import scene.level.LevelEditorPageLevelSelector;
import scene.level.PlayPageLevelSelector;
import scene.leveleditor.LevelEditorScene;
import scene.play.PlayScene;
import util.Helper;

final public class LevelEditorPage extends MainMenuPage implements ILevelSelectCallback {

	protected LevelEditorPage(MainMenuScene parent) {
		super(parent);
		resetVariables();
	}

	private LevelEditorPageLevelSelector levelSelector;

	private void resetVariables() {
		this.levelSelector = new LevelEditorPageLevelSelector();
		this.levelSelector.setLevelSelectCallback(this);
	}

	private boolean isFadingOutToLevelEditorScene = false;
	private int fadeOutToLevelEditorProgress = 0;
	private final int fadeOutToLevelEditorDuration = 100 * 10;
	private LevelData selectedLevel;

	@Override
	protected void update(int step) {
		progressShowAnimation(step);
		if (isFadingOutToLevelEditorScene) {
			fadeOutToLevelEditorProgress += step;
			if (fadeOutToLevelEditorProgress > fadeOutToLevelEditorDuration) {
				fadeOutToLevelEditorProgress = fadeOutToLevelEditorDuration;
				SceneManager.getInstance().setNextScene(new LevelEditorScene(selectedLevel));
			}
		} else {
			if (!levelSelector.isEscapeKeyHandled()) {
				checkForPageExit();
			}

			if (this.isVisible) {
				levelSelector.update(step);
			}
		}
	}

	@Override
	protected void setVisible(boolean visible) {
		super.setVisible(visible);

		// Do a variables reset
		if (visible) {
			resetVariables();
		}
	}

	@Override
	protected void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		super.draw(g, sceneWidth, sceneHeight);

		if (this.showStep > 0) {
			int shiftDist = getShiftDistance(sceneWidth);
			int pageMarginX = 100;
			int pageMarginY = 50;

			levelSelector.draw(g, pageMarginX + shiftDist, TOP_MARGIN + pageMarginY, sceneWidth - pageMarginX * 2,
					sceneHeight - TOP_MARGIN - pageMarginY * 2);

			if (fadeOutToLevelEditorProgress > 0) {
				g.setColor(Helper.getAlphaColorPercentage(Color.BLACK,
						(float) fadeOutToLevelEditorProgress / fadeOutToLevelEditorDuration));
				g.fillRect(0, 0, sceneWidth, sceneHeight);

			}
		}
	}

	@Override
	protected String getPageName() {
		if (levelSelector.isPlayModeSelected()) {
			return levelSelector.getCurrentModeSelectName() + " | " + levelSelector.getSelectedPlayMode().getFullModeName();
		} else {
			return "Level Editor";
		}
	}

	@Override
	public void onLevelSelect(LevelData levelData) {
		selectedLevel = levelData;
		isFadingOutToLevelEditorScene = true;
	}

}
