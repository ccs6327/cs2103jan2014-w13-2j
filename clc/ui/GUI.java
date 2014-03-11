package clc.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class GUI {

	private String input;

	private JFrame frame;
	private final JTextField inputTextBox = new JTextField();
	private JTextArea displayBox = new JTextArea();

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		focusOnInputTextBox();
		initializeTextInInputTextBox();
		actionWhenEnterIsPressed();
	}

	/**
	 * Launch the application.
	 */
	public String launchAndGetInputAndExecute() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return input;
	}

	private void focusOnInputTextBox() {
		frame.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				inputTextBox.requestFocusInWindow();
			}
		});
	}

	private void actionWhenEnterIsPressed() {
		inputTextBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = inputTextBox.getText().trim();
				UserInterface.setInputAndExecute(input);
				showToUser(UserInterface.getFeedback());
				initializeTextInInputTextBox();
			}
		});
	}

	private void initializeTextInInputTextBox() {
		inputTextBox.setText(" ");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 627, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		inputTextBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		inputTextBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputTextBox.setBounds(12, 426, 597, 38);
		frame.getContentPane().add(inputTextBox);
		inputTextBox.setColumns(10);

		displayBox.setEditable(false);
		displayBox.setFont(new Font("Monospaced", Font.PLAIN, 15));
		displayBox.setBounds(12, 13, 597, 400);
		frame.getContentPane().add(displayBox);
		initializeTextInDisplayBox();
	}

	private void initializeTextInDisplayBox() {
		showToUser(UserInterface.getWelcomeMessage());
	}

	protected void showToUser(String string) {
		displayBox.append(" " + string + "\n");
	}
}
