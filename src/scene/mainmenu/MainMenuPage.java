package scene.mainmenu;

import java.awt.Font;
import java.awt.Graphics2D;

import util.Helper;
import util.Resource;
import util.Constants.ColorSwatch;

abstract class MainMenuPage {

	protected boolean isVisible;
	protected MainMenuScene parent;
	protected Font headerFont;

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

	protected void update(int step) {
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

	private void drawShifterBackground(Graphics2D g, int sceneWidth, int sceneHeight) {
		if (this.showStep < this.showDuration) {
			g.setColor(ColorSwatch.BACKGROUND);
			int backgroundWidth = (int) Helper.sineInterpolate(0, sceneWidth, (float) showStep / showDuration);
			g.fillRect(sceneWidth - backgroundWidth, 0, backgroundWidth, sceneHeight);
		}
	}

	private void drawHeader(Graphics2D g, int sceneWidth, String pageName) {
		g.setFont(this.headerFont);
		int headerX = (int) Helper.sineInterpolate(sceneWidth, 100, (float) showStep / showDuration);
		int headerY = 30 + g.getFontMetrics().getAscent();
		g.setColor(ColorSwatch.SHADOW);
		g.drawString(pageName, headerX + 5, headerY + 5);
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawString(pageName, headerX, headerY);
	}

	protected void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		if (this.showStep > 0) {
			drawShifterBackground(g, sceneWidth, sceneHeight);
			drawHeader(g, sceneWidth, "About Us");
		}
	}

}