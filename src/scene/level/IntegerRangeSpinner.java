package scene.level;

import java.awt.Font;

import core.ScrollableUIList;

public final class IntegerRangeSpinner extends ScrollableUIList {
	private static final long serialVersionUID = -3087439501554272723L;

	public IntegerRangeSpinner(int low, int high) {
		super();
		for (int i = low; i <= high; i++) {
			this.add(new IntegerListItem(i));
		}
	}

}
