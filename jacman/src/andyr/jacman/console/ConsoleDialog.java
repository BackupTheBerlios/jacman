/*
 * Copyright 2005 The Jacman Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package andyr.jacman.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import andyr.jacman.SwingWorker;
import andyr.jacman.utils.I18nManager;

/*
 * Created on Jun 19, 2005
 * 
 */
public class ConsoleDialog extends JDialog {

	private JPanel consolePanel;
	private JPanel mainContent;
	private JButton noButton;
	private JButton yesButton;
	//private JButton abortButton;
	private JTextPane console;
	private JButton btnCloseDialog;
	private ConsoleBufferVO consoleBufferVO;
	private I18nManager i18n;
	
	//default colours
	private Color foreground = Color.WHITE; // regular text colour
	private Color background = Color.BLACK;
	private Color textBackground = background;;
	private Color errorForeground = Color.RED;
	private Color errorTextBackground = background;
	//default colours for console escape colours (used with iLoveCandy)
	//*** the "pacman" is yellow
	//*** the "dots" are white
	private Color yellowEscapeColor = Color.YELLOW;
	private Color whiteEscapeColor = Color.WHITE; // this has to be a dark colour if the console background is white
	//default font
	private Font font = new Font("Monospaced", Font.PLAIN, 10);

	// constructor for dialog parents
	public ConsoleDialog(String[] command, Dialog owner, String title,
			boolean modal) throws HeadlessException {
		super(owner, title, modal);
		setUpConsoleDialog(command, owner);
	}

	// constructor for frame parents
	public ConsoleDialog(String[] command, Frame owner, String title,
			boolean modal) throws HeadlessException {
		super(owner, title, modal);
		setUpConsoleDialog(command, owner);
	}

