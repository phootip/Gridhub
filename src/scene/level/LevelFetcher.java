package scene.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import util.Constants;
import util.Constants.PlayMode;

final public class LevelFetcher {

	private static final LevelFetcher instance = new LevelFetcher();

	public static final LevelFetcher getInstance() {
		return instance;
	}

	private List<Chapter> singlePlayerChapterList;
	private List<Chapter> coopModeChapterList;

	private ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}

	private URI getLevelFileURI(String levelFilePath, PlayMode playMode) throws URISyntaxException {
		return getClassLoader().getResource("leveldata/" + playMode.getLevelFolderName() + "/" + levelFilePath).toURI();
	}

	private String getFileContent(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();

		return new String(data);
	}

	private String getFileContentFromPath(String levelFilePath, PlayMode playMode)
			throws IOException, URISyntaxException {
		return getFileContent(new File(getLevelFileURI(levelFilePath, playMode)));
	}

	private LevelFetcher() {
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

	private void populateChapterList() throws IOException, URISyntaxException {

		// Single player
		Chapter[] singlePlayerChapters = new Gson().fromJson(
				getFileContentFromPath(Constants.CHAPTER_LIST_FILE_NAME, PlayMode.SINGLE_PLAYER), Chapter[].class);

		for (int i = 0; i < singlePlayerChapters.length; i++) {
			singlePlayerChapters[i].setChapterOrder(i + 1);
			singlePlayerChapterList.add(singlePlayerChapters[i]);
		}

		// Co-op mode
		Chapter[] coopModeChapters = new Gson().fromJson(
				getFileContentFromPath(Constants.CHAPTER_LIST_FILE_NAME, PlayMode.COOP_MODE), Chapter[].class);

		for (int i = 0; i < coopModeChapters.length; i++) {
			coopModeChapters[i].setChapterOrder(i + 1);
			coopModeChapterList.add(coopModeChapters[i]);
		}
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
