package clc.ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.TimerTask;
import java.awt.Font;

public class Reminder extends TimerTask {

	private JFrame frameReminder = new JFrame();
	private JTextArea reminderBox = new JTextArea();
	private JButton closeButton;
	
	public void run() {
		frameReminder.setVisible(true);
	}

	protected Reminder(String reminderInfo, final long taskId) {
		initializeFrame();
		initializeReminderBox(reminderInfo);
		initializeButton(taskId);
	}

	private void initializeFrame() {
		frameReminder.setAlwaysOnTop(true);
		frameReminder.setTitle("REMINDER");
		frameReminder.setResizable(false);
		frameReminder.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameReminder.setBounds(100, 100, 500, 242);
		frameReminder.getContentPane().setBackground(Color.BLACK);
		frameReminder.getContentPane().setLayout(null);
	}

	private void initializeReminderBox(String reminderInfo) {
		reminderBox.setLineWrap(true);
		reminderBox.setFont(new Font("Monospaced", Font.BOLD, 17));
		reminderBox.setForeground(Color.WHITE);
		reminderBox.append(reminderInfo);
		reminderBox.setEditable(false);
		reminderBox.setBounds(12, 13, 470, 143);
		reminderBox.setBackground(Color.DARK_GRAY);
		frameReminder.getContentPane().add(reminderBox);
	}

	private void initializeButton(final long taskId) {
		closeButton = new JButton("Mark As Done");
		
		closeButton.setBounds(12, 169, 470, 25);
		closeButton.setBackground(Color.DARK_GRAY);
		closeButton.setForeground(Color.WHITE);
		actionWhenButtonIsPressed(taskId);
		requestFocusOnButton();
		frameReminder.getContentPane().add(closeButton);
	}

	private void actionWhenButtonIsPressed(final long taskId) {
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserInterface.markReminderTask(taskId);
				frameReminder.dispose();
			}
		});
	}

	private void requestFocusOnButton() {
		frameReminder.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				closeButton.requestFocusInWindow();
			}
		});
	}

}
