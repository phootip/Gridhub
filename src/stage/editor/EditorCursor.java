package stage.editor;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import com.sun.glass.events.KeyEvent;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.FloorLevel;
import stage.gameobj.ICameraAssignable;
import stage.gameobj.IDrawable;
import util.InputManager;
import util.Constants;
import util.Constants.ColorSwatch;
import util.Constants.PlayerHelper;
import util.Helper;

public class EditorCursor implements IDrawable, ICameraAssignable {

	private FloorLevel floorLevelMap;
	private int currentX;
	private int currentY;
	private int currentZ;

	private Camera assignedCamera;

	@Override
	public void assignCamera(Camera camera) {
		this.assignedCamera = camera;
	}

	public EditorCursor(FloorLevel floorLevelMap) {
		this(floorLevelMap, floorLevelMap.getSizeX() / 2, floorLevelMap.getSizeY() / 2);
	}

	public EditorCursor(FloorLevel floorLevelMap, int initialX, int initialY) {
		this.floorLevelMap = floorLevelMap;
		this.currentX = initialX;
		this.currentY = initialY;
		updateCurrentZ();
	}

	private void updateCurrentZ() {
		this.currentZ = floorLevelMap.getZValueFromXY(currentX, currentY);
	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(currentX, currentY, currentZ - 0.45f);
	}

	public void update(int step) {
		int yDir = 0;
		int xDir = 0;

		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_UP, false)) {
			yDir--;
		}
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_DOWN, false)) {
			yDir++;
		}
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_LEFT, false)) {
			xDir--;
		}
		if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_RIGHT, false)) {
			xDir++;
		}

		if (xDir != 0 || yDir != 0) {
			if (assignedCamera != null) {
				switch (assignedCamera.getRotation()) {
					case 0:
						currentX += xDir;
						currentY += yDir;
						break;
					case 1:
						currentX += yDir;
						currentY -= xDir;
						break;
					case 2:
						currentX -= xDir;
						currentY -= yDir;
						break;
					case 3:
						currentX -= yDir;
						currentY += xDir;
						break;
				}
				updateCurrentZ();
			}
		}
	}

	private static final float CURSOR_SIZE = 0.4f;
	private float[][] rectShifter = { { CURSOR_SIZE, CURSOR_SIZE }, { -CURSOR_SIZE, CURSOR_SIZE },
			{ -CURSOR_SIZE, -CURSOR_SIZE }, { CURSOR_SIZE, -CURSOR_SIZE } };

	@Override
	public void draw(Graphics2D g, Camera camera) {
		int[] drawPositionX = new int[4];
		int[] drawPositionY = new int[4];
		for (int i = 0; i < 4; i++) {
			Vector2 drawPos = camera.getDrawPosition(currentX + rectShifter[i][0], currentY + rectShifter[i][1],
					currentZ);
			drawPositionX[i] = drawPos.getIntX();
			drawPositionY[i] = drawPos.getIntY();
		}

		g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.FOREGROUND, 0.5f));
		g.fillPolygon(drawPositionX, drawPositionY, 4);

		g.setStroke(new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawPolygon(drawPositionX, drawPositionY, 4);

		g.setStroke(new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.FOREGROUND, 0.5f));
		g.drawPolygon(drawPositionX, drawPositionY, 4);
	}

	private static final BasicStroke OVERLAY_STROKE = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
			10, new float[] { 5f, 4f }, 0);

	public void drawOverlay(Graphics2D g, Camera camera) {
		int[] drawPositionX = new int[4];
		int[] drawPositionY = new int[4];
		for (int i = 0; i < 4; i++) {
			Vector2 drawPos = camera.getDrawPosition(currentX + rectShifter[i][0], currentY + rectShifter[i][1],
					currentZ);
			drawPositionX[i] = drawPos.getIntX();
			drawPositionY[i] = drawPos.getIntY();
		}

		g.setStroke(OVERLAY_STROKE);
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawPolygon(drawPositionX, drawPositionY, 4);
	}

}
