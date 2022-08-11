package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.UIManager;

public class TradeRecordWindow extends ProgramWindow { //display's the user's trade history

	//declare private variables
	private JPanel contentPane;
	private String sortType;
	private String activeFilter1;
	private String activeFilter2;
	private String activeFilter3;

	private int currYear;
	private int currDay;
	private int currMonth;
	private int oldestMonth;
	private int oldestDay;
	private int oldestYear;

	private int fromMonthFilter;
	private int fromDayFilter;
	private int fromYearFilter;
	private int toMonthFilter;
	private int toDayFilter;
	private int toYearFilter;
	private boolean allVariablesInitialized;
	private boolean changeTimeFilterRadioButton;
	private ArrayList<Stock> filteredList1;
	private ArrayList<Stock> filteredList2;

	private DefaultTableModel tableModel;
	private ButtonGroup typeButtonGroup = new ButtonGroup();
	private ButtonGroup timeButtonGroup = new ButtonGroup();
	private JTextField searchField;

	private JComboBox fromMonthComboBox;
	private JComboBox fromDayComboBox;
	private JComboBox fromYearComboBox;
	private JComboBox toMonthComboBox;
	private JComboBox toDayComboBox;
	private JComboBox toYearComboBox;

	private File icon;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TradeRecordWindow frame = new TradeRecordWindow();
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
	public TradeRecordWindow() {
		//set window icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 755, 529);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//create window title label
		JLabel tradeHistoryLbl = new JLabel("Trade History");
		tradeHistoryLbl.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 25));
		tradeHistoryLbl.setBounds(26, 8, 171, 35);
		contentPane.add(tradeHistoryLbl);

		//initialize private variables
		sortType = "dateNewest";
		activeFilter1 = "all";
		activeFilter2 = "all";
		activeFilter3 = "all";
		filteredList1 = new ArrayList<Stock>();
		filteredList2 = new ArrayList<Stock>();

		currYear = Calendar.getInstance().get(Calendar.YEAR);
		currDay = Calendar.getInstance().get(Calendar.DATE);
		currMonth = 1 + Calendar.getInstance().get(Calendar.MONTH);
		oldestMonth = 1;
		oldestDay = 1;
		oldestYear = 2022;

		icon = new File("Investment Manager Icon.png");
		
		createTable();
		createScrollPane();

		//create back button that takes user back to main menu
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
				//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				//back.setIconImage(img);
				setVisible(false);
			}
		});
		backBtn.setBounds(26, 461, 89, 23);
		contentPane.add(backBtn);

		JLabel filterDataLabel = new JLabel("Filter Data");
		filterDataLabel.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 18));
		filterDataLabel.setBounds(598, 51, 113, 16);
		contentPane.add(filterDataLabel);

		//create time filter buttons
		JRadioButton allTimeRadioBtn = new JRadioButton("All Time");
		allTimeRadioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeFilter2 = "all";
				filtersUpdated();
				toMonthComboBox.setSelectedIndex(currMonth - 1);
				toDayComboBox.setSelectedIndex(currDay - 1);
				toYearComboBox.setSelectedIndex(0);
			}
		});
		timeButtonGroup.add(allTimeRadioBtn);
		allTimeRadioBtn.setBounds(595, 174, 93, 19);
		contentPane.add(allTimeRadioBtn);
		allTimeRadioBtn.setSelected(true);

		JRadioButton selectTimePeriodRadioBtn = new JRadioButton("Select Time Period");
		selectTimePeriodRadioBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkIfValidDate()) {
					activeFilter2 = "time";
					filtersUpdated();
				} else {
					allTimeRadioBtn.setSelected(true);
					activeFilter2 = "all";
					JOptionPane.showMessageDialog(null,
							"Make sure dates exist, are not in the future, and from dates are before to dates.",
							"Invalid Date", JOptionPane.ERROR_MESSAGE);
				}
				sort("filteredTradeHistoryFile.txt");
			}
		});
		timeButtonGroup.add(selectTimePeriodRadioBtn);
		selectTimePeriodRadioBtn.setBounds(595, 196, 131, 24);
		contentPane.add(selectTimePeriodRadioBtn);

		//create labels for date
		JLabel fromMonth = new JLabel("Month");
		fromMonth.setBounds(596, 262, 40, 11);
		contentPane.add(fromMonth);

		JLabel fromDay = new JLabel("Day");
		fromDay.setBounds(596, 290, 40, 11);
		contentPane.add(fromDay);

		JLabel fromYear = new JLabel("Year");
		fromYear.setBounds(595, 318, 40, 11);
		contentPane.add(fromYear);

		fromMonthComboBox = new JComboBox();
		fromMonthComboBox.setBackground(SystemColor.controlHighlight);
		fromMonthComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //if comboBox is changed, update the filter
				System.out.println(allVariablesInitialized);
				if (allVariablesInitialized && fromMonthComboBox.getSelectedIndex() + 1 != fromMonthFilter) {
					fromMonthFilter = fromMonthComboBox.getSelectedIndex() + 1;
					filterDatesChanged();
				}
				if (changeTimeFilterRadioButton) {
					allTimeRadioBtn.setSelected(true);
					changeTimeFilterRadioButton = false;
				}
			}
		});
		fromMonthComboBox.setBounds(636, 260, 75, 19);
		contentPane.add(fromMonthComboBox);
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		for (String tempMonth : months) { //add each month to the comboBox
			fromMonthComboBox.addItem(tempMonth);
		}

		fromDayComboBox = new JComboBox();
		fromDayComboBox.setBackground(SystemColor.controlHighlight);
		fromDayComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //if comboBox is changed, update the filter
				if (allVariablesInitialized && fromDayComboBox.getSelectedIndex() + 1 != fromDayFilter) {
					fromDayFilter = fromDayComboBox.getSelectedIndex() + 1;
					filterDatesChanged();
				}
				if (changeTimeFilterRadioButton) {
					allTimeRadioBtn.setSelected(true);
					changeTimeFilterRadioButton = false;
				}
			}
		});
		fromDayComboBox.setBounds(636, 287, 75, 19);
		contentPane.add(fromDayComboBox);
		for (int i = 1; i <= 31; i++) {
			fromDayComboBox.addItem(i);
		}

		fromYearComboBox = new JComboBox();
		fromYearComboBox.setBackground(SystemColor.controlHighlight);
		fromYearComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //if comboBox is changed, update the filter
				if (allVariablesInitialized && fromYearComboBox.getSelectedIndex() + 1 != fromYearFilter) {
					fromYearFilter = currYear - fromYearComboBox.getSelectedIndex();
					filterDatesChanged();
				}
				if (changeTimeFilterRadioButton) {
					allTimeRadioBtn.setSelected(true);
					changeTimeFilterRadioButton = false;
				}
			}
		});
		fromYearComboBox.setBounds(636, 314, 75, 19);
		contentPane.add(fromYearComboBox);
		ArrayList<Integer> years = new ArrayList<>();
		for (int i = currYear; i >= currYear - 100; i--) {
			years.add(i);
			fromYearComboBox.addItem(i);
		}
		
		//finds the oldest date in the table
		File tradeHistoryFile = new File("tradeHistoryFile.txt");
		if (tradeHistoryFile.length() != 0) {
			findOldestDate("tradeHistoryFile.txt");
			fromMonthComboBox.setSelectedIndex(oldestMonth - 1);
			fromDayComboBox.setSelectedIndex(oldestDay - 1);
			fromYearComboBox.setSelectedIndex(currYear - oldestYear);
		}

		JLabel toMonth = new JLabel("Month");
		toMonth.setBounds(596, 374, 40, 11);
		contentPane.add(toMonth);

		JLabel toDay = new JLabel("Day");
		toDay.setBounds(596, 402, 40, 11);
		contentPane.add(toDay);

		JLabel toYear = new JLabel("Year");
		toYear.setBounds(595, 430, 40, 11);
		contentPane.add(toYear);

		toMonthComboBox = new JComboBox();
		toMonthComboBox.setBackground(SystemColor.controlHighlight);
		toMonthComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //if comboBox is changed, update the filter
				if (allVariablesInitialized && toMonthComboBox.getSelectedIndex() + 1 != toMonthFilter) {
					toMonthFilter = toMonthComboBox.getSelectedIndex() + 1;
					filterDatesChanged();
				}
				if (changeTimeFilterRadioButton) {
					allTimeRadioBtn.setSelected(true);
					changeTimeFilterRadioButton = false;
				}
			}
		});
		toMonthComboBox.setBounds(636, 372, 75, 19);
		contentPane.add(toMonthComboBox);
		for (String tempMonth : months) {
			toMonthComboBox.addItem(tempMonth);
		}
		toMonthComboBox.setSelectedIndex(currMonth - 1);

		toDayComboBox = new JComboBox();
		toDayComboBox.setBackground(SystemColor.controlHighlight);
		toDayComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //if comboBox is changed, update the filter
				if (allVariablesInitialized && toDayComboBox.getSelectedIndex() + 1 != toDayFilter) {
					toDayFilter = toDayComboBox.getSelectedIndex() + 1;
					filterDatesChanged();
				}
				if (changeTimeFilterRadioButton) {
					allTimeRadioBtn.setSelected(true);
					changeTimeFilterRadioButton = false;
				}
			}
		});
		toDayComboBox.setBounds(636, 399, 75, 19);
		contentPane.add(toDayComboBox);
		for (int i = 1; i <= 31; i++) {
			toDayComboBox.addItem(i);
		}
		toDayComboBox.setSelectedIndex(currDay - 1);

		toYearComboBox = new JComboBox();
		toYearComboBox.setBackground(SystemColor.controlHighlight);
		toYearComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //if comboBox is changed, update the filter
				if (allVariablesInitialized && toYearComboBox.getSelectedIndex() + 1 != toYearFilter) {
					toYearFilter = years.get(toYearComboBox.getSelectedIndex());
					filterDatesChanged();
				}
				if (changeTimeFilterRadioButton) {
					allTimeRadioBtn.setSelected(true);
					changeTimeFilterRadioButton = false;
				}
			}
		});
		toYearComboBox.setBounds(636, 426, 75, 19);
		contentPane.add(toYearComboBox);
		for (int i = currYear; i >= currYear - 100; i--) {
			toYearComboBox.addItem(i);
		}

		//link date variables with comboBox values
		fromMonthFilter = fromMonthComboBox.getSelectedIndex() + 1;
		fromDayFilter = fromDayComboBox.getSelectedIndex() + 1;
		fromYearFilter = years.get(fromYearComboBox.getSelectedIndex());
		toMonthFilter = toMonthComboBox.getSelectedIndex() + 1;
		toDayFilter = toDayComboBox.getSelectedIndex() + 1;
		toYearFilter = years.get(toYearComboBox.getSelectedIndex());

		searchField = new JTextField();
		searchField.setBackground(Color.WHITE);
		searchField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				String searchName = searchField.getText();
				if (searchName.equals("")) {
					activeFilter3 = "all";
					filtersUpdated();
				}
			}
		});
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String searchName = searchField.getText();
				if (searchName.equals("")) {
					activeFilter3 = "all";
					filtersUpdated();
				}
			}
		});
		searchField.setBounds(25, 51, 231, 23);
		contentPane.add(searchField);
		searchField.setColumns(10);

		//create search button
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setBackground(new Color(153, 204, 255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchName = searchField.getText();
				if (searchName.equals("")) {
					activeFilter3 = "all";
				} else {
					activeFilter3 = "search";
				}
				filtersUpdated();
			}
		});
		btnNewButton.setBounds(267, 51, 89, 23);
		contentPane.add(btnNewButton);

		//create order type filter buttons
		JRadioButton allBtn = new JRadioButton("All Order Types");
		allBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeFilter1 = "all";
				System.out.println(activeFilter2);
				filtersUpdated();
			}
		});
		typeButtonGroup.add(allBtn);
		allBtn.setBounds(595, 79, 116, 23);
		contentPane.add(allBtn);
		allBtn.setSelected(true);

		JRadioButton buyOrdersBtn = new JRadioButton("Buy Orders");
		buyOrdersBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				activeFilter1 = "buy";
				filtersUpdated();
			}
		});
		typeButtonGroup.add(buyOrdersBtn);
		buyOrdersBtn.setBounds(595, 105, 97, 23);
		contentPane.add(buyOrdersBtn);

		JRadioButton sellOrdersBtn = new JRadioButton("Sell Orders");
		sellOrdersBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeFilter1 = "sell";
				filtersUpdated();
			}
		});
		typeButtonGroup.add(sellOrdersBtn);
		sellOrdersBtn.setBounds(595, 131, 97, 23);
		contentPane.add(sellOrdersBtn);

		JLabel fromLbl = new JLabel("Orders From");
		fromLbl.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 14));
		fromLbl.setBounds(594, 236, 79, 14);
		contentPane.add(fromLbl);

		JLabel toLbl = new JLabel("To");
		toLbl.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 14));
		toLbl.setBounds(594, 349, 45, 14);
		contentPane.add(toLbl);

		//create sorts comboBox
		JComboBox sorts = new JComboBox();
		sorts.setBackground(SystemColor.controlHighlight);
		sorts.setFont(new Font("Tahoma", Font.PLAIN, 12));
		sorts.addItem("Most Recent Date");
		sorts.addItem("Oldest Date");
		sorts.addItem("Highest Total Price");
		sorts.addItem("Lowest Total Price");
		try {
			//default sort is by newest date
			String initialSort = "dateNewest";
			sortType = initialSort;
			sorts.setSelectedIndex(0);
			
			sort("tradeHistoryFile.txt");
		} catch (Exception e) {
			System.out.println(e);
		}
		sorts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sorts.getSelectedIndex() == 0) {
					sortType = "dateNewest"; // Sorting by recent date
				}
				if (sorts.getSelectedIndex() == 1) {
					sortType = "dateOldest"; // Sorting by oldest date
				}
				if (sorts.getSelectedIndex() == 2) {
					sortType = "totalPriceHighest"; // Sorting by highest price
				}
				if (sorts.getSelectedIndex() == 3) {
					sortType = "totalPriceLowest"; // Sorting by lowest price
				}
				String fileToBeSorted = "";
				if (activeFilter1.equals("buy") || activeFilter1.equals("sell") || activeFilter2.equals("time"))
					// a filter is active so the filtered trade history should be sorted
					fileToBeSorted = "filteredTradeHistoryFile.txt";
				else
					// no filter is active so the complete trade history should be sorted
					fileToBeSorted = "tradeHistoryFile.txt";

				ArrayList<Stock> tradeHistoryListToBeSorted = convertFileToList(fileToBeSorted);
				// convert the file contents into an ArrayList
				if (tradeHistoryListToBeSorted.size() != 0) { // if the list is not empty
					clearFile(fileToBeSorted); // clear the file

					ArrayList<Stock> sortedList = quickSort(tradeHistoryListToBeSorted, 0,
							tradeHistoryListToBeSorted.size() - 1, sortType);
					copyArrayListBackIntoFile(sortedList, fileToBeSorted);
					// QuickSort the ArrayList and write the sorted contents back to file

					while (tableModel.getRowCount() > 0) // clear table
						tableModel.removeRow(0);
					convertFileToTable(fileToBeSorted); // convert file contents (now sorted) into the table
				}
			}
		});
		sorts.setBounds(430, 50, 144, 23);
		contentPane.add(sorts);

		JLabel sortByLbl = new JLabel("Sort By");
		sortByLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sortByLbl.setBounds(377, 47, 50, 26);
		contentPane.add(sortByLbl);

		//erase history button clears all trade history
		JButton eraseHistoryBtn = new JButton("Erase History");
		eraseHistoryBtn.setBackground(new Color(255, 153, 153));
		eraseHistoryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //confirm window to clear history
				JFrame frame = new JFrame();
				int result = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to erase all orders from the trade history?", "Are you sure?",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					clearFile("tradeHistoryFile.txt");
					clearFile("filteredTradeHistoryFile.txt");
					while (tableModel.getRowCount() > 0)
						tableModel.removeRow(0);
				} else if (result == JOptionPane.NO_OPTION)
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		eraseHistoryBtn.setBounds(456, 460, 119, 23);
		contentPane.add(eraseHistoryBtn);

		allVariablesInitialized = true;
	}

	public void filterDatesChanged() { //re-sort trade history file when filters are changed
		if (activeFilter2.equals("time")) {
			if (checkIfValidDate()) {
				filtersUpdated();
			} else {
				changeTimeFilterRadioButton = true;
				activeFilter2 = "all";
				JOptionPane.showMessageDialog(null,
						"Make sure dates exist, are not in the future, and from dates are before to dates.",
						"Invalid Date", JOptionPane.ERROR_MESSAGE);
				if (activeFilter1.equals("all"))
					sort("tradeHistoryFile.txt");
				else
					sort("filteredTradeHistoryFile.txt");
			}

		}
	}

	public boolean dateIsAfter(Stock tempStock) { //returns true if tempStock's date is after the from filter date, else false
		if (tempStock.getYear() == fromYearFilter && tempStock.getMonth() == fromMonthFilter	
				&& tempStock.getDay() == fromDayFilter)
			return true;
		if (tempStock.getYear() > fromYearFilter)
			return true;
		if (tempStock.getYear() < fromYearFilter)
			return false;
		if (tempStock.getMonth() > fromMonthFilter)
			return true;
		if (tempStock.getMonth() < fromMonthFilter)
			return false;
		if (tempStock.getDay() > fromDayFilter)
			return true;
		if (tempStock.getDay() < fromDayFilter)
			return false;
		return true;
	}

	public boolean dateIsBefore(Stock tempStock) { //returns true if tempStock's date is before the to filter date, else false
		if (tempStock.getYear() == toYearFilter && tempStock.getMonth() == toMonthFilter
				&& tempStock.getDay() == toDayFilter)
			return true;
		if (tempStock.getYear() > toYearFilter)
			return false;
		if (tempStock.getYear() < toYearFilter)
			return true;
		if (tempStock.getMonth() > toMonthFilter)
			return false;
		if (tempStock.getMonth() < toMonthFilter)
			return true;
		if (tempStock.getDay() > toDayFilter)
			return false;
		if (tempStock.getDay() < toDayFilter)
			return true;
		return true;
	}

	public void searchFilter(String searchName, boolean filterInUse) { //filters list so that only stocks being searched for are visible
		ArrayList<Stock> copiedList;
		if (filterInUse)
			copiedList = filteredList1;
		else
			copiedList = convertFileToList("tradeHistoryFile.txt");
		for (Stock tempStock : copiedList) {
			if (tempStock.getName().equalsIgnoreCase(searchName) || tempStock.getTicker().equalsIgnoreCase(searchName))
				filteredList2.add(tempStock);
		}
		transferLists();
	}

	public void buyFilter() { //filters list so that only buy orders are shown
		ArrayList<Stock> copiedList = convertFileToList("tradeHistoryFile.txt");
		for (Stock tempStock : copiedList) {
			if (tempStock.getType().equals("BUY"))
				filteredList2.add(tempStock);
		}
		transferLists();
	}

	public void sellFilter(boolean filterInUse) { //filters list so that only sell orders are shown
		ArrayList<Stock> copiedList;
		if (filterInUse)
			copiedList = filteredList1;
		else
			copiedList = convertFileToList("tradeHistoryFile.txt");
		for (Stock tempStock : copiedList) {
			if (tempStock.getType().equals("SELL"))
				filteredList2.add(tempStock);
		}
		transferLists();
	}

	public void timeFilter(boolean filterInUse) { //filters list so that only trades within the time period are shown
		ArrayList<Stock> copiedList;
		if (filterInUse)
			copiedList = filteredList1;
		else
			copiedList = convertFileToList("tradeHistoryFile.txt");
		for (Stock tempStock : copiedList) {
			if (dateIsAfter(tempStock) && dateIsBefore(tempStock))
				filteredList2.add(tempStock);
			System.out.println(tempStock);
			System.out.println("after " + dateIsAfter(tempStock));
			System.out.println("before " + dateIsBefore(tempStock));
		}
		transferLists();
	}

	public void updateFilteredFileAndTable(ArrayList<Stock> filteredList1) { //reloads the table so that updated filter is active
		while (tableModel.getRowCount() > 0) // clear table
			tableModel.removeRow(0);
		clearFile("filteredTradeHistoryFile.txt");
		// if (filteredList1.size() > 0) {
		copyArrayListBackIntoFile(filteredList1, "filteredTradeHistoryFile.txt");
		convertFileToTable("filteredTradeHistoryFile.txt");
		// }
	}

	public void createTable() { //creates trade record table
		String col[] = { "Symbol", "Name", "Price", "# of Shares", "Total Price", "BUY/SELL", "Date" };
		tableModel = new DefaultTableModel(col, 0);
		convertFileToTable("tradeHistoryFile.txt");
	}

	public void createScrollPane() { //creates scroll pane for table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 84, 548, 361);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);

		JTable tradeHistoryTable = new JTable(tableModel);
		tradeHistoryTable.getTableHeader().setReorderingAllowed(false); // disable the reordering and resizing of table columns
		tradeHistoryTable.getTableHeader().setResizingAllowed(false);
		//set columns to custom lengths
		tradeHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(45);
		tradeHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(65);
		tradeHistoryTable.getColumnModel().getColumn(2).setPreferredWidth(55);
		tradeHistoryTable.getColumnModel().getColumn(3).setPreferredWidth(60);
		tradeHistoryTable.getColumnModel().getColumn(4).setPreferredWidth(95);
		tradeHistoryTable.getColumnModel().getColumn(5).setPreferredWidth(60);
		tradeHistoryTable.getColumnModel().getColumn(6).setPreferredWidth(60);
		scrollPane.setViewportView(tradeHistoryTable);

		tradeHistoryTable.addMouseListener(new MouseAdapter() { //when a row is clicked, user is sent to edit trade window
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				String fileToBeUsed;
				//file being used depends whether or not a filter is active
				if (activeFilter1.equals("buy") || activeFilter1.equals("sell") || activeFilter2.equals("time")
						|| activeFilter3.equals("search"))
					fileToBeUsed = "filteredTradeHistoryFile.txt";
				else
					fileToBeUsed = "tradeHistoryFile.txt";

				ArrayList<Stock> tradeHistoryList = convertFileToList(fileToBeUsed);
				System.out.println(row);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				EditTradeWindow editTradeWindowFrame = new EditTradeWindow(tradeHistoryList, row);
				editTradeWindowFrame.setTitle("Edit Trade");
				int x = (screenSize.width - editTradeWindowFrame.getWidth()) / 2;
				int y = (screenSize.height - editTradeWindowFrame.getHeight()) / 2;
				editTradeWindowFrame.setLocation(x, y);
				editTradeWindowFrame.setVisible(true);
				//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				//editTradeWindowFrame.setIconImage(img);
				setVisible(false);
			}
		});
	}

	public void convertFileToTable(String file) { //displays file contents in a JTable
		try {
			File tradeHistoryFile = new File(file);
			Scanner scan = new Scanner(tradeHistoryFile);

			while (scan.hasNext()) {
				String ticker = scan.nextLine();
				String name = scan.nextLine();
				String price = scan.nextLine();
				String num = scan.nextLine();
				String totalPrice = scan.nextLine();
				String type = scan.nextLine();
				String date = scan.nextLine();

				Object[] data = { ticker, name, price, num, totalPrice, type, date };
				tableModel.addRow(data);
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void clearFile(String fileName) { //deletes everything in the file
		try {
			FileWriter writer = new FileWriter(fileName, false);
			PrintWriter eraser = new PrintWriter(writer, false);
			eraser.flush();
			eraser.close();
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void sort(String file) { //uses quickSort to sort a file
		ArrayList<Stock> tradeHistoryListToBeSorted = convertFileToList(file);
		if (tradeHistoryListToBeSorted.size() != 0) {
			clearFile(file);
			ArrayList<Stock> sortedList = quickSort(tradeHistoryListToBeSorted, 0,
					tradeHistoryListToBeSorted.size() - 1, sortType);
			copyArrayListBackIntoFile(sortedList, file);
			while (tableModel.getRowCount() > 0)
				tableModel.removeRow(0);
			convertFileToTable(file);
		}
	}

	public ArrayList<Stock> quickSort(ArrayList<Stock> list, int first, int last, String sortType) { //quickSort
		int f = first;
		int l = last;
		int midIndex = (first + last) / 2;
		Stock midValue = list.get(midIndex);
		Stock temp = null;
		do {
			//if statements decide what to compare when sorting
			if (sortType.equals("dateNewest")) {
				while (list.get(f).dateCompareTo(midValue) > 0)
					f++;
				while (list.get(l).dateCompareTo(midValue) < 0)
					l--;
			} else if (sortType.equals("dateOldest")) {
				while (list.get(f).dateCompareTo(midValue) < 0)
					f++;
				while (list.get(l).dateCompareTo(midValue) > 0)
					l--;
			} else if (sortType.equals("totalPriceHighest")) {
				while (list.get(f).totalPriceCompareTo(midValue) > 0)
					f++;
				while (list.get(l).totalPriceCompareTo(midValue) < 0)
					l--;
			} else if (sortType.equals("totalPriceLowest")) {
				while (list.get(f).totalPriceCompareTo(midValue) < 0)
					f++;
				while (list.get(l).totalPriceCompareTo(midValue) > 0)
					l--;
			}
			if (f <= l) {
				// swap values at f and l;
				temp = list.get(f);
				list.set(f, list.get(l));
				list.set(l, temp);

				f++;
				l--;
			}
		} while (f < l);
		if (l > first)
			quickSort(list, first, l, sortType);
		if (f < last)
			quickSort(list, f, last, sortType);
		return list;
	}

	public void findOldestDate(String fileName) { //finds oldest date in the history
		String fileToBeSortedByOldest = fileName;
		ArrayList<Stock> tradeHistoryListToBeOldSorted = convertFileToList(fileToBeSortedByOldest);
		ArrayList<Stock> sortedByOldestList = quickSort(tradeHistoryListToBeOldSorted, 0,
				tradeHistoryListToBeOldSorted.size() - 1);
		Stock oldestStock = sortedByOldestList.get(0);
		oldestMonth = oldestStock.getMonth();
		oldestDay = oldestStock.getDay();
		oldestYear = oldestStock.getYear();
		System.out.println("OLDEST DATE: " + oldestMonth + " " + oldestDay + " " + oldestYear);
	}

	public boolean checkIfValidDate() { //returns true if the filter date exists and from/to dates chronologically make sense
		int newYear = 0;
		int newMonth = 0;
		int newDay = 0;
		for (int count = 1; count <= 2; count++) {
			if (count == 1) {
				newYear = fromYearFilter;
				newMonth = fromMonthFilter;
				newDay = fromDayFilter;
			} else {
				newYear = toYearFilter;
				newMonth = toMonthFilter;
				newDay = toDayFilter;
			}
			// System.out.println(newMonth + " " + newDay + " " + newYear);
			if (newYear % 4 == 0 && newMonth == 2 && newDay > 29) // if the date is after 2/29 on a leap year
				return false;
			else if (newMonth == 2 && newDay > 28 && newYear % 4 != 0) // if the date is after 2/28 on a normal year
				return false;
			else if ((newMonth == 4 || newMonth == 6 || newMonth == 9 || newMonth == 11) && newDay > 30)
				return false;
		}
		if (fromYearFilter > toYearFilter)
			return false;
		else if (fromMonthFilter > toMonthFilter && fromYearFilter == toYearFilter)
			return false;
		else if (fromDayFilter > toDayFilter && fromMonthFilter == toMonthFilter && fromYearFilter == toYearFilter)
			return false;
		return true;
	}

	public void copyArrayListBackIntoFile(ArrayList<Stock> list, String file) { //copies list into file
		for (int i = 0; i < list.size(); i++) {
			Stock tempStock = list.get(i);
			String ticker = tempStock.getTicker();
			String name = tempStock.getName();
			double price = tempStock.getPrice();
			int num = tempStock.getNum();
			double totalPrice = price * num;
			String type = tempStock.getType();
			String date = tempStock.getDate();

			try {
				PrintWriter writer = new PrintWriter(new FileWriter(file, true));
				writer.write(ticker + "\n" + name + "\n" + convertPriceToString(price) + "\n" + num + "\n"
						+ convertPriceToString(totalPrice) + "\n" + type + "\n" + date + "\n");
				writer.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public void filtersUpdated() { //when filters are changed, filter methods are called to filter the table
		clearFilteredList1();
		clearFilteredList2();
		clearFile("filteredTradeHistoryFile.txt");
		boolean filterInUse = false;
		if (activeFilter1.equals("buy")) {
			buyFilter(); // filterFile not in use yet
			filterInUse = true;
		}
		if (activeFilter1.equals("sell")) {
			sellFilter(filterInUse);
			filterInUse = true;
		}
		if (activeFilter2.equals("time")) {
			System.out.println("from " + fromMonthFilter + " " + fromDayFilter + " " + fromYearFilter);
			System.out.println("to " + toMonthFilter + " " + toDayFilter + " " + toYearFilter);
			timeFilter(filterInUse);
			filterInUse = true;
		}
		if (activeFilter3.equals("search")) {
			String searchName = searchField.getText();
			searchFilter(searchName, filterInUse);
			filterInUse = true;
		}
		if (filterInUse) {
			updateFilteredFileAndTable(filteredList1);
			if (filteredList1.size() > 0)
				sort("filteredTradeHistoryFile.txt");
			// clearFilteredList1();
			// clearFilteredList2();
			File filteredTradeHistoryFile = new File("filteredTradeHistoryFile.txt");

		} else {
			sort("tradeHistoryFile.txt");
			File tradeHistoryFile = new File("tradeHistoryFile.txt");

			if (tradeHistoryFile.length() != 0) {
				findOldestDate("tradeHistoryFile.txt");
				fromMonthComboBox.setSelectedIndex(oldestMonth - 1);
				fromDayComboBox.setSelectedIndex(oldestDay - 1);
				fromYearComboBox.setSelectedIndex(currYear - oldestYear);
			}
		}
	}

	public void transferLists() { //copies filteredList2 into filteredList 1 (used when more than one filter is active)
		clearFilteredList1();
		for (Stock transferStock : filteredList2)
			filteredList1.add(transferStock);
		clearFilteredList2();
	}

	public void clearFilteredList1() { //deletes the contents of filteredList1
		while (filteredList1.size() > 0)
			filteredList1.remove(0);
	}

	public void clearFilteredList2() { //deletes the contents of filteredList2
		while (filteredList2.size() > 0)
			filteredList2.remove(0);
	}
}
