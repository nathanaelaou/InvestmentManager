package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ProgramWindow extends JFrame { //parent class for all window classes

	protected String convertPriceToString(double price) { //adds dollar sign, comma, and rounds to 2 decimal places

		String res = "$" + String.format("%,.2f", price);

		return res;
	}

	protected ArrayList<Stock> convertFileToList(String file) { //converts the file passed in into an ArrayList
		try {
			ArrayList<Stock> tradeHistoryList = new ArrayList<Stock>();
			File tradeHistoryFile = new File(file);
			Scanner scan = new Scanner(tradeHistoryFile);

			while (scan.hasNext()) {
				String ticker = scan.nextLine();
				String name = scan.nextLine();
				String price = scan.nextLine();
				String num = scan.nextLine();
				scan.nextLine(); // skip total price
				String type = scan.nextLine();
				String date = scan.nextLine();

				price = price.substring(1); // remove dollar sign
				String priceNoCommas = price.replaceAll(",", "");
				System.out.println("NUMBERNOCOMMAS: " + priceNoCommas);
				double priceDouble = Double.valueOf(priceNoCommas);
				int numInt = Integer.valueOf(num);

				// convert date which is currently a string into 3 separate integers for month,
				// day, and year
				String dateCopy = date; // 3/10/2022
				String month = dateCopy.substring(0, dateCopy.indexOf("/"));
				dateCopy = dateCopy.substring(dateCopy.indexOf("/") + 1);
				String day = dateCopy.substring(0, dateCopy.indexOf("/"));
				dateCopy = dateCopy.substring(dateCopy.indexOf("/") + 1);
				String year = dateCopy;

				// System.out.println(month + " " + day + " " + year);

				int monthInt = Integer.valueOf(month);
				int dayInt = Integer.valueOf(day);
				int yearInt = Integer.valueOf(year);

				Stock tempStock = new Stock(ticker, name, priceDouble, numInt, type, monthInt, dayInt, yearInt);
				tradeHistoryList.add(tempStock);
			}
			return tradeHistoryList;
		} catch (Exception scanFileException) {
			System.out.println(scanFileException);
		}
		return null;
	}

	// In quickSort, the pivot is a value that all other values are compared to.
	// quickSort uses swaps to make values in front of the pivot "less than" the
	// pivot and
	// values after the pivot "greater than" the pivot.
	// Then, quickSort partitions the list into 2 parts and repeats the process on
	// each.
	protected ArrayList<Stock> quickSort(ArrayList<Stock> list, int first, int last) {
		int f = first, l = last;

		int midIndex = (first + last) / 2;
		Stock midValue = list.get(midIndex); // median is chosen to be pivot

		Stock temp = null; // placeholder
		do {
			// first find objects to swap
			while (list.get(f).dateCompareTo(midValue) < 0) // while list[f] < pivot
				f++; // move up an object
			while (list.get(l).dateCompareTo(midValue) > 0) // while list[l] > pivot
				l--; // move down an object

			if (f <= l) {
				// swap(list[f], list[l]);
				temp = list.get(f);
				list.set(f, list.get(l));
				list.set(l, temp);
				f++;
				l--;
			}
		} while (f < l);
		// divide list in 2 and repeat with each part
		if (l > first)
			quickSort(list, first, l);
		if (f < last)
			quickSort(list, f, last);
		return list;
	}
}
