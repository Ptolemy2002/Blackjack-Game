package cards.blackjack;

import cards.EnumCardNumber;

import java.util.ArrayList;

import cards.Card;
import cards.CardGame;
import cards.CardPlayer;
import main.Tools;

@SuppressWarnings("serial")
public class BlackjackPlayer extends CardPlayer {
	protected boolean isFirstPlay = true;
	protected boolean valuableAce = false;
	protected boolean surrendered = false;
	protected boolean isDoublingdown = false;
	protected boolean goneBust = false;

	public BlackjackPlayer(BlackjackGame game, int id) {
		super(id);
		this.gameIn = game;
	}

	public boolean hasNatural() {
		return this.getHand().getCards().size() == 2
				&& (this.getHand().getBottom().isTenCard() && this.getHand().getTop().number == EnumCardNumber.ACE)
				|| (this.getHand().getTop().isTenCard() && this.getHand().getBottom().number == EnumCardNumber.ACE);
	}

	public boolean canDoubleDown() {
		return isFirstPlay && (getValue() == 9 || getValue() == 10 || getValue() == 11);
	}

	public boolean isDoublingdown() {
		return isDoublingdown;
	}
	
	public void setDoublingdown(boolean isDoublingdown) {
		this.isDoublingdown = isDoublingdown;
	}
	
	public boolean goneBust() {
		return goneBust;
	}

	@Override
	public void play() {
		if (surrendered) {
			if (!this.hasNatural()) {
				System.out.println(this.toString() + " has surrendered, so they can't play.");
			} else {
				System.out.println(this.toString() + " has the hand " + this.getHand().toString() + " with the value "
						+ this.getVisibleValue() + (this.hasNatural() ? " That's a natural!" : ""));
				System.out.println("They won't play anymore.");
			}

		} else {
			System.out.println("It's " + this.toString() + "'s turn!");
			if (isFirstPlay) {
				if (canDoubleDown()) {
					System.out.println(this.toString() + " can double down if they would like.");
					isDoublingdown = Tools.Console.askBoolean(
							"Would " + this.toString() + " like to double down (this decision is irreversible)?", true);
					if (isDoublingdown) {
						this.deal(gameIn.getDeck().drawTop().setFaceUp(false));
					}
				}

				isFirstPlay = false;
			}
			System.out.println("Type a command. Type \"help\" to view your choices.");

			int maxHits = ((BlackjackGame) gameIn).getMaxHits();
			int hits = 0;
			loop: while (hits < maxHits) {
				ArrayList<String> choices = new ArrayList<String>() {
					{
						add("hit");
						add("pass");
						add("surrender");
						add("view stats");
						add("help");
					}
				};

				if (this.hasSoftHand()) {
					if (!(getValue(true) > 21)) {
						if (valuableAce == false) {
							System.out.println(this.toString() + "'s value will be " + (this.getValue(true))
									+ " if they count their ace as 11. Otherwise, it will be " + this.getValue(false));
							this.valuableAce = Tools.Console
									.askBoolean("Would " + this.toString() + " like to count their ace as 11?", true);
						}
						choices.add("valuable ace");
						choices.add("invaluable ace");
					} else {
						if (valuableAce == true) {
							valuableAce = false;
							if (!(getValue() > 21)) {
								System.out.println(
										"If " + this.toString() + " counts their ace as 11, they will go bust!");
								System.out.println(this.toString() + " is forced to count their ace as 1.");
							}
						}
					}
				}
				
				if (this.getValue() > 21) {
					this.surrendered = true;
					for (Card i : this.hand.getCards()) {
						i.setFaceUp(true);
					}
					System.out.println(
							this.toString() + " has gone bust with the hand " + this.getHand() + "! They are forced to surrender and lose their bet ($"
									+ this.getBet() + ")!");
					goneBust = true;
					this.collect(this.getBet());
					break;
				}

				String choice = Tools.Console.askSelection("Choices", choices, true,
						"Blackjack\\game\\" + this.toString() + ">", null, true, false, false);
				System.out.println("");
				switch (choice) {
				case "view stats":
					System.out.println(this.toString() + "'s hand is " + this.getHand().toString() + " with the value "
							+ this.getValue());
					System.out.println(this.toString() + " has hit " + hits + "/"
							+ (maxHits == Integer.MAX_VALUE ? "Infinity" : maxHits) + " times.");
					break;
				case "hit":
					this.deal(gameIn.getDeck().drawTop().setFaceUp(true));
					System.out.println(this.toString() + " has hit!");
					System.out.println(this.toString() + " now has the hand " + this.getHand() + " with the value "
							+ this.getValue());
					hits++;
					if (this.getGame().getDeck().getCards().size() == 0) {
						System.out.println("The deck ran out of cards!");
						System.out.println("Resetting it...");
						this.getGame().resetDeck();
						this.getGame().getDeck().shuffle();
					}
					break;
				case "pass":
					System.out.println(this.toString() + " has passed.");
					break loop;
				case "surrender":
					this.collect(this.getBet() * 0.5);
					surrendered = true;
					System.out.println(this.toString() + " has surrendered and lost half their bet ($"
							+ this.getBet() * 0.5 + ")");
					break loop;
				case "help":
					System.out.println("hit - draw a card. You can do this "
							+ (maxHits == Integer.MAX_VALUE ? "Infinity" : maxHits) + " times in a turn.");
					System.out.println("pass - pass your turn and do not draw a card.");
					System.out.println("surrender - surrender and lose half your bet");
					System.out.println("view stats - view your hand and your hit amount.");
					if (choices.contains("valuable ace")) {
						System.out.println("valuable ace - count your ace as 11.");
						System.out.println("invaluable ace - count your ace as 1.");
					}
					break;
				}
				for (Card i : this.hand.getCards()) {
					i.setFaceUp(true);
				}
				System.out.println("");
			}

			if (hits == maxHits) {
				System.out.println(this.toString() + " has run out of hits!");
			}
		}

	}

