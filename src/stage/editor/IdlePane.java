package stage.editor;

import java.awt.Font;
import java.awt.Graphics2D;

import core.geom.Vector2;
import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;
import util.Resource.FontWeight;

public class IdlePane extends Pane {

	@Override
	protected int getMaxPaneWidth() {
		return 300;
	}

	private String[][] tipContents = { { "1", "Add" }, { "-", "Lower Floor" }, { "+", "Raise Floor" } };
	private int tipHeight = 50;
	private int tipSpacing = 20;

	@Override
	protected void drawPaneContent(Graphics2D g, int x, int y, int height) {
		Font tipFont = Resource.getInstance().getDefaultFont(40, FontWeight.BOLD);
		g.setFont(tipFont);

		for (int i = 0; i < tipContents.length; i++) {
			String keyString = tipContents[i][0];
			Vector2 tipPosition = Helper.getCenteredTextPosition(keyString, tipFont, g, x + 30,
					y + height - (tipContents.length - i) * (tipHeight + tipSpacing), 50, tipHeight);

			g.setColor(ColorSwatch.FOREGROUND);
			g.fillRoundRect(x + 30, y + height - (tipContents.length - i) * (tipHeight + tipSpacing), 50, tipHeight, 10,
					10);
			g.setColor(ColorSwatch.BACKGROUND);
			g.drawString(keyString, tipPosition.getX(), tipPosition.getY());
			g.setColor(ColorSwatch.FOREGROUND);
			g.drawString(tipContents[i][1], x + 100, tipPosition.getY());
		}

	}

}
