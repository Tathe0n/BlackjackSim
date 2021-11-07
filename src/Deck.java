import java.util.Random;

public class Deck {
	private Cards deck[];
	int counter;
	int x;

	public Deck(int nX) {
		x = nX;
		deck = new Cards[52 * x];
		counter = 0;

	}

	public void fillDeck() {
		// Zuweisung Farbe
		char colour;

		for (int j = 0; j < 4 * x; j++) {
			if (j == 0) {
				colour = 'H'; // Hearts(Herz)
			} else if (j == 1) {
				colour = 'S'; // Spades(Pik)
			} else if (j == 2) {
				colour = 'D'; // Diamonds(Karo)
			} else {
				colour = 'C'; // Clubs(Kreuz)
			}

			// Zuweisung Zahlen(2-10)
			for (int i = 2; i <= 10; i++) {
				deck[counter] = new Cards(colour, i, "", i);
				counter++;

			}
			// Zuweisung Bilder(J-A)
			deck[counter] = new Cards(colour, 10, "J", 0);
			counter++;
			deck[counter] = new Cards(colour, 10, "Q", 0);
			counter++;
			deck[counter] = new Cards(colour, 10, "K", 0);
			counter++;
			deck[counter] = new Cards(colour, 11, "A", 0);
			counter++;

		}

	}

	// random Karte finden
	// Karte löschen
	public Cards randomCard() {
		Random rand = new Random();
		int index = rand.nextInt(52 * x);
		while (deck[index] == null) {
			index = rand.nextInt(52 * x);
		}
		Cards card = deck[index];

		deck[index] = null;

		counter = counter - 1;
		return card;

	}

	public void deleteCard(char color, int value) {
		for (int i = 0; i < 52 * x; i++) {
			if (deck[i] != null) {
				if (value == deck[i].getValue()) {
					if (color == deck[i].getColour()) {
						deck[i] = null;
					}
				}
			}
			else {
				i++;
			}
		}
	}

	public int counter() {
		return counter;
	}

	// no Aces
	public int lowerAs(int cardValue) {
		int lowerAs = 0;
		cardValue = 21 - cardValue;
		if (cardValue != 0) {
			for (int i = 0; i < 52 * x; i++) {
				try {
					if (deck[i].getValue() <= cardValue || deck[i].getValue() == 11) {
						lowerAs++;
					}
				} catch (NullPointerException e) {
				}
			}
		} else {
			return 0;
		}
		return lowerAs;
	}

	// spezifischer Wert | no Aces
	public int cardsWithValue(int value) {
		int numberOfCards = 0;
		for (int i = 0; i < 52 * x; i++) {
			if (deck[i].getValue() == value) {
				numberOfCards++;
			}
		}
		return numberOfCards;
	}

	public Cards[] copyDeck() {
		return deck.clone();
	}

}
