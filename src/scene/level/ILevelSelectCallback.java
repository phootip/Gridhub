package scene.level;

/**
 * This provides a callback interface for {@link PlayPageLevelSelector} and {@link LevelEditorPageLevelSelector}.
 * 
 * @author Kasidit Iamthong
 *
 */
public interface ILevelSelectCallback {
	/**
	 * The callback function when the level is selected.
	 * 
	 * @param levelData
	 *            the {@link LevelData} object selected by the level selector.
	 */
	public void onLevelSelect(LevelData levelData);
}
