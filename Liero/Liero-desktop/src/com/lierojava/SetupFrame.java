/*
 * 
 */
package com.lierojava;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

// TODO: Auto-generated Javadoc
/**
 * The Class hostFrame.
 */
public class SetupFrame extends JFrame {

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
					final SetupFrame frame = new SetupFrame();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** The content pane. */
	private final JPanel contentPane;

	/** The text field. */
	private final JTextField textField;

	/**
	 * Create the frame.
	 */
	public SetupFrame() {
		setTitle("Host");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 970, 560);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		final JLabel lblGameName = new JLabel("Game name:");
		lblGameName.setForeground(Color.WHITE);

		final JLabel lblMaxPlayers = new JLabel("Max. player's:");
		lblMaxPlayers.setForeground(Color.WHITE);

		final JLabel lblLifes = new JLabel("Lives:");
		lblLifes.setForeground(Color.WHITE);

		final JLabel lblAmountOfRocks = new JLabel("Amount of rocks:");
		lblAmountOfRocks.setForeground(Color.WHITE);

		final JList list = new JList();
		list.setBackground(new Color(105, 105, 105));

		final JLabel lblPlayers = new JLabel("Players");
		lblPlayers.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblPlayers.setForeground(Color.LIGHT_GRAY);

		final JLabel lblRank = new JLabel("Rank");
		lblRank.setForeground(Color.LIGHT_GRAY);

		final JLabel lblPing = new JLabel("Ping");
		lblPing.setForeground(Color.LIGHT_GRAY);

		final JList list_1 = new JList();
		list_1.setBackground(new Color(105, 105, 105));

		textField = new JTextField();
		textField.setText("Typ Here");
		textField.setColumns(10);
		textField.setBackground(Color.GRAY);

		final JLabel lblBack = new JLabel("BACK");
		lblBack.setForeground(Color.WHITE);
		lblBack.setFont(new Font("Lucida Grande", Font.PLAIN, 20));

		final JLabel lblWeaponsOfChoice = new JLabel("WEAPONS OF CHOICE");
		lblWeaponsOfChoice.setForeground(Color.LIGHT_GRAY);
		lblWeaponsOfChoice.setFont(new Font("Lucida Grande", Font.PLAIN, 20));

		final JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(new Color(255, 140, 0));

		final JComboBox comboBox_4 = new JComboBox();

		final JComboBox comboBox = new JComboBox();
		comboBox.setToolTipText("");

		final JComboBox comboBox_1 = new JComboBox();

		final JComboBox comboBox_2 = new JComboBox();

		final JComboBox comboBox_3 = new JComboBox();

		final JLabel label_1 = new JLabel("SETTINGS");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		final GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
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
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.TRAILING)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addContainerGap()
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addComponent(
																																lblGameName)
																														.addComponent(
																																lblMaxPlayers)
																														.addComponent(
																																lblAmountOfRocks)
																														.addComponent(
																																lblLifes))
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												387,
																												Short.MAX_VALUE))
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addContainerGap()
																										.addComponent(
																												list_1,
																												GroupLayout.PREFERRED_SIZE,
																												481,
																												GroupLayout.PREFERRED_SIZE)
																										.addGap(18)))
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addComponent(
																												lblPlayers)
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												277,
																												Short.MAX_VALUE)
																										.addComponent(
																												lblRank)
																										.addGap(58)
																										.addComponent(
																												lblPing,
																												GroupLayout.PREFERRED_SIZE,
																												35,
																												GroupLayout.PREFERRED_SIZE))
																						.addComponent(
																								list,
																								GroupLayout.PREFERRED_SIZE,
																								451,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								lblWeaponsOfChoice)
																						.addComponent(
																								comboBox_2,
																								GroupLayout.PREFERRED_SIZE,
																								142,
																								GroupLayout.PREFERRED_SIZE)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addComponent(
																																comboBox_1,
																																GroupLayout.PREFERRED_SIZE,
																																142,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																comboBox,
																																GroupLayout.PREFERRED_SIZE,
																																142,
																																GroupLayout.PREFERRED_SIZE))
																										.addGap(46)
																										.addGroup(
																												gl_contentPane
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addComponent(
																																comboBox_4,
																																GroupLayout.PREFERRED_SIZE,
																																142,
																																GroupLayout.PREFERRED_SIZE)
																														.addComponent(
																																comboBox_3,
																																GroupLayout.PREFERRED_SIZE,
																																142,
																																GroupLayout.PREFERRED_SIZE)))))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				lblBack)
																		.addGap(59)
																		.addComponent(
																				label_1,
																				GroupLayout.PREFERRED_SIZE,
																				94,
																				GroupLayout.PREFERRED_SIZE)))
										.addContainerGap())
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(textField,
												GroupLayout.PREFERRED_SIZE,
												481, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED,
												303, Short.MAX_VALUE)
										.addComponent(progressBar,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(24)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGap(36)
										.addComponent(lblGameName)
										.addGap(5)
										.addComponent(lblMaxPlayers)
										.addGap(30)
										.addComponent(lblLifes)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(lblAmountOfRocks)
										.addPreferredGap(
												ComponentPlacement.RELATED,
												118, Short.MAX_VALUE)
										.addComponent(list_1,
												GroupLayout.PREFERRED_SIZE,
												201, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(textField,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																label_1,
																GroupLayout.PREFERRED_SIZE,
																25,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblBack,
																GroupLayout.PREFERRED_SIZE,
																25,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap())
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGap(24)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblPlayers)
														.addComponent(lblRank)
														.addComponent(lblPing))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(list,
												GroupLayout.PREFERRED_SIZE,
												274, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(lblWeaponsOfChoice,
												GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addGap(19)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																comboBox,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																comboBox_4,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																comboBox_1,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																comboBox_3,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addPreferredGap(
																				ComponentPlacement.RELATED,
																				27,
																				Short.MAX_VALUE)
																		.addComponent(
																				progressBar,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(36))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				comboBox_2,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addContainerGap()))));
		contentPane.setLayout(gl_contentPane);
	}
}
