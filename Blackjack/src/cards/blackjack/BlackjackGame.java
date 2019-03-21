package cards.blackjack;

import java.util.ArrayList;

import cards.Card;
import cards.CardGame;
import cards.CardPlayer;
import cards.Deck;
import cards.EnumCardNumber;
import main.Tools;

/**
 * The game will act as the dealer.
 */

public class BlackjackGame extends CardGame {
	public Integer maxHits;
	protected boolean valuableAce = false;

	public BlackjackGame(Deck deck) {
		this.setDeck(deck);
	}

	public void printDescription() {
		System.out.println("");
		System.out.println(
				"Each player is dealt two face up cards. The dealer is dealt one face up and one face down card.");
		System.out.println("");
		System.out.println("Each player calculates their total.");
		if (Tools.Console.askBoolean("Would you like to hear the counting rules?", true)) {
			System.out.println("Each card is worth it's pip value.");
			System.out.println("A face is worth 10.");
			System.out.println("An ace is worth either 1 or 11 (you decide)");
		}
		System.out.println("");
		System.out.println("Each player tries to get as close as possible to 21.");
		System.out.println("When it is your turn, you can either stand, hit, or surrender.");
		System.out.println("Standing means that you don't draw anything.");
		System.out.println("Hitting means you draw a card.");
		System.out.println("If you surrender, you take back half your bet and stop playing.");
		System.out.println("You can hit up to " + (this.maxHits == Integer.MAX_VALUE ? "Infinity" : this.maxHits)
				+ " times (changable in properties).");
		System.out.println("If you go over 21, you lose!");
		System.out.println("This is called going 'bust'");
		System.out.println("You can change the value of your aces to avoid going bust.");

		System.out.println("The dealer goes after every player, and must hit until the total is over 17.");
		System.out.println("If the dealer goes bust, the user wins.");

		System.out.println("The dealer's play is automatic, so the computer handles his moves.");
		System.out.println("");

		System.out.println("If you are dealt a 21 (face and an ace) originally, you have a natural!");
		System.out.println("If you have a natural and the dealer does not, you get 1.5 times your bet!");
		System.out.println("If the dealer has a natural and you don't, you must pay the dealer your bet!");
		System.out.println("If both you and the dealer have naturals, you neither lose nor gain anything.");
		System.out.println("");

		System.out.println("Once the dealer goes over 17, the winner is determined.");
		System.out.println("If you are closer to 21 than the dealer, you win and take double your bet!");
		System.out.println("If you are farther from 21 than the dealer, you lose your bet.");
	}

	@Override
	public CardPlayer addNewPlayer(boolean ai) {
		CardPlayer player;
		if (ai) {
			player = new BlackjackPlayerAI(this, this.getPlayers().size() + 1);
		} else {
			player = new BlackjackPlayer(this, this.getPlayers().size() + 1);
		}

		this.addPlayer(player);
		return player;
	}

	public void dealerPlay() {
		for (Card i : this.dealerHand.getCards()) {
			i.setFaceUp(true);
		}
		System.out.println(
				"The dealer has the hand " + this.dealerHand + " with the value " + this.getVisibleDealerValue());
		int hits = 0;
		while (this.getDealerValue() < 17 && hits < maxHits) {
			this.dealDealer(this.getDeck().drawTop().setFaceUp(true));
			this.valuableAce = !(this.getDealerValue(true) > 21);
			System.out.println("The dealer has hit!");
			System.out.println("The dealer now has the hand " + this.dealerHand + " with the value "
					+ this.getVisibleDealerValue());
			hits++;
			if (this.getDeck().getCards().size() == 0) {
				System.out.println("The deck ran out of cards!");
				System.out.println("Resetting it...");
				this.resetDeck();
				this.getDeck().shuffle();
			}
		}
	}

