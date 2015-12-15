package stage.editor;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import util.Helper;
import util.Constants.ColorSwatch;

public abstract class Pane {

	private boolean visible = false;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void update(int step) {
		if (this.visible) {
			paneShowAnimTimer += step;
			if (paneShowAnimTimer > paneShowAnimDuration) {
				paneShowAnimTimer = paneShowAnimDuration;
			}
		} else {
			paneShowAnimTimer -= step;
			if (paneShowAnimTimer < 0) {
				paneShowAnimTimer = 0;
			}
		}
	}

	private int paneShowAnimTimer = 0;
	private int paneShowAnimDuration = 100 * 20;

	protected abstract int getMaxPaneWidth();

	protected int getCurrentPaneWidth() {
		return (int) Helper.sineInterpolate(0, getMaxPaneWidth(), (float) paneShowAnimTimer / paneShowAnimDuration,
				false, true);
	}

	protected abstract void drawPaneContent(Graphics2D g, int x, int y, int height);

	public int draw(Graphics2D g, int x, int y, int height) {
		int paneWidth = getCurrentPaneWidth();
		if (paneShowAnimTimer > 0) {
			Rectangle oldClip = g.getClipBounds();
			g.setClip(x, y, paneWidth, height);

			g.setColor(ColorSwatch.FOREGROUND);
			g.fillRect(x, y, paneWidth, height);

			drawPaneContent(g, x + paneWidth - getMaxPaneWidth(), y, height);

			g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.FOREGROUND,
					1 - (float) paneShowAnimTimer / paneShowAnimDuration));
			g.fillRect(x, y, paneWidth, height);

			g.setClip(oldClip);
		}
		return paneWidth;
	}
	
	public final boolean shouldRemovePane() {
		return !visible && (paneShowAnimTimer == 0);
	}

}
