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
import stage.FloorLevel;
import stage.ObjectMap;
import util.Constants;
import util.Resource;
import util.Constants.ColorSwatch;

/**
 * This class represents the object Block 
 * @author Thanat Jatuphattharachat
 *	
 */
public class Block implements PushableObject, WalkThroughable {

	private static final float BLOCK_SIZE = 0.5f;
	protected static final float BLOCK_HEIGHT = 1.0f;
	private int x, y, z, nextX, nextY, nextZ, weight;
	private Vector3 drawPosition;
	private transient boolean isWalkThroughable;
	private transient FloorLevel floorLevelMap;
	private transient ObjectMap objectMap;
	// private boolean isObjectAbove;
	
	public int getX() {
		return x;
	}

	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getWeight() {
		return weight;
	}

	public Block(int x, int y, int z, FloorLevel floorLevelMap) {
		this(x, y, z, 50, false, floorLevelMap);
	}

	public void setZ(int diffZ) {
		if (z + diffZ < 0)
			return;
		IDrawable nextZObject = objectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z + diffZ));
		if (nextZObject instanceof Block)
			return;
		if (nextZObject instanceof Slope)
			return;

		if (objectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z + diffZ, "Player" + util.Constants.PLAYER1_ID)) != null
				|| objectMap.drawableObjectHashMap
						.get(new ObjectVector(x, y, z + diffZ, "Player" + util.Constants.PLAYER2_ID)) != null)
			return;

		objectMap.drawableObjectHashMap.remove(new ObjectVector(x, y, this.z));
		this.z += diffZ;
		objectMap.drawableObjectHashMap.put(new ObjectVector(x, y, this.z), this);
	}

	public Block(int x, int y, int z, int weight, boolean isWalkThroughable, FloorLevel floorLevelMap) {
		this.floorLevelMap = floorLevelMap;
		this.x = x;
		this.y = y;
		this.z = z;
		this.nextX = x;
		this.nextY = y;
		this.nextZ = z;
		this.weight = weight;
		this.isWalkThroughable = isWalkThroughable;
		
		this.drawPosition = new Vector3(x, y, z);
	}

	public boolean isPushable() {

		if (weight >= 100 || objectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z + 1)) != null)
			return false;
		if (objectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z + 1, "Player" + util.Constants.PLAYER1_ID)) != null
				|| objectMap.drawableObjectHashMap
						.get(new ObjectVector(x, y, z + 1, "Player" + util.Constants.PLAYER2_ID)) != null)
			return false;
		return true;
	}

	public boolean push(int previousWeight, int diffX, int diffY, int diffZ) {
		if (z + diffZ < 0)
			return false;
		if (floorLevelMap.isOutOfMap(x + diffX, y + diffY))
			return false;
		if (this.weight + previousWeight > 100 || !isPushable())
			return false;
		if (objectMap.drawableObjectHashMap
				.get(new ObjectVector(x + diffX, y + diffY, z + diffZ, "Player" + util.Constants.PLAYER1_ID)) != null
				|| objectMap.drawableObjectHashMap.get(new ObjectVector(x + diffX, y + diffY, z + diffZ,
						"Player" + util.Constants.PLAYER2_ID)) != null)
			return false;

		IDrawable nextObjectObstacles = objectMap.drawableObjectHashMap
				.get(new ObjectVector(x + diffX, y + diffY, z + diffZ));
		if (nextObjectObstacles instanceof TeleportGate || nextObjectObstacles instanceof FinishArea
				|| nextObjectObstacles instanceof TeleportDestionation)
			return false;
		if (z != floorLevelMap.getZValueFromXY(x + diffX, y + diffY)) {
			IDrawable nextObjectBelow = objectMap.drawableObjectHashMap
					.get(new ObjectVector(x + diffX, y + diffY, z + diffZ - 1));

			if (!(nextObjectBelow instanceof Block))
				return false;
		}

		if (nextObjectObstacles == null || nextObjectObstacles instanceof IWalkOnAble) {
			objectMap.drawableObjectHashMap.put(new ObjectVector(x + diffX, y + diffY, z + diffZ), this);
			objectMap.drawableObjectHashMap.remove(new ObjectVector(x, y, z));
			this.x += diffX;
			this.y += diffY;
			this.z += diffZ;
			return true;

		} else {
			if (nextObjectObstacles instanceof PushableObject) {
				boolean isPushed = ((PushableObject) nextObjectObstacles).push(previousWeight + this.weight, diffX,
						diffY, diffZ);
				if (isPushed) {
					objectMap.drawableObjectHashMap.put(new ObjectVector(x + diffX, y + diffY, z + diffZ), this);
					objectMap.drawableObjectHashMap.remove(new ObjectVector(x, y, z));
					this.x += diffX;
					this.y += diffY;
					this.z += diffZ;
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isWalkThroughable() {
		return isWalkThroughable;
	}
	

	public void setFloorLevelMap(FloorLevel floorLevelMap) {
		this.floorLevelMap = floorLevelMap;
	}



	private static HashMap<Camera, BufferedImage> cachedBoxImg = new HashMap<>();
	private static int cachedBoxImgSize = 150;
	private static final float[][] cornerShifter = new float[][] { { -BLOCK_SIZE, -BLOCK_SIZE },
			{ +BLOCK_SIZE, -BLOCK_SIZE }, { +BLOCK_SIZE, +BLOCK_SIZE }, { -BLOCK_SIZE, +BLOCK_SIZE } };

	public static void drawBlock(Graphics2D g, Camera camera, Vector3 pos, boolean isRawDrawPosition) {

		float x = pos.getX();
		float y = pos.getY();
		float z = pos.getZ();

		Vector2[] basis = new Vector2[4];
		for (int i = 0; i < 4; i++) {
			basis[i] = camera.getDrawPosition(x + cornerShifter[i][0], y + cornerShifter[i][1], z, isRawDrawPosition);
		}

		int furthestBaseId = 0;
		Vector2 furthestBase = basis[0];

		for (int i = 1; i < 4; i++) {
			if (basis[i].getY() < furthestBase.getY()) {
				furthestBaseId = i;
				furthestBase = basis[i];
			}
		}

		Vector2[] outerBorder = new Vector2[6];
		Vector2 innerPoint;

		outerBorder[0] = basis[(furthestBaseId + 2) % 4];
		outerBorder[1] = basis[(furthestBaseId + 3) % 4];
		outerBorder[2] = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 3) % 4][0],
				y + cornerShifter[(furthestBaseId + 3) % 4][1], z + BLOCK_HEIGHT, isRawDrawPosition);
		outerBorder[3] = camera.getDrawPosition(x + cornerShifter[furthestBaseId][0],
				y + cornerShifter[furthestBaseId][1], z + BLOCK_HEIGHT, isRawDrawPosition);
		outerBorder[4] = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 1) % 4][0],
				y + cornerShifter[(furthestBaseId + 1) % 4][1], z + BLOCK_HEIGHT, isRawDrawPosition);
		outerBorder[5] = basis[(furthestBaseId + 1) % 4];

		innerPoint = camera.getDrawPosition(x + cornerShifter[(furthestBaseId + 2) % 4][0],
				y + cornerShifter[(furthestBaseId + 2) % 4][1], z + BLOCK_HEIGHT, isRawDrawPosition);

		int[] outerBorderCoordX = new int[6];
		int[] outerBorderCoordY = new int[6];
		for (int i = 0; i < 6; i++) {
			outerBorderCoordX[i] = (int) outerBorder[i].getX();
			outerBorderCoordY[i] = (int) outerBorder[i].getY();
		}

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		g.setStroke(Resource.getGameObjectThickStroke());
		g.setColor(ColorSwatch.FOREGROUND);
		g.drawPolygon(new Polygon(outerBorderCoordX, outerBorderCoordY, 6));

		g.setStroke(Resource.getGameObjectThinStroke());
		g.drawLine(innerPoint.getIntX(), innerPoint.getIntY(), outerBorder[0].getIntX(), outerBorder[0].getIntY());
		g.drawLine(innerPoint.getIntX(), innerPoint.getIntY(), outerBorder[2].getIntX(), outerBorder[2].getIntY());
		g.drawLine(innerPoint.getIntX(), innerPoint.getIntY(), outerBorder[4].getIntX(), outerBorder[4].getIntY());
	}

	public static void refreshDrawCache(Camera camera) {
		if (Constants.CACHE_DRAWABLE) {
			boolean requireRedraw = camera.isDeformationChanged();

			if (!cachedBoxImg.containsKey(camera)) {
				cachedBoxImg.put(camera, DrawManager.getInstance().createBlankBufferedImage(cachedBoxImgSize,
						cachedBoxImgSize, Transparency.BITMASK));
				requireRedraw = true;
			}

			if (requireRedraw) {
				Graphics2D g = cachedBoxImg.get(camera).createGraphics();

				g.setComposite(AlphaComposite.Src);
				g.setColor(new Color(0, 0, 0, 0));
				g.fillRect(0, 0, cachedBoxImgSize, cachedBoxImgSize); // Clears the image.
				g.setComposite(AlphaComposite.SrcOver);

				g.setTransform(AffineTransform.getTranslateInstance(cachedBoxImgSize / 2, cachedBoxImgSize / 2));
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				drawBlock(g, camera, Vector3.ZERO, true);

				g.dispose();
			}
		}
	}

	public void draw(Graphics2D g, Camera camera) {

		if (Constants.CACHE_DRAWABLE) {
			Vector2 drawPosition2 = camera.getDrawPosition(drawPosition);

			// g.drawImage(cachedBoxImg, drawPosition2.getIntX() - cachedBoxImgSize / 2,
			// drawPosition2.getIntY() - cachedBoxImgSize / 2, null);
			g.drawImage(cachedBoxImg.get(camera), null, drawPosition2.getIntX() - cachedBoxImgSize / 2,
					drawPosition2.getIntY() - cachedBoxImgSize / 2);
		} else {
			drawBlock(g, camera, drawPosition, false);
		}

	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(x, y, z);
	}

	public void update(int step) {
		drawPosition.add(new Vector3(x, y, z).subtract(drawPosition)
				.multiply((float) (1 / Math.pow(Math.pow(5, 1.0 / 100), step))));
	}

	public ObjectVector getObjectVector() {
		return new ObjectVector(x, y, z);
	}

}
