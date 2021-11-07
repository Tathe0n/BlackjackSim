public class Dealer {

	public Cards dealerHand[];
	private Deck deck;
	int counter;
	
	public Dealer(Deck nDeck) {
		dealerHand = new Cards[11];
		deck = nDeck;
		counter = 0;
	}

	public void addCard() {
		for (int i = 0; dealerHand[counter] != null; i++) {
			counter++;
		}
		dealerHand[counter] = deck.randomCard();
	}

	public String showDealerHand() {
		String ret = "Dealer-Hand: ";
		for (int i = 0; dealerHand[i] != null; i++) {
			ret = ret + dealerHand[i].getColour() + dealerHand[i].getSymbol() + "|";
		}
		return ret;
	}
	public int sumValue() {
		int ace = 0;
		int sum = 0;
		for (int i = 0; dealerHand[i] != null; i++) {
			if (dealerHand[i].getValue() == 11) {
				ace++;
			}
			sum = sum + dealerHand[i].getValue();
		}
		while (ace != 0 && sum > 21) {
			sum = sum - 10;
			ace = ace - 1;
		}
		return sum;
	}
	
	public boolean bust() {
		if(sumValue() > 21) {
			return true;	
		}
		else {
			return false;
		}
		
	}
	public void hit(){
		addCard();		
	}
	public void clearHand() {
		for(int i = 0; i <= 10; i++) {
			dealerHand[i] = null;
					
		}
		counter = 0;
	}
	public int counter() {
		return counter + 1;
	}
	public int ContainsAce() {
		int counter = 0;
		int aces = 0;
		while (dealerHand[counter] != null) {
			if (dealerHand[counter].getSymbol().equals("A")) {
				aces++;
			}
			counter++;
		}
		return aces;
	}
	
	
	public int getCurrentCardValue() {
		for (int i = 0; dealerHand[counter] != null; i++) {
			counter++;
		}
		return dealerHand[counter-1].getValue();
	}

}
