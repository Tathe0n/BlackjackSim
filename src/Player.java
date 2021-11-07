public class Player {

	public Cards playerHand[];
	private Deck deck;
	private boolean endGame = false;
	int counter;

	public Player(Deck nDeck) {
		playerHand = new Cards[11];
		deck = nDeck;
		counter = 0;
	}

	public Player(Deck nDeck, Cards nCards) {
		playerHand = new Cards[11];
		deck = nDeck;
		playerHand[0] = nCards;
		counter = 0;
	}

	public void addCard() {

		for (int i = 0; playerHand[counter] != null; i++) {
			counter++;
		}
		playerHand[counter] = deck.randomCard();
	}
//testing
	public void addCard2() {
		char colorToDelete1 = 'H';
		int valueToDelete1 = 9;
		String pictureToDelete1 = "A";
		playerHand[0] = new Cards(colorToDelete1, valueToDelete1, pictureToDelete1, valueToDelete1);
		deck.deleteCard(colorToDelete1, valueToDelete1);

		char colorToDelete2 = 'C';
		int valueToDelete2 = 10;
		String pictureToDelete2 = "";
		playerHand[1] = new Cards(colorToDelete2, valueToDelete2, pictureToDelete2, valueToDelete2);
		deck.deleteCard(colorToDelete2, valueToDelete2);
	}

	public int getCurrentCardValue() {
		for (int i = 0; playerHand[counter] != null; i++) {
			counter++;
		}
		return playerHand[counter - 1].getValue();
	}

	public String showPlayerHand() {
		String ret = "Player-Hand: ";
		for (int i = 0; playerHand[i] != null; i++) {
			ret = ret + playerHand[i].getColour() + playerHand[i].getSymbol() + "|";
		}
		return ret;
	}

	public String showPlayerHand2() {
		String ret = "Player-Hand: ";
		for (int i = 0; i <= 10; i++) {
			try {
				ret = ret + playerHand[i].getColour() + playerHand[i].getSymbol() + "|";
			} catch (NullPointerException e) {
				System.out.println("null");
			}
		}
		return ret;
	}

	public int sumValue() {
		int ace = 0;
		int sum = 0;
		for (int i = 0; playerHand[i] != null; i++) {
			if (playerHand[i].getValue() == 11) {
				ace++;
			}
			sum = sum + playerHand[i].getValue();
		}
		while (ace != 0 && sum > 21) {
			sum = sum - 10;
			ace = ace - 1;
		}
		return sum;
	}

	public boolean bust() {
		if (sumValue() > 21) {
			return true;
		} else {
			return false;
		}

	}

	public void hit() {
		addCard();
	}

	public void doubleDown() {
		addCard();
		endGame = true;
	}

	public void stand() {
		endGame = true;
	}

	public boolean endGame() {
		return endGame;
	}

	public Player split() {
		if (playerHand[2] != null || playerHand[0].getValue() != (playerHand[1].getValue())) {
			System.out.println("Split nicht möglich!");
			return null;
		} else {
			Player player2 = new Player(deck, playerHand[1]);
			playerHand[1] = null;
			counter--;
			addCard();
			player2.addCard();
			return player2;
		}
	}

	public boolean splitPossible() {
		if (playerHand[2] != null || playerHand[0].getValue() != (playerHand[1].getValue())) {
			return false;
		} else {
			return true;
		}
	}

	public void clearHand() {
		for (int i = 0; i <= 10; i++) {
			playerHand[i] = null;

		}
		endGame = false;
		counter = 0;
	}

	public int counter() {
		return counter + 1;
	}

	public int ContainsAce() {
		int counter = 0;
		int aces = 0;
		while (playerHand[counter] != null) {
			if (playerHand[counter].getSymbol().equals("A")) {
				aces++;
			}
			counter++;
		}
		return aces;
	}

}
