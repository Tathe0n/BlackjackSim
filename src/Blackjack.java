import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.text.*;

public class Blackjack {
	int NumberOfdecks = 1;
	Deck deck = new Deck(NumberOfdecks);
	Player player = new Player(deck);
	Dealer dealer = new Dealer(deck);
	Scanner in = new Scanner(System.in);
	ArrayList<Player> players = new ArrayList<Player>();

	int currentPlayer = 0;
	int wins = 0;
	int rounds = 0;
	int runningCount = 0;
	int trueCount = 0;
	int total = 0;
	int combinationCounter = 0;
	int valueFrom = 0;
	int valueTo = 0;

	public Blackjack() {

		// Deck erstellen
		deck.fillDeck();
		players.add(player);
		System.out.println(
				"Liste der möglichen Aktionen: hit, double, stand, split, %, Werte von x bis y, genauer Wert, Werte von x bis y, genaue Werte");

		while (deck.counter() >= 4) {

			// erste Karten verteilen
			player.addCard();
			player.addCard();
			dealer.addCard();

			System.out.println(dealer.showDealerHand());

			// Aktionen des Spielers
			String action = "";
			breakout: while (true) {
				if (player.bust() == true || player.sumValue() == 21 || player.endGame()) {
					if (players.size() <= 1) {
					} else {
						System.out
								.println("Player " + (currentPlayer + 1) + " finished with " + player.showPlayerHand());
					}
					currentPlayer++;

					if (currentPlayer < players.size()) {
						player = players.get(currentPlayer);
					} else {
						break breakout;
					}
				}
				System.out.println(player.showPlayerHand());

				if (players.size() > 1) {
					System.out.println('\n' + "Aktion(Stapel " + (currentPlayer + 1) + "): ");
				} else {
					System.out.println('\n' + "Aktion: ");
				}

				action = in.nextLine();
				switch (action) {
				case "hit":
					player.hit();
					runningCount(player.getCurrentCardValue());
					break;
				case "double":
					player.doubleDown();
					System.out.println(player.showPlayerHand());
					runningCount(player.getCurrentCardValue());
					break;
				case "stand":
					player.stand();
					System.out.println(player.showPlayerHand());
					break;
				case "split":
					Player nPlayer = player.split();
					if (nPlayer != null) {
						players.add(nPlayer);
					}
					rounds++;
					break;
				// Gibt aktuelle Gewinnwahrscheinlichkeit gerundet zurück
				case "%":
					DecimalFormat df = new DecimalFormat("#.##");
					System.out.println("Gewinnchance: " + df.format((double) wins / (double) rounds * 100) + "%");
					System.out.println("Runden: " + rounds);
					System.out.println("Gewonnene-Runden: " + wins);
					break;
				case "!!!":
					valueFrom = 21;
					valueTo = 32;
					Vector<Vector<Cards>> allCombinations = possibleCombinations();
					System.out.println("alle Kombinationsmöglichkeiten: " + combinationCounter);
					// printAllCombinations(allCombinations);
					break;

				case "genauer Wert":
					System.out.println("Welcher Wert genau?");
					String specific = in.nextLine();

					try {
						valueFrom = Integer.parseInt(specific);
					} catch (NumberFormatException e) {
						System.out.println("Fehler, Eingabe muss eine Nummer sein!");
						break;
					}
					valueTo = valueFrom;
					Vector<Vector<Cards>> exactCombinations = possibleCombinations();
					System.out.println(combinationCounter);
					// printAllCombinations(exactCombinations);
					System.out.println(overallChances(exactCombinations.size()) + " %");

					break;

				case "genaue Werte":
					System.out.println("Welcher Wert genau?");
					String specificTo = in.nextLine();
					int specificToInt;
					int valueFromValueRange = player.sumValue() + 1;
					valueFrom = valueFromValueRange;
					//System.out.println(valueFromValueRange);
					try {
						specificToInt = Integer.parseInt(specificTo);
					} catch (NumberFormatException e) {
						System.out.println("Fehler, Eingabe muss eine Nummer sein!");
						break;
					}
					for (int i = 0; i <= specificToInt; i++) {
						
						
						valueFromValueRange=+ i;
						valueTo = valueFromValueRange;
						
						valueFrom = valueTo;
						Vector<Vector<Cards>> exactToCombinations = possibleCombinations();
						//System.out.println(combinationCounter);
						// printAllCombinations(exactCombinations);
						if (valueFromValueRange >= player.sumValue())
						System.out.println(overallChances(exactToCombinations.size()) + " %		Wert: " + valueFromValueRange);
					}

					break;
				case "Werte von x bis y":
					System.out.println("Wert von?");
					String valueX = in.nextLine();
					System.out.println("Wert bis?");
					String valueY = in.nextLine();

					try {
						valueFrom = Integer.parseInt(valueX);
					} catch (NumberFormatException e) {
						System.out.println("Fehler, Eingabe muss eine Nummer sein!");
						break;
					}

					try {
						valueTo = Integer.parseInt(valueY);
					} catch (NumberFormatException e) {
						System.out.println("Fehler, Eingabe muss eine Nummer sein!");
						break;
					}
					Vector<Vector<Cards>> FromToCombinations = possibleCombinations();
					// System.out.println(combinationCounter);
					// printAllCombinations(FromToCombinations);
					System.out.println(overallChances(FromToCombinations.size()) + " %");
					break;
				default:
					System.out.println("Befehl existiert (noch) nicht!");
				}
			}
			rounds++;
			if (playerBlackjack() || player.bust()) {
				dealer.hit();

			} else {
				// Aktionen des Dealers
				while (dealer.bust() == false && dealer.sumValue() < 17) {
					dealer.hit();
					runningCount(dealer.getCurrentCardValue());
					System.out.println(dealer.showDealerHand());
				}

			}
			System.out.print('\n' + dealer.showDealerHand());
			System.out.println("Value: " + dealer.sumValue());
			if (players.size() <= 1) {
				System.out.print(players.get(0).showPlayerHand());
				System.out.println("Value: " + players.get(0).sumValue());
			} else {
				for (int i = 0; i < players.size(); i++) {
					System.out.print((i + 1) + ". ");
					System.out.print(players.get(i).showPlayerHand());
					System.out.println("Value: " + players.get(i).sumValue());
				}
			}
			// Auswertung
			// Bei Unentschieden Runde entfernt da nicht relevant
			// Blackjack
			if (playerBlackjack() == true && dealerBlackjack() == false) {
				System.out.println("Winner, winner, chicken dinner!!!");
				wins++;
				rounds++;
			}
			if (playerBlackjack() == false && dealerBlackjack() == true) {
				System.out.println("Dealer-Blackjack");
				rounds++;
			}
			// Unentschieden Blackjack
			else if (playerBlackjack() && dealerBlackjack()) {
				System.out.println("Spieler: Untentschieden!");
				rounds--;

			} else {

				for (int i = 0; i < players.size(); i++) {
					// player !bust | dealer bust
					if (players.get(i).bust() == false && dealer.bust() == true) {
						System.out.println("Spieler" + (i + 1) + ": Gewonnen!");
						wins++;
						rounds++;
					} else if (players.get(i).bust() == true && dealer.bust() == false) {
						System.out.println("Spieler" + (i + 1) + ": Verloren!");
						wins++;
						rounds++;
					}
					// player Punkte > dealer Punkte
					else if (players.get(i).sumValue() > dealer.sumValue()) {
						System.out.println("Spieler" + (i + 1) + ": Gewonnen!");
						wins++;
						rounds++;
					}
					// Unentschieden Punkte
					else if (players.get(i).sumValue() == dealer.sumValue()) {
						System.out.println("Spieler" + (i + 1) + ": Untentschieden!");
						rounds--;
					} else {
						System.out.println("Spieler" + (i + 1) + ": Verloren!");
					}

				}

			}
			in.nextLine();

			dealer.clearHand();
			players.get(0).clearHand();
			currentPlayer = 0;
			for (int i = 1; i < players.size(); i++) {
				players.remove(i);

			}
			// Console "clearen"
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
		}

	}

