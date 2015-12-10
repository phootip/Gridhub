package gamestage.gameobject;

/**
 * This Interface is used for the object in the game which can be push by the player.
 * 
 * @author Thanat Jatuphattharachat
 *
 */
public interface PushableObject extends IDrawable{
	/**
	 * Get the status of the Pushable object wheter it can be moved.
	 * @return a boolean indicate the status of the object 
	 */
	public boolean isPushable();

	/**
	 * This method is called when the object is push. Please note that the object will be called recursively 
	 * if there exists more than one object in a row.
	 * 
	 * @param previousWeight
	 * 				total weight of every previous Pushable objects 
	 * @param diffX
	 * 				The different (in X direction) between the present coordinate and next coordinate.
	 * @param diffY
	 * 				The different (in Y direction) between the present coordinate and next coordinate.
	 * @param diffZ
	 * 				The different (in Z direction) between the present coordinate and next coordinate.
	 * 
	 * @return boolean indicate that the objects can be push.
	 */
	public boolean push(int previousWeight, int diffX, int diffY, int diffZ);
}
