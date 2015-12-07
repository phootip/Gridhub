package scene.mainmenu;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import scene.mainmenu.MainMenuScene.PageName;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;

class AboutPage extends MainMenuPage {

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
	}

}