	@Override
	public Double makeBet(Double min, Double max) {
		if (this.getMoney() < min) {
			System.out.println(this.toString() + " does not have enough money to avoid going into debt.");
			System.out.println(this.toString() + " should be careful with their bet!");
		}

		System.out.println(
				this.toString() + " has $" + this.getMoney() + ". The minimum bet is $" + min + ". The maximum bet is $"
						+ (max > this.getMoney() ? this.getMoney() < min ? max : this.getMoney() : max));
		if (this.getMoney() > min) {
			this.setBet(Tools.Numbers.roundDouble(
					Tools.Console.askDouble("How much would " + this.toString() + " like to bet?", true,
							x -> x >= min && x <= (max > this.getMoney() ? this.getMoney() : max),
							this.toString() + " has $" + this.getMoney() + "The minimum bet is $" + min
									+ ". The maximum bet is $"
									+ (max > this.getMoney() ? this.getMoney() < min ? max : this.getMoney() : max)),
					2));
		} else {
			this.setBet(
					Tools.Numbers
							.roundDouble(
									Tools.Console.askDouble("How much would " + this.toString() + " like to bet?", true,
											x -> x >= min && x <= max, this.toString() + " has $" + this.getMoney()
													+ "The minimum bet is $" + min + ". The maximum bet is $" + max),
									2));
		}

		return this.getBet();
	}

	public CardGame getGame() {
		return gameIn;
	}

	public int getValue() {
		int res = 0;
		int aces = 0;
		for (Card i : this.hand.getCards()) {
			if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
				res += 10;
			} else if (i.number == EnumCardNumber.ACE) {
				aces++;
				// A player cannot count more than 1 ace as 11, or they will go bust.
				if (valuableAce && aces == 1) {
					res += 11;
				} else {
					res++;
				}
			} else {
				res += i.number.ordinal() + 1;
			}
		}

		return res;
	}

	public int getVisibleValue() {
		int res = 0;
		int aces = 0;
		for (Card i : this.hand.getCards()) {
			if (i.faceUp) {
				if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
					res += 10;
				} else if (i.number == EnumCardNumber.ACE) {
					aces++;
					// A player cannot count more than 1 ace as 11, or they will go bust.
					if (valuableAce && aces == 1) {
						res += 11;
					} else {
						res++;
					}
				} else {
					res += i.number.ordinal() + 1;
				}
			}
		}

		return res;
	}

	public int getValue(boolean valuableAce) {
		int res = 0;
		int aces = 0;
		for (Card i : this.hand.getCards()) {
			if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
				res += 10;
			} else if (i.number == EnumCardNumber.ACE) {
				aces++;
				// A player cannot count more than 1 ace as 11, or they will go bust.
				if (valuableAce && aces == 1) {
					res += 11;
				} else {
					res++;
				}
			} else {
				res += i.number.ordinal() + 1;
			}
		}

		return res;
	}

	public int getVisibleValue(boolean valuableAce) {
		int res = 0;
		int aces = 0;
		for (Card i : this.hand.getCards()) {
			if (i.faceUp) {
				if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
					res += 10;
				} else if (i.number == EnumCardNumber.ACE) {
					aces++;
					// A player cannot count more than 1 ace as 11, or they will go bust.
					if (valuableAce && aces == 1) {
						res += 11;
					} else {
						res++;
					}
				} else {
					res += i.number.ordinal() + 1;
				}
			}
		}

		return res;
	}

	public boolean hasSoftHand() {
		for (Card i : this.hand.getCards()) {
			if (i.number == EnumCardNumber.ACE)
				return true;
		}

		return false;
	}

	public boolean isAceValuabe() {
		return valuableAce;
	}

	public BlackjackPlayer setSurrendered(boolean surrendered) {
		this.surrendered = surrendered;
		return this;
	}

}
