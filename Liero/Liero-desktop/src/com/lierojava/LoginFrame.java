/*
 * 
 */
package com.lierojava;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

/**
 * The Class inlogScherm.
 */
public class LoginFrame extends JFrame {

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
					final LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** The content pane. */
	private final JPanel contentPane;

	/** The password field. */
	private final JPasswordField passwordField;

	/** The txt username. */
	private final JTextField txtUsername;

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		final JLabel lblActiveGames = new JLabel("Active games:");
		lblActiveGames.setForeground(Color.WHITE);

		final JLabel lblActivePlayers = new JLabel("Active players:");
		lblActivePlayers.setForeground(Color.WHITE);

		final JLabel lblTotalKills = new JLabel("Total kills:");
		lblTotalKills.setForeground(Color.WHITE);

		final JLabel label = new JLabel("290");
		label.setForeground(Color.WHITE);

		final JLabel label_1 = new JLabel("613");
		label_1.setForeground(Color.WHITE);

		final JLabel label_2 = new JLabel("390456");
		label_2.setForeground(Color.WHITE);

		txtUsername = new JTextField();
		txtUsername.setBackground(Color.LIGHT_GRAY);
		txtUsername.setText("Username");
		txtUsername.setColumns(10);

		final JLabel lblConfirm = new JLabel("Confirm");
		lblConfirm.setForeground(Color.WHITE);
		lblConfirm.setFont(new Font("Lucida Grande", Font.PLAIN, 20));

		final JLabel lblRegister = new JLabel("Register");
		lblRegister.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblRegister.setForeground(Color.LIGHT_GRAY);

		final JLabel lblKillemAll = new JLabel("Kill 'em all!");
		lblKillemAll.setForeground(new Color(255, 140, 0));
		lblKillemAll.setFont(new Font("Lucida Grande", Font.PLAIN, 40));

		passwordField = new JPasswordField();
		passwordField.setBackground(Color.LIGHT_GRAY);
		final GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addComponent(
																				lblRegister)
																		.addGap(54)
																		.addComponent(
																				lblConfirm))
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
																										.addComponent(
																												lblActiveGames)
																										.addGap(18)
																										.addComponent(
																												label))
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addComponent(
																												lblActivePlayers,
																												GroupLayout.PREFERRED_SIZE,
																												102,
																												GroupLayout.PREFERRED_SIZE)
																										.addGap(4)
																										.addComponent(
																												label_1,
																												GroupLayout.PREFERRED_SIZE,
																												24,
																												GroupLayout.PREFERRED_SIZE))
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addComponent(
																												lblTotalKills,
																												GroupLayout.PREFERRED_SIZE,
																												78,
																												GroupLayout.PREFERRED_SIZE)
																										.addGap(28)
																										.addComponent(
																												label_2)))
																		.addGap(57)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.TRAILING,
																								false)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addComponent(
																												lblKillemAll)
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE))
																						.addComponent(
																								txtUsername,
																								GroupLayout.DEFAULT_SIZE,
																								196,
																								Short.MAX_VALUE)
																						.addComponent(
																								passwordField,
																								Alignment.LEADING,
																								GroupLayout.PREFERRED_SIZE,
																								215,
																								GroupLayout.PREFERRED_SIZE))))
										.addGap(27)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblActiveGames)
														.addComponent(label))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																lblKillemAll)
														.addGroup(
																Alignment.LEADING,
																gl_contentPane
																		.createSequentialGroup()
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblActivePlayers)
																						.addComponent(
																								label_1))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(
																								lblTotalKills)
																						.addComponent(
																								label_2))))
										.addGap(80)
										.addComponent(txtUsername,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(passwordField,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblConfirm)
														.addComponent(
																lblRegister))
										.addContainerGap(7, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}
}
