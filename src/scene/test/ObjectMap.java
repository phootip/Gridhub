package scene.test;

import java.util.HashMap;

import objectInterface.IDrawable;

public class ObjectMap {
	public static HashMap<String, IDrawable> drawableObjectHashMap;
	
	public ObjectMap() {
	}
	
	public ObjectMap getInstace() {
		return this;
	}
}
