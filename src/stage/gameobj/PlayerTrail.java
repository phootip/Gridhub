package stage.gameobj;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import util.Helper;

public class PlayerTrail implements ILargeDrawable {

	class PlayerTrailDot {
		private Vector3 cloudDotPosition;
		private Vector3 cloudDotDirection;
		private Vector3 startPosition, endPosition;
		private boolean renderCloud;

		private int cloudAlphaFader = 0;
		private final int cloudAlphaFaderMax = 2000;

		public PlayerTrailDot(Vector3 startPosition, Vector3 endPosition, boolean renderCloud) {
			this.startPosition = new Vector3(startPosition);
			this.endPosition = new Vector3(endPosition);

			this.renderCloud = renderCloud;

			this.cloudDotPosition = new Vector3(startPosition);
			this.cloudDotDirection = new Vector3(1, 0, 0).rotateXY(Math.random() * Math.PI * 2)
					.rotateXZ(Math.random() * Math.PI * 2).rotateYZ(Math.random() * Math.PI * 2);
		}

		public void update(int step) {
			this.cloudDotPosition.add(new Vector3(cloudDotDirection).multiply((float) (0.00005 * step)));
			cloudAlphaFader += step;
			if (cloudAlphaFader > cloudAlphaFaderMax)
				cloudAlphaFader = cloudAlphaFaderMax;
		}

		public void draw(Graphics2D g, Camera camera) {
			float animRatio = 1 - (float) cloudAlphaFader / cloudAlphaFaderMax;

			if (renderCloud && animRatio > 0) {

				Vector2 drawPos = camera.getDrawPosition(cloudDotPosition);

				g.setStroke(glowTrailCloudStroke);
				g.setColor(Helper.getAlphaColorPercentage(glowTrailCloudColor,
						glowTrailCloudColor.getAlpha() * animRatio / 255));
				g.drawLine(drawPos.getIntX(), drawPos.getIntY(), drawPos.getIntX(), drawPos.getIntY());

				g.setStroke(maignTrailCloudStroke);
				g.setColor(Helper.getAlphaColorPercentage(mainTrailCloudColor,
						mainTrailCloudColor.getAlpha() * animRatio / 255));
				g.drawLine(drawPos.getIntX(), drawPos.getIntY(), drawPos.getIntX(), drawPos.getIntY());
			}

			// Liner

			Vector2 drawFrom = camera.getDrawPosition(startPosition);
			Vector2 drawTo = camera.getDrawPosition(endPosition);

			g.setStroke(mainTrailStroke);
			g.setColor(mainTrailColor);
			g.drawLine(drawFrom.getIntX(), drawFrom.getIntY(), drawTo.getIntX(), drawTo.getIntY());
		}

	}

	private static float BALL_TRAIL_RADIUS = 0.4f;
	private static int MAX_TRAIL_LENGTH = 50;

	private Player player;

	private Stroke mainTrailStroke;
	private Color mainTrailColor;
	private Stroke maignTrailCloudStroke;
	private Color mainTrailCloudColor;
	private Stroke glowTrailCloudStroke;
	private Color glowTrailCloudColor;

	private ArrayList<ArrayList<Vector3>> trailPosition;
	private ArrayList<ArrayList<Vector3>> shiftedTrailPosition;

	private Map<Vector3, PlayerTrailDot> dots;

	private int stepCounter = 0;

	public void update(int step, float diffX, float diffY) {

		stepCounter += step;
		boolean wantCloud = false;
		if (stepCounter > 200) {
			wantCloud = true;
			while (stepCounter > 200) {
				stepCounter -= 200;
			}
		}

		float angleXZ = -diffX / Player.BALL_RADIUS;
		float angleYZ = -diffY / Player.BALL_RADIUS;

		for (int i = 0; i < trailPosition.size(); i++) {
			ArrayList<Vector3> trail = trailPosition.get(i);

			Vector3 newTrailDot = new Vector3(trail.get(trail.size() - 1));
			newTrailDot.rotateXZ(angleXZ).rotateYZ(angleYZ);

			trail.add(newTrailDot);
			Vector3 newV = new Vector3(newTrailDot).multiply(BALL_TRAIL_RADIUS).add(player.getDrawPosition()).add(0, 0,
					Player.BALL_RADIUS);
			shiftedTrailPosition.get(i).add(newV);
			dots.put(newV,
					new PlayerTrailDot(
							(shiftedTrailPosition.get(i).size() >= 2)
									? shiftedTrailPosition.get(i).get(shiftedTrailPosition.get(i).size() - 2) : newV,
					newV, wantCloud));

			if (trail.size() > MAX_TRAIL_LENGTH) {
				trail.remove(0);
				dots.remove(shiftedTrailPosition.get(i).get(0));
				shiftedTrailPosition.get(i).remove(0);
			}
		}

		for (PlayerTrailDot dot : dots.values()) {
			dot.update(step);
		}
	}

	public PlayerTrail(Player player, Color trailColor) {
		this.player = player;

		// Create initial trail dots
		float rotationX = 1.234f;
		float rotationY = 2.345f;
		float rotationZ = 3.456f;

		Vector3[] trailDots = new Vector3[6];

		trailDots[0] = new Vector3(1, 0, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[1] = new Vector3(-1, 0, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[2] = new Vector3(0, 1, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[3] = new Vector3(0, -1, 0).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[4] = new Vector3(0, 0, 1).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);
		trailDots[5] = new Vector3(0, 0, -1).rotateXY(rotationZ).rotateXZ(rotationY).rotateYZ(rotationX);

		trailPosition = new ArrayList<>();
		shiftedTrailPosition = new ArrayList<>();
		dots = new HashMap<>();
		for (int i = 0; i < trailDots.length; i++) {
			trailPosition.add(new ArrayList<>());
			shiftedTrailPosition.add(new ArrayList<>());

			trailPosition.get(i).add(trailDots[i]);
			Vector3 newV = new Vector3(trailDots[i]).multiply(BALL_TRAIL_RADIUS).add(player.getDrawPosition()).add(0, 0,
					Player.BALL_RADIUS);
			shiftedTrailPosition.get(i).add(newV);
			dots.put(newV, new PlayerTrailDot(newV, newV, false));
		}

		// Set drawing assets
		maignTrailCloudStroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		mainTrailCloudColor = Helper.getAlphaColor(trailColor, 150);
		glowTrailCloudStroke = new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		glowTrailCloudColor = Helper.getAlphaColor(trailColor, 100);
		mainTrailStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		mainTrailColor = Helper.getAlphaColor(trailColor, 150);
	}

	@Override
	public Vector3 getDrawPosition() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void draw(Graphics2D g, Camera camera) {
		throw new UnsupportedOperationException();
	}

	private List<Vector3> drawPositionList;

	@Override
	public Collection<Vector3> getDrawPositionList() {
		drawPositionList = new ArrayList<>();
		for (ArrayList<Vector3> pointList : shiftedTrailPosition) {
			for (int i = 0; i < pointList.size() - 1; i++) {
				if (new Vector3(pointList.get(i)).subtract(pointList.get(i + 1)).getLengthSquared() > Math.pow(0.02,
						2)) {
					drawPositionList.add(pointList.get(i));
				}
			}
		}
		return drawPositionList;
	}

	@Override
	public void draw(Graphics2D g, Camera camera, Vector3 position) {
		PlayerTrailDot dot = dots.get(position);
		if (dot != null) {
			dot.draw(g, camera);
		}
	}

}
