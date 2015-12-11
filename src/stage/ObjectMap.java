package stage;

import java.util.HashMap;

import stage.gameobj.IDrawable;
import stage.gameobj.ObjectVector;

public class ObjectMap {
	public static HashMap<ObjectVector, IDrawable> drawableObjectHashMap;
	
	public ObjectMap() {
	}
	
	public ObjectMap getInstance() {
		return this;
	}
}
