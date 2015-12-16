package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.ObjectMap;
import stage.gameobj.IDrawable;
import stage.gameobj.IWalkOnAble;
import util.Constants.ColorSwatch;
import util.Helper;

/**
 * @author Thanat
 *
 */
public abstract class TeleportGate implements IDrawable, IWalkOnAble, IControlable {
	protected int x, y, z;
	protected boolean isActive, isAsserted;
	protected transient ObjectMap objectMap;

	private static final float TELEPORT_RADIUS = 0.4f;

	public TeleportGate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		isAsserted = true;
	}

	protected abstract void teleport(Player p);

	// the progress checking is just a mock up. It should be set up later
	private static final int TELEPORT_PROGRESS_CONTROL = 100 * 50;
	protected int teleportProgress = 0;
	protected int teleportSpinSpeed = 0;
	protected float teleportSpinPhase = 0;

	public void setObjectMap(ObjectMap objectMap) {
		this.objectMap = objectMap;
	}

	public void update(int step) {

		if (!isObjectAbove()) {
			isActive = true;
		}
		if (isObjectAbove() && isActive && isAsserted) {
			teleportProgress += step;

		} else {
			teleportProgress = 0;
		}
		if (teleportProgress >= TELEPORT_PROGRESS_CONTROL) {
			teleportProgress = 0;
			teleport(getPlayerAbove());
		}

		if (teleportSpinSpeed < teleportProgress) {
			teleportSpinSpeed = teleportProgress;
		} else if (teleportSpinSpeed > teleportProgress) {
			teleportSpinSpeed -= 100 * 1;
			if (teleportSpinSpeed < teleportProgress) {
				teleportSpinSpeed = teleportProgress;
			}
		}

		teleportSpinPhase = (teleportSpinPhase + (float) teleportSpinSpeed / TELEPORT_PROGRESS_CONTROL / 4) % 1;

	}

	private static final int SPIN_POINT_COUNT = 3;
	private static final float spinPointAngle = 360.0f / SPIN_POINT_COUNT;

	public static void draw(Graphics2D g, Camera camera, ObjectVector position) {
		draw(g, camera, position, 0, 0, 0);
	}

	public static void draw(Graphics2D g, Camera camera, ObjectVector position, float teleportSpinPhase, int spinLength,
			int teleportSpinSpeed) {

		Vector2 centerPos = camera.getDrawPosition(position.toVector3());
		int width = (int) camera.getDrawSizeX(TELEPORT_RADIUS * 2);
		int height = (int) camera.getDrawSizeY(TELEPORT_RADIUS * 2);
		int x = (int) (centerPos.getX() - camera.getDrawSizeX(TELEPORT_RADIUS));
		int y = (int) (centerPos.getY() - camera.getDrawSizeY(TELEPORT_RADIUS));

		g.setColor(new Color(0, 150, 255, 150));
		g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int i = 0; i < SPIN_POINT_COUNT; i++) {
			g.drawArc(x, y, width, height, Math.round(spinPointAngle * i + teleportSpinPhase * spinPointAngle),
					spinLength);
		}
		for (int i = 0; i < SPIN_POINT_COUNT; i++) {
			g.setColor(new Color(255, 255, 255, 200));
			g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.drawArc(x, y, width, height, Math.round(spinPointAngle * i + teleportSpinPhase * spinPointAngle),
					spinLength);
		}

		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(new BasicStroke(2));
		g.drawOval(x, y, width, height);
		g.setColor(Helper.getAlphaColorPercentage(ColorSwatch.FOREGROUND, Helper.sineInterpolate(
				Helper.clamp(0, 1, ((float) teleportSpinSpeed / TELEPORT_PROGRESS_CONTROL - 0.5f) * 2), true, false)));
		g.fillOval(x, y, width, height);
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		draw(g, camera, new ObjectVector(x, y, z), teleportSpinPhase,
				Math.round(teleportSpinSpeed * spinPointAngle / TELEPORT_PROGRESS_CONTROL), teleportSpinSpeed);
	}

	@Override
	public boolean isObjectAbove() {
		// teleportGate check only player above
		if (getPlayerAbove() != null) {
			return true;
		}
		return false;
	}

	public Player getPlayerAbove() {
		if (objectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)) != null) {
			return (Player) (objectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)));

		} else if (objectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)) != null) {
			return (Player) (objectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)));
		}

		return null;
	}

	@Override
	public void activate() {
		this.isAsserted = true;

	}

	@Override
	public void deActivate() {
		this.isAsserted = false;

	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isAsserted() {
		return isAsserted;
	}

	public void setAsserted(boolean isAsserted) {
		this.isAsserted = isAsserted;
	}

	@Override
	public Vector3 getDrawPosition() {
		return new Vector3(this.x, this.y, this.z - 0.48f);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

}
