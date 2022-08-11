 package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class MainMenu extends JFrame {
	private File icon;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//create Main Menu Window
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					MainMenu mainMenu = new MainMenu();
					mainMenu.setTitle("Main Menu");
					int x = (screenSize.width - mainMenu.getWidth()) / 2;
					int y = (screenSize.height - mainMenu.getHeight()) / 2;
					mainMenu.setLocation(x, y);
					mainMenu.setVisible(true);
					mainMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		//set window icon
	    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 482, 346);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		icon = new File("Investment Manager Icon.png");
		
		//create recordNewTrade Button
		JButton recordNewTradeBtn = new JButton("Record New Trade");
		recordNewTradeBtn.setForeground(SystemColor.desktop);
		recordNewTradeBtn.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 13));
		recordNewTradeBtn.setBackground(new Color(173, 216, 230));
		recordNewTradeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RecordNewTradeWindow recordNewTradeWindowFrame = new RecordNewTradeWindow("", "");
				recordNewTradeWindowFrame.setTitle("Record New Trade");
				int x = (screenSize.width - recordNewTradeWindowFrame.getWidth()) / 2;
				int y = (screenSize.height - recordNewTradeWindowFrame.getHeight()) / 2;
				recordNewTradeWindowFrame.setLocation(x, y);
				recordNewTradeWindowFrame.setVisible(true);
				//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				recordNewTradeWindowFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
				setVisible(false);
			}
		});
		recordNewTradeBtn.setBounds(159, 115, 141, 34);
		contentPane.add(recordNewTradeBtn);

		
		//create currentPositions Button
		JButton currentPositionsBtn = new JButton("Current Positions");
		currentPositionsBtn.setForeground(SystemColor.desktop);
		currentPositionsBtn.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 13));
		currentPositionsBtn.setBackground(new Color(173, 216, 230));
		currentPositionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CurrentPositionsWindow currentPositionsWindowFrame = new CurrentPositionsWindow();
				currentPositionsWindowFrame.setTitle("Current Positions");
				int x = (screenSize.width - currentPositionsWindowFrame.getWidth()) / 2;
				int y = (screenSize.height - currentPositionsWindowFrame.getHeight()) / 2;
				currentPositionsWindowFrame.setLocation(x, y);
				currentPositionsWindowFrame.setVisible(true);
				//Image img = Toolkit.getDefaultToolkit().getImage(icon.getAbsolutePath());
				currentPositionsWindowFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Investment Manager Icon.png")));
				setVisible(false);
			}
		});
		currentPositionsBtn.setBounds(159, 174, 141, 34);
		contentPane.add(currentPositionsBtn);

		//create tradeRecord Button
		JButton tradeRecordBtn = new JButton("Trade History");
		tradeRecordBtn.setForeground(SystemColor.desktop);
		tradeRecordBtn.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 13));
		tradeRecordBtn.setBackground(new Color(173, 216, 230));
		tradeRecordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		tradeRecordBtn.setBounds(159, 234, 141, 34);
		contentPane.add(tradeRecordBtn);

		JLabel investmentManagerLbl = new JLabel("Investment Manager");
		investmentManagerLbl.setForeground(SystemColor.desktop);
		investmentManagerLbl.setFont(new Font("Imprint MT Shadow", Font.PLAIN, 38));
		investmentManagerLbl.setBounds(64, 28, 443, 51);
		contentPane.add(investmentManagerLbl);

	}
}