	@Override
	public void start() {
		for (CardPlayer i : this.getPlayers()) {
			i.setMoney(Tools.Numbers.roundDouble(i.getMoney(), 2));
		}
		
		if (this.getDeck().getCards().size() < (2 * this.getPlayers().size()) + 2) {
			System.out.println("The deck must be at least " + ((2 * this.getPlayers().size()) + 2)
					+ " cards long to play this game.");
			return;
		}

		System.out.println("It's time to play blackjack!");
		System.out.println("Shuffling deck...");
		this.getDeck().shuffle();
		System.out.println("The deck has been shuffled.");
		System.out.println("Dealing players...");
		dealHands();
		System.out.println("All players have been dealt.");
		if (this.hasNatural()) {
			System.out.println("The dealer has a natural!");
			for (CardPlayer i : this.getPlayers()) {
				if (((BlackjackPlayer) i).hasNatural()) {
					System.out.println(i.toString() + " also has a natural, so they get nothing.");
				} else {
					i.collect(i.getBet());
					System.out.println(
							i.toString() + " does not hava a natural, so they lose their bet ($" + i.getBet() + ")!");
				}
				return;
			}
		} else {
			for (CardPlayer i : this.getPlayers()) {
				if (((BlackjackPlayer) i).hasNatural()) {
					i.pay(i.getBet() * 1.5);
					((BlackjackPlayer) i).setSurrendered(true);
					System.out.println(i.toString() + " has a natural, so they win with a payout of 3:2 ($"
							+ (i.getBet() * 1.5) + ")! They will no longer play.");
				}
			}
		}

		// The dealer makes a decision on whether or not to count his ace as 11
		// initially.
		this.valuableAce = !(this.getDealerValue(true) > 21);
		// Play at least once.
		for (CardPlayer i : this.getPlayers()) {
			if (Tools.Console.askBoolean("Would you like to view everyone's hands?", true)) {
				System.out.println("The dealer has the hand " + this.getDealerHand() + " with the value "
						+ this.getVisibleDealerValue());
				for (CardPlayer j : this.getPlayers()) {
					if (((BlackjackPlayer) j).surrendered) {
						System.out.println(j.toString() + " has surrendered!");
					} else {
						System.out.println(j.toString() + " has the hand " + j.getHand().toString() + " with the value "
								+ ((BlackjackPlayer) j).getValue());
					}
				}
			}
			i.play();
		}
		dealerPlay();

		while (this.getDealerValue() < 17 && this.getDealerValue() <= 21) {
			for (CardPlayer i : this.getPlayers()) {
				if (Tools.Console.askBoolean("Would you like to view everyone's hands?", true)) {
					System.out.println("The dealer has the hand " + this.getDealerHand() + " with the value "
							+ this.getVisibleDealerValue());
					for (CardPlayer j : this.getPlayers()) {
						if (((BlackjackPlayer) j).surrendered) {
							System.out.println(j.toString() + " has surrendered!");
						} else {
							System.out.println(j.toString() + " has the hand " + j.getHand().toString()
									+ " with the value " + ((BlackjackPlayer) j).getValue());
						}
					}
				}
				i.play();
			}
			dealerPlay();
		}

		System.out.println("The game ended!");
		if (this.getDealerValue() > 21) {
			System.out.println("The dealer has gone bust!");
		}

		for (Card i : this.dealerHand.getCards()) {
			i.setFaceUp(true);
		}

		if (Tools.Console.askBoolean("Would you like to view everyone's final hands?", true)) {
			if (this.getDealerValue() <= 21) {
				System.out.println("The dealer has the hand " + this.getDealerHand() + " with the value "
						+ this.getVisibleDealerValue());
			} else {
				System.out.println("The dealer has gone bust!");
			}

			for (CardPlayer j : this.getPlayers()) {
				if (((BlackjackPlayer) j).surrendered) {
					System.out.println(j.toString() + " has surrendered!");
				} else {
					System.out.println(j.toString() + " has the hand " + j.getHand().toString() + " with the value "
							+ ((BlackjackPlayer) j).getValue());
				}
			}
		}
		
		System.out.println("Winners will now be determined.");
		for (CardPlayer i : this.getPlayers()) {
			if (!((BlackjackPlayer) i).surrendered) {
				if (((BlackjackPlayer) i).getValue() > this.getDealerValue() || this.getDealerValue() > 21) {
					i.pay(Tools.Numbers.roundDouble(i.getBet(), 2));
					System.out.println(i.toString() + " has beat the dealer and won with a payout of 1:1 ($"
							+ Tools.Numbers.roundDouble(i.getBet(), 2) + ")");
				} else if (((BlackjackPlayer) i).getValue() < this.getDealerValue()) {
					i.collect(i.getBet());
					System.out
							.println(i.toString() + " hasn't beat the dealer and lost their bet ($" + i.getBet() + ")");
				} else {
					System.out.println(i.toString() + " has tied the dealer, so they get nothing.");
				}
			} else {
				System.out.println(i.toString() + " has surrendered.");
			}
		}
		for (CardPlayer i : this.getPlayers()) {
			i.setMoney(Tools.Numbers.roundDouble(i.getMoney(), 2));
		}
		if (Tools.Console.askBoolean("Would you like to reset everyone's bets?", true)) {
			for (CardPlayer i : this.getPlayers()) {
				i.setBet(0.0);
			}
		}
	}

