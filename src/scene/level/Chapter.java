package scene.level;

class Chapter {

	private String chapterName;
	private String folderName;
	private boolean userFolder;
	private transient int chapterOrder;

	/**
	 * @return the chapterOrder
	 */
	protected int getChapterOrder() {
		return chapterOrder;
	}

	/**
	 * @param chapterOrder
	 *            the chapterOrder to set
	 */
	protected void setChapterOrder(int chapterOrder) {
		this.chapterOrder = chapterOrder;
	}

	/**
	 * Get this chapter name.
	 * 
	 * @return The chapter name.
	 */
	protected String getChapterName() {
		return chapterName;
	}

	/**
	 * Get the folder name that is used for collecting this chapter's level files.
	 * 
	 * @return Name of folder containing level files of this chapter.
	 */
	protected String getFolderName() {
		return folderName;
	}

	/**
	 * Get the boolean value determining whether or not this chapter is used as a user folder. In other words, the
	 * imported/exported levels should be in this chapter. There should be exactly one chapter that is marked as user
	 * folder.
	 * 
	 * @return Whether or not this chapter is used as a user folder.
	 */
	protected boolean isUserFolder() {
		return userFolder;
	}

	private Chapter() {
	}

	@Override
	public String toString() {
		return "Chapter " + chapterOrder + " : " + chapterName;
	}

}
