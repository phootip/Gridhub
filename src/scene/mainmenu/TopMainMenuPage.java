package scene.mainmenu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.sun.glass.events.KeyEvent;

import util.Constants;
import util.Helper;
import util.InputManager;
import util.Resource;
import util.Constants.ColorSwatch;

public class TopMainMenuPage extends MainMenuPage {

	Font logoTextFont;
	Font menuTextFont;
	Font selectedMenuTextFont;

	private long cumulativeStep = 0;
	private int hiddenShifterStep = 0;
	private int hiddenShifterDuration = 100 * 30;

	protected TopMainMenuPage(MainMenuScene parent) {
		super(parent);

		this.logoTextFont = Resource.getInstance().getDefaultFont(200);
		this.menuTextFont = Resource.getInstance().getDefaultFont(60, Resource.FontWeight.BOOK);
		this.selectedMenuTextFont = Resource.getInstance().getDefaultFont(75, Resource.FontWeight.BOLD);
	}

	protected void skipIntroAnimation() {
		cumulativeStep = 100 * 180;
		menuFadeInProgress = 1;
	}

	@Override
	protected void update(int step) {
		cumulativeStep += step;

		if (cumulativeStep > 100 * 120) {
			if (cumulativeStep > 100 * 165) {
				cumulativeStep = 100 * 165;
				if (this.isVisible && InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
					switch (selectingMenu) {
					case 0:
						this.parent.setPage(MainMenuScene.PageName.ONE_PLAYER);
						break;
					case 1:
						this.parent.setPage(MainMenuScene.PageName.TWO_PLAYER);
						break;
					case 2:
						this.parent.setPage(MainMenuScene.PageName.LEVEL_EDITOR);
						break;
					case 3:
						this.parent.setPage(MainMenuScene.PageName.OPTION);
						break;
					case 4:
						this.parent.setPage(MainMenuScene.PageName.ABOUT);
						break;
					}
				}
			}

			if (this.isVisible) {
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP)) {
					selectingMenu--;
				}
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN)) {
					selectingMenu++;
				}
			}
			selectingMenu = (selectingMenu + menuItem.length) % menuItem.length;

			currentMenuPosition += (selectingMenu - currentMenuPosition) / Math.pow(Math.pow(5, 1.0 / 100), step);
			currentMenuHighlighterWidth += (currentMenuItemWidth - currentMenuHighlighterWidth)
					/ Math.pow(Math.pow(3, 1.0 / 100), step);

			menuFadeInProgress += step / (100f * 45);
			if (menuFadeInProgress > 1) {
				menuFadeInProgress = 1;
			}
		}

		if (!this.isVisible) {
			hiddenShifterStep += step;
			if (hiddenShifterStep > hiddenShifterDuration) {
				hiddenShifterStep = hiddenShifterDuration;
			}
		} else {
			hiddenShifterStep -= step;
			if (hiddenShifterStep < 0) {
				hiddenShifterStep = 0;
			}
		}
	}

	private final String[] menuItem = new String[] { "Single Player", "Co-op Mode", "Level Editor", "Option",
			"About Us" };
	private final float menuItemSpacing = 30f;
	private int selectingMenu = 0;
	private float currentMenuPosition = 0f;
	private int currentMenuHighlighterWidth = 0;
	private int currentMenuItemWidth = 0;
	private float menuFadeInProgress = 0;

	private int getHiddenShifter(int sceneWidth) {
		return (int) Helper.sineInterpolate(0, sceneWidth / 2, (float) hiddenShifterStep / hiddenShifterDuration);
	}

	private void drawMenuItem(Graphics2D g, int sceneWidth, int sceneHeight) {
		int hiddenShifter = getHiddenShifter(sceneWidth);

		FontMetrics menuItemFontMetric = g.getFontMetrics(selectedMenuTextFont);
		float fontHeight = menuItemFontMetric.getHeight();
		float fontAscent = menuItemFontMetric.getAscent();

		float baseFontY = (sceneHeight - fontHeight) / 2f + fontAscent;
		baseFontY -= (fontHeight + menuItemSpacing) * currentMenuPosition;

		int paneStartX = sceneWidth * 3 / 5;
		int paneWidth = sceneWidth - 100 - paneStartX;
		paneStartX += Helper.sineInterpolate((sceneWidth - paneStartX), 0, menuFadeInProgress, true, true);
		paneStartX -= hiddenShifter;

		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g.fillRect(paneStartX, 0, paneWidth, sceneHeight);

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(paneStartX + (int) (paneWidth - currentMenuHighlighterWidth - 60) / 2,
				(int) (sceneHeight - fontHeight) / 2, (int) currentMenuHighlighterWidth + 60, (int) fontHeight);

		for (int i = 0; i < menuItem.length; i++) {
			int menuItemTextWidth;
			if (i == selectingMenu) {
				g.setColor(ColorSwatch.FOREGROUND);
				g.setFont(selectedMenuTextFont);
				menuItemTextWidth = g.getFontMetrics().stringWidth(menuItem[i]);
				currentMenuItemWidth = menuItemTextWidth;
			} else {
				g.setColor(ColorSwatch.BACKGROUND);
				g.setFont(menuTextFont);
				menuItemTextWidth = g.getFontMetrics().stringWidth(menuItem[i]);
			}
			g.drawString(menuItem[i], paneStartX + (paneWidth - menuItemTextWidth) / 2,
					baseFontY + i * (fontHeight + menuItemSpacing));
		}
	}

	private void drawTextLogo(Graphics2D g, int sceneWidth, int sceneHeight) {
		int hiddenShifter = getHiddenShifter(sceneWidth);

		// Draw text
		FontMetrics centerTextFontMetric = g.getFontMetrics(this.logoTextFont);
		Rectangle2D centerTextBound = centerTextFontMetric.getStringBounds(Constants.PROGRAM_NAME, g);

		g.setFont(this.logoTextFont);
		float centerTextX = (float) (sceneWidth - centerTextBound.getWidth()) / 2.0f - hiddenShifter;
		float centerTextY = (float) ((sceneHeight - centerTextBound.getHeight()) / 2.0f
				+ centerTextFontMetric.getAscent());

		float blendRatio = 1;
		if (cumulativeStep < 100 * 30) {
			blendRatio = cumulativeStep / (float) (100 * 30);
		}

		if (cumulativeStep > 100 * 120) {
			centerTextX -= Helper.sineInterpolate(0, sceneWidth / 5, (cumulativeStep - 100 * 120) / (float) (100 * 45));
		}

		g.setColor(Helper.blendColor(ColorSwatch.BACKGROUND, ColorSwatch.SHADOW, blendRatio));
		g.drawString(Constants.PROGRAM_NAME, centerTextX + 5, centerTextY + 5);

		g.setColor(Helper.blendColor(ColorSwatch.BACKGROUND, ColorSwatch.FOREGROUND, blendRatio));
		g.drawString(Constants.PROGRAM_NAME, centerTextX, centerTextY);

		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));

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
	protected void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		if (hiddenShifterStep < hiddenShifterDuration) {
			drawTextLogo(g, sceneWidth, sceneHeight);
			drawMenuItem(g, sceneWidth, sceneHeight);

			// Draw shifter fade out
			if (hiddenShifterStep > 0) {
				g.setColor(
						Helper.getAlphaColorPercentage(Color.BLACK, hiddenShifterStep * 0.8f / hiddenShifterDuration));
				g.fillRect(0, 0, sceneWidth, sceneHeight);
			}
		}
	}

	@Override
	protected String getPageName() {
		return null;
	}

}
