package cards.blackjack;

import cards.Card;
import cards.EnumCardNumber;
import main.Tools;

public class BlackjackPlayerAI extends BlackjackPlayer {
	private int target = 0;

	public BlackjackPlayerAI(BlackjackGame game, int id) {
		super(game, id);
		this.valuableAce = false;
	}

	public int drawUntilTarget(int maxHits) {
		//Attempt to compensate for the unknown card
		if (this.isDoublingdown) {
			target -= 3;
		}
		
		if (this.hasSoftHand()) {
			this.valuableAce = this.getVisibleValue(true) >= target && this.getVisibleValue(true) <= 21;
			if (this.valuableAce) {
				System.out.println(this.toString() + " has decided to count their ace as 11!");
			} else {
				System.out.println(this.toString() + " has decided not to count their ace as 11!");
			}
		}

		if (this.getValue() > 21) {
			this.surrendered = true;
			for (Card i : this.hand.getCards()) {
				i.setFaceUp(true);
			}
			System.out.println(this.toString() + " has gone bust with the hand " + this.getHand() + " and the value "
					+ this.getValue() + "! They are forced to surrender and lose their bet ($"
					+ (this.getBet() * (isDoublingdown ? 2 : 1)) + ")!");
			this.collect((this.getBet() * (isDoublingdown ? 2 : 1)));
		}

		int hits = 0;
		while (hits < maxHits && this.getVisibleValue() < this.target) {
			this.deal(gameIn.getDeck().drawTop().setFaceUp(true));
			hits++;
			if (this.getGame().getDeck().getCards().size() == 0) {
				System.out.println("The deck ran out of cards!");
				System.out.println("Resetting it...");
				this.getGame().resetDeck();
				this.getGame().getDeck().shuffle();
			}
			System.out.println(this.toString() + " has hit!");
			System.out.println(this.toString() + " now has the hand " + this.getHand() + " with the value "
					+ this.getVisibleValue());
		}

		return hits;
	}

	@Override
	public void play() {
		if (!this.surrendered) {
			System.out.println("It's " + this.toString() + "'s turn!");
			if (isFirstPlay) {
				if (canDoubleDown()) {
					if (getValue() == 11) {
						isDoublingdown = true;
					} else if (getValue() == 10 && !(gameIn.getDealerHand().hasCard(EnumCardNumber.ACE, true)
							|| gameIn.getDealerHand().hasCard(EnumCardNumber.TEN, true))) {
						isDoublingdown = true;
					} else if (getValue() == 9 && ((BlackjackGame) gameIn).getVisibleDealerValue() >= 2
							&& ((BlackjackGame) gameIn).getVisibleDealerValue() <= 6) {
						isDoublingdown = true;
					} else {
						isDoublingdown = false;
					}

					if (isDoublingdown) {
						System.out.println(this.toString() + " has decided to double down!");
						System.out.println(this.toString() + " has been dealt a face down card.");
						System.out.println(this.toString() + " now has the hand " + this.getHand().toString()
								+ " with the value " + this.getVisibleValue());
						this.deal(gameIn.getDeck().drawTop().setFaceUp(false));
					} else {
						System.out.println(this.toString() + " has decided not to double down!");
					}
				}
				isFirstPlay = false;
			}

			int maxHits = ((BlackjackGame) gameIn).getMaxHits();
			int hits = 0;
			int dealerValue = ((BlackjackGame) gameIn).getVisibleDealerValue();

			if (this.hasSoftHand()) {
				target = 18;
			} else if (dealerValue >= 7) {
				target = 17;
			} else if (dealerValue >= 4) {
				target = 12;
			} else {
				target = 13;
			}

			hits += drawUntilTarget(maxHits);

			for (Card i : this.hand.getCards()) {
				i.setFaceUp(true);
			}

			if (hits < maxHits && !goneBust) {
				System.out.println(this.toString() + " has passed.");
			} else if (!goneBust) {
				System.out.println(this.toString() + " has run out of hits!");
			}
			
			if (this.getValue() > 21) {
				this.surrendered = true;
				this.goneBust = true;
				for (Card i : this.hand.getCards()) {
					i.setFaceUp(true);
				}
				System.out.println(this.toString() + " has gone bust with the hand " + this.getHand()
						+ " and the value " + this.getValue() + "! They are forced to surrender and lose their bet ($"
						+ (this.getBet() * (isDoublingdown ? 2 : 1)) + ")!");
				this.collect((this.getBet() * (isDoublingdown ? 2 : 1)));
			}
			
			if (isDoublingdown && isFirstPlay) {
				System.out.println("Cards that were face down have been turned face up.");
				System.out.println(this.toString() + " now has the hand " + this.getHand().toString()
						+ " with the value " + this.getVisibleValue());
			}
		} else {
			if (this.hasNatural()) {
				System.out.println(this.toString() + " has the hand " + this.getHand().toString() + " with the value "
						+ this.getVisibleValue() + (this.hasNatural() ? " That's a natural!" : ""));
				System.out.println("They won't play anymore.");
			} else if (goneBust) {
				System.out.println(this.toString() + " has gone bust with the hand " + this.getHand()
						+ " and the value " + this.getValue());
				System.out.println("They won't play anymore.");
			} else {
				System.out.println(this.toString() + " has surrendered, so they can't play.");
			}
		}
	}

	@Override
	public Double makeBet(Double min, Double max) {
		System.out.println(this.toString() + " has $" + this.getMoney());
		// If they don't have enough money, they will bet less.
		Double result = Tools.Numbers.roundDouble(Tools.Numbers.randomDouble(min,
				(max > this.getMoney() ? this.getMoney() < min ? min + 10 > max ? max : min + 10 : this.getMoney()
						: max)),
				2);
		System.out.println(this.toString() + " is betting $" + result);
		this.setBet(result);
		return result;
	}

	@Override
	public boolean isAI() {
		return true;
	}

}
