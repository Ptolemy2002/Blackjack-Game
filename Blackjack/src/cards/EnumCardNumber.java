package cards;

import java.util.ArrayList;
import java.util.List;

import main.Tools;

public enum EnumCardNumber {
	ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

	/**
	 * Pick a random value
	 * 
	 * @return A random value in this enum.
	 */
	public static EnumCardNumber pickRandom() {
		return values()[Tools.Numbers.randomInt(0, values().length - 1)];
	}

	/**
	 * Pick a random number (not a face). The numbers are {@code [ONE, TWO, THREE,
	 * FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN]}
	 * 
	 * @return A random value classified as a number in this Enum.
	 */
	public static EnumCardNumber pickRandomNumber() {
		return getNumbers().get(Tools.Numbers.randomInt(0, getNumbers().size() - 1));
	}

	/**
	 * Pick a random face. The faces are {@code [JACK, QUEEN, KING, ACE]}
	 * 
	 * @return A random value classified as a face in this Enum.
	 */
	public static EnumCardNumber pickRandomFace() {
		return getFaces().get(Tools.Numbers.randomInt(0, getFaces().size() - 1));
	}

	/**
	 * Get all values classified as a number in this enum. The numbers are
	 * {@code [ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN]}
	 * 
	 * @return An ArrayList containing all values classified as a number in this
	 *         enum.
	 */
	public static List<EnumCardNumber> getNumbers() {
		EnumCardNumber[] temp = new EnumCardNumber[] { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN };
		List<EnumCardNumber> res = new ArrayList<>();

		for (EnumCardNumber i : temp) {
			res.add(i);
		}

		return res;
	}

	/**
	 * Get all values classified as a face in this enum. The faces are
	 * {@code [JACK, QUEEN, KING]}
	 * 
	 * @return An ArrayList containing all values classified as a face in this enum.
	 */
	public static List<EnumCardNumber> getFaces() {
		EnumCardNumber[] temp = new EnumCardNumber[] { JACK, QUEEN, KING };
		List<EnumCardNumber> res = new ArrayList<>();

		for (EnumCardNumber i : temp) {
			res.add(i);
		}

		return res;
	}

	/**
	 * Get all values in this enum.
	 * 
	 * @return An ArrayList containing all values in this enum.
	 */
	public static List<EnumCardNumber> getValues() {
		EnumCardNumber[] temp = values();
		List<EnumCardNumber> res = new ArrayList<>();

		for (EnumCardNumber i : temp) {
			res.add(i);
		}

		return res;
	}

	/**
	 * Test if the item is a face. The faces are {@code [JACK, QUEEN, KING]}
	 * 
	 * @param item The item to test
	 * @return Whether the item is a face
	 */
	public static boolean isFace(EnumCardNumber item) {
		return item == JACK || item == QUEEN || item == KING;
	}

	/**
	 * Test if the item is a number (not a face). The numbers are
	 * {@code [ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN]}
	 * 
	 * @param item The item to test
	 * @return Whether the item is a face
	 */
	public static boolean isNumber(EnumCardNumber item) {
		return !isFace(item);
	}

	/**
	 * Convert this object into a string.
	 * 
	 * @return Either the number form (1, 2, 3, ...), The string form of the face
	 *         (Queen, King, ...), or {@code "Jack"} (in case of Joker)
	 */
	@Override
	public String toString() {
		if (isNumber(this) && this != ACE) {
			return ((Integer) (this.ordinal() + 1)).toString();
		} else {
			switch (this) {
			case JACK:
				return "Jack";
			case QUEEN:
				return "Queen";
			case KING:
				return "King";
			case ACE:
				return "Ace";
			default:
				return null;
			}
		}
	}

	/**
	 * Convert this string into the corresponding value of this enum.
	 * 
	 * @return The value of this enum corresponding to the string or null if no
	 *         value corresponds.
	 */
	public static EnumCardNumber fromString(String s) {
		try {
			Integer index = Integer.parseInt(s);
			return getNumbers().get(index - 1);
		} catch (Exception e) {
			switch (s.toLowerCase()) {
			case "one":
				return ONE;
			case "two":
				return TWO;
			case "three":
				return THREE;
			case "four":
				return FOUR;
			case "five":
				return FIVE;
			case "six":
				return SIX;
			case "seven":
				return SEVEN;
			case "eight":
				return EIGHT;
			case "nine":
				return NINE;
			case "ten":
				return TEN;
			case "jack":
				return JACK;
			case "king":
				return KING;
			case "queen":
				return QUEEN;
			case "ace":
				return ACE;
			}
		}

		return null;
	}
}
