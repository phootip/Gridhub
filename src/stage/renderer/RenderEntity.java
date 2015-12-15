package stage.renderer;

import core.geom.Vector2;
import core.geom.Vector3;
import stage.Camera;
import stage.gameobj.IDrawable;

/**
 * A render entity used internally in {@link LevelRenderer}. Support z-ordering mechanism by support comparing the
 * {@link RenderEntity} objects by the order of which one should draw first.
 * 
 * @author Kasidit Iamthong
 *
 */
class RenderEntity implements Comparable<RenderEntity> {
	private Vector3 position;
	private IDrawable drawableObject;
	private float drawOrderer;
	private Vector2 drawPosition;

	/**
	 * Create a {@link RenderEntity} object with specified properties.
	 * 
	 * @param position
	 *            specified position of the {@link IDrawable}.
	 * @param drawableObject
	 *            an {@link IDrawable} object.
	 * @param camera
	 *            camera for calculation of z-ordering mechanism.
	 */
	protected RenderEntity(Vector3 position, IDrawable drawableObject, Camera camera) {
		this.position = position;
		this.drawableObject = drawableObject;
		this.drawOrderer = camera.getYPosition(-position.getX(), -position.getY(), position.getZ());
		this.drawPosition = camera.getDrawPosition(position);
	}

	/**
	 * Get the draw position of the {@link RenderEntity}.
	 * 
	 * @return Draw position in screen coordinate system.
	 */
	protected Vector2 getDrawPosition() {
		return drawPosition;
	}

	/**
	 * Get the position of the {@link RenderEntity}.
	 * 
	 * @return Object position in game coordinate system.
	 */
	protected Vector3 getPosition() {
		return position;
	}

	/**
	 * Get the {@link IDrawable} object linked with this object.
	 * 
	 * @return The {@link IDrawable} object.
	 */
	protected IDrawable getDrawableObject() {
		return drawableObject;
	}

	@Override
	public int compareTo(RenderEntity o) {
		return (int) Math.signum(o.drawOrderer - this.drawOrderer);
	}
}