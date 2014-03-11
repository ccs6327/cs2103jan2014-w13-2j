package clc.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Window.Type;


public class GUI {

	private String input;

	private JFrame frmClcV;
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
					window.frmClcV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return input;
	}

	private void focusOnInputTextBox() {
		frmClcV.addWindowFocusListener(new WindowAdapter() {
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
		frmClcV = new JFrame();
		frmClcV.getContentPane().setBackground(Color.GRAY);
		frmClcV.setTitle("CLC V0.1");
		frmClcV.setResizable(false);
		frmClcV.setBounds(100, 100, 669, 496);
		frmClcV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClcV.getContentPane().setLayout(null);
		inputTextBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		inputTextBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputTextBox.setBounds(12, 417, 639, 38);
		frmClcV.getContentPane().add(inputTextBox);
		inputTextBox.setColumns(10);
		
		
		displayBox.setBackground(Color.DARK_GRAY);
		displayBox.setForeground(Color.WHITE);
		displayBox.setEditable(false);
		displayBox.setFont(new Font("Calibri", Font.PLAIN, 17));
		displayBox.setBounds(12, 13, 597, 400);
		JScrollPane scrollPane = new JScrollPane(displayBox);
		scrollPane.setBorder(null);
		scrollPane.setBounds(12, 13, 639, 391);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmClcV.getContentPane().add(scrollPane);
		initializeTextInDisplayBox();
	}

	private void initializeTextInDisplayBox() {
		showToUser(UserInterface.getWelcomeMessage());
	}

	protected void showToUser(String string) {
		displayBox.append(" " + string + "\n");
	}
}
