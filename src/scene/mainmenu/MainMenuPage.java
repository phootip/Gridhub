package scene.mainmenu;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import scene.mainmenu.MainMenuScene.PageName;
import util.Helper;
import util.InputManager;
import util.Resource;
import util.Constants.ColorSwatch;

abstract class MainMenuPage {

	protected boolean isVisible;
	protected MainMenuScene parent;
	protected Font headerFont;

	protected abstract String getPageName();

	protected MainMenuPage(MainMenuScene parent) {
		this.parent = parent;
		this.headerFont = Resource.getInstance().getDefaultFont(100, Resource.FontWeight.REGULAR);
	}

	protected boolean isVisible() {
		return isVisible;
	}

	protected void setVisible(boolean visible) {
		this.isVisible = visible;
	}

	protected int showStep = 0;
	protected int showDuration = 100 * 30;

	protected void progressShowAnimation(int step) {
		if (this.isVisible) {
			showStep += step;
			if (showStep > showDuration) {
				showStep = showDuration;
			}
		} else {
			showStep -= step;
			if (showStep < 0) {
				showStep = 0;
			}
		}
	}

	protected void checkForPageExit() {
		if (this.isVisible && InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ESCAPE)) {
			this.parent.setPage(PageName.TOP_MAIN_MENU);
		}
	}

	protected void update(int step) {
		progressShowAnimation(step);
		checkForPageExit();
	}

	protected int getShiftDistance(int sceneWidth) {
		return (int) Helper.sineInterpolate(sceneWidth, 0, (float) showStep / showDuration);
	}

	private void drawShifterBackground(Graphics2D g, int sceneWidth, int sceneHeight) {
		if (this.showStep < this.showDuration) {
			g.setColor(ColorSwatch.BACKGROUND);
			int shiftDistance = getShiftDistance(sceneWidth);
			g.fillRect(shiftDistance, 0, sceneWidth - shiftDistance, sceneHeight);
		}
	}

	private void drawHeader(Graphics2D g, int sceneWidth, String pageName) {
		if (pageName == null)
			return;

		g.setFont(this.headerFont);
		int headerX = getShiftDistance(sceneWidth) + 100;
		int headerY = 50 + g.getFontMetrics().getAscent();
		g.setColor(ColorSwatch.SHADOW);
		g.drawString(pageName, headerX + 5, headerY + 5);
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawString(pageName, headerX, headerY);
	}

	protected void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		if (this.showStep > 0) {
			drawShifterBackground(g, sceneWidth, sceneHeight);
			drawHeader(g, sceneWidth, getPageName());
		}
	}

}