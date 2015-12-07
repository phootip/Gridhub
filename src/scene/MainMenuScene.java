package scene;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.sun.glass.events.KeyEvent;

import scene.core.Scene;
import util.Resource;
import util.Constants;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;

public class MainMenuScene extends Scene {

	Font logoTextFont;
	Font menuTextFont;
	Font selectedMenuTextFont;

	public MainMenuScene(boolean showWelcomeScreenAnimation) {
		this.logoTextFont = Resource.getInstance().getDefaultFont(200);
		this.menuTextFont = Resource.getInstance().getDefaultFont(60, Resource.FontWeight.BOOK);
		this.selectedMenuTextFont = Resource.getInstance().getDefaultFont(75, Resource.FontWeight.BOLD);

		if (!showWelcomeScreenAnimation) {
			cumulativeStep = 100 * 180;
			menuFadeInProgress = 1;
		}
	}

	private long cumulativeStep = 0;

	@Override
	public void update(int step) {
		cumulativeStep += step;

		if (cumulativeStep > 100 * 120) {
			if (cumulativeStep > 100 * 180) {
				cumulativeStep = 100 * 180;
			}

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

			menuFadeInProgress += step / (100f * 60);
			if (menuFadeInProgress > 1) {
				menuFadeInProgress = 1;
			}
		}
	}

	private final String[] level1MenuItem = new String[] { "Single Player", "Co-op Mode", "Level Editor", "Option",
			"About Us" };
	private final float menuItemSpacing = 30f;
	private int selectingMenu = 0;
	private float currentMenuPosition = 0f;
	private float currentMenuHighlighterWidth = 0;
	private float currentMenuItemWidth = 0;
	private float menuFadeInProgress = 0;

	private void drawMenuItem(Graphics2D g, int sceneWidth, int sceneHeight) {

		FontMetrics menuItemFontMetric = g.getFontMetrics(selectedMenuTextFont);
		float fontHeight = menuItemFontMetric.getHeight();
		float fontAscent = menuItemFontMetric.getAscent();

		float baseFontY = (sceneHeight - fontHeight) / 2f + fontAscent;
		baseFontY -= (fontHeight + menuItemSpacing) * currentMenuPosition;

		int paneStartX = sceneWidth * 3 / 5;
		int paneWidth = sceneWidth - 100 - paneStartX;
		paneStartX += Helper.sineInterpolate((sceneWidth - paneStartX), 0, menuFadeInProgress, false, true);

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

	private void drawTextLogo(Graphics2D g, int sceneWidth, int sceneHeight) {
		// Draw text
		FontMetrics centerTextFontMetric = g.getFontMetrics(this.logoTextFont);
		Rectangle2D centerTextBound = centerTextFontMetric.getStringBounds(Constants.PROGRAM_NAME, g);

		g.setFont(this.logoTextFont);
		float centerTextX = (float) (sceneWidth - centerTextBound.getWidth()) / 2.0f;
		float centerTextY = (float) ((sceneHeight - centerTextBound.getHeight()) / 2.0f
				+ centerTextFontMetric.getAscent());

		float blendRatio = 1;
		if (cumulativeStep < 100 * 30) {
			blendRatio = cumulativeStep / (float) (100 * 30);
		}

		if (cumulativeStep > 100 * 120) {
			centerTextX -= Helper.sineInterpolate(0, sceneWidth / 5, (cumulativeStep - 100 * 120) / (float) (100 * 60));
		}

		g.setColor(Helper.blendColor(ColorSwatch.BACKGROUND, ColorSwatch.SHADOW, blendRatio));
		g.drawString(Constants.PROGRAM_NAME, centerTextX + 5, centerTextY + 5);

		g.setColor(Helper.blendColor(ColorSwatch.BACKGROUND, ColorSwatch.FOREGROUND, blendRatio));
		g.drawString(Constants.PROGRAM_NAME, centerTextX, centerTextY);

		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(new BasicStroke(5));

		// Draw line
		if (cumulativeStep > 100 * 30 && cumulativeStep < 100 * 75) {
			float topLineStartRatio = 1;
			if (cumulativeStep < 100 * 60) {
				topLineStartRatio = Helper.sineInterpolate((cumulativeStep - 100 * 30) / 100f / 30);
				topLineStartRatio *= topLineStartRatio; // For more power
			}
			float lineEndRatio = 0;
			if (cumulativeStep > 100 * 45) {
				lineEndRatio = Helper.sineInterpolate((cumulativeStep - 100 * 45) / 100f / 30);
				lineEndRatio *= lineEndRatio; // For more power
			}

			int topLineY = (int) ((sceneHeight - centerTextBound.getHeight()) / 2.0f);
			int topLineStartX = (int) (sceneWidth - centerTextBound.getWidth()) / 2;
			g.drawLine(topLineStartX + (int) (centerTextBound.getWidth() * topLineStartRatio), topLineY,
					topLineStartX + (int) (centerTextBound.getWidth() * lineEndRatio), topLineY);
		}
		int bottomDelay = 10;
		if (cumulativeStep > 100 * (30 + bottomDelay) && cumulativeStep < 100 * (75 + bottomDelay)) {
			float topLineStartRatio = 1;
			if (cumulativeStep < 100 * (60 + bottomDelay)) {
				topLineStartRatio = Helper.sineInterpolate((cumulativeStep - 100 * (30 + bottomDelay)) / 100f / 30);
				topLineStartRatio *= topLineStartRatio; // For more power
			}
			float lineEndRatio = 0;
			if (cumulativeStep > 100 * (45 + bottomDelay)) {
				lineEndRatio = Helper.sineInterpolate((cumulativeStep - 100 * (45 + bottomDelay)) / 100f / 30);
				lineEndRatio *= lineEndRatio; // For more power
			}

			int bottomLineY = (int) ((sceneHeight + centerTextBound.getHeight()) / 2.0f);
			int bottomLineStartX = (int) (sceneWidth + centerTextBound.getWidth()) / 2;
			g.drawLine(bottomLineStartX - (int) (centerTextBound.getWidth() * topLineStartRatio), bottomLineY,
					bottomLineStartX - (int) (centerTextBound.getWidth() * lineEndRatio), bottomLineY);
		}
	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		drawTextLogo(g, sceneWidth, sceneHeight);

		drawMenuItem(g, sceneWidth, sceneHeight);
	}

}
