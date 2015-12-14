package scene.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

	private URI getLevelFileURIIfExists(String levelFileName, Chapter chapter) throws URISyntaxException {
		System.out.println("leveldata/" + chapter.getPlayMode().getLevelFolderName() + "/" + chapter.getFolderName()
				+ "/" + levelFileName);

		URL resourceURL = getClassLoader().getResource("leveldata/" + chapter.getPlayMode().getLevelFolderName() + "/"
				+ chapter.getFolderName() + "/" + levelFileName);
		if (resourceURL == null)
			return null;
		return resourceURL.toURI();
	}

	private URI getChapterFileURI(String chapterFilePath, PlayMode playMode) throws URISyntaxException {
		return getClassLoader().getResource("leveldata/" + playMode.getLevelFolderName() + "/" + chapterFilePath)
				.toURI();
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
		return getFileContent(new File(getChapterFileURI(levelFilePath, playMode)));
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
			singlePlayerChapters[i].setPlayMode(PlayMode.SINGLE_PLAYER);
			singlePlayerChapterList.add(singlePlayerChapters[i]);
			populateLevelInChapter(singlePlayerChapters[i]);
		}

		// Co-op mode
		Chapter[] coopModeChapters = new Gson().fromJson(
				getFileContentFromPath(Constants.CHAPTER_LIST_FILE_NAME, PlayMode.COOP_MODE), Chapter[].class);

		for (int i = 0; i < coopModeChapters.length; i++) {
			coopModeChapters[i].setChapterOrder(i + 1);
			coopModeChapters[i].setPlayMode(PlayMode.COOP_MODE);
			coopModeChapterList.add(coopModeChapters[i]);
			populateLevelInChapter(coopModeChapters[i]);
		}
	}

	private void populateLevelInChapter(Chapter chapter) throws URISyntaxException, IOException {

		PlayMode playMode = chapter.getPlayMode();

		int currentFileCounter = 0;
		final String fileNameSuffix = ".lev";

		while (true) {
			URI fileURI = getLevelFileURIIfExists(currentFileCounter + fileNameSuffix, chapter);
			if (fileURI == null) {
				break;
			}

			chapter.addLevel(getFileContent(new File(fileURI)));
			currentFileCounter++;
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
