package stage.editor;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.geom.Vector2;
import util.Resource;
import util.Constants.ColorSwatch;
import util.Helper;
import util.InputManager;
import util.Resource.FontWeight;

public class AddPane extends Pane {

	public enum AddableObject {
		BOX("1", "Box");

		private String objectName;
		private String keyName;

		private AddableObject(String keyName, String objectName) {
			this.objectName = objectName;
			this.keyName = keyName;
		}

		protected String getKeyName() {
			return keyName;
		}

		protected String getObjectName() {
			return objectName;
		}
	}

	private AddableObject selectedAddableObject = null;

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

			Vector2 textPos = Helper.getCenteredTextPosition(text, g.getFont(), g, left, top, width,
					height);
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
