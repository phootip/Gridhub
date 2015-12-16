package stage.gameobj;

/**
 * @author Thanat Jatuphattharachat
 * This interface represent the object which is controllable by {@link SwitchControl}
 *
 */
public interface IControlable {
	/**
	 * activate the controled object. Make the object be able to work properly
	 */
	public void activate();
	/**
	 * deactivate the control object. Stop object from working normally
	 */
	public void deActivate();
}
