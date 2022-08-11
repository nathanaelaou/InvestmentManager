package main;

import java.awt.EventQueue;

import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ButtonGroup;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;

public class RecordNewTradeWindow extends ProgramWindow { //allows user to record a new stock trade

	private JPanel contentPane;
	private JTextField txtStockName;
	private JTextField txtStockTickerSymbol;
	private JTextField txtStockPrice;
	private JTextField txtNumOfShares;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private boolean usePreset;
	private String presetName;
	private String presetTicker;

	private String type;
	private File tradeHistoryFile;
	private File icon;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecordNewTradeWindow frame = new RecordNewTradeWindow("", "");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RecordNewTradeWindow(String name, String ticker) {
		
		//set window icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 392, 348);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		icon = new File("Investment Manager Icon.png");
		
		JLabel tradeTypeLbl = new JLabel("Trade Type");
		tradeTypeLbl.setBounds(24, 60, 76, 14);
		contentPane.add(tradeTypeLbl);

		usePreset = false;
		presetName = "";
		presetTicker = "";
		if (!name.equals("") && !ticker.equals("")) {
			usePreset = true;
			presetName = name;
			presetTicker = ticker;
		}

		tradeHistoryFile = new File("tradeHistoryFile.txt");
		type = "";

		//create buy/sell radio buttons
		JRadioButton buyButton = new JRadioButton("Buy");
		buyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = "BUY";
			}
		});
		buttonGroup.add(buyButton);
		buyButton.setBounds(24, 81, 61, 23);
		contentPane.add(buyButton);

		JRadioButton sellButton = new JRadioButton("Sell");
		sellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = "SELL";
			}
		});
		buttonGroup.add(sellButton);
		sellButton.setBounds(24, 107, 61, 23);
		contentPane.add(sellButton);

		//create labels and text fields for stock name, ticker symbol, price, # shares
		txtStockName = new JTextField();
		txtStockName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtStockName.getText().equals("Stock name"))
					txtStockName.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtStockName.getText().equals(""))
					txtStockName.setText("Stock name");
			}
		});
		if (usePreset)
			txtStockName.setText(presetName);
		else
			txtStockName.setText("Stock name");
		txtStockName.setBounds(24, 145, 129, 23);
		contentPane.add(txtStockName);

		txtStockTickerSymbol = new JTextField();
		txtStockTickerSymbol.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtStockTickerSymbol.getText().equals("Ticker symbol"))
					txtStockTickerSymbol.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtStockTickerSymbol.getText().equals(""))
					txtStockTickerSymbol.setText("Ticker symbol");
			}
		});
		if (usePreset)
			txtStockTickerSymbol.setText(presetTicker);
		else
			txtStockTickerSymbol.setText("Ticker symbol");
		txtStockTickerSymbol.setBounds(171, 145, 82, 23);
		contentPane.add(txtStockTickerSymbol);
		txtStockTickerSymbol.setColumns(10);

		txtStockPrice = new JTextField();
		txtStockPrice.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtStockPrice.getText().equals("Stock price"))
					txtStockPrice.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtStockPrice.getText().equals(""))
					txtStockPrice.setText("Stock price");
			}
		});
		txtStockPrice.setText("Stock price");
		txtStockPrice.setBounds(34, 187, 79, 23);
		contentPane.add(txtStockPrice);
		txtStockPrice.setColumns(10);

		txtNumOfShares = new JTextField();
		txtNumOfShares.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtNumOfShares.getText().equals("# of shares"))
					txtNumOfShares.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (txtNumOfShares.getText().equals(""))
					txtNumOfShares.setText("# of shares");
			}
		});
		txtNumOfShares.setText("# of shares");
		txtNumOfShares.setBounds(132, 187, 94, 23);
		contentPane.add(txtNumOfShares);
		txtNumOfShares.setColumns(10);

		//create comboboxes for date
		JComboBox monthBox = new JComboBox();
		monthBox.setBackground(SystemColor.controlHighlight);
		monthBox.setBounds(63, 232, 51, 22);
		contentPane.add(monthBox);

		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		for (String tempMonth : months) {
			monthBox.addItem(tempMonth);
		}

		JComboBox dayBox = new JComboBox();
		dayBox.setBackground(SystemColor.controlHighlight);
		dayBox.setBounds(150, 232, 51, 22);
		contentPane.add(dayBox);

		for (int i = 1; i <= 31; i++) {
			dayBox.addItem(i);
		}

		JComboBox yearBox = new JComboBox();
		yearBox.setBackground(SystemColor.controlHighlight);
		yearBox.setBounds(240, 232, 61, 22);
		contentPane.add(yearBox);

		int currYear = Calendar.getInstance().get(Calendar.YEAR);
		int currDay = Calendar.getInstance().get(Calendar.DATE);
		int currMonth = 1 + Calendar.getInstance().get(Calendar.MONTH);
		System.out.println(currMonth + " " + currDay + " " + currYear);

		ArrayList<Integer> years = new ArrayList<>();
		for (int i = currYear; i >= currYear - 100; i--) {
			years.add(i);
			yearBox.addItem(i);
		}
		monthBox.setSelectedIndex(currMonth - 1);
		dayBox.setSelectedIndex(currDay - 1);

		JLabel monthLabel = new JLabel("Month");
		monthLabel.setBounds(23, 236, 51, 14);
		contentPane.add(monthLabel);

		JLabel daylabel = new JLabel("Day");
		daylabel.setBounds(126, 236, 30, 14);
		contentPane.add(daylabel);

		JLabel yearLabel = new JLabel("Year");
		yearLabel.setBounds(212, 236, 30, 14);
		contentPane.add(yearLabel);

		JLabel lblNewLabel_2 = new JLabel("$");
		lblNewLabel_2.setBounds(24, 191, 15, 14);
		contentPane.add(lblNewLabel_2);

		
		//create back button that takes user back to main menu
		JButton backButton = new JButton("Back");
		backButton.setBackground(new Color(153, 204, 255));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				MainMenu back = new MainMenu();
				back.setTitle("Main Menu");
				int x = (screenSize.width - back.getWidth()) / 2;
				int y = (screenSize.height - back.getHeight()) / 2;
				back.setLocation(x, y);
				back.setVisible(true);
				//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				back.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
				setVisible(false);
			}
		});
		backButton.setBounds(8, 280, 82, 23);
		contentPane.add(backButton);

		//create addTrade button
		JButton addTradeButton = new JButton("Add Trade");
		addTradeButton.setBackground(new Color(153, 204, 102));
		addTradeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//store user input in variables
					String name = txtStockName.getText();
					String ticker = txtStockTickerSymbol.getText().toUpperCase();
					double price = Double.parseDouble(txtStockPrice.getText());
					int num = Integer.parseInt(txtNumOfShares.getText());
					int month = monthBox.getSelectedIndex() + 1;
					int day = dayBox.getSelectedIndex() + 1;
					int year = years.get(yearBox.getSelectedIndex());
					String date = month + "/" + day + "/" + year;
					double totalPrice = price * num;
					// create a Stock using user input
					Stock recordedStock = new Stock(ticker, name, price, num, type, month, day, year);

					// check that user did not input a negative amount for number of shares
					boolean numSharesInvalid = false;
					if (num <= 0)
						numSharesInvalid = true;

					boolean cannotBeSold = false;
					int numSharesOwned = 0;
					if (type.equals("SELL")) {
						if (tradeHistoryFile.length() == 0)
							cannotBeSold = true;
						else {
							ArrayList<Stock> tempTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
							ArrayList<Stock> tradeHistoryList = quickSort(tempTradeHistoryList, 0,
									tempTradeHistoryList.size() - 1);
							for (Stock tempStock : tradeHistoryList) {
								if (tempStock.dateCompareTo(recordedStock) <= 0) {
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
								cannotBeSold = true;
						}
					}
					// check for valid date
					boolean invalidDate = false;
					if (year % 4 == 0 && month == 2 && day > 29) // if the date is after 2/29 on a leap year
						invalidDate = true;
					else if (month == 2 && day > 28 && year % 4 != 0) // if the date is after 2/28 on a normal year
						invalidDate = true;
					else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30)
						invalidDate = true;
					else if (month > currMonth && year == currYear)
						invalidDate = true;
					else if (month == currMonth && year == currYear && day > currDay)
						invalidDate = true;

					boolean invalidPrice = false;
					if (price < 0)
						invalidPrice = true;

					// check if user input is valid, if not display a specific message that explains the problem
					if (type.equals("")) {
						JOptionPane.showMessageDialog(null, "Trade type must be filled out (BUY/SELL)", "Missing"
								+ " Type",
								JOptionPane.ERROR_MESSAGE);
					} else if (invalidPrice) {
						JOptionPane.showMessageDialog(null, "Price cannot be negative.", "Invalid Price",
								JOptionPane.ERROR_MESSAGE);
					} else if (numSharesInvalid) {
						JOptionPane.showMessageDialog(null, "Number of shares must be at least 1.",
								"Invalid Number of Shares", JOptionPane.ERROR_MESSAGE);
					} else if (invalidDate) {
						JOptionPane.showMessageDialog(null, "Date does not exist or is in the future.", "Invalid Date",
								JOptionPane.ERROR_MESSAGE);
					} else if (checkIfNoDuplicate(ticker, name, month, day, year) == false) {
						JOptionPane.showMessageDialog(null, "You may only trade the same stock once per day.",
								"Trade limit error", JOptionPane.ERROR_MESSAGE);
					} else if (cannotBeSold) {
						JOptionPane.showMessageDialog(null,
								"You cannot sell " + num + " shares of " + name + " ($" + ticker + ") because you own "
										+ numSharesOwned + " as of " + recordedStock.getDate() + ".",
								"Cannot Sell Stock", JOptionPane.ERROR_MESSAGE);
					} else if (checkIfValidOrder(recordedStock) == false) {
						JOptionPane.showMessageDialog(null,
								"This trade cannot be recorded because it prevents a future sell order that has already been recorded.",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else if (num > 10000) {
						JOptionPane.showMessageDialog(null, "You can trade at most 10,000 shares",
								"Exceeding Number of Shares", JOptionPane.ERROR_MESSAGE);
					} else if (price > 10000) {
						JOptionPane.showMessageDialog(null, "Please enter a price between $0 - $10,000",
								"Exceeding Price", JOptionPane.ERROR_MESSAGE);
					} else {

						// write recorded Stock to file
						try {
							PrintWriter writer = new PrintWriter(new FileWriter(tradeHistoryFile, true));
							writer.write(ticker + "\n" + name + "\n" + convertPriceToString(price) + "\n" + num + "\n"
									+ convertPriceToString(totalPrice) + "\n" + type + "\n" + date + "\n");
							writer.close();
							JOptionPane.showMessageDialog(null, "Trade has been added to Trade History Record.",
									"Trade Added", JOptionPane.PLAIN_MESSAGE);

						} catch (IOException exception) {
							System.out.println(exception);
							// exception is printed and no error window appears because this is a
							// programming side issue, not client side
						}

						// reset input boxes
						txtStockName.setText("Stock name");
						txtStockTickerSymbol.setText("Ticker symbol");
						txtStockPrice.setText("Stock price");
						txtNumOfShares.setText("# of shares");
						buttonGroup.clearSelection();
						type = "";
						monthBox.setSelectedIndex(currMonth - 1);
						dayBox.setSelectedIndex(currDay - 1);
						yearBox.setSelectedIndex(0);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Make sure all information is filled out and input correctly (Price should be a numerical value and # shares should be an integer).",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		addTradeButton.setBounds(265, 280, 99, 23);
		contentPane.add(addTradeButton);

		//window title label
		JLabel lblRecordNewTrade = new JLabel("Record New Trade");
		lblRecordNewTrade.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 25));
		lblRecordNewTrade.setBounds(22, 16, 342, 24);
		contentPane.add(lblRecordNewTrade);
	}

	// This method checks if another trade has already been recorded for the same
	// stock on the same day.
	// The program limits the user to recording only one trade per stock per day.
	public boolean checkIfNoDuplicate(String ticker, String name, int month, int day, int year) {
		ArrayList<Stock> tradeHistoryList = convertFileToList("tradeHistoryFile.txt");

		for (Stock tempStock : tradeHistoryList) { // find a stock trade with matching date, name, and ticker symbol
			if (tempStock.getMonth() == month && tempStock.getDay() == day && tempStock.getYear() == year
					&& tempStock.getTicker().equals(ticker) && tempStock.getName().equals(name))
				return false;
		}
		return true;
	}

	// This method checks if this recorded stock trade will cause problems with
	// future stock selling.
	public boolean checkIfValidOrder(Stock recordedStock) {
		int numSharesOwned = 0;
		ArrayList<Stock> tempTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
		tempTradeHistoryList.add(recordedStock);
		ArrayList<Stock> tradeHistoryList = quickSort(tempTradeHistoryList, 0, tempTradeHistoryList.size() - 1);
		for (Stock tempStock : tradeHistoryList) {
			// find all trades of the stock in the trade history and adjust the number of
			// shares owned accordingly
			if (tempStock.getType().equals("BUY") && tempStock.getName().equals(recordedStock.getName())
					&& tempStock.getTicker().equals(recordedStock.getTicker())) {
				numSharesOwned += tempStock.getNum();
			}
			if (tempStock.getType().equals("SELL") && tempStock.getName().equals(recordedStock.getName())
					&& tempStock.getTicker().equals(recordedStock.getTicker())) {
				numSharesOwned -= tempStock.getNum();
				if (numSharesOwned < 0) // if the amount of shares owned ever is negative, it would cause an error
					return false;
			}
		}
		return true;
	}
}