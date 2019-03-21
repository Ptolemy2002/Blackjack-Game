package cards;

import java.util.ArrayList;

public abstract class CardGame {
	protected ArrayList<CardPlayer> players = new ArrayList<>();
	protected Deck deck;
	protected Deck deckClone;
	/**
	 * Optional. Some games may use this.
	 */
	protected Deck dealerHand;
	
	public CardGame addPlayer(CardPlayer player) {
		players.add(player);
		return this;
	}
	
	public CardGame removePlayer(CardPlayer player) {
		players.remove(player);
		return this;
	}
	
	public CardGame removePlayer(int player) {
		players.remove(player - 1);
		return this;
	}
	
	public ArrayList<CardPlayer> getPlayers() {
		return players;
	}
	
	public Deck getDeck() {
		return deck;
	}
	
	public Deck getDealerHand() {
		return dealerHand;
	}
	
	public CardGame dealDealer(Card card) {
		this.getDealerHand().putCardAtTop(card);
		return this;
	}
	
	public CardGame setDealerHand(Deck hand) {
		this.dealerHand = hand;
		return this;
	}
	
	public CardGame setDeck(Deck deck) {
		this.deck = deck;
		this.deckClone = new Deck(deck);
		return this;
	}
	
	public void printHands() {
		for (CardPlayer i : players) {
			i.printHand();
		}
	}
	
	/**
	 * Will reset the deck to it's original state.
	 */
	public void resetDeck() {
		this.deck = new Deck(deckClone);
	}
	
	public abstract CardPlayer addNewPlayer(boolean ai);
	public abstract void start();
	public abstract void makeBets(Double min, Double max);
	public abstract void dealHands();
}
