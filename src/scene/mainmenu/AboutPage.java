package scene.mainmenu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import scene.mainmenu.MainMenuScene.PageName;
import util.Resource.FontWeight;
import util.InputManager;
import util.Resource;

class AboutPage extends MainMenuPage {

	Font nameFont = Resource.getInstance().getDefaultFont(60, FontWeight.REGULAR);
	Font idFont = Resource.getInstance().getDefaultFont(40, FontWeight.BOOK);

	public AboutPage(MainMenuScene parent) {
		super(parent);
	}

	@Override
	protected void update(int step) {
		super.update(step);

		if (this.isVisible && InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ESCAPE)) {
			this.parent.setPage(PageName.TOP_MAIN_MENU);
		}
	}

	@Override
	protected void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		super.draw(g, sceneWidth, sceneHeight);

		if (this.showStep > 0) {
			drawGameCreatorCard("Thanat Jatuphattharachat", "5730243121", 400, g, sceneWidth);
			drawGameCreatorCard("Kasidit Iamthong", "5731005321", 200, g, sceneWidth);
		}

	}

	private void drawGameCreatorCard(String name, String id, int startY, Graphics2D g, int sceneWidth) {
		int currentY = startY;
		g.setColor(Color.WHITE);

		int textX = getShiftDistance(sceneWidth) + 100;

		g.setFont(nameFont);
		currentY += g.getFontMetrics().getAscent();
		g.drawString(name, textX, currentY);

		g.setFont(idFont);
		currentY += g.getFontMetrics().getAscent() + 20;
		g.drawString(id, textX, currentY);
	}

}
