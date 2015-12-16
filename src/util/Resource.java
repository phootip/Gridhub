package util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;



/**
 * This class contains resources that is required in the game. The game will load the required resource from file
 * automatically after starting the game.
 * 
 * @author Kasidit Iamthong
 *
 */
public class Resource {

	private static Resource instance = new Resource();

	/**
	 * Get an instance of {@code Resource}.
	 * 
	 * @return An instance of {@code Resource}
	 */
	public static Resource getInstance() {
		return instance;
	}

	private ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}

	private InputStream getResourceAsStream(String resourcePath) {
		return getClassLoader().getResourceAsStream("res/" + resourcePath);
	}

	/**
	 * Initialize the resource.
	 * 
	 * @return Whether the resource loading is successful.
	 */
	public boolean initialize() {
		try {
			loadFont();
			loadSound();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * List of available font weights of game's font faces.
	 * 
	 * @author Kasidit Iamthong
	 *
	 */
	public static enum FontWeight {
		THIN("Bebas Neue Thin", "BebasNeue Thin.ttf"), LIGHT("BebasNeueLight",
				"BebasNeue Light.ttf"), REGULAR("BebasNeueRegular", "BebasNeue Regular.ttf"), BOLD("BebasNeueBold",
						"BebasNeue Bold.ttf"), BOOK("BebasNeueBook", "BebasNeue Book.ttf");

		private final String fontName;
		private final String fontFileName;

		FontWeight(String fontName, String fontFileName) {
			this.fontName = fontName;
			this.fontFileName = fontFileName;
		}

		/**
		 * Get the font name associated with the font file.
		 * 
		 * @return the font name.
		 */
		public String getFontName() {
			return fontName;
		}

		/**
		 * Get the font resource file name.
		 * 
		 * @return File name of the font.
		 */
		public String getFontFileName() {
			return fontFileName;
		}
	}

	/**
	 * Get the game {@link Font} with specified size and weight.
	 * 
	 * @param size
	 *            the point size of the font.
	 * @param weight
	 *            the weight of the font.
	 * @return A game {@link Font} with specified size and weight.
	 * @see #getDefaultFont(int)
	 */
	public Font getDefaultFont(int size, FontWeight weight) {
		return new Font(weight.getFontName(), Font.PLAIN, size);
	}

	/**
	 * Get the game {@link Font} with specified size.
	 * 
	 * @param size
	 *            the point size of the font.
	 * @return A game {@link Font} with specified size and regular weight.
	 * @see #getDefaultFont(int, FontWeight)
	 */
	public Font getDefaultFont(int size) {
		return getDefaultFont(size, FontWeight.REGULAR);
	}

	private void loadFont() throws FontFormatException, IOException, URISyntaxException {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		for (FontWeight fontWeight : FontWeight.values()) {
			Font newFont = Font.createFont(Font.TRUETYPE_FONT,
					getResourceAsStream("fonts/" + fontWeight.getFontFileName()));
			ge.registerFont(newFont);
		}
	}

	private static final Stroke gameObjThickStroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private static final Stroke gameObjThinStroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	public static Stroke getGameObjectThickStroke() {
		return gameObjThickStroke;
	}

	public static Stroke getGameObjectThinStroke() {
		return gameObjThinStroke;
	}
	
	public AudioClip themeSong;
	public AudioClip warpEffect;
	private void loadSound() {
		try {
			themeSong = Applet.newAudioClip(this.getClass().getClassLoader().getResource("res/sound/themeSong.wav").toURI().toURL());
			warpEffect = Applet.newAudioClip(this.getClass().getClassLoader().getResource("res/sound/teleport.wav").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
