package scene.leveleditor;

import java.io.FileNotFoundException;

import scene.level.LevelData;
import scene.level.LevelDataBuilder;
import scene.level.LevelFileManager;
import scene.mainmenu.MainMenuScene;
import scene.play.PlayScene;
import stage.GameStage;
import stage.GameStageType;

public final class LevelEditorScene extends PlayScene {

	public LevelEditorScene(LevelData levelData) {
		super(levelData);
	}

	private String[] menuItems = new String[] { "Save Level", "Load Last Saved", "Exit" };

	@Override
	protected String[] getMenuItems() {
		return menuItems;
	}

	@Override
	protected void performMenuAction(int selectedMenuItem) {

		switch (selectedMenuItem) {
			case 0:
				try {
					levelData = LevelFileManager.getInstance().saveLevelData(gameStage.buildLevelData());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					isPause = !isPause;
				}
				break;
			case 1: // Load Last Saved. Same as Restart
				isSceneFadingOut = true;
				nextScene = new LevelEditorScene(levelData);
				break;
			case 2: // Exit
				isSceneFadingOut = true;
				nextScene = new MainMenuScene(false);
				break;
		}
	}

	@Override
	protected void createGameStage() {
		this.gameStage = new GameStage(this.levelData, GameStageType.LEVEL_EDITOR);
	}

}
