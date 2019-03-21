package cards;

public class Card {
	public EnumCardSuit suit;
	public EnumCardNumber number;
	public boolean faceUp;
	public boolean hideFaceDown;

	public Card(EnumCardNumber number, EnumCardSuit suit) {
		this.number = number;
		this.suit = suit;
		this.faceUp = false;
		this.hideFaceDown = true;
	}

	public Card(EnumCardNumber number, EnumCardSuit suit, boolean faceUp) {
		this.number = number;
		this.suit = suit;
		this.faceUp = faceUp;
		this.hideFaceDown = true;
	}
	
	public Card(EnumCardNumber number, EnumCardSuit suit, boolean faceUp, boolean hideFaceDown) {
		this.number = number;
		this.suit = suit;
		this.faceUp = faceUp;
		this.hideFaceDown = hideFaceDown;
	}

	public Card() {
		this.number = EnumCardNumber.pickRandom();
		this.suit = EnumCardSuit.pickRandom();
		this.faceUp = false;
		this.hideFaceDown = true;
	}

	public Card(boolean face) {
		this.number = face ? EnumCardNumber.pickRandomFace() : EnumCardNumber.pickRandomNumber();
		this.suit = EnumCardSuit.pickRandom();
		this.faceUp = false;
		this.hideFaceDown = true;
	}

	public Card(boolean face, boolean faceUp) {
		this.number = face ? EnumCardNumber.pickRandomFace() : EnumCardNumber.pickRandomNumber();
		this.suit = EnumCardSuit.pickRandom();
		this.faceUp = faceUp;
		this.hideFaceDown = true;
	}
	
	public Card(Card c) {
		this.faceUp = c.faceUp;
		this.number = c.number;
		this.suit = c.suit;
		this.hideFaceDown = c.hideFaceDown;
	}

	public Card setSuit(EnumCardSuit suit) {
		this.suit = suit;
		return this;
	}

	public Card setNumber(EnumCardNumber number) {
		this.number = number;
		return this;
	}

	public Card setFaceUp(boolean faceUp) {
		this.faceUp = faceUp;
		return this;
	}
	
	public Card setHideFaceDown(boolean hideFaceDown) {
		this.faceUp = hideFaceDown;
		return this;
	}

	public boolean isCard(EnumCardNumber number, EnumCardSuit suit) {
		return this.number == number && this.suit == suit;
	}
	
	public boolean isTenCard() {
		return this.number == EnumCardNumber.TEN || EnumCardNumber.isFace(this.number);
	}
	
	public Card flipOver() {
		this.faceUp = !this.faceUp;
		return this;
	}
	
	/**
	 * Convert this object into a string.
	 * 
	 * @return {@code number + " of " + type + "s"}
	 */
	@Override
	public String toString() {
		if (faceUp) {
			return number.toString() + " of " + suit.toString() + "s"; // Examples: 3 of clubs, jack of diamonds
		} else {
			return hideFaceDown ? "FACE DOWN" : number.toString() + " of " + suit.toString() + "s";
		}
	}
}
