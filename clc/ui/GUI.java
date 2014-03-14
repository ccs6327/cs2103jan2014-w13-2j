package clc.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;



public class GUI {

	private JFrame frameClc;
	private JTextField inputTextBox = new JTextField();
	private JTextArea displayBox = new JTextArea();

	/**
	 * Create the application.
	 */
	protected GUI() {
		initialize();
		focusOnInputTextBox();
		initializeInputTextBox();
		actionWhenEnterIsPressed();
	}

	/**
	 * Launch the application.
	 */
	protected void launchAndGetInputAndExecute() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frameClc.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void focusOnInputTextBox() {
		frameClc.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				inputTextBox.requestFocusInWindow();
			}
		});
	}

	private void actionWhenEnterIsPressed() {
		inputTextBox.setBorder(null);
		inputTextBox.setBackground(Color.DARK_GRAY);
		inputTextBox.setForeground(Color.WHITE);
		inputTextBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = inputTextBox.getText().trim();
				String feedback = UserInterface.setInputAndExecute(input);
				if (isCaseClearScreen(input)) {
					emptyDisplayBox();
				} else {
					showToUser(feedback);
				}
				initializeInputTextBox();
			}

			private void emptyDisplayBox() {
				displayBox.setText("");
			}

			private boolean isCaseClearScreen(String input) {
				return input.equals("");
			}
		});
	}

	private void initializeInputTextBox() {
		inputTextBox.setText("  ");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameClc = new JFrame();
		frameClc.getContentPane().setBackground(Color.GRAY);
		frameClc.setTitle("CLC V0.1");
		frameClc.setResizable(false);
		frameClc.setBounds(100, 100, 669, 496);
		frameClc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameClc.getContentPane().setLayout(null);
		inputTextBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		inputTextBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputTextBox.setBounds(12, 417, 639, 38);
		inputTextBox.setColumns(10);
		inputTextBox.setCaretColor(Color.WHITE);
		frameClc.getContentPane().add(inputTextBox);
				
		displayBox.setWrapStyleWord(true);
		displayBox.setMargin(new Insets(10, 10, 10, 10));
		displayBox.setBackground(Color.DARK_GRAY);
		displayBox.setForeground(Color.WHITE);
		displayBox.setEditable(false);
		displayBox.setFont(new Font("Calibri", Font.PLAIN, 17));
		displayBox.setBounds(12, 13, 597, 400);
		displayBox.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(displayBox);
		scrollPane.setBorder(null);
		scrollPane.setBounds(12, 13, 639, 391);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frameClc.getContentPane().add(scrollPane);
		initializeDisplayBox();
	}

	private void initializeDisplayBox() {
		showToUser(UserInterface.getWelcomeMessage());
	}

	private void showToUser(String string) {
		displayBox.append(string + "\n");
		displayBox.append("==========================================================================\n");
	}
}
