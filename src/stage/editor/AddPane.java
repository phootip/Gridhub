package stage.editor;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.geom.Vector2;
import stage.gameobj.Slope;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;
import util.Resource.FontWeight;

public class AddPane extends Pane {

	private AddableObject selectedAddableObject = null;
	private int slopeAlignment;

	public int getSlopeAlignment() {
		return slopeAlignment;
	}

	public AddableObject getSelectedAddableObject() {
		return selectedAddableObject;
	}

	@Override
	public void update(int step) {
		super.update(step);
		if (isVisible()) {
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_1)) {
				selectedAddableObject = AddableObject.BOX;
			}
			if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_2)) {
				if (selectedAddableObject != AddableObject.SLOPE) {
					selectedAddableObject = AddableObject.SLOPE;
					slopeAlignment = Slope.ALIGNMENT_RIGHT;
				} else {
					switch (slopeAlignment) {
						case Slope.ALIGNMENT_RIGHT:
							slopeAlignment = Slope.ALIGNMENT_DOWN;
							break;
						case Slope.ALIGNMENT_DOWN:
							slopeAlignment = Slope.ALIGNMENT_LEFT;
							break;
						case Slope.ALIGNMENT_LEFT:
							slopeAlignment = Slope.ALIGNMENT_UP;
							break;
						case Slope.ALIGNMENT_UP:
							slopeAlignment = Slope.ALIGNMENT_RIGHT;
							break;
					}
				}
			}
		}
	}

	@Override
	protected int getMaxPaneWidth() {
		return 300;
	}

	private void drawAddableObjectList(Graphics2D g, int x, int y) {
		Font normalItemFont = Resource.getInstance().getDefaultFont(40, FontWeight.REGULAR);
		Font selectedItemFont = Resource.getInstance().getDefaultFont(40, FontWeight.BOLD);

		int left = x + 20;
		int width = getMaxPaneWidth() - 40;
		int height = 100;
		int top = y;

		for (AddableObject obj : AddableObject.values()) {
			if (obj == selectedAddableObject) {
				g.setColor(ColorSwatch.BACKGROUND);
				g.fillRect(left, top, width, height);

				g.setColor(ColorSwatch.FOREGROUND);
				g.setFont(selectedItemFont);
			} else {
				g.setColor(ColorSwatch.BACKGROUND);
				g.setFont(normalItemFont);
			}

			String text = "[" + obj.getKeyName() + "] " + obj.getObjectName();

			Vector2 textPos = Helper.getCenteredTextPosition(text, g.getFont(), g, left, top, width, height);
			g.drawString(text, left + 30, textPos.getY());

			top += height;
		}
	}

	@Override
	protected void drawPaneContent(Graphics2D g, int x, int y, int height) {
		Font headerFont = Resource.getInstance().getDefaultFont(80, FontWeight.REGULAR);
		g.setColor(ColorSwatch.BACKGROUND);
		g.setFont(headerFont);
		g.drawString("Add", x + 50, y + 40 + g.getFontMetrics().getAscent());

		drawAddableObjectList(g, x, y + 150);
	}

}
