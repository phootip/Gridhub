package stage;

import java.util.HashMap;

import stage.gameobj.IDrawable;

public class ObjectMap {
	public static HashMap<String, IDrawable> drawableObjectHashMap;
	
	public ObjectMap() {
	}
	
	public ObjectMap getInstance() {
		return this;
	}
}
