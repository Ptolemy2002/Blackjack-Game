package cards;

import java.util.ArrayList;
import java.util.List;

import main.Tools;

public enum EnumCardSuit {
	CLUB, HEART, SPADE, DIAMOND;

	/**
	 * Pick a random value
	 * 
	 * @return A random value in this enum.
	 */
	public static EnumCardSuit pickRandom() {
		return values()[Tools.Numbers.randomInt(0, values().length - 1)];
	}

	/**
	 * Convert this object into a string.
	 * 
	 * @return The singular form of the face name. To get the plural form, append
	 *         "s" to the value.
	 */
	@Override
	public String toString() {
		switch (this) {
		case CLUB:
			return "Club";
		case HEART:
			return "Heart";
		case SPADE:
			return "Spade";
		case DIAMOND:
			return "Diamond";
		default:
			return null;
		}
	}

	/**
	 * Get all values in this enum.
	 * 
	 * @return An ArrayList containing all values in this enum.
	 */
	public static List<EnumCardSuit> getValues() {
		EnumCardSuit[] temp = values();
		List<EnumCardSuit> res = new ArrayList<>();

		for (EnumCardSuit i : temp) {
			res.add(i);
		}

		return res;
	}

	/**
	 * Convert this string into the corresponding value of this enum. Can be
	 * singular or plural form.
	 * 
	 * @return The value of this enum corresponding to the string or null if no
	 *         value corresponds.
	 */
	public static EnumCardSuit fromString(String s) {
		switch (s.toLowerCase()) {
		case "diamonds":
		case "diamond":
			return DIAMOND;
		case "spades":
		case "spade":
			return SPADE;
		case "hearts":
		case "heart":
			return HEART;
		case "clubs":
		case "club":
			return CLUB;
		}
		
		return null;
	}
}
