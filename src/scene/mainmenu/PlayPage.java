package scene.mainmenu;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.geom.Vector2;
import scene.level.LevelSelector;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;
import util.Resource;
import util.Resource.FontWeight;

final public class PlayPage extends MainMenuPage {

	protected PlayPage(MainMenuScene parent) {
		super(parent);
		resetVariables();
	}

	private LevelSelector levelSelector;

	private void resetVariables() {
		this.levelSelector = new LevelSelector();
	}

	@Override
	protected void update(int step) {
		progressShowAnimation(step);
		if (!levelSelector.isEscapeKeyHandled()) {
			checkForPageExit();
		}

		if (this.isVisible) {
			levelSelector.update(step);
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

}
