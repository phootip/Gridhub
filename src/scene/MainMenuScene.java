package scene;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.sun.glass.events.KeyEvent;

import core.SceneManager;
import scene.core.Scene;
import scene.test.TestScene2;
import util.Resource;
import util.Constants;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;

public class MainMenuScene extends Scene {

	Font logoTextFont;
	Font menuTextFont;
	Font selectedMenuTextFont;

	public MainMenuScene() {
		this.logoTextFont = Resource.getInstance().getDefaultFont(200);
		this.menuTextFont = Resource.getInstance().getDefaultFont(60, Resource.FontWeight.BOOK);
		this.selectedMenuTextFont = Resource.getInstance().getDefaultFont(75, Resource.FontWeight.BOLD);
	}

	@Override
	public void update(int step) {
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP)) {
			selectingMenu--;
		}
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN)) {
			selectingMenu++;
		}
		selectingMenu = (selectingMenu + level1MenuItem.length) % level1MenuItem.length;

		currentMenuPosition += (selectingMenu - currentMenuPosition) / Math.pow(Math.pow(5, 1.0 / 100), step);
		currentMenuHighlighterWidth += (currentMenuItemWidth - currentMenuHighlighterWidth)
				/ Math.pow(Math.pow(3, 1.0 / 100), step);
	}

	private final String[] level1MenuItem = new String[] { "Single Player", "Co-op Mode", "Level Editor", "Option",
			"About Us" };
	private final float menuItemSpacing = 30f;
	private int selectingMenu = 0;
	private float currentMenuPosition = 0f;
	private float currentMenuHighlighterWidth = 0;
	private float currentMenuItemWidth = 0;

	private void drawMenuItem(Graphics2D g, int sceneWidth, int sceneHeight) {

		FontMetrics menuItemFontMetric = g.getFontMetrics(selectedMenuTextFont);
		float fontHeight = menuItemFontMetric.getHeight();
		float fontAscent = menuItemFontMetric.getAscent();

		float baseFontY = (sceneHeight - fontHeight) / 2f + fontAscent;
		baseFontY -= (fontHeight + menuItemSpacing) * currentMenuPosition;
		int paneStartX = sceneWidth * 3 / 5;
		int paneEndX = sceneWidth - 100;
		int paneWidth = paneEndX - paneStartX;

		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g.fillRect(paneStartX, 0, paneWidth, sceneHeight);

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(paneStartX + (int) (paneWidth - currentMenuHighlighterWidth - 60) / 2,
				(int) (sceneHeight - fontHeight) / 2, (int) currentMenuHighlighterWidth + 60, (int) fontHeight);

		for (int i = 0; i < level1MenuItem.length; i++) {
			int menuItemTextWidth;
			if (i == selectingMenu) {
				g.setColor(ColorSwatch.FOREGROUND);
				g.setFont(selectedMenuTextFont);
				menuItemTextWidth = g.getFontMetrics().stringWidth(level1MenuItem[i]);
				currentMenuItemWidth = menuItemTextWidth;
			} else {
				g.setColor(ColorSwatch.BACKGROUND);
				g.setFont(menuTextFont);
				menuItemTextWidth = g.getFontMetrics().stringWidth(level1MenuItem[i]);
			}
			g.drawString(level1MenuItem[i], paneStartX + (paneWidth - menuItemTextWidth) / 2,
					baseFontY + i * (fontHeight + menuItemSpacing));
		}
	}

	private void drawTextLogo(Graphics2D g, int sceneWidth, int sceneHeight, int xShifter) {
		FontMetrics logoFontMetric = g.getFontMetrics(this.logoTextFont);
		Rectangle2D logoBound = logoFontMetric.getStringBounds(Constants.PROGRAM_NAME, g);

		g.setFont(this.logoTextFont);
		float centerTextX = (float) (sceneWidth - logoBound.getWidth()) / 2.0f + xShifter;
		float centerTextY = (float) ((sceneHeight - logoBound.getHeight()) / 2.0f + logoFontMetric.getAscent());

		g.setColor(ColorSwatch.SHADOW);
		g.drawString(Constants.PROGRAM_NAME, centerTextX + 5, centerTextY + 5);

		g.setColor(ColorSwatch.FOREGROUND);
		g.drawString(Constants.PROGRAM_NAME, centerTextX, centerTextY);
	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		drawTextLogo(g, sceneWidth, sceneHeight, sceneWidth / -5);

		drawMenuItem(g, sceneWidth, sceneHeight);
	}

}