	private int valueFrom(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean dealerBlackjack() {
		if (dealer.counter() == 2 && dealer.sumValue() == 21) {
			return true;
		} else {
			return false;
		}
	}

	public boolean playerBlackjack() {
		if (players.size() == 1 && players.get(0).counter() == 2 && players.get(0).sumValue() == 21) {
			return true;
		} else {
			return false;
		}

	}

	public void printAllCombinations(Vector<Vector<Cards>> allCombinations) {
		// int counter = 0;
		for (int i = 0; i < allCombinations.size(); i++) {
			for (int j = 0; j < allCombinations.get(i).size(); j++) {
				if (allCombinations.get(i).get(j) != null) {
					System.out.print(allCombinations.get(i).get(j).getSymbol());
					// counter++;
				}
			}
			System.out.println("");

		}
		System.out.println(allCombinations.size());
	}

	// Prüfen ob aktueller Kartenwert innerhalb der geg. Werte ist
	public int valueInRange(Vector<Cards> combination) {

		int ace = 0;
		int sum = 0;
		// 1 = fortfahren und nicht speichern | 0 = abbrechen und speichern |
		// 2 = abbrechen, aber nicht speichern | 3 = fortfahren, aber speichern
		for (int i = 0; i < combination.size(); i++) {
			if (combination.get(i) != null) {
				if (combination.get(i).getValue() == 11) {
					ace++;
				}
				sum = sum + combination.get(i).getValue();

				while (ace != 0 && sum > 21) {
					sum = sum - 10;
					ace = ace - 1;
				}
			}
		}
		// nicht speichern und abbrechen
		if ((sum > 21 && (sum < valueFrom || sum > valueTo)) || sum > valueTo) {
			return 2;
		} // abbrechen und speichern
		else if (sum > 21) {
			return 0;
		} // fortfahren und speichern
		else if (sum >= valueFrom && sum <= valueTo) {
			return 3;
		} // fortfahren und nicht speichern
		else if (sum < valueFrom) {
			return 1;
		} // abbrechen und speichern/ eigentlich nicht notwendig -> alle Möglichkeiten
			// oben
		else
			return 0;
	}

	// Math
	public double chancesIfHit() {
		total = players.get(currentPlayer).sumValue();
		return ((double) deck.lowerAs(total) / (double) deck.counter()) * 100;
	}

	public float overallChances(int zaehler) {
		float chances;
		valueFrom = 21;
		valueTo = 31;
		int nenner = possibleCombinations().size();
		System.out.println(nenner + "  " + zaehler);
		chances = (float) zaehler / nenner * 100;
		return chances;
	}

	// wird zuerst aufgerufen; fügt die Karten des aktiven Spielers dem
	// Vector(combination) hinzu
	public Vector<Vector<Cards>> possibleCombinations() {
		Vector<Vector<Cards>> allCombinations = new Vector<Vector<Cards>>();
		combinationCounter = 0;
		Vector<Cards> combination = new Vector<Cards>();
		for (int i = 0; i < player.playerHand.length; i++) {
			combination.add(player.playerHand[i]);
		}
		// Start der rekursiven Methode
		possibleCombinationsWithStartCards(combination, deck.copyDeck(), allCombinations);
		return allCombinations;
	}

//zu viele Decks -> out of memory(-> stürzt ab)
	// Rekursiv
//*in den Text*
	// Fügt weitere Karte aus deckCopy einem Vector hinzu
	public void possibleCombinationsWithStartCards(Vector<Cards> combination, Cards[] deckCopy,
			Vector<Vector<Cards>> allCombinations) {
		// Alle übrigen Karten, werden jeweils hinzugefügt
		for (int i = 0; i < deckCopy.length; i++) {
			// Karte im Deck?
			if (deckCopy[i] != null) {
				Vector<Cards> newCombination = (Vector<Cards>) combination.clone();
				Cards[] newDeckCopy = deckCopy.clone();
				newCombination.add(newDeckCopy[i]);
				newDeckCopy[i] = null;
				// temp. Variable valueInRange
				int vIR = valueInRange(newCombination);
				// Abbrechen der Rekursion, s. valueInRange

				if (vIR == 0 || vIR == 2) {
					// Abbrechen und speichern
					if (vIR == 0) {
						combinationCounter++;
						allCombinations.add(newCombination);
					}
					// Weiterführen der Rekursion
				} else {
					if (vIR == 3) {
						combinationCounter++;
						allCombinations.add(newCombination);
						possibleCombinationsWithStartCards(newCombination, newDeckCopy, allCombinations);
					} else {
						possibleCombinationsWithStartCards(newCombination, newDeckCopy, allCombinations);
					}
				}
			}
		}
	}

	// Counting
	public int runningCount(int currentValue) {
		if (currentValue >= 10)
			runningCount--;
		else if (currentValue <= 6)
			runningCount++;
		trueCount = runningCount / NumberOfdecks;
		return runningCount;
	}
}