package stage.gameobj;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import core.DrawManager;
import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import util.Constants;
import util.Helper;
import util.Resource;
import util.Constants.ColorSwatch;

public class FloorPiece implements IDrawable {

	private static final float BOUNDARY_BORDER_PADDING = 0.5f;
	private static final float BOUNDARY_BORDER_SHIFTER = 0.55f;
	private int x, y, z;
	private boolean showPXBorder, showPYBorder, showNXBorder, showNYBorder;

	public FloorPiece(int x, int y, int z, boolean showPXBorder, boolean showPYBorder, boolean showNXBorder,
			boolean showNYBorder) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.showPXBorder = showPXBorder;
		this.showPYBorder = showPYBorder;
		this.showNXBorder = showNXBorder;
		this.showNYBorder = showNYBorder;
	}

	@Deprecated
	public FloorPiece(int x, int y, int z) {
		this(x, y, z, false, false, false, false);
	}

	public ObjectVector getObjectVector() {
		return new ObjectVector(x, y, z, "FloorPiece");
	}

	private static HashMap<Camera, BufferedImage[]> cachedFloorImg = new HashMap<>();
	private static int cachedFloorImgWidth = 64;
	private static int cachedFloorImgHeight = 46;
	private static final float OUTER_PADDING = 0.5f;
	private static final float INNER_PADDING = 0.4f;

	private static void drawFloor(Graphics2D g, Camera camera, float x, float y, float z, boolean isRawDrawPosition,
			boolean[] showBorderList) {

		if (Constants.CACHE_DRAWABLE
				&& (showBorderList[0] || showBorderList[1] || showBorderList[2] || showBorderList[3])) {
			g.drawImage(cachedFloorImg.get(camera)[0], null, cachedFloorImgWidth / -2, cachedFloorImgHeight / -2);
		} else {

			Vector2 cornerA = camera.getDrawPosition(x + OUTER_PADDING, y + OUTER_PADDING, z, isRawDrawPosition);
			Vector2 cornerB = camera.getDrawPosition(x - OUTER_PADDING, y + OUTER_PADDING, z, isRawDrawPosition);
			Vector2 cornerC = camera.getDrawPosition(x - OUTER_PADDING, y - OUTER_PADDING, z, isRawDrawPosition);
			Vector2 cornerD = camera.getDrawPosition(x + OUTER_PADDING, y - OUTER_PADDING, z, isRawDrawPosition);

			int[] outerPolygonX = new int[4];
			int[] outerPolygonY = new int[4];
			outerPolygonX[0] = cornerA.getIntX();
			outerPolygonY[0] = cornerA.getIntY();
			outerPolygonX[1] = cornerB.getIntX();
			outerPolygonY[1] = cornerB.getIntY();
			outerPolygonX[2] = cornerC.getIntX();
			outerPolygonY[2] = cornerC.getIntY();
			outerPolygonX[3] = cornerD.getIntX();
			outerPolygonY[3] = cornerD.getIntY();

			g.setColor(ColorSwatch.BACKGROUND);
			for (int i = 0; i < 5; i++) {
				g.fillPolygon(outerPolygonX, outerPolygonY, 4);
				for (int j = 0; j < 4; j++) {
					outerPolygonY[j]++;
				}
			}

			Vector2 cornerA2 = camera.getDrawPosition(x + INNER_PADDING, y + INNER_PADDING, z, isRawDrawPosition);
			Vector2 cornerB2 = camera.getDrawPosition(x - INNER_PADDING, y + INNER_PADDING, z, isRawDrawPosition);
			Vector2 cornerC2 = camera.getDrawPosition(x - INNER_PADDING, y - INNER_PADDING, z, isRawDrawPosition);
			Vector2 cornerD2 = camera.getDrawPosition(x + INNER_PADDING, y - INNER_PADDING, z, isRawDrawPosition);

			int[] innerPolygonX = new int[4];
			int[] innerPolygonY = new int[4];
			innerPolygonX[0] = cornerA2.getIntX();
			innerPolygonY[0] = cornerA2.getIntY();
			innerPolygonX[1] = cornerB2.getIntX();
			innerPolygonY[1] = cornerB2.getIntY();
			innerPolygonX[2] = cornerC2.getIntX();
			innerPolygonY[2] = cornerC2.getIntY();
			innerPolygonX[3] = cornerD2.getIntX();
			innerPolygonY[3] = cornerD2.getIntY();

			g.setStroke(Resource.getGameObjectThinStroke());
			g.setColor(ColorSwatch.SHADOW);
			g.drawPolygon(innerPolygonX, innerPolygonY, 4);

			g.setStroke(new java.awt.BasicStroke(7));
			g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.SHADOW, 0.2));
			g.drawPolygon(innerPolygonX, innerPolygonY, 4);

		}

		g.setStroke(Resource.getGameObjectThickStroke());
		g.setColor(ColorSwatch.FOREGROUND);

		if (showBorderList[0]) {
			Vector2 from = camera.getDrawPosition(x + BOUNDARY_BORDER_PADDING, y + BOUNDARY_BORDER_SHIFTER, z,
					isRawDrawPosition);
			Vector2 to = camera.getDrawPosition(x + BOUNDARY_BORDER_PADDING, y - BOUNDARY_BORDER_SHIFTER, z,
					isRawDrawPosition);
			g.drawLine(from.getIntX(), from.getIntY(), to.getIntX(), to.getIntY());
		}
		if (showBorderList[1]) {
			Vector2 from = camera.getDrawPosition(x - BOUNDARY_BORDER_SHIFTER, y + BOUNDARY_BORDER_PADDING, z,
					isRawDrawPosition);
			Vector2 to = camera.getDrawPosition(x + BOUNDARY_BORDER_SHIFTER, y + BOUNDARY_BORDER_PADDING, z,
					isRawDrawPosition);
			g.drawLine(from.getIntX(), from.getIntY(), to.getIntX(), to.getIntY());
		}
		if (showBorderList[2]) {
			Vector2 from = camera.getDrawPosition(x - BOUNDARY_BORDER_PADDING, y + BOUNDARY_BORDER_SHIFTER, z,
					isRawDrawPosition);
			Vector2 to = camera.getDrawPosition(x - BOUNDARY_BORDER_PADDING, y - BOUNDARY_BORDER_SHIFTER, z,
					isRawDrawPosition);
			g.drawLine(from.getIntX(), from.getIntY(), to.getIntX(), to.getIntY());
		}
		if (showBorderList[3]) {
			Vector2 from = camera.getDrawPosition(x + BOUNDARY_BORDER_SHIFTER, y - BOUNDARY_BORDER_PADDING, z,
					isRawDrawPosition);
			Vector2 to = camera.getDrawPosition(x - BOUNDARY_BORDER_SHIFTER, y - BOUNDARY_BORDER_PADDING, z,
					isRawDrawPosition);
			g.drawLine(from.getIntX(), from.getIntY(), to.getIntX(), to.getIntY());
		}

	}

	public static void refreshDrawCache(Camera camera) {
		if (Constants.CACHE_DRAWABLE && camera.isDeformationChanged()) {

			boolean requireRedraw = camera.isDeformationChanged();

			if (!cachedFloorImg.containsKey(camera)) {
				BufferedImage[] imgs = new BufferedImage[16];
				for (int i = 0; i < 16; i++) {
					imgs[i] = DrawManager.getInstance().createBlankBufferedImage(cachedFloorImgWidth,
							cachedFloorImgHeight, Transparency.BITMASK);
				}
				cachedFloorImg.put(camera, imgs);
				requireRedraw = true;
			}

			if (requireRedraw) {
				for (int i = 0; i < 16; i++) {
					Graphics2D g = cachedFloorImg.get(camera)[i].createGraphics();

					g.setComposite(AlphaComposite.Src);
					g.setColor(new Color(0, 0, 0, 0));
					g.fillRect(0, 0, cachedFloorImgWidth, cachedFloorImgHeight); // Clears the image.
					g.setComposite(AlphaComposite.SrcOver);

					g.setTransform(
							AffineTransform.getTranslateInstance(cachedFloorImgWidth / 2, cachedFloorImgHeight / 2));
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					drawFloor(g, camera, 0, 0, 0, true, decodeBorderHashing(i));

					g.dispose();
				}
			}
		}
	}

	private int getBorderHashing() {
		int ans = 0;
		if (showPXBorder)
			ans += 1 << 1;
		if (showPYBorder)
			ans += 1 << 2;
		if (showNXBorder)
			ans += 1 << 3;
		if (showNYBorder)
			ans += 1 << 4;

		return ans / 2;
	}

	private static boolean[] decodeBorderHashing(int num) {
		boolean[] ans = new boolean[4];
		for (int i = 0; i < 4; i++) {
			ans[i] = num % 2 == 1;
			num /= 2;
		}
		return ans;
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		if (Constants.CACHE_DRAWABLE) {
			Vector2 drawPosition = camera.getDrawPosition(x, y, z);

			// g.drawImage(cachedFloorImg[getBorderHashing()], drawPosition.getIntX() - cachedFloorImgWidth / 2,
			// drawPosition.getIntY() - cachedFloorImgHeight / 2, null);
			g.drawImage(cachedFloorImg.get(camera)[getBorderHashing()], null, drawPosition.getIntX() - cachedFloorImgWidth / 2,
					drawPosition.getIntY() - cachedFloorImgHeight / 2);
		} else {
			drawFloor(g, camera, x, y, z, false, decodeBorderHashing(getBorderHashing()));
		}

	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(x, y, z - 0.5f);
	}

}
