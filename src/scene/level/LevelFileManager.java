package scene.level;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import scene.core.Scene;
import scene.mainmenu.MainMenuScene;
import scene.play.PlayScene;
import util.Constants;
import util.Constants.PlayMode;

final public class LevelFileManager {

	private static final LevelFileManager instance = new LevelFileManager();

	public static final LevelFileManager getInstance() {
		return instance;
	}

	public static final String LEVEL_FILE_NAME_SUFFIX = ".json";

	private List<Chapter> singlePlayerChapterList;
	private List<Chapter> coopModeChapterList;

	private Chapter singlePlayerUserChapter;
	private Chapter coopModeUserChapter;

	protected Chapter getUserChapter(PlayMode playMode) {
		switch (playMode) {
			case COOP_MODE:
				return getCoopModeUserChapter();
			case SINGLE_PLAYER:
				return getSinglePlayerUserChapter();
			default:
				throw new IllegalArgumentException("Invalid playMode : " + playMode);
		}
	}

	protected Chapter getSinglePlayerUserChapter() {
		return singlePlayerUserChapter;
	}

	protected Chapter getCoopModeUserChapter() {
		return coopModeUserChapter;
	}

	private InputStream getLevelResourceFileAsInputStream(String resourcePath) throws FileNotFoundException {
		return new FileInputStream("./levels/" + resourcePath);
	}

	private OutputStream getLevelResourceFileAsOutputStream(String resourcePath) throws FileNotFoundException {
		return new FileOutputStream("./levels/" + resourcePath);
	}

	private InputStream getLevelFileInputStream(String levelFileName, Chapter chapter) throws FileNotFoundException {
		return getLevelResourceFileAsInputStream(
				chapter.getPlayMode().getLevelFolderName() + "/" + chapter.getFolderName() + "/" + levelFileName);
	}

	private OutputStream getLevelDataOutputStream(LevelData levelData) throws FileNotFoundException {
		Chapter chapter = levelData.getChapter();
		return getLevelResourceFileAsOutputStream(chapter.getPlayMode().getLevelFolderName() + "/"
				+ chapter.getFolderName() + "/" + levelData.getLevelFileName());
	}

	private InputStream getChapterFileInputStream(String chapterFilePath, PlayMode playMode)
			throws FileNotFoundException {
		return getLevelResourceFileAsInputStream(playMode.getLevelFolderName() + "/" + chapterFilePath);
	}

	private String getFileContent(InputStream stream) {
		// From http://stackoverflow.com/a/5445161

		Scanner s = new Scanner(stream, "UTF-8");
		s.useDelimiter("\\A");

		String content = s.hasNext() ? s.next() : "";

		s.close();

		return content;

	}

	public void saveLevelData(LevelData levelData) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(getLevelDataOutputStream(levelData));
		writer.print(levelData.getLevelDataJSON());
		writer.close();
	}

	private String getFileContentFromPath(String levelFilePath, PlayMode playMode) throws FileNotFoundException {
		return getFileContent(getChapterFileInputStream(levelFilePath, playMode));
	}

	private LevelFileManager() {
		this.singlePlayerChapterList = new ArrayList<>();
		this.coopModeChapterList = new ArrayList<>();
	}

	public boolean initialize() {
		try {
			populateChapterList();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void populateChapterList() throws JsonSyntaxException, FileNotFoundException {

		// Single player
		Chapter[] singlePlayerChapters = new Gson().fromJson(
				getFileContentFromPath(Constants.CHAPTER_LIST_FILE_NAME, PlayMode.SINGLE_PLAYER), Chapter[].class);

		for (int i = 0; i < singlePlayerChapters.length; i++) {
			singlePlayerChapters[i].setChapterOrder(i + 1);
			singlePlayerChapters[i].setPlayMode(PlayMode.SINGLE_PLAYER);
			if (singlePlayerChapters[i].isUserFolder()) {
				singlePlayerUserChapter = singlePlayerChapters[i];
			}

			singlePlayerChapterList.add(singlePlayerChapters[i]);
			populateLevelInChapter(singlePlayerChapters[i]);
		}

		// Co-op mode
		Chapter[] coopModeChapters = new Gson().fromJson(
				getFileContentFromPath(Constants.CHAPTER_LIST_FILE_NAME, PlayMode.COOP_MODE), Chapter[].class);

		for (int i = 0; i < coopModeChapters.length; i++) {
			coopModeChapters[i].setChapterOrder(i + 1);
			coopModeChapters[i].setPlayMode(PlayMode.COOP_MODE);
			if (coopModeChapters[i].isUserFolder()) {
				coopModeUserChapter = coopModeChapters[i];
			}

			coopModeChapterList.add(coopModeChapters[i]);
			populateLevelInChapter(coopModeChapters[i]);
		}
	}

	private void populateLevelInChapter(Chapter chapter) {

		int currentFileCounter = 0;

		while (true) {
			try {
				String fileName = currentFileCounter + LEVEL_FILE_NAME_SUFFIX;
				InputStream fileStream = getLevelFileInputStream(fileName, chapter);
				chapter.addLevel(getFileContent(fileStream), fileName);
				currentFileCounter++;
			} catch (FileNotFoundException ex) {
				break;
			}
		}
	}

	public Scene getNextLevelPlayScene(LevelData currentLevel) {
		Chapter chapter = currentLevel.getChapter();
		List<LevelData> levelList = chapter.getLevelDataList();

		if (levelList.indexOf(currentLevel) < levelList.size() - 1) { // Not last level
			return new PlayScene(levelList.get(levelList.indexOf(currentLevel) + 1));
		} else {
			List<Chapter> chapterList = (currentLevel.getPlayerCount() == 1) ? singlePlayerChapterList
					: coopModeChapterList;

			int i = singlePlayerChapterList.indexOf(chapter) + 1;
			while (i < singlePlayerChapterList.size()) {
				if (!singlePlayerChapterList.get(i).isUserFolder()
						&& singlePlayerChapterList.get(i).getLevelDataList().size() > 0) {
					return new PlayScene(singlePlayerChapterList.get(i).getLevelDataList().get(0));
				}
				i++;
			}
		}

		return new MainMenuScene(false);

	}

	public List<Chapter> getChapterList(PlayMode playMode) {
		switch (playMode) {
			case SINGLE_PLAYER:
				return singlePlayerChapterList;
			case COOP_MODE:
				return coopModeChapterList;
			default:
				throw new IllegalArgumentException("Invalid playMode : " + playMode);
		}
	}

}
