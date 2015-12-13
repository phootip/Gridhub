package core;

/**
 * Game play modes available in game.
 * @author Kasidit Iamthong
 *
 */
public enum PlayMode {
	SINGLE_PLAYER("singleplayer"), COOP_MODE("coop");

	private String folderName;

	/**
	 * @return The folder name corresponding to play mode.
	 */
	public String getLevelFolderName() {
		return folderName;
	}

	private PlayMode(String folderName) {
		this.folderName = folderName;
	}

}