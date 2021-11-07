public class Cards {
private char colour;
private String symbolLetter;
private int symbolNumber;
private int value;
	
	public Cards (char nColour, int nValue, String nSymbolLetter, int nSymbolNumber) {
		colour = nColour;
		value = nValue;
		symbolLetter = nSymbolLetter;
		symbolNumber = nSymbolNumber;
	}

	
	public char getColour() {
		return colour;		
	}

	public String getSymbol() {
		if (symbolNumber == 0) {
			return symbolLetter;
		}
		else {
			return Integer.toString(symbolNumber);
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int nValue) {
		value = nValue;
	}
}