	@Override
	public void makeBets(Double min, Double max) {
		System.out.println("It's time for bet setup!");

		for (CardPlayer i : this.getPlayers()) {
			i.makeBet(min, max);
			System.out.println("\"" + i.getName() + "\" is betting $" + i.getBet());
		}

		System.out.println("All bets have been made.");
	}

	public void makeBets(Double min, Double max, Double aiMin, Double aiMax) {
		System.out.println("It's time for bet setup!");

		for (CardPlayer i : this.getPlayers()) {
			if (i.isAI()) {
				i.makeBet(aiMin, aiMax);
			} else {
				i.makeBet(min, max);
			}
		}

		System.out.println("All bets have been made.");
	}

	@Override
	public void dealHands() {
		this.setDealerHand(new Deck(new ArrayList<>()));
		// Dealer gets one face up and one face down card.
		this.getDealerHand().putCardAtTop(this.getDeck().drawTop().setFaceUp(true))
				.putCardAtTop(this.getDeck().drawTop().setFaceUp(false));

		for (CardPlayer i : this.getPlayers()) {
			((BlackjackPlayer) i).setSurrendered(false);
			i.getHand().setCards(new ArrayList<>());
			// Each player gets two face up cards
			i.deal(this.getDeck().drawTop().setFaceUp(true)).deal(this.getDeck().drawTop().setFaceUp(true));
		}
	}

	public BlackjackGame setMaxHits(Integer hits) {
		this.maxHits = hits;
		return this;
	}

	public int getMaxHits() {
		return maxHits;
	}

	public int getDealerValue() {
		int res = 0;
		int aces = 0;
		for (Card i : this.dealerHand.getCards()) {
			if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
				res += 10;
			} else if (i.number == EnumCardNumber.ACE) {
				aces++;
				// A player cannot count more than 1 ace as 11, or they will go bust.
				if (this.isAceValuabe() && aces == 1) {
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

	public int getVisibleDealerValue() {
		int res = 0;
		int aces = 0;
		for (Card i : this.dealerHand.getCards()) {
			if (i.faceUp) {
				if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
					res += 10;
				} else if (i.number == EnumCardNumber.ACE) {
					aces++;
					// A player cannot count more than 1 ace as 11, or they will go bust.
					if (this.isAceValuabe() && aces == 1) {
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

	public int getDealerValue(boolean valuableAce) {
		int res = 0;
		int aces = 0;
		for (Card i : this.dealerHand.getCards()) {
			if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
				res += 10;
			} else if (i.number == EnumCardNumber.ACE) {
				aces++;
				// A player cannot count more than 1 ace as 11, or they will go bust.
				if (this.isAceValuabe() && aces == 1) {
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

	public int getVisibleDealerValue(boolean valuableAce) {
		int res = 0;
		int aces = 0;
		for (Card i : this.dealerHand.getCards()) {
			if (i.faceUp) {
				if (EnumCardNumber.isFace(i.number) || i.number == EnumCardNumber.TEN) {
					res += 10;
				} else if (i.number == EnumCardNumber.ACE) {
					aces++;
					// A player cannot count more than 1 ace as 11, or they will go bust.
					if (this.isAceValuabe() && aces == 1) {
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

	public boolean isAceValuabe() {
		return valuableAce;
	}

	public boolean hasNatural() {
		return this.getDealerHand().getCards().size() == 2
				&& (this.getDealerHand().getBottom().isTenCard()
						&& this.getDealerHand().getTop().number == EnumCardNumber.ACE)
				|| (this.getDealerHand().getTop().isTenCard()
						&& this.getDealerHand().getBottom().number == EnumCardNumber.ACE);
	}

}
