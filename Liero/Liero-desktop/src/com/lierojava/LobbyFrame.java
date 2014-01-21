/*
 * 
 */
package com.lierojava;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

// TODO: Auto-generated Javadoc
/**
 * The Class Lobby.
 */
public class LobbyFrame extends JFrame {

	/**
	 * Launch the application.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final LobbyFrame frame = new LobbyFrame();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** The content pane. */
	private final JPanel contentPane;

	/** The txt typ here. */
	private final JTextField txtTypHere;

	/**
	 * Create the frame.
	 */
	public LobbyFrame() {
		setTitle("Lobby");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 970, 560);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		final JList listGames = new JList();
		listGames.setBackground(new Color(105, 105, 105));

		final JList list = new JList();
		list.setBackground(new Color(105, 105, 105));

		txtTypHere = new JTextField();
		txtTypHere.setText("Type Here");
		txtTypHere.setBackground(new Color(128, 128, 128));
		txtTypHere.setColumns(10);

		JLabel lblJoin = new JLabel("JOIN");
		lblJoin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		lblJoin.setForeground(new Color(255, 255, 255));
		lblJoin.setFont(new Font("Lucida Grande", Font.PLAIN, 20));

		final JLabel lblHost = new JLabel("HOST");
		lblHost.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SetupFrame sf = new SetupFrame();
				sf.setVisible(true);
			}
		});
		lblHost.setForeground(Color.WHITE);
		lblHost.setFont(new Font("Lucida Grande", Font.PLAIN, 20));

		final JLabel lblQuickMatch = new JLabel("QUICK MATCH");
		lblQuickMatch.setForeground(Color.WHITE);
		lblQuickMatch.setFont(new Font("Lucida Grande", Font.PLAIN, 20));

		final JLabel lblSettings = new JLabel("SETTINGS");
		lblSettings.setForeground(Color.WHITE);
		lblSettings.setFont(new Font("Lucida Grande", Font.PLAIN, 20));

		final JLabel lblHello = new JLabel("Hello, <username>");
		lblHello.setForeground(new Color(255, 255, 255));
		lblHello.setFont(new Font("Lucida Grande", Font.PLAIN, 30));

		final JLabel lblGamesPlayed = new JLabel("Games played:");
		lblGamesPlayed.setForeground(new Color(255, 255, 255));

		final JLabel lblGamesWon = new JLabel("Games won:");
		lblGamesWon.setForeground(new Color(255, 255, 255));

		final JLabel lblWinlostRatio = new JLabel("Win/Lost ratio:");
		lblWinlostRatio.setForeground(new Color(255, 255, 255));

		final JLabel lblKills = new JLabel("Kills:");
		lblKills.setForeground(new Color(255, 255, 255));

		final JLabel lblDeads = new JLabel("Deads:");
		lblDeads.setForeground(new Color(255, 255, 255));

		final JLabel lblKd = new JLabel("K/D:");
		lblKd.setForeground(new Color(255, 255, 255));

		final JLabel label = new JLabel("13");
		label.setForeground(Color.WHITE);

		final JLabel label_1 = new JLabel("4");
		label_1.setForeground(Color.WHITE);

		final JLabel label_2 = new JLabel("1.8");
		label_2.setForeground(Color.WHITE);

		final JLabel label_3 = new JLabel("30");
		label_3.setForeground(Color.WHITE);

		final JLabel label_4 = new JLabel("8");
		label_4.setForeground(Color.WHITE);

		final JLabel label_5 = new JLabel("2.6");
		label_5.setForeground(Color.WHITE);

		final JLabel lblGameName = new JLabel("Game name");
		lblGameName.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblGameName.setForeground(Color.LIGHT_GRAY);

		final JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setForeground(Color.LIGHT_GRAY);
		lblPlayers.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		final JLabel lblPing = new JLabel("Ping");
		lblPing.setForeground(Color.LIGHT_GRAY);
		lblPing.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		final GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblHello)
																						.addComponent(
																								txtTypHere,
																								GroupLayout.DEFAULT_SIZE,
																								481,
																								Short.MAX_VALUE)
																						.addComponent(
																								list,
																								Alignment.TRAILING,
																								GroupLayout.DEFAULT_SIZE,
																								481,
																								Short.MAX_VALUE)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addComponent(
																																lblGamesPlayed)
																														.addComponent(
																																lblGamesWon)
																														.addComponent(
																																lblWinlostRatio)
																														.addComponent(
																																lblKills)
																														.addComponent(
																																lblDeads)
																														.addComponent(
																																lblKd))
																										.addGap(37)
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addComponent(
																																label_5,
																																GroupLayout.PREFERRED_SIZE,
																																29,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																label_4,
																																GroupLayout.PREFERRED_SIZE,
																																29,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																label_3,
																																GroupLayout.PREFERRED_SIZE,
																																29,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																label_2,
																																GroupLayout.PREFERRED_SIZE,
																																29,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																label_1,
																																GroupLayout.PREFERRED_SIZE,
																																29,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																label,
																																GroupLayout.PREFERRED_SIZE,
																																29,
																																GroupLayout.PREFERRED_SIZE))))
																		.addGap(18)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addComponent(
																												lblGameName)
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												lblPlayers,
																												GroupLayout.PREFERRED_SIZE,
																												58,
																												GroupLayout.PREFERRED_SIZE)
																										.addGap(18)
																										.addComponent(
																												lblPing))
																						.addComponent(
																								listGames,
																								GroupLayout.PREFERRED_SIZE,
																								449,
																								GroupLayout.PREFERRED_SIZE)))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(6)
																		.addComponent(
																				lblSettings)
																		.addPreferredGap(
																				ComponentPlacement.RELATED,
																				572,
																				Short.MAX_VALUE)
																		.addComponent(
																				lblQuickMatch)
																		.addGap(18)
																		.addComponent(
																				lblHost)
																		.addGap(18)
																		.addComponent(
																				lblJoin)))
										.addContainerGap()));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(15)
																		.addComponent(
																				lblHello)
																		.addGap(18)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblGamesPlayed)
																						.addComponent(
																								label))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblGamesWon)
																						.addComponent(
																								label_1))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblWinlostRatio)
																						.addComponent(
																								label_2))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblKills)
																						.addComponent(
																								label_3))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblDeads)
																						.addComponent(
																								label_4))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblKd)
																						.addComponent(
																								label_5))
																		.addGap(62)
																		.addComponent(
																				list,
																				GroupLayout.PREFERRED_SIZE,
																				201,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				txtTypHere,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED,
																				26,
																				Short.MAX_VALUE))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(17)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblGameName)
																						.addComponent(
																								lblPing)
																						.addComponent(
																								lblPlayers))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				listGames,
																				GroupLayout.PREFERRED_SIZE,
																				447,
																				GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)))
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createParallelGroup(
																				Alignment.BASELINE)
																		.addComponent(
																				lblJoin)
																		.addComponent(
																				lblHost,
																				GroupLayout.PREFERRED_SIZE,
																				25,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				lblQuickMatch,
																				GroupLayout.PREFERRED_SIZE,
																				25,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				lblSettings,
																				GroupLayout.PREFERRED_SIZE,
																				25,
																				GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}
}
