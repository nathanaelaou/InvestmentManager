package main;

public class CurrStock { //a CurrStock object represents one of the user's current stock positions

	//declare private variables
	private String myTicker;
	private String myName;
	private int myCurrNum;
	private double myCostBasis;

	//constructor initializes private variables
	public CurrStock(String ticker, String name, int currNum, double costBasis) {
		myTicker = ticker;
		myName = name;
		myCurrNum = currNum;
		myCostBasis = costBasis;
	}
	
	//getter and setter methods for private variables
	public void setTicker(String newTicker) {
		myTicker = newTicker;
	}

	public void setName(String newName) {
		myName = newName;
	}

	public void setCurrNum(int newCurrNum) {
		myCurrNum = newCurrNum;
	}
	
	public void setCostBasis(double newCostBasis) {
		myCostBasis = newCostBasis;
	}
	
	public String getTicker() {
		return myTicker;
	}

	public String getName() {
		return myName;
	}

	public int getCurrNum() {
		return myCurrNum;
	}
	
	public double getCostBasis() {
		return myCostBasis;
	}

	public int costBasisCompareTo(CurrStock other) { //compares CurrStocks by their cost basis
		if (myCostBasis >other.getCostBasis())
			return 1;
		if (myCostBasis < other.getCostBasis())
			return -1;
		return 0;
	}
}


