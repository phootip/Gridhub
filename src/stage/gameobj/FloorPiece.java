package stage.gameobj;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import core.DrawManager;
import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import util.Constants;
import util.Helper;
import util.Resource;
import util.Constants.ColorSwatch;

public class FloorPiece implements IDrawable {

	private int x, y, z;

	public FloorPiece(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ObjectVector getObjectVector() {
		return new ObjectVector(x, y, z, "FloorPiece");
	}

	private static BufferedImage cachedFloorImg;
	private static int cachedFloorImgSize = 65;
	private static final float OUTER_PADDING = 0.5f;
	private static final float INNER_PADDING = 0.4f;

	private static void drawFloor(Graphics2D g, Camera camera, float x, float y, float z, boolean isRawDrawPosition) {

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

	public static void refreshDrawCache(Camera camera) {
		if (Constants.CACHE_DRAWABLE) {
			cachedFloorImg = DrawManager.getInstance().createBlankBufferedImage(cachedFloorImgSize, cachedFloorImgSize,
					Transparency.BITMASK);
			Graphics2D g = cachedFloorImg.createGraphics();

			g.setComposite(AlphaComposite.Src);
			g.setColor(new Color(0, 0, 0, 0));
			g.fillRect(0, 0, cachedFloorImgSize, cachedFloorImgSize); // Clears the image.
			g.setComposite(AlphaComposite.SrcOver);

			g.setTransform(AffineTransform.getTranslateInstance(cachedFloorImgSize / 2, cachedFloorImgSize / 2));
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			drawFloor(g, camera, 0, 0, 0, true);

			g.dispose();
		}
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		if (Constants.CACHE_DRAWABLE) {
			Vector2 drawPosition = camera.getDrawPosition(x, y, z);

			g.drawImage(cachedFloorImg, drawPosition.getIntX() - cachedFloorImgSize / 2,
					drawPosition.getIntY() - cachedFloorImgSize / 2, null);
		} else {
			drawFloor(g, camera, x, y, z, false);
		}
	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(x, y, z - 0.5f);
	}

}
