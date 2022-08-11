package main;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

public class Driver extends JFrame {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			
			//creates the text files needed for the program
			File textFile1 = new File("tradeHistoryFile.txt");
			File textFile2 = new File("filteredTradeHistoryFile.txt");
			try {
				textFile1.createNewFile();
				textFile2.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
			
			//opens the main menu
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			MainMenu mainMenu = new MainMenu();
			mainMenu.setTitle("Main Menu");
			int x = (screenSize.width - mainMenu.getWidth()) / 2;
			int y = (screenSize.height - mainMenu.getHeight()) / 2;
			mainMenu.setLocation(x, y);
			mainMenu.setVisible(true);

		});
	}
}