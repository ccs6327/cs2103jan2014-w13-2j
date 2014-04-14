//@author generated

package clc.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import clc.common.LogHelper;
import static clc.common.Constants.MESSAGE_SEPARATOR;
import static clc.common.Constants.NEWLINE;
import static clc.common.Constants.EMPTY;
import static clc.common.Constants.SPACE;
import static clc.common.Constants.THIS_WEEK;
import static clc.common.Constants.TYPE_DISPLAY;
import static clc.common.Constants.TYPE_DISPLAY_SHORT;
import static clc.common.Constants.TYPE_SHOW;
import static clc.common.Constants.TYPE_LIST;
import static clc.common.Constants.TYPE_HELP;
import static clc.common.Constants.TYPE_SEARCH;

public class GUI implements NativeKeyListener {
	private JFrame frameClc;
	private JTextField inputTextBox = new JTextField();
	private JTextPane displayBox = new JTextPane();
	private String input = EMPTY;
	private JScrollPane scrollPane;
	private StyledDocument doc = displayBox.getStyledDocument();
	private boolean isPressingCtrl;
	private ArrayList<String> previousInput = new ArrayList<String>();
	private int inputIndex = 0;
	protected boolean isWindowActivated;
	private boolean isPressingShift;

	protected GUI() { 
		initialize();
		LogHelper.info("GUI components initialized");
	}

	protected static void launchAndGetInputAndExecute() {
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
		LogHelper.info("GUI launched");
	}

	private void initialize() {
		setUpJFrame();
		setUpInputTextBox();
		setUpDisplayBoxAndScrollPane();
		initializeDisplayBox();
		focusOnInputTextBox();
		initializeInputTextBox();
		actionWhenEnterIsPressed();
		initiateWindowListener();
		centreWindow(frameClc); 
	}
	
	private void setUpJFrame() {
		frameClc = new JFrame();
		frameClc.setName("frame");
		frameClc.getContentPane().setBackground(Color.GRAY);
		frameClc.setTitle("CLC V0.5");
		frameClc.setResizable(false);
		frameClc.setBounds(10, 10, 650, 500);
		frameClc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameClc.getContentPane().setLayout(null);
	}

	private void setUpInputTextBox() {
		inputTextBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		inputTextBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputTextBox.setBounds(10, 415, 625, 40);
		inputTextBox.setColumns(10);
		inputTextBox.setCaretColor(Color.WHITE);
		inputTextBox.setBorder(null);
		inputTextBox.setBackground(Color.DARK_GRAY);
		inputTextBox.setForeground(Color.WHITE);
		frameClc.getContentPane().add(inputTextBox);
	}

	private void setUpDisplayBoxAndScrollPane() {
		displayBox.setMargin(new Insets(10, 10, 10, 10));
		displayBox.setBackground(Color.DARK_GRAY);
		displayBox.setForeground(Color.WHITE);
		displayBox.setEditable(false);
		displayBox.setFont(new Font("Calibri Light", Font.PLAIN, 17));
		displayBox.setBounds(10, 10, 625, 400);
		scrollPane = new JScrollPane(displayBox);
		scrollPane.setBorder(null);
		scrollPane.setBounds(10, 10, 625, 400);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frameClc.getContentPane().add(scrollPane);
	}
	
	//@author A0112089J
	private void focusOnInputTextBox() {
		frameClc.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				inputTextBox.requestFocusInWindow();
			}
		});
	}

	private void initializeInputTextBox() {
		inputTextBox.setText(SPACE);
		inputTextBox.setCaretPosition(SPACE.length());
	}

	private void actionWhenEnterIsPressed() {
		inputTextBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input = inputTextBox.getText().trim();
				if (isCaseClearScreen()) {
					emptyDisplayBox();
				} else {
					previousInput.add(input); // cache the previous input
					inputIndex ++; // use it to traverse previous input
					String feedback = UserInterface.setInputAndExecute(input);
					if (isCaseDisplayOrHelp()) {
						clearDisplayBoxAndStayTop(feedback);
						displayBox.setCaretPosition(0);
					} else {
						showToUser(feedback);
						displayBox.setCaretPosition(doc.getLength());
					}
				}
				initializeInputTextBox();
				inputIndex = previousInput.size();
			}

			private boolean isCaseDisplayOrHelp() {
				String[] command = input.split(SPACE);

				return command[0].equalsIgnoreCase(TYPE_DISPLAY)
						|| command[0].equalsIgnoreCase(TYPE_DISPLAY_SHORT)
						|| command[0].equalsIgnoreCase(TYPE_SHOW)
						|| command[0].equalsIgnoreCase(TYPE_LIST)
						|| command[0].equalsIgnoreCase(TYPE_HELP)
						|| command[0].equalsIgnoreCase(TYPE_SEARCH);
			}

			private void emptyDisplayBox() {
				displayBox.setText(EMPTY);
			}

			private boolean isCaseClearScreen() {
				return input.equals(EMPTY);
			}
		});
	}

	private void clearDisplayBoxAndStayTop(String feedback) {
		try {
			doc.remove(0,doc.getLength());
			showToUser(feedback);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		scrollPane.getVerticalScrollBar().setValue(0);
	}

	private void initiateWindowListener() {
		frameClc.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
				isWindowActivated = true;
			}
			public void windowClosed(WindowEvent arg0) {
				//Clean up the native hook.
				GlobalScreen.unregisterNativeHook();
			}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {
				isWindowActivated = false;
			}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {
				initiateGlobalKeyListener();
				initiateKeyListener();
			}
		});
	}

	private void initiateGlobalKeyListener() {
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.exit(1);
		}

		//Construct the example object and initialize native hook.
		GlobalScreen.getInstance().addNativeKeyListener(this);
	}

	private void initiateKeyListener() {
		inputTextBox.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) { 
				if (e.getKeyCode() == NativeKeyEvent.VK_CONTROL) {
					isPressingCtrl = true;
				}

				showHelpIfPressedF1(e);
				clearInputTextBoxIfPressedEsc(e);
				if (isPressingCtrl) {
					scrollUpOrDownDisplayBoxIfPressedCtrlUpOrDown(e);
				} else {
					traversePreviousInputIfPressedUpOrDown(e);
				}
			}
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == NativeKeyEvent.VK_CONTROL) {
					isPressingCtrl = false;
				}
			}
			public void keyTyped(KeyEvent e) {}
		});
	}

	public static void centreWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	private void initializeDisplayBox() {
		showToUser(UserInterface.getWelcomeMessage());
		String feedback = UserInterface.setInputAndExecute(TYPE_DISPLAY + SPACE + THIS_WEEK);
		showToUser(feedback);
		displayBox.setCaretPosition(0);
	}

	private void showToUser(String string) {
		try {
			doc.insertString(doc.getLength(), string + NEWLINE, null);
			doc.insertString(doc.getLength(), MESSAGE_SEPARATOR, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Override //NativeKeyListener
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VK_SHIFT) {
			isPressingShift = true;
		}
		if (isPressingShift) {
			changeWindowVisibility(e);
		}	
	}
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VK_SHIFT) {
			isPressingShift = false;
		}
	}
	public void nativeKeyTyped(NativeKeyEvent e) {}

	private void changeWindowVisibility(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VK_SPACE) { //shift  + space
			if (frameClc.isVisible()) { 
				frameClc.setVisible(false);
				LogHelper.info("set windows visible to false");
			} else if (!frameClc.isVisible()){
				frameClc.setVisible(true);
				frameClc.setState(Frame.NORMAL);
				LogHelper.info("set windows visible to true");
			} 
		}
	}

	private void traversePreviousInputIfPressedUpOrDown(KeyEvent e) { 
		String cachedInput;

		if (e.getKeyCode() == KeyEvent.VK_UP && inputIndex - 1 >= 0) {
			cachedInput = previousInput.get(--inputIndex);
			inputTextBox.setText(SPACE + cachedInput);
			inputTextBox.setCaretPosition(cachedInput.length() + 1);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && inputIndex + 1 < previousInput.size()) {
			cachedInput = previousInput.get(++inputIndex);
			inputTextBox.setText(SPACE + cachedInput);
			inputTextBox.setCaretPosition(cachedInput.length() + 1);
		}
	}

	private void scrollUpOrDownDisplayBoxIfPressedCtrlUpOrDown(KeyEvent e) {
		int incrementValue = scrollPane.getVerticalScrollBar().getBlockIncrement();
		if (e.getKeyCode() == KeyEvent.VK_UP) { //ctrl + up
			int currPosition = scrollPane.getVerticalScrollBar().getValue(); 
			scrollPane.getVerticalScrollBar().setValue(currPosition - incrementValue);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) { //ctrl + down
			int currPosition = scrollPane.getVerticalScrollBar().getValue(); 
			scrollPane.getVerticalScrollBar().setValue(currPosition + incrementValue);
		}
	}

	private void clearInputTextBoxIfPressedEsc(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			initializeInputTextBox();
			inputIndex = previousInput.size();
		}
	}

	private void showHelpIfPressedF1(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			String feedback = UserInterface.setInputAndExecute(TYPE_HELP);
			clearDisplayBoxAndStayTop(feedback);
			displayBox.setCaretPosition(0);
		}
	}
}
