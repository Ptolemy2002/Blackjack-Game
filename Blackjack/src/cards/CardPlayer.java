package cards;

import java.util.ArrayList;

public abstract class CardPlayer {
	
	protected CardGame gameIn;
	protected Deck hand = new Deck(new ArrayList<Card>());
	protected Double money = 0.0;
	protected Double bet = 0.0;
	protected int id;
	protected String name;
	
	public CardPlayer(int id) {
		this.id = id;
	}
	
	public CardPlayer(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CardPlayer deal(Card card) {
		this.hand.putCardAtBottom(card);
		return this;
	}
	
	public Deck getHand() {
		return this.hand;
	}
	
	public Double getBet() {
		return this.bet;
	}
	
	public Double getMoney() {
		return this.money;
	}
	
	public CardPlayer setBet(Double bet) {
		this.bet = bet;
		return this;
	}
	
	public CardPlayer addBet(Double bet) {
		this.bet += bet;
		return this;
	}
	
	public CardPlayer removeBet(Double bet) {
		this.bet -= bet;
		return this;
	}
	
	public CardPlayer pay(Double amount) {
		this.money += amount;
		return this;
	}
	
	public CardPlayer collect(Double amount) {
		this.money -= amount;
		return this;
	}
	
	public CardPlayer setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return name == null ? "Player " + id : name;
	}
	
	@Override
	public String toString() {
		return name == null ? "Player " + id : name;
	}
	
	public void printHand() {
		System.out.println(this.toString() + " has the hand " + this.getHand().toString());
	}
	
	public boolean isAI() {
		return false;
	}
	
	public abstract void play();
	public abstract Double makeBet(Double min, Double max);

	public CardPlayer setMoney(Double money) {
		this.money = money;
		return this;
	}

}
