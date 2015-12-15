package stage.renderer;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.gameobj.IDrawable;

class RenderEntity implements Comparable<RenderEntity> {
	private Vector3 position;
	private IDrawable drawableObject;
	private float drawOrderer;
	private Vector2 drawPosition;

	protected RenderEntity(Vector3 position, IDrawable drawableObject, Camera camera) {
		this.position = position;
		this.drawableObject = drawableObject;
		this.drawOrderer = camera.getYPosition(-position.getX(), -position.getY(), position.getZ());
		this.drawPosition = camera.getDrawPosition(position);
	}

	protected Vector2 getDrawPosition() {
		return drawPosition;
	}

	/**
	 * @return the position
	 */
	protected Vector3 getPosition() {
		return position;
	}

	/**
	 * @return the drawableObject
	 */
	protected IDrawable getDrawableObject() {
		return drawableObject;
	}

	@Override
	public int compareTo(RenderEntity o) {
		return (int) Math.signum(o.drawOrderer - this.drawOrderer);
	}
}