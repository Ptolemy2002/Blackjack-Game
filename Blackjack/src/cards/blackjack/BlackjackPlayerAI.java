package cards.blackjack;

import main.Tools;

public class BlackjackPlayerAI extends BlackjackPlayer {
	private int target = 0;

	public BlackjackPlayerAI(BlackjackGame game, int id) {
		super(game, id);
		this.valuableAce = false;
	}

	public int drawUntilTarget(int maxHits) {
		if (this.hasSoftHand()) {
			this.valuableAce = this.getValue(true) >= target && this.getValue(true) <= 21;
			if (this.valuableAce) {
				System.out.println(this.toString() + " has decided to count their ace as 11!");
			}
		}

		int hits = 0;
		while (hits < maxHits && this.getValue() < this.target) {
			this.deal(gameIn.getDeck().drawTop().setFaceUp(true));
			hits++;
			if (this.getGame().getDeck().getCards().size() == 0) {
				System.out.println("The deck ran out of cards!");
				System.out.println("Resetting it...");
				this.getGame().resetDeck();
				this.getGame().getDeck().shuffle();
			}
			System.out.println(this.toString() + " has hit!");
			System.out.println(
					this.toString() + " now has the hand " + this.getHand() + " with the value " + this.getValue());
			if (this.getValue() > 21) {
				this.surrendered = true;
				System.out.println(
						this.toString() + " has gone bust! They are forced to surrender and lose their bet ($"
								+ this.getBet() + ")!");;
				this.collect(this.getBet());
				break;
			}
		}

		return hits;
	}

	@Override
	public void play() {
		if (!this.surrendered) {
			System.out.println("It's " + this.toString() + "'s turn!");
			int maxHits = ((BlackjackGame) gameIn).getMaxHits();
			int hits = 0;
			int dealerValue = ((BlackjackGame) gameIn).getVisibleDealerValue();
			
			if (this.hasSoftHand()) {
				target = 18;
			}
			else if (dealerValue >= 7) {
				target = 17;
			} else if (dealerValue >= 4) {
				target = 12;
			} else {
				target = 13;
			}

			hits += drawUntilTarget(maxHits);
			if (hits < maxHits) {
				System.out.println(this.toString() + " has passed.");
			} else {
				System.out.println(this.toString() + " has run out of hits!");
			}
		} else {
			if (this.hasNatural()) {
				System.out.println(this.toString() + " has the hand " + this.getHand().toString()
						+ " with the value " + this.getValue()
						+ (this.hasNatural() ? " That's a natural!" : ""));
				System.out.println("They won't play anymore.");
			} else {
				System.out.println(this.toString() + " has surrendered, so they can't play.");
			}
		}
	}

	@Override
	public Double makeBet(Double min, Double max) {
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
