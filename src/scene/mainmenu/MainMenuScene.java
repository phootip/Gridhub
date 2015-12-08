package scene.mainmenu;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import scene.core.Scene;
import util.Constants.ColorSwatch;

public class MainMenuScene extends Scene {

	private List<MainMenuPage> pageList;
	private MainMenuPage currentPage;

	private TopMainMenuPage topMainMenuPage;
	private AboutPage aboutPage;

	public MainMenuScene(boolean showWelcomeScreenAnimation) {
		pageList = new ArrayList<>();

		topMainMenuPage = new TopMainMenuPage(this);
		if (!showWelcomeScreenAnimation) {
			topMainMenuPage.skipIntroAnimation();
		}
		topMainMenuPage.setVisible(true);

		aboutPage = new AboutPage(this);

		// -----
		// Add pages to page list for easier updating and rendering
		// -----
		// Add top menu the first one, so the rendering will be behind the others
		pageList.add(topMainMenuPage);
		pageList.add(aboutPage);
		
		this.currentPage = topMainMenuPage;
	}

	@Override
	public void update(int step) {
		for (MainMenuPage p : pageList) {
			p.update(step);
		}
	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		for (MainMenuPage p : pageList) {
			p.draw(g, sceneWidth, sceneHeight);
		}
	}

	protected void setPage(MainMenuPage page) {
		this.currentPage.setVisible(false);
		this.currentPage = page;
		this.currentPage.setVisible(true);
	}

	protected enum PageName {
		TOP_MAIN_MENU, ABOUT
	}

	protected void setPage(PageName pageName) {
		switch (pageName) {
		case ABOUT:
			setPage(aboutPage);
			break;
		case TOP_MAIN_MENU:
			setPage(topMainMenuPage);
			break;
		default:
			throw new IllegalArgumentException("Invalid page name.");
		}
	}

}
