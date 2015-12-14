package scene.mainmenu;

import java.awt.Color;
import java.awt.Graphics2D;

import core.SceneManager;
import scene.level.ILevelSelectCallback;
import scene.level.LevelData;
import scene.level.LevelSelector;
import scene.play.PlayScene;
import util.Helper;

public class PlayPage extends MainMenuPage implements ILevelSelectCallback {

	protected PlayPage(MainMenuScene parent) {
		super(parent);
		resetVariables();
	}

	private LevelSelector levelSelector;

	private void resetVariables() {
		this.levelSelector = new LevelSelector();
		this.levelSelector.setLevelSelectCallback(this);
	}

	private boolean isFadingOutToPlayGameScene = false;
	private int fadeOutToPlayGameProgress = 0;
	private final int fadeOutToPlayGameDuration = 100 * 10;
	private LevelData selectedLevel;

	@Override
	protected void update(int step) {
		progressShowAnimation(step);
		if (isFadingOutToPlayGameScene) {
			fadeOutToPlayGameProgress += step;
			if (fadeOutToPlayGameProgress > fadeOutToPlayGameDuration) {
				fadeOutToPlayGameProgress = fadeOutToPlayGameDuration;
				SceneManager.getInstance().setNextScene(new PlayScene(selectedLevel));
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
			int pageMargin = 50;

			levelSelector.draw(g, pageMargin + shiftDist, TOP_MARGIN + pageMargin, sceneWidth - pageMargin * 2,
					sceneHeight - TOP_MARGIN - pageMargin * 2);

			if (fadeOutToPlayGameProgress > 0) {
				g.setColor(Helper.getAlphaColorPercentage(Color.BLACK,
						(float) fadeOutToPlayGameProgress / fadeOutToPlayGameDuration));
				g.fillRect(0, 0, sceneWidth, sceneHeight);

			}
		}
	}

	@Override
	protected String getPageName() {
		if (levelSelector.isPlayModeSelected()) {
			return "Play | " + levelSelector.getSelectedPlayMode().getFullModeName();
		} else {
			return "Play";
		}
	}

	@Override
	public void onLevelSelect(LevelData levelData) {
		selectedLevel = levelData;
		isFadingOutToPlayGameScene = true;
	}

}
