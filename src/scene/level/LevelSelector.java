package scene.level;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.PlayMode;
import core.geom.Vector2;
import util.Helper;
import util.InputManager;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Resource.FontWeight;

public class LevelSelector {

	private PlayMode selectedPlayMode;
	private int playModeAnimStep = 0;
	private final int playModeAnimLength = 100 * 10;

	public LevelSelector() {
		selectedPlayMode = PlayMode.SINGLE_PLAYER;
	}

	public void update(int step) {
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_RIGHT)
				|| InputManager.getInstance().isKeyTriggering(KeyEvent.VK_LEFT)) {
			// Toggle
			selectedPlayMode = (selectedPlayMode == PlayMode.SINGLE_PLAYER) ? PlayMode.COOP_MODE
					: PlayMode.SINGLE_PLAYER;
		}

		if (selectedPlayMode == PlayMode.SINGLE_PLAYER) {
			playModeAnimStep -= step;
			if (playModeAnimStep < 0) {
				playModeAnimStep = 0;
			}
		} else {
			playModeAnimStep += step;
			if (playModeAnimStep > playModeAnimLength) {
				playModeAnimStep = playModeAnimLength;
			}
		}
	}

	public void draw(Graphics2D g, int x, int y, int width, int height) {

		int modeSelectMargin = 50;
		float modeSelectAnimRatio = (float) playModeAnimStep / playModeAnimLength;
		Font modeSelectFont = Resource.getInstance().getDefaultFont(100, FontWeight.BOLD);

		g.setStroke(new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
		g.setFont(modeSelectFont);

		int coopModeRectLeft = x + (width + modeSelectMargin) / 2;
		int singlePlayerRectLeft = x;
		int modesRectTop = y;
		int modesRectWidth = (width - modeSelectMargin) / 2;
		int modesRectHeight = height;

		// Single player
		String singlePlayerText = "Single Player";

		Vector2 singlePlayerTextPos = Helper.getCenteredTextPosition(singlePlayerText, modeSelectFont, g, singlePlayerRectLeft,
				modesRectTop, modesRectWidth, modesRectHeight);

		g.setColor(Helper.blendColor(ColorSwatch.FOREGROUND, ColorSwatch.SHADOW, modeSelectAnimRatio));
		g.drawRect(singlePlayerRectLeft, modesRectTop, modesRectWidth, modesRectHeight);
		g.drawString(singlePlayerText, singlePlayerTextPos.getX(), singlePlayerTextPos.getY());

		// Co-op
		String coopModeText = "Co-op Mode";

		Vector2 coopModeTextPos = Helper.getCenteredTextPosition(coopModeText, modeSelectFont, g, coopModeRectLeft,
				modesRectTop, modesRectWidth, modesRectHeight);

		g.setColor(Helper.blendColor(ColorSwatch.FOREGROUND, ColorSwatch.SHADOW, 1 - modeSelectAnimRatio));
		g.drawRect(coopModeRectLeft, modesRectTop, modesRectWidth, modesRectHeight);
		g.drawString(coopModeText, coopModeTextPos.getX(), coopModeTextPos.getY());
	}

}