	private void setUpConsoleDialog(String[] command, Component owner) {
		i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale
				.getDefault());
		setupGUI();
		if (owner != null)
			setLocationRelativeTo(owner);
		final String cmd[] = command;
		final SwingWorker worker = new SwingWorker() {
			@Override
			public Object construct() {
				runCommand(cmd);
				return "done";
			}
		};
		worker.start();
		setVisible(true);
	}
	
	public void setConsoleForeground(Color color){
		console.setForeground(color);
	}
	public void setConsoleBackground(Color color){
		console.setBackground(color);
	}
	public void setConsoleTextBackground(Color color){
		this.textBackground = color;
	}
	public void setConsoleErrorForeground(Color color){
		this.errorForeground = color;
	}
	public void setConsoleErrorTextBackground(Color color){
		this.errorTextBackground = color;
	}
	public void setConsoleFont(Font font){
		console.setFont(font);
	}
	public void setYellowEscape(Color color){
		this.yellowEscapeColor = color;
	}
	public void setWhiteEscape(Color color){
		this.whiteEscapeColor = color;
	}
	

	private JPanel getConsolePanel() {

		if (consolePanel == null) {
			consolePanel = new JPanel(new BorderLayout());
			this.console = new JTextPane();
			this.console.setEditable(false);
			this.setConsoleFont(font);
			JScrollPane scroll = new JScrollPane(console);
			scroll.setPreferredSize(new Dimension(500, 200));
			consolePanel.add(scroll);
			this.setConsoleForeground(foreground);
			this.setConsoleBackground(background);
		}

		return consolePanel;
	}

	private JPanel getMainContent() {

		if (mainContent == null) {
			mainContent = new JPanel(new BorderLayout());
			JLabel lblConsoleOutput = new JLabel(i18n
					.getString("ConsoleDialogLblOutput"));
			lblConsoleOutput.setBorder(new EmptyBorder(3, 3, 3, 3));
			mainContent.add(lblConsoleOutput, BorderLayout.NORTH);
			mainContent.add(getConsolePanel(), BorderLayout.CENTER);

			btnCloseDialog = new JButton(i18n.getString("ConsoleDialogBtnDone"));
			btnCloseDialog.setEnabled(false);
			btnCloseDialog.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			initYesButton();
			initNoButton();
			//initAbortButton();
			JPanel buttonPanel = new JPanel();
			//buttonPanel.add(abortButton);
			buttonPanel.add(btnCloseDialog);
			buttonPanel.add(yesButton);
			buttonPanel.add(noButton);
			mainContent.add(buttonPanel, BorderLayout.SOUTH);
		}
		return mainContent;
	}

	private void setupGUI() {

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		getContentPane().add(getMainContent(), BorderLayout.CENTER);
		pack();
	}

	private void initYesButton() {
		yesButton = new JButton(i18n.getString("ConsoleDialogBtnYes"));
		yesButton.setEnabled(false);
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					consoleBufferVO.getBufferedWriter().write("Y"); // say yes
					consoleBufferVO.getBufferedWriter().write("\n");// hit enter
					consoleBufferVO.getBufferedWriter().flush(); // flush,
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				setYesNoEnabled(false);
			}
		});
	}

	private void setYesNoEnabled(boolean enable) {
		yesButton.setEnabled(enable);
		noButton.setEnabled(enable);
	}

	private void initNoButton() {
		noButton = new JButton(i18n.getString("ConsoleDialogBtnNo"));
		noButton.setEnabled(false);
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					consoleBufferVO.getBufferedWriter().write("n"); // say no
					consoleBufferVO.getBufferedWriter().write("\n");// hit enter
					consoleBufferVO.getBufferedWriter().flush(); // flush,
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				setYesNoEnabled(false);
			}
		});
	}

	private void runCommand(String[] command) {
		StringBuilder cmdString = new StringBuilder();
		cmdString.append("# ");
		for (String c : command)
			cmdString.append(" " + c);
		cmdString.append('\n');
		console.setText(cmdString.toString());
		consoleBufferVO = Run.getConsoleBufferVO(command, null, null);

		new Thread() {
			@Override
			public void run() {
				// the different escape character sequences
				String yellowEscape = new String(new char[] { 27, '[', '1',
						';', '3', '3', 'm' });
				String whiteEscape = new String(new char[] { 27, '[', '0', ';',
						'3', '7', 'm' });
				String noEscape = new String(new char[] { 27, '[', 'm' });
				boolean escape = false;
				int tmp;
				// create the document for the console
				DefaultStyledDocument doc = new DefaultStyledDocument();
				console.setDocument(doc);

				MutableAttributeSet keyword = new SimpleAttributeSet();
				// text colour
				StyleConstants.setForeground(keyword, foreground);
				// text background colour
				StyleConstants.setBackground(keyword,textBackground);
				
				//some variables needed to know where in the document to insert/replace text
				int offset = doc.getLength();
				int preOffset = offset;
				StringBuilder escCode = new StringBuilder();
				try {
					while ((tmp = consoleBufferVO.getBufferedReader().read()) != -1) {
						//27 is the escape character indicating the start of an escape sequence
						if (27 == tmp) {
							escape = true;
						}
						// if we are in an escape sequence - add the char to the escapesequencebuffer
						if (escape) {
							escCode.append((char) tmp);
							// m marks the end of an escape sequence - lets compare it to known escape sequences
							if ('m' == tmp) {
								String esc = escCode.toString();
								if (esc.equals(yellowEscape)) {
									StyleConstants.setForeground(keyword,
											yellowEscapeColor);
								} else if (esc.equals(whiteEscape)) {
									StyleConstants.setForeground(keyword,
											whiteEscapeColor);
								} else if (esc.equals(noEscape)) {
									StyleConstants.setForeground(keyword,
											foreground);
								}
								// empty the escape code sequence buffer, and flag that we are no longer reading an escape code sequence
								escCode.delete(0, escCode.length());
								escape = false;
							}
						} else {
							// handle new line
							if ('\n' == tmp) {
								offset = doc.getLength();
								doc.insertString(offset, "\n", keyword);
								offset++;
								preOffset = offset;
							} 
							//handle carriage return
							else if ('\r' == tmp) {
								offset = preOffset;
							} else {
								// replace character if we are in the document
								if (offset < doc.getLength()) {
									doc.replace(offset, 1, Character
											.toString((char) tmp), keyword);
								} 
								// or else, insert string at the end if the document 
								else {
									doc.insertString(offset, Character
											.toString((char) tmp), keyword);
								}
								// update navigation variables
								offset++;
								console.setCaretPosition(preOffset);
							}
							// enable yes/no buttons if pacman wants us to answer yes/no to a question
							if (console.getText().endsWith("[Y/n] ")) {
								setYesNoEnabled(true);
								doc.insertString(offset, "\n", keyword);
								offset++;
								preOffset = offset;
							}
						}
					} // end while loop
					// close the stream
					consoleBufferVO.getBufferedReader().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				btnCloseDialog.setEnabled(true);
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				StyledDocument doc = console.getStyledDocument();
				MutableAttributeSet errorStdOut = new SimpleAttributeSet();
				StyleConstants.setForeground(errorStdOut, errorForeground);
				StyleConstants.setBackground(errorStdOut,errorTextBackground);
				int tmp;
				try {
					while ((tmp = consoleBufferVO.getBufferedErrorReader()
							.read()) != -1) {
						doc.insertString(doc.getLength(), Character
								.toString((char) tmp), errorStdOut);
					}
					consoleBufferVO.getBufferedErrorReader().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
