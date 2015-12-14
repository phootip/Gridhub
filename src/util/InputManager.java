package util;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

// TODO: Implement MouseListener and MouseMoveListener

/**
 * This manages the input of the game, e.g. keyboard. It also support inputing in the triggering way, in the similar
 * manner to monostable multivibrator, i.e. the input that is asserted true for only one frame.
 * 
 * @author Kasidit Iamthong
 *
 */
public class InputManager implements KeyListener {
	private final static InputManager instance = new InputManager();

	/**
	 * Get an instance of {@link InputManager}.
	 * 
	 * @return An instance of {@link InputManager}.
	 */
	public static InputManager getInstance() {
		return instance;
	}

	/**
	 * Set keyboard and mouse listener to specified {@code Component}.
	 * 
	 * @param c
	 *            the component to listen.
	 */
	public static void setListenerTo(Component c) {
		c.addKeyListener(InputManager.getInstance());
	}

	private Set<Integer> keyPressHasher = new HashSet<>();
	private Set<Integer> keyTriggerHasher = new HashSet<>();
	private Set<Integer> keyRepeatedTriggerHasher = new HashSet<>();

	private Set<Integer> keyPressBuffer = new HashSet<>();
	private Set<Integer> keyReleaseBuffer = new HashSet<>();

	private InputManager() {
		keyPressHasher = new HashSet<>();
		keyTriggerHasher = new HashSet<>();
	}

	/**
	 * Determines whether specified key is currently being pressed or not.
	 * 
	 * @param keyCode
	 *            an integer representing key code to check.
	 * @return Whether or not the specified key is being pressed.
	 */
	public boolean isKeyPressing(int keyCode) {
		return keyPressHasher.contains(keyCode);
	}

	/**
	 * Determines whether specified key is triggering or not. This filter out repeated key fires.
	 * 
	 * @param keyCode
	 *            an integer representing key code to check.
	 * @return Whether or not the specified key is being triggered.
	 * @see InputManager#isKeyTriggering(int, boolean)
	 */
	public boolean isKeyTriggering(int keyCode) {
		return isKeyTriggering(keyCode, true);
	}

	/**
	 * Determines whether specified key is triggering or not. Can be specified whether or not it should filter out
	 * repeated key fires.
	 * 
	 * @param keyCode
	 *            an integer representing key code to check.
	 * @param filterRepeatedFire
	 *            whether of not return value should filter out repeated key fires.
	 * @return Whether or not the specified key is being triggered.
	 * @see InputManager#isKeyTriggering(int)
	 */
	public boolean isKeyTriggering(int keyCode, boolean filterRepeatedFire) {
		if (filterRepeatedFire) {
		return keyTriggerHasher.contains(keyCode);
		}else {
			return keyRepeatedTriggerHasher.contains(keyCode);
		}
	}

	/**
	 * Determines whether specified key is triggering or not, and, if triggering, set the key state as "not triggered".
	 * 
	 * @param keyCode
	 *            an integer representing key code to check.
	 * @return Whether or not the specified key is being triggered.
	 */
	public boolean checkKeyTriggeringWithRemoval(int keyCode) {
		if (keyTriggerHasher.contains(keyCode)) {
			keyTriggerHasher.remove(keyCode);
			return true;
		} else
			return false;
	}

	/**
	 * This should be called from only from listener, and should not be called explicitly by user.
	 */
	@Override
	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = (e.getKeyCode() != 0) ? e.getKeyCode() : e.getExtendedKeyCode();
		// if (!keyPressHasher.contains(keyCode)) {
		keyPressBuffer.add(keyCode);
		// }
	}

	/**
	 * This should be called from only from listener, and should not be called explicitly by user.
	 */
	@Override
	public synchronized void keyReleased(KeyEvent e) {
		int keyCode = (e.getKeyCode() != 0) ? e.getKeyCode() : e.getExtendedKeyCode();
		keyReleaseBuffer.add(keyCode);
	}

	/**
	 * This should be called from only from listener, and should not be called explicitly by user.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Update the InputManager, such as clear triggered value. This should be called every frame before doing any
	 * operation.
	 */
	public synchronized void update() {
		keyTriggerHasher.clear();
		keyRepeatedTriggerHasher.clear();

		for (Integer i : keyPressBuffer) {
			keyRepeatedTriggerHasher.add(i);
			if (!keyPressHasher.contains(i)) {
				keyPressHasher.add(i);
				keyTriggerHasher.add(i);
			}
		}

		for (Integer i : keyReleaseBuffer) {
			keyPressHasher.remove(i);
		}

		keyPressBuffer.clear();
		keyReleaseBuffer.clear();
	}

}
