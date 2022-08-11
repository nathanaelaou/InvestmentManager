package main;

public class Stock { //a Stock object represents a stock trade

	//declare private variables
	private String myTicker;
	private String myName;
	private double myPrice;
	private int myNum;
	private String myType;
	private int myDay;
	private int myMonth;
	private int myYear;

	//constructor initializes private variables
	public Stock(String ticker, String name, double price, int num, String type, int month, int day, int year) {
		myTicker = ticker;
		myName = name;
		myPrice = price;
		myNum = num;
		myType = type;
		myMonth = month;
		myDay = day;
		myYear = year;
	}

	//getter and setter methods for private variables
	public void setTicker(String newTicker) {
		myTicker = newTicker;
	}

	public void setName(String newName) {
		myName = newName;
	}

	public void setPrice(double newPrice) {
		myPrice = newPrice;
	}

	public void setNum(int newNum) {
		myNum = newNum;
	}

	public void setType(String newType) {
		myType = newType;
	}

	public void setMonth(int newMonth) {
		myMonth = newMonth;
	}

	public void setDay(int newDay) {
		myDay = newDay;
	}

	public void setYear(int newYear) {
		myYear = newYear;
	}

	public String getTicker() {
		return myTicker;
	}

	public String getName() {
		return myName;
	}

	public double getPrice() {
		return myPrice;
	}

	public int getNum() {
		return myNum;
	}

	public String getType() {
		return myType;
	}

	public int getMonth() {
		return myMonth;
	}

	public int getDay() {
		return myDay;
	}

	public int getYear() {
		return myYear;
	}

	public String getDate() { //returns the date as a String
		return getMonth() + "/" + getDay() + "/" + getYear();
	}

	public double getTotalPrice() { //returns the total price by multiplying price and num
		return myPrice * myNum;
	}

	public String toString() {
		return "Ticker Symbol: " + getTicker() + ", Name: " + getName() + ", Price: $" + getPrice() + ", # of shares: "
				+ getNum() + ", Type: " + getType() + ", Date: " + getMonth() + " / " + getDay() + " / " + getYear();
	}

	public int totalPriceCompareTo(Stock other) { //compares the stocks' total price
		if (myNum * myPrice > other.getNum() * other.getPrice())
			return 1;
		if (myNum * myPrice < other.getNum() * other.getPrice())
			return -1;
		return 0;
	}

	public int dateCompareTo(Stock other) { //compares the stocks' date
		if (myYear > other.getYear())
			return 1;
		if (myYear < other.getYear())
			return -1;
		if (myMonth > other.getMonth())
			return 1;
		if (myMonth < other.getMonth())
			return -1;
		if (myDay > other.getDay())
			return 1;
		if (myDay < other.getDay())
			return -1;
		else
			return totalPriceCompareTo(other);
	}

	public boolean equals(Stock other) { //returns true if the stocks are equal, else false
		if (myTicker.equals(other.getTicker()) && myName.equals(other.getName()) && myPrice == other.getPrice()
				&& myNum == other.getNum() && myType.equals(other.getType()) && myMonth == other.getMonth()
				&& myDay == other.getDay() && myYear == other.getYear())
			return true;
		return false;
	}



}



