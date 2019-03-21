package cards;

import java.util.ArrayList;

import main.Tools;

@SuppressWarnings("unchecked")
public class Deck {
	/**
	 * Standard 52 card Deck.
	 */
	public static final Deck STANDARD_52 = new Deck(getAll(EnumCardNumber.ACE, EnumCardNumber.TWO, EnumCardNumber.THREE,
			EnumCardNumber.FOUR, EnumCardNumber.FIVE, EnumCardNumber.SIX, EnumCardNumber.SEVEN, EnumCardNumber.EIGHT,
			EnumCardNumber.NINE, EnumCardNumber.TEN, EnumCardNumber.JACK, EnumCardNumber.QUEEN, EnumCardNumber.KING));

	private ArrayList<Card> cards = new ArrayList<Card>();
	
	/**
	 * A deck with the specified cards
	 * 
	 * @param cards The card to add
	 */
	public Deck(Card... cards) {
		for (Card i : cards) {
			this.cards.add(new Card(i));
		}
	}
	
	/**
	 * A deck with the specified cards
	 * 
	 * @param cards The card to add
	 */
	public Deck(ArrayList<Card> cards) {
		this.cards = (ArrayList<Card>) cards.clone();
	}
	
	/**
	 * A deck with all the cards in the specified decks
	 * 
	 * @param cards The decks to add.
	 */
	public Deck(Deck... cards) {
		for (Deck i : cards) {
			i.getCards().forEach(x -> this.getCards().add(new Card(x)));
		}
	}
	
	/**
	 * Able to clone a deck.
	 * 
	 * @param deck The deck to clone
	 */
	public Deck(Deck deck) {
		this.setCards((ArrayList<Card>) deck.getCards().clone());
	}
	
	/**
	 * Return a new instance of the Standard deck.
	 */
	public Deck() {
		this.setCards((ArrayList<Card>) STANDARD_52.getCards().clone());
	}

	public static Deck getAll(EnumCardNumber number) {
		Deck res = new Deck(new ArrayList<Card>());

		for (EnumCardSuit i : EnumCardSuit.getValues()) {
			res.putCardAtBottom(new Card(number, i));
		}

		return res;
	}

	public static Deck getAll(EnumCardSuit suit) {
		Deck res = new Deck(new ArrayList<Card>());

		for (EnumCardNumber i : EnumCardNumber.getValues()) {
			res.putCardAtBottom(new Card(i, suit));
		}

		return res;
	}

	public static Deck getAll(EnumCardSuit... suits) {
		Deck res = new Deck(new ArrayList<Card>());

		for (EnumCardSuit suit : suits) {
			res.appendDeck(getAll(suit));
		}

		return res;
	}

	public static Deck getAll(EnumCardNumber... numbers) {
		Deck res = new Deck(new ArrayList<Card>());

		for (EnumCardNumber number : numbers) {
			res.appendDeck(getAll(number));
		}

		return res;
	}

	/**
	 * Get the current list of cards in the deck.
	 * 
	 * @return the current list of cards in the deck.
	 */
	public ArrayList<Card> getCards() {
		return this.cards;
	}

	/**
	 * Set the cards in the deck to your own list
	 * 
	 * @param cards The list to set the list of cards to.
	 * @return This Deck
	 */
	public Deck setCards(ArrayList<Card> cards) {
		this.cards = cards;
		return this;
	}

	/**
	 * Put a card at the specified index.
	 * 
	 * @param card  the card to put
	 * @param index The index to put the card at
	 * @return This Deck
	 */
	public Deck putCardAt(Card card, int index) {
		this.cards.add(index, card);
		return this;
	}

	/**
	 * Put the card at the top of the deck (index 0)
	 * 
	 * @param card the card to put
	 * @return This Deck
	 */
	public Deck putCardAtTop(Card card) {
		this.cards.add(0, card);
		return this;
	}

	/**
	 * Put the card at the bottom of the deck (index {@code size() - 1})
	 * 
	 * @param card the card to put
	 * @return This Deck
	 */
	public Deck putCardAtBottom(Card card) {
		this.cards.add(card);
		return this;
	}
	/**
	 * Append a deck of cards to this one.
	 * 
	 * @param deck The deck to append
	 * @return This Deck
	 */
	public Deck appendDeck(Deck deck) {
		this.cards.addAll(deck.getCards());
		return this;
	}

	/**
	 * Put the card at a random index of the deck.
	 * 
	 * @param card the card to put
	 * @return This Deck
	 */
	public Deck putCardAtRandom(Card card) {
		putCardAt(card, Tools.Numbers.randomInt(0, cards.size() - 1));
		return this;
	}

	/**
	 * Remove the card at the specified index from the deck.
	 * 
	 * @param index the index to remove the card from.
	 * @return This Deck
	 */
	public Deck removeCard(int index) {
		this.cards.remove(index);
		return this;
	}

	/**
	 * Remove a random card from the deck.
	 * 
	 * @return This Deck
	 */
	public Deck removeRandomCard() {
		this.cards.remove(Tools.Numbers.randomInt(0, cards.size() - 1));
		return this;
	}

	/**
	 * Remove the card at the top of the deck (index 0)
	 * 
	 * @return This Deck
	 */
	public Deck removeTop() {
		removeCard(0);
		return this;
	}

	/**
	 * Remove the card at the bottom of the deck (index {@code size() - 1})
	 * 
	 * @return This Deck
	 */
	public Deck removeBottom() {
		removeCard(cards.size() - 1);
		return this;
	}

	/**
	 * Remove the card at the specified index from the deck, and return it.
	 * 
	 * @param index the index to draw the card from.
	 * @return The card that was drawn
	 */
	public Card drawCard(int index) {
		return this.cards.remove(index);
	}

	/**
	 * Remove the card at the bottom of the deck (index {@code size() - 1}), and
	 * return it.
	 * 
	 * @return The card that was drawn
	 */
	public Card drawBottom() {
		return drawCard(cards.size() - 1);
	}

	/**
	 * Remove the card at the top of the deck (index 0), and return it.
	 * 
	 * @return The card that was drawn
	 */
	public Card drawTop() {
		return drawCard(0);
	}

	/**
	 * Remove a random card from the deck, and return it.
	 * 
	 * @return The card that was drawn
	 */
	public Card drawRandomCard() {
		return drawCard(Tools.Numbers.randomInt(0, cards.size() - 1));
	}

	/**
	 * Get the card at the top of the deck (index 0)
	 * 
	 * @return the card at the top of the deck
	 */
	public Card getTop() {
		return cards.get(0);
	}

	/**
	 * Get the card at the bottom of the deck (index {@code size() - 1})
	 * 
	 * @return the card at the bottom of the deck
	 */
	public Card getBottom() {
		return cards.get(cards.size() - 1);
	}

	/**
	 * Get a random card in the deck.
	 * 
	 * @return a random card in the deck.
	 */
	public Card getRandom() {
		return cards.get(Tools.Numbers.randomInt(0, cards.size() - 1));
	}

	/**
	 * Take the current card in the deck, shuffle them around, and put them back
	 * into the deck.
	 * 
	 * @return This Deck
	 */
	public Deck shuffle() {
		ArrayList<Card> res = new ArrayList<>();
		ArrayList<Card> temp = (ArrayList<Card>) this.getCards().clone();
		int times = temp.size();

		for (int i = 0; i < times; i++) {
			res.add(temp.remove(Tools.Numbers.randomInt(0, temp.size() - 1)));
		}

		setCards(res);
		return this;
	}
	
	@Override
	public String toString() {
		return this.getCards().toString();
	}
}
