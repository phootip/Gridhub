package stage.gameobj;

/**
 * This method represent the walk on able object which means that the object that can be standing over by the player but not
 * everyObject can beee drawn
 * @author Thanat
 *
 */
public interface IWalkOnAble {
	/**
	 * 
	 * @return whether or not the object above this object
	 */
	public boolean isObjectAbove();
	
}
