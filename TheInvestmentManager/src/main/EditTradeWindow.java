package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;

public class EditTradeWindow extends ProgramWindow { // this class shows the details of a stock trade, allowing the user to edit data
	
	//declare private variables
	private JPanel contentPane;
	private ArrayList<Stock> myTradeHistoryList;
	private ArrayList<Stock> myTradeHistoryList2;
	private int myRow;
	private JTextField tickerField;
	private JTextField nameField;
	private JTextField priceField;
	private JTextField numField;
	private JLabel tickerLabel;
	private JLabel nameLabel;
	private JLabel priceLabel;
	private JLabel numLabel;
	private JLabel typeLabel;
	private JButton switchTypeButton;

	private int newMonth;
	private int newDay;
	private int newYear;

	private String originalTicker;
	private String originalName;

	private Stock currStock;
	private Stock editedStock;
	private ArrayList<Stock> unfilteredTradeHistoryList;
	private ArrayList<Stock> testTradeHistoryList;
	private Stock testEditedStock;
	private int numSharesOwned;
	
	private File icon;
	
	public EditTradeWindow(ArrayList<Stock> tradeHistoryList, int row) {
		//set window icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 459, 313);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// create Edit Trade Details label
		JLabel editTradeDetailsLabel = new JLabel("Edit Trade");
		editTradeDetailsLabel.setFont(new Font("Leelawadee UI", Font.PLAIN, 25));
		editTradeDetailsLabel.setBounds(22, 14, 212, 46);
		contentPane.add(editTradeDetailsLabel);

		//initialize private variables
		icon = new File("Investment Manager Icon.png");
		
		myTradeHistoryList = tradeHistoryList;
		myRow = row;
		editedStock = myTradeHistoryList.get(myRow);
		originalTicker = editedStock.getTicker();
		originalName = editedStock.getName();
		double originalPrice = editedStock.getPrice();
		int originalNum = editedStock.getNum();
		String originalType = editedStock.getType();
		int originalMonth = editedStock.getMonth();
		int originalDay = editedStock.getDay();
		int originalYear = editedStock.getYear();
		currStock = new Stock(originalTicker, originalName, originalPrice, originalNum, originalType, originalMonth,
				originalDay, originalYear);
		unfilteredTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
		testTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
		testEditedStock = null;
		numSharesOwned = 0;

		// create input field for stock ticker
		tickerField = new JTextField();
		tickerField.setText(currStock.getTicker());
		tickerField.setBounds(124, 68, 104, 20);
		contentPane.add(tickerField);
		tickerField.setColumns(10);

		// create input field for stock name
		nameField = new JTextField();
		nameField.setText(currStock.getName());
		nameField.setBounds(124, 105, 104, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);

		// create input field for stock price
		priceField = new JTextField();
		priceField.setText(String.valueOf(currStock.getPrice()) + "0");
		priceField.setBounds(326, 69, 86, 20);
		contentPane.add(priceField);
		priceField.setColumns(10);

		// create input field for number of shares traded
		numField = new JTextField();
		numField.setText(String.valueOf(currStock.getNum()));
		numField.setBounds(326, 105, 86, 20);
		contentPane.add(numField);
		numField.setColumns(10);

		// create Ticker Symbol label
		tickerLabel = new JLabel("Ticker Symbol:");
		tickerLabel.setBounds(25, 71, 89, 14);
		contentPane.add(tickerLabel);

		// create Stock Name label
		nameLabel = new JLabel("Stock Name:");
		nameLabel.setBounds(25, 108, 94, 14);
		contentPane.add(nameLabel);

		// create Price label
		priceLabel = new JLabel("Price:         $");
		priceLabel.setBounds(247, 71, 86, 14);
		contentPane.add(priceLabel);

		// create # of Shares label
		numLabel = new JLabel("# of Shares:");
		numLabel.setBounds(247, 107, 69, 14);
		contentPane.add(numLabel);

		// create Trade Type label
		typeLabel = new JLabel("Current Trade Type: " + originalType);
		typeLabel.setBounds(25, 147, 154, 14);
		contentPane.add(typeLabel);
		String oppositeType = "";
		if (editedStock.getType().equals("BUY"))
			oppositeType = "SELL";
		else
			oppositeType = "BUY";

		// create button to switch the trade type between buy and sell
		switchTypeButton = new JButton("Switch trade type to " + oppositeType);
		switchTypeButton.setBackground(new Color(153, 204, 255));
		switchTypeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newType = "";
				if (editedStock.getType().equals("BUY"))
					newType = "SELL";
				else
					newType = "BUY";
				switchTypeButton.setText("Switch trade type to " + editedStock.getType());
				editedStock.setType(newType);
				typeLabel.setText("Current Trade Type: " + newType);
			}
		});
		switchTypeButton.setBounds(226, 143, 186, 23);
		contentPane.add(switchTypeButton);

		//create comboBoxes for the date
		JComboBox monthBox = new JComboBox();
		monthBox.setBackground(SystemColor.controlHighlight);
		monthBox.setBounds(65, 186, 50, 22);
		contentPane.add(monthBox);
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		for (String currMonth : months) {
			monthBox.addItem(currMonth);
		}
		monthBox.setSelectedIndex(currStock.getMonth() - 1);

		JComboBox dayBox = new JComboBox();
		dayBox.setBackground(SystemColor.controlHighlight);
		dayBox.setBounds(152, 186, 40, 22);
		contentPane.add(dayBox);
		for (int i = 1; i < 32; i++) {
			dayBox.addItem(i);
		}
		dayBox.setSelectedIndex(currStock.getDay() - 1);

		JComboBox yearBox = new JComboBox();
		yearBox.setBackground(SystemColor.controlHighlight);
		yearBox.setBounds(234, 186, 60, 22);
		contentPane.add(yearBox);
		int currYear = Calendar.getInstance().get(Calendar.YEAR);
		int currDay = Calendar.getInstance().get(Calendar.DATE);
		int currMonth = 1 + Calendar.getInstance().get(Calendar.MONTH);

		ArrayList<Integer> years = new ArrayList<>();
		for (int i = currYear; i >= currYear - 100; i--) {
			years.add(i);
			yearBox.addItem(i);
		}
		yearBox.setSelectedIndex(currYear - currStock.getYear());

		//create button to confirm the edit
		JButton confirmButton = new JButton("Confirm Changes");
		confirmButton.setBackground(new Color(153, 204, 102));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // if confirm changes button is pressed:
				try {
					newMonth = monthBox.getSelectedIndex() + 1;
					newDay = dayBox.getSelectedIndex() + 1;
					newYear = years.get(yearBox.getSelectedIndex());

					testEditedStock = new Stock(tickerField.getText().toUpperCase(), nameField.getText(),
							Double.valueOf(priceField.getText()), Integer.valueOf(numField.getText()),
							editedStock.getType(), newMonth, newDay, newYear); // create hypothetical Stock based on
																				// input data to test for errors
					// check if input data is valid and if not, display a specific error message explaining the problem
					if (Integer.valueOf(numField.getText()) < 1) {
						JOptionPane.showMessageDialog(null, "Number of shares must be at least 1.",
								"Invalid Number of Shares", JOptionPane.ERROR_MESSAGE);
					} else if (Double.valueOf(priceField.getText()) < 1)
						JOptionPane.showMessageDialog(null, "Price cannot be negative.", "Invalid Price",
								JOptionPane.ERROR_MESSAGE);
					else if (!checkIfValidDate()) { // if input date is invalid
						JOptionPane.showMessageDialog(null, "Date does not exist or is in the future.", "Invalid Date",
								JOptionPane.ERROR_MESSAGE);
					}
					else if (checkIfNoDuplicate(tickerField.getText(), nameField.getText()) == false) {
						JOptionPane.showMessageDialog(null, "You may only trade the same stock once per day.",
								"Trade limit error", JOptionPane.ERROR_MESSAGE);
					} else if (editedStock.getType().equals("SELL") && !checkIfCanBeSold(tickerField.getText(),
							nameField.getText(), Integer.valueOf(numField.getText()))) {
						JOptionPane.showMessageDialog(null,
								"You cannot sell " + Integer.valueOf(numField.getText()) + " shares of "
										+ nameField.getText() + " ($" + tickerField.getText() + ") because you own "
										+ numSharesOwned + " as of " + testEditedStock.getDate() + ".",
								"Cannot Sell Stock", JOptionPane.ERROR_MESSAGE);
					} else if (checkIfValidOrder(originalTicker, originalName, false) == false) {
						testTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
						JOptionPane.showMessageDialog(null,
								"Invalid Input: The changes made cause an error with selling " + originalName
										+ " shares either currently or at a future date.",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else if (checkIfValidOrder(tickerField.getText(), nameField.getText(), false) == false) {
						testTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
						JOptionPane.showMessageDialog(null,
								"Invalid Input: The changes cause an error with selling " + nameField.getText()
										+ " shares either currently or at a future date..",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						// if no errors, edit the Stock using setter methods
						editedStock.setMonth(newMonth);
						editedStock.setDay(newDay);
						editedStock.setYear(newYear);
						editedStock.setTicker(tickerField.getText().toUpperCase());
						editedStock.setName(nameField.getText());
						editedStock.setPrice(Double.valueOf(priceField.getText()));
						editedStock.setNum(Integer.valueOf(numField.getText()));

						editStock(false);
						clearFile();
						copyArrayListBackIntoFile();
						JOptionPane.showMessageDialog(null, "Trade info has been updated", "Info Updated",
								JOptionPane.PLAIN_MESSAGE);
						
						//once edit is complete, take user back to tradeRecordWindow
						Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
						TradeRecordWindow tradeRecordWindowFrame = new TradeRecordWindow();
						tradeRecordWindowFrame.setTitle("Trade History");
						int x = (screenSize.width - tradeRecordWindowFrame.getWidth()) / 2;
						int y = (screenSize.height - tradeRecordWindowFrame.getHeight()) / 2;
						tradeRecordWindowFrame.setLocation(x, y);
						tradeRecordWindowFrame.setVisible(true);
						//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
						tradeRecordWindowFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
						setVisible(false);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Make sure all information was input correctly",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		confirmButton.setBounds(301, 240, 132, 23);
		contentPane.add(confirmButton);

		//create back button that takes user back to trade record window
		JButton backButton = new JButton("Back");
		backButton.setBackground(new Color(153, 204, 255));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editedStock.setType(originalType);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				TradeRecordWindow tradeRecordWindowFrame = new TradeRecordWindow();
				tradeRecordWindowFrame.setTitle("Trade History");
				int x = (screenSize.width - tradeRecordWindowFrame.getWidth()) / 2;
				int y = (screenSize.height - tradeRecordWindowFrame.getHeight()) / 2;
				tradeRecordWindowFrame.setLocation(x, y);
				tradeRecordWindowFrame.setVisible(true);
				//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				tradeRecordWindowFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
				setVisible(false);
			}
		});
		backButton.setBounds(10, 240, 89, 23);
		contentPane.add(backButton);

		JLabel monthLbl = new JLabel("Month");
		monthLbl.setBounds(26, 190, 46, 14);
		contentPane.add(monthLbl);

		JLabel dayLbl = new JLabel("Day");
		dayLbl.setBounds(125, 190, 38, 14);
		contentPane.add(dayLbl);

		JLabel yearLbl = new JLabel("Year");
		yearLbl.setBounds(202, 190, 46, 14);
		contentPane.add(yearLbl);

		//create button to remove the trade from the trade history
		JButton removeButton = new JButton("Remove Trade");
		removeButton.setBackground(new Color(255, 153, 153));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkIfValidOrder(originalTicker, originalName, true) == false) {
					JOptionPane.showMessageDialog(null,
							"This trade cannot be removed because it causes an error with selling stocks at a future date.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					clearFile();
					editStock(true); //passes in true to indicate the trade is being deleted, and its contents are being edited
					copyArrayListBackIntoFile();
					JOptionPane.showMessageDialog(null, "Trade has been removed from the record", "Trade removed",
							JOptionPane.PLAIN_MESSAGE);
					//after trade is removed, take user back to trade record window
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					TradeRecordWindow tradeRecordWindowFrame = new TradeRecordWindow();
					tradeRecordWindowFrame.setTitle("Trade History");
					int x = (screenSize.width - tradeRecordWindowFrame.getWidth()) / 2;
					int y = (screenSize.height - tradeRecordWindowFrame.getHeight()) / 2;
					tradeRecordWindowFrame.setLocation(x, y);
					tradeRecordWindowFrame.setVisible(true);
					//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
					tradeRecordWindowFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
					setVisible(false);
				}
			}
		});
		removeButton.setBounds(144, 240, 116, 23);
		contentPane.add(removeButton);

	}

	//checks if the stock has not already been traded the same day
	public boolean checkIfNoDuplicate(String ticker, String name) {
		ArrayList<Stock> tradeHistoryList = convertFileToList("tradeHistoryFile.txt");
		int count = 0;
		for (Stock tempStock : tradeHistoryList) {
			if (tempStock.getMonth() == newMonth && tempStock.getDay() == newDay && tempStock.getYear() == newYear
					&& tempStock.getTicker().equals(ticker) && tempStock.getName().equals(name)) {
				count++;
				if (count > 1)
					return false;
			}
		}
		return true;
	}

	//checks if the new edited trade works with future sell orders to make sure the shares owned by the user never is below 0
	public boolean checkIfValidOrder(String newTicker, String newName, boolean stockRemoved) {
		int numSharesOwned = 0;
		testTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
		if (testTradeHistoryList.size() == 1)
			return true;
		for (int i = 0; i < testTradeHistoryList.size(); i++) {
			Stock tempStock = testTradeHistoryList.get(i);
			if (tempStock.equals(currStock)) {
				testTradeHistoryList.remove(i); //remove the original trade (before edit)
				if (!stockRemoved)
					testTradeHistoryList.add(i, testEditedStock);
				//if the edit is not to remove the trade, add the newly edited trade to the list
			}
		}
		ArrayList<Stock> tradeHistoryList = quickSort(testTradeHistoryList, 0, testTradeHistoryList.size() - 1);
		for (Stock tempStock : tradeHistoryList) {
			if (tempStock.getType().equals("BUY") && tempStock.getName().equals(newName)
					&& tempStock.getTicker().equals(newTicker)) {
				numSharesOwned += tempStock.getNum();
			}
			if (tempStock.getType().equals("SELL") && tempStock.getName().equals(newName)
					&& tempStock.getTicker().equals(newTicker)) {
				numSharesOwned -= tempStock.getNum();

				if (numSharesOwned < 0) //if the number of shares ever is negative on a future date, the edit is invalid
					return false;
			}
		}
		return true;

	}

	public boolean checkIfValidDate() { //checks if date exists and not in the future
		int currYear = Calendar.getInstance().get(Calendar.YEAR);
		int currDay = Calendar.getInstance().get(Calendar.DATE);
		int currMonth = 1 + Calendar.getInstance().get(Calendar.MONTH);

		System.out.println("curr date: " + currMonth + " " + currDay + " " + currYear);

		boolean dateIsValid = true;

		if (newYear % 4 == 0 && newMonth == 2 && newDay > 29) // if the date is after 2/29 on a leap year
			dateIsValid = false;
		else if (newMonth == 2 && newDay > 28 && newYear % 4 != 0) // if the date is after 2/28 on a normal year
			dateIsValid = false;
		else if ((newMonth == 4 || newMonth == 6 || newMonth == 9 || newMonth == 11) && newDay > 30)
			dateIsValid = false;
		else if (newMonth > currMonth && newYear == currYear) {
			dateIsValid = false;
		} else if (newMonth == currMonth && newYear == currYear && newDay > currDay) {
			dateIsValid = false;
		}

		return dateIsValid;
	}

	public boolean checkIfCanBeSold(String ticker, String name, int num) { //checks if the user has enough shares to sell newly edited stock
		numSharesOwned = 0;
		if ("tradeHistoryFile.txt".length() == 0)
			return false;
		else {
			ArrayList<Stock> tempTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
			for (int i = 0; i < tempTradeHistoryList.size(); i++) {
				Stock loopStock = tempTradeHistoryList.get(i);
				if (loopStock.equals(currStock)) {
					tempTradeHistoryList.remove(i);
				}
			}
			System.out.println(testEditedStock);
			tempTradeHistoryList.add(testEditedStock);
			ArrayList<Stock> tradeHistoryList = quickSort(tempTradeHistoryList, 0, tempTradeHistoryList.size() - 1);
			for (Stock tempStock : tradeHistoryList) {
				if (tempStock.dateCompareTo(testEditedStock) < 0) {
					if (tempStock.getType().equals("BUY") && tempStock.getName().equals(name)
							&& tempStock.getTicker().equals(ticker)) {
						numSharesOwned += tempStock.getNum();
					}
					if (tempStock.getType().equals("SELL") && tempStock.getName().equals(name)
							&& tempStock.getTicker().equals(ticker)) {
						numSharesOwned -= tempStock.getNum();
					}
				}
			}
			if (numSharesOwned < num)
				return false;
		}
		return true;
	}

	public void clearFile() { //clears tradeHistoryFile
		try {
			FileWriter fwOb = new FileWriter("tradeHistoryFile.txt", false);
			PrintWriter pwOb = new PrintWriter(fwOb, false);
			pwOb.flush();
			pwOb.close();
			fwOb.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// this method deletes the pre-edited Stock and inserts the edited Stock
	public void editStock(boolean stockWasRemoved) {
		for (int i = 0; i < unfilteredTradeHistoryList.size(); i++) {
			Stock tempStock = unfilteredTradeHistoryList.get(i);
			// find the index of the Stock that was chosen to be edited.
			if (tempStock.equals(currStock)) {
				unfilteredTradeHistoryList.remove(i);
				// the original pre-edited Stock is deleted
				if (!stockWasRemoved) {
					// if the user chose to edit, not delete the stock trade,
					// the newly edited Stock would replace it using .add()
					unfilteredTradeHistoryList.add(i, editedStock);
				}
				i = unfilteredTradeHistoryList.size();
			}
		}
	}

	public void copyArrayListBackIntoFile() { // writes trade history list to text file
		for (int i = 0; i < unfilteredTradeHistoryList.size(); i++) {
			Stock tempStock = unfilteredTradeHistoryList.get(i);
			String ticker = tempStock.getTicker();
			String name = tempStock.getName();
			double price = tempStock.getPrice();
			int num = tempStock.getNum();
			double totalPrice = price * num;
			String type = tempStock.getType();
			String date = tempStock.getDate();

			try {
				PrintWriter writer = new PrintWriter(new FileWriter("tradeHistoryFile.txt", true));
				writer.write(ticker + "\n" + name + "\n" + convertPriceToString(price) + "\n" + num + "\n"
						+ convertPriceToString(totalPrice) + "\n" + type + "\n" + date + "\n");
				writer.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}
}
