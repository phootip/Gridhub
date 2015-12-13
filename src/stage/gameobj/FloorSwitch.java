package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;



import core.geom.Vector2;
<<<<<<< HEAD:src/stage/gameobj/FloorSwitch.java
import stage.Camera;
=======
import stage.gameobj.Block;
import stage.gameobj.IDrawable;
import stage.gameobj.IWalkOnAble;
import stage.gameobj.Player;
import stage.gameobj.ObjectVector;
>>>>>>> origin/ControllerAndObjectFix:src/stage/FloorSwitch.java
import util.Helper;

public class FloorSwitch implements IDrawable,IWalkOnAble{

	private final float EPS = 1e-3f;

	private int x, y, z;
	private boolean defaultAssertion;
	private int minimumWeight;

	private int currentWeight = 0;

	protected int getX() {
		return x;
	}
	
	protected int getY() {
		return y;
	}

	protected int getZ() {
		return z;
	}

	protected boolean isDefaultAssertion() {
		return defaultAssertion;
	}

	protected int getMinimumWeight() {
		return minimumWeight;
	}
	
	

	public boolean isAsserting() {
		return isAsserting;
	}

	public FloorSwitch(int x, int y, int z, boolean defaultAssertion, int minimumWeight) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.defaultAssertion = defaultAssertion;
		this.minimumWeight = minimumWeight;
		this.currentWeight = 0;
		this.isAsserting = defaultAssertion;
		this.currentCenterAlpha = defaultAssertion ? 1 : 0;
	}

	// For side bar
	private float currentShowingWeight = 0;
	private final double showingWeightSpeedFactor = Math.pow(10, 1.0 / 100);
	// For center cell and assertion
	private int switchProgress = 0;
	private final int switchProgressFactor = 100 * 30;

	private boolean isAsserting;
	private float currentCenterAlpha;
	private final float centerAlphaSpeedFactor = 100 * 10.0f;

	public void update(int step) {

		// This is just for testing. In production, link FloorSwitch with the
		// HashMap instead.
		
		Player p = getPlayerAbove();
		Block b = getBlockAbove();
		
		if(p != null) {
			this.currentWeight = p.getWeight();
		} else if(b != null) {
			this.currentWeight = b.getWeight();
		} else {
			this.currentWeight = 0;
		}

		currentShowingWeight += (currentWeight - currentShowingWeight) / Math.pow(showingWeightSpeedFactor, step);

		this.isAsserting = this.defaultAssertion;
		if (currentWeight * switchProgressFactor >= switchProgress) {
			switchProgress += step * currentWeight;
			if (switchProgress > switchProgressFactor * minimumWeight) {
				switchProgress = switchProgressFactor * minimumWeight;
				this.isAsserting = !this.isAsserting;
			}
		} else {
			switchProgress = currentWeight;
			if (switchProgress < 0)
				switchProgress = 0;
		}

		if (this.isAsserting) {
			currentCenterAlpha += step / centerAlphaSpeedFactor;
			if (currentCenterAlpha > 1)
				currentCenterAlpha = 1;
		} else {
			currentCenterAlpha -= step / centerAlphaSpeedFactor;
			if (currentCenterAlpha < 0)
				currentCenterAlpha = 0;
		}

	}

	private final float innerCellShift = 0.4f;
	private final float innerCellSize = 0.8f;

	public void draw(Graphics2D g, Camera camera) {
		Color baseColor = new Color(200, 250, 100);

		Vector2 cornerA = camera.getDrawPosition(x + innerCellShift, y + innerCellShift, z);
		Vector2 cornerB = camera.getDrawPosition(x + innerCellShift, y - innerCellShift, z);
		Vector2 cornerC = camera.getDrawPosition(x - innerCellShift, y - innerCellShift, z);
		Vector2 cornerD = camera.getDrawPosition(x - innerCellShift, y + innerCellShift, z);

		Path2D.Float innerBorderPath = new Path2D.Float();
		innerBorderPath.moveTo(cornerA.getX(), cornerA.getY());
		innerBorderPath.lineTo(cornerB.getX(), cornerB.getY());
		innerBorderPath.lineTo(cornerC.getX(), cornerC.getY());
		innerBorderPath.lineTo(cornerD.getX(), cornerD.getY());
		innerBorderPath.closePath();

		g.setStroke(new BasicStroke(2));
		g.setColor(Helper.getAlphaColor(baseColor, 150));
		g.draw(innerBorderPath);

		g.setColor(Helper.getAlphaColorPercentage(baseColor, currentCenterAlpha / 2.0f));
		g.fill(innerBorderPath);

		if (currentShowingWeight > -EPS) {
			float lineLength = Math.min(minimumWeight, currentShowingWeight) * innerCellSize / minimumWeight;
			Vector2 toA = camera.getDrawPosition(x + innerCellShift, y + innerCellShift - lineLength, z);
			Vector2 toB = camera.getDrawPosition(x + innerCellShift - lineLength, y - innerCellShift, z);
			Vector2 toC = camera.getDrawPosition(x - innerCellShift, y - innerCellShift + lineLength, z);
			Vector2 toD = camera.getDrawPosition(x - innerCellShift + lineLength, y + innerCellShift, z);

			g.setStroke(new BasicStroke(4));
			g.setColor(new Color(150, 200, 50, 150));
			g.draw(new Line2D.Float(cornerA.getX(), cornerA.getY(), toA.getX(), toA.getY()));
			g.draw(new Line2D.Float(cornerB.getX(), cornerB.getY(), toB.getX(), toB.getY()));
			g.draw(new Line2D.Float(cornerC.getX(), cornerC.getY(), toC.getX(), toC.getY()));
			g.draw(new Line2D.Float(cornerD.getX(), cornerD.getY(), toD.getX(), toD.getY()));
		}
	}

	@Override
	public boolean isObjectAbove() {
		// teleportGate check only player above
		if (getPlayerAbove() != null || getBlockAbove() != null) {
			return true;
		}
		
		return false;
	}

	private Player getPlayerAbove() {
		if (ObjectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)) != null) {
			return (Player) (ObjectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER1_ID)));

		} else if (ObjectMap.drawableObjectHashMap
				.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)) != null) {
			return (Player) (ObjectMap.drawableObjectHashMap
					.get(new ObjectVector(x, y, z, "Player" + util.Constants.PLAYER2_ID)));
		}

		return null;
	}
	private Block getBlockAbove() {
		IDrawable objectAbove = ObjectMap.drawableObjectHashMap.get(new ObjectVector(x, y, z));
		if(objectAbove != null && objectAbove instanceof Block) {
			return (Block)objectAbove;
		}
		else return null;
	}
	
	
	
	
	@Override
	public float getDrawX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public float getDrawY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public float getDrawZ() {
		// TODO Auto-generated method stub
		return z;
	}

}
