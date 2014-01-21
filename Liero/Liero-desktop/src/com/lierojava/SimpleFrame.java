package com.lierojava;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class SimpleFrame extends JFrame {
	private JTextField tbHost;

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
					final SimpleFrame frame = new SimpleFrame();
					frame.setMinimumSize(new Dimension(300, 100));
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public SimpleFrame() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnHost = new JButton("Host");
		btnHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
				cfg.title = "Liero";
				cfg.useGL20 = true;
				cfg.width = 1280;
				cfg.height = 720;
				
				setVisible(false);
				new LwjglApplication(new Liero(), cfg);
			}
		});
		panel.add(btnHost);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tbHost.getText().isEmpty()) {
					return;
				}
				
				LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
				cfg.title = "Liero";
				cfg.useGL20 = true;
				cfg.width = 1280;
				cfg.height = 720;
				
				setVisible(false);
				new LwjglApplication(new Liero(tbHost.getText()), cfg);
			}
		});
		panel.add(btnJoin);
		
		tbHost = new JTextField();
		getContentPane().add(tbHost, BorderLayout.CENTER);
		tbHost.setColumns(10);
	}

}
