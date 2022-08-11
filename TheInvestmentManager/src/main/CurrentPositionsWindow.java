package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;
import java.awt.Toolkit;

public class CurrentPositionsWindow extends ProgramWindow { // displays the user's current stock positions

	private JPanel contentPane;
	private DefaultTableModel tableModel;
	private double amountInvested;
	private double gainFromInvestment;
	private double costOfInvestment;
	private int numPositions;
	private double totalCostBasis;
	private ArrayList<CurrStock> currPosList;
	private ArrayList<CurrStock> sortedList;
	private ArrayList<CurrStock> alreadyQueued;
	private JTextField searchField;

	private int totalNumSharesSold;
	private int totalNumSharesBought;
	private File icon;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CurrentPositionsWindow frame = new CurrentPositionsWindow();
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
	public CurrentPositionsWindow() {
		// set window icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 808, 494);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		icon = new File("Investment Manager Icon.png");

		// create back button that takes user back to main menu
		JButton backBtn = new JButton("Back");
		backBtn.setBackground(new Color(153, 204, 255));
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				MainMenu back = new MainMenu();
				back.setTitle("Main Menu");
				int x = (screenSize.width - back.getWidth()) / 2;
				int y = (screenSize.height - back.getHeight()) / 2;
				back.setLocation(x, y);
				back.setVisible(true);
				// Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				back.setIconImage(
						Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
				setVisible(false);
			}
		});
		backBtn.setBounds(18, 426, 89, 23);
		contentPane.add(backBtn);

		// initialize private variables
		currPosList = new ArrayList<CurrStock>();
		sortedList = new ArrayList<CurrStock>();
		alreadyQueued = new ArrayList<CurrStock>();
		amountInvested = 0;
		gainFromInvestment = 0;
		costOfInvestment = 0;
		totalCostBasis = 0;
		numPositions = 0;
		totalNumSharesSold = 0;
		totalNumSharesBought = 0;

		boolean fileIsEmpty = false;
		File tradeHistoryFile = new File("tradeHistoryFile.txt");
		if (tradeHistoryFile.length() != 0)
			fileIsEmpty = true;
		createTable(fileIsEmpty);
		createScrollPane();

		// create currently investing label that states total cost basis and number of
		// positions
		JLabel currInvesting;
		if (numPositions > 1)
			currInvesting = new JLabel("Currently Investing " + convertPriceToString(totalCostBasis) + " in "
					+ numPositions + " Positions");
		else
			currInvesting = new JLabel("Currently Investing " + convertPriceToString(totalCostBasis) + " in "
					+ numPositions + " Position");
		currInvesting.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 25));
		currInvesting.setBounds(18, 11, 564, 35);
		contentPane.add(currInvesting);

		// create statistics labels
		JLabel filterDataLabel = new JLabel("Statistics");
		filterDataLabel.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 22));
		filterDataLabel.setBounds(624, 97, 98, 22);
		contentPane.add(filterDataLabel);

		JLabel roiLabel = new JLabel("ROI: ");
		roiLabel.setFont(new Font("Montserrat", Font.PLAIN, 15));
		roiLabel.setBounds(581, 138, 47, 35);
		contentPane.add(roiLabel);

		JLabel roiStat = new JLabel(calculateROI() + "%");
		roiStat.setFont(new Font("Montserrat", Font.PLAIN, 15));
		roiStat.setBounds(707, 138, 79, 35);
		contentPane.add(roiStat);

		JLabel netReturnLbl = new JLabel("Net Return:     ");
		netReturnLbl.setFont(new Font("Montserrat", Font.PLAIN, 15));
		netReturnLbl.setBounds(581, 181, 115, 50);
		contentPane.add(netReturnLbl);

		JLabel netReturnStat = new JLabel(convertPriceToString(gainFromInvestment - costOfInvestment));
		netReturnStat.setFont(new Font("Montserrat", Font.PLAIN, 15));
		netReturnStat.setBounds(707, 181, 158, 50);
		contentPane.add(netReturnStat);

		JLabel costOfLbl = new JLabel("Cost of");
		costOfLbl.setFont(new Font("Montserrat", Font.PLAIN, 15));
		costOfLbl.setBounds(581, 230, 115, 59);
		contentPane.add(costOfLbl);

		JLabel investmentLbl = new JLabel("Investment: ");
		investmentLbl.setFont(new Font("Montserrat", Font.PLAIN, 15));
		investmentLbl.setBounds(581, 256, 115, 59);
		contentPane.add(investmentLbl);

		JLabel coiStat = new JLabel(convertPriceToString(costOfInvestment));
		coiStat.setFont(new Font("Montserrat", Font.PLAIN, 15));
		coiStat.setBounds(707, 245, 105, 50);
		contentPane.add(coiStat);

		JLabel sharesBoughtStat = new JLabel("" + totalNumSharesBought + " Shares Bought");
		sharesBoughtStat.setFont(new Font("Montserrat", Font.PLAIN, 15));
		sharesBoughtStat.setBounds(582, 312, 188, 50);
		contentPane.add(sharesBoughtStat);

		JLabel sharesSoldStat = new JLabel("" + totalNumSharesSold + " Shares Sold");
		sharesSoldStat.setFont(new Font("Montserrat", Font.PLAIN, 15));
		sharesSoldStat.setBounds(583, 362, 178, 50);
		contentPane.add(sharesSoldStat);

		// create search bar
		searchField = new JTextField();
		searchField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if (searchField.getText().equals(""))
					search();
			}
		});
		searchField.setColumns(10);
		searchField.setBounds(18, 54, 231, 23);
		contentPane.add(searchField);

		// create search button
		JButton searchButton = new JButton("Search");
		searchButton.setBackground(new Color(153, 204, 255));
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		searchButton.setBounds(260, 54, 79, 23);
		contentPane.add(searchButton);

	}

	// create scroll pane for table
	public void createScrollPane() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(18, 91, 548, 327);
		contentPane.add(scrollPane);

		// create current positions table
		JTable tradeHistoryTable = new JTable(tableModel);
		tradeHistoryTable.getTableHeader().setReorderingAllowed(false); // disable the reordering and resizing of table
																		// columns
		tradeHistoryTable.getTableHeader().setResizingAllowed(false);
		scrollPane.setViewportView(tradeHistoryTable);
		tradeHistoryTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) { // when a row is clicked, user is taken to record new
																// window trade
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);

				CurrStock stockToBeTraded = sortedList.get(row);
				String name = stockToBeTraded.getName();
				String ticker = stockToBeTraded.getTicker();

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				RecordNewTradeWindow recordNewTradeWindowFrame = new RecordNewTradeWindow(name, ticker);
				recordNewTradeWindowFrame.setTitle("Record New Trade");
				int x = (screenSize.width - recordNewTradeWindowFrame.getWidth()) / 2;
				int y = (screenSize.height - recordNewTradeWindowFrame.getHeight()) / 2;
				recordNewTradeWindowFrame.setLocation(x, y);
				recordNewTradeWindowFrame.setVisible(true);
				// Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				recordNewTradeWindowFrame.setIconImage(
						Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
				setVisible(false);
			}
		});
	}

	public void createTable(boolean fileIsEmpty) {
		String col[] = { "Ticker Symbol", "Name", "# of Shares", "Avg Price", "Cost Basis" };
		tableModel = new DefaultTableModel(col, 0);
		if (fileIsEmpty)
			loadCurrentPositionsToTable();
	}

	public void loadCurrentPositionsToTable() {
		// this method uses the trade history to calculate the user's current stock
		// positions
		// and display them in a table

		ArrayList<Stock> tempTradeHistoryList = convertFileToList("tradeHistoryFile.txt");
		// convert trade history from text file to ArrayList tempTradeHistoryList
		ArrayList<Stock> tradeHistoryList = quickSort(tempTradeHistoryList, 0, tempTradeHistoryList.size() - 1);
		// sort the tempTradeHistoryList by newest date and store sorted contents in
		// ArrayList tradeHistoryList

		// for reference, CurrStock is an object for a current stock position
		// and currPosList is an ArrayList<CurrStock> containing all of the user's
		// current stock positions

		for (Stock stock1 : tradeHistoryList) { // for every Stock in tradeHistoryList:
			if (stock1.getType().equals("BUY")) { // if it is a buy order
				totalNumSharesBought += stock1.getNum();
				System.out.println("total num shares bought: " + totalNumSharesBought);
				if (currPosList.size() == 0) { // if there are no current positions yet
					CurrStock newStock = new CurrStock(stock1.getTicker(), stock1.getName(), stock1.getNum(),
							stock1.getTotalPrice()); // create a CurrStock object using the Stock's information
					currPosList.add(newStock); // add it to the list of current positions (currPosList)
					numPositions++;
				} else { // else if a current position has already been added to currPosList
					boolean matchFound = false;
					for (int i = 0; i < currPosList.size(); i++) { // loop through list of already created stock
																	// positions
						CurrStock stock2 = currPosList.get(i);
						if (sameStock(stock1, stock2)) { // if a position has ALREADY been created for this buy order's
															// stock
							int numSharesBought = stock1.getNum();
							stock2.setCurrNum(stock2.getCurrNum() + numSharesBought); // adjust that position's number
																						// of shares
							double cost = stock1.getTotalPrice();
							stock2.setCostBasis(stock2.getCostBasis() + cost); // adjust that position's cost basis
							matchFound = true;
							i = currPosList.size();
						}
					}
					if (!matchFound) { // if a position has NOT been created for this sell buy order's stock
						CurrStock newStock = new CurrStock(stock1.getTicker(), stock1.getName(), stock1.getNum(),
								stock1.getTotalPrice());
						currPosList.add(newStock); // make a new stock position and add it to currPosList
					}
				}
			} else { // if it is a sell order
				gainFromInvestment += stock1.getNum() * stock1.getPrice(); // adjust variable that will be used to
																			// calculate ROI
				for (int i = 0; i < currPosList.size(); i++) {
					CurrStock stock2 = currPosList.get(i);
					if (sameStock(stock1, stock2)) { // find the Stock's matching stock position and adjust number of
														// shares
						int numSharesSold = stock1.getNum();
						stock2.setCurrNum(stock2.getCurrNum() - numSharesSold);

						// to adjust the stock position's cost basis, a FIFO method using queues must be
						// utilized
						// (see Queue explanation in Criterion C)
						if (stockNotAlreadyQueued(stock2)) { // only one Queue is needed for each stock position,
																// so check if a Queue has already been made for that
																// stock position
							Queue<Double> costBasisQueue = new LinkedList<Double>();
							double costBasis = 0;
							for (int j = 0; j < tradeHistoryList.size(); j++) {
								Stock stock3 = tradeHistoryList.get(j);
								if (sameStock(stock3, stock2)) { // search through tradeHistoryList to find all stock
																	// trades (buy/sell)
																	// that change the stock position's cost basis
									for (int k = 1; k <= stock3.getNum(); k++) { // for each INDIVIDUAL stock traded
										if (stock3.getType().equals("BUY")) {
											costBasis += stock3.getPrice();
											costBasisQueue.add(stock3.getPrice()); // increase the position's cost basis
										} else {
											costOfInvestment += costBasisQueue.peek();
											double amountSold = costBasisQueue.remove();
											costBasis -= amountSold;
											stock2.setCostBasis(costBasis);
											// decrease the position's cost basis by subtracting the price of the
											// earliest Stock added to the Queue
											totalNumSharesSold++;
										}
									}
									alreadyQueued.add(stock2); // mark the stock as already queued
								}
							}
						}
					}
				}
			}
		}
		for (int b = 0; b < currPosList.size(); b++) {
			CurrStock testStock = currPosList.get(b);
			if (testStock.getCurrNum() == 0) {
				currPosList.remove(b);
				b--;
			}
		}
		numPositions = currPosList.size();
		for (CurrStock temp : currPosList)
			totalCostBasis += temp.getCostBasis();

		// put variables into array and add to table
		if (currPosList.size() > 0) {
			sortedList = currStockQuickSort(currPosList, 0, currPosList.size() - 1);
			for (CurrStock stockToBeAdded : sortedList) {
				String ticker = stockToBeAdded.getTicker();
				String name = stockToBeAdded.getName();
				int currNum = stockToBeAdded.getCurrNum();
				double costBasis = stockToBeAdded.getCostBasis();
				double avgPrice = costBasis / (double) currNum;
				String costBasisString = convertPriceToString(costBasis);
				String avgPriceString = convertPriceToString(avgPrice);
				Object[] data = { ticker, name, currNum, avgPriceString, costBasisString };
				tableModel.addRow(data);
			}
		}
	}

	public boolean stockNotAlreadyQueued(CurrStock stock) { // checks if a row has already been created for the position
		for (CurrStock tempStock : alreadyQueued)
			if (tempStock.getName().equals(stock.getName()) && tempStock.getTicker().equals(stock.getTicker()))
				return false;
		return true;
	}

	public boolean sameStock(Stock stock1, CurrStock stock2) { // checks if two stocks have the same ticker symbol and
																// name
		if (stock1.getTicker().equals(stock2.getTicker()) && stock1.getName().equals(stock2.getName()))
			return true;
		return false;
	}

	public String calculateROI() { // calculate return on investment
		System.out.println("amountInvested: " + amountInvested);
		System.out.println("gainFromInvestment: " + gainFromInvestment);
		System.out.println("totalCostBasis: " + totalCostBasis);
		System.out.println("costOfInvestment: " + costOfInvestment);

		if (costOfInvestment == 0)
			return "0";
		double roi = ((gainFromInvestment - costOfInvestment) / costOfInvestment) * 100;
		return String.format("%.2f", roi);
	}

	public void search() { // search for ticker symbol / name
		String searchName = searchField.getText();
		ArrayList<CurrStock> filteredList = new ArrayList<CurrStock>();
		if (searchName.equals("")) {
			for (CurrStock tempStock : currPosList)
				filteredList.add(tempStock);
		} else {
			for (CurrStock tempStock : currPosList) {
				if (tempStock.getName().equalsIgnoreCase(searchName)
						|| tempStock.getTicker().equalsIgnoreCase(searchName))
					filteredList.add(tempStock);
			}
		}
		// clear table
		while (tableModel.getRowCount() > 0)
			tableModel.removeRow(0);
		// sort filtered list by cost basis and insert list into table
		if (filteredList.size() != 0) {
			sortedList = currStockQuickSort(filteredList, 0, filteredList.size() - 1);
			for (CurrStock stockToBeAdded : sortedList) {
				String ticker = stockToBeAdded.getTicker();
				String name = stockToBeAdded.getName();
				int currNum = stockToBeAdded.getCurrNum();
				double costBasis = stockToBeAdded.getCostBasis();
				double avgPrice = costBasis / (double) currNum;
				String costBasisString = convertPriceToString(costBasis);
				String avgPriceString = convertPriceToString(avgPrice);
				Object[] data = { ticker, name, currNum, avgPriceString, costBasisString };
				tableModel.addRow(data);
			}
		}
	}

	public ArrayList<CurrStock> currStockQuickSort(ArrayList<CurrStock> list, int first, int last) { // quickSort for
																										// currStocks
		int g = first, h = last;

		int midIndex = (first + last) / 2;
		CurrStock dividingValue = list.get(midIndex);

		CurrStock temp = null;
		do {
			// sort buy highest cost basis
			while (list.get(g).costBasisCompareTo(dividingValue) > 0)
				g++;
			while (list.get(h).costBasisCompareTo(dividingValue) < 0)
				h--;

			if (g <= h) {
				// swap(list[g], list[h]);
				temp = list.get(g);
				list.set(g, list.get(h));
				list.set(h, temp);

				// swap(list,g,h);
				g++;
				h--;
			}
		} while (g < h);
		if (h > first)
			currStockQuickSort(list, first, h);
		if (g < last)
			currStockQuickSort(list, g, last);
		return list;
	}
}