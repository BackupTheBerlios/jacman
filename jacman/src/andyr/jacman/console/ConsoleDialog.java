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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
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
	private JTextPane console;
	private JButton btnCloseDialog;
	private ConsoleBufferVO consoleBufferVO;
	private I18nManager i18n;

	// TODO Make code reusable and pretty

	public ConsoleDialog(String[] command, Dialog owner, String title,
			boolean modal) throws HeadlessException {
		super(owner, title, modal);

		i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale
				.getDefault());
		setupGUI();

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

	public JPanel getConsolePanel() {

		if (consolePanel == null) {
			consolePanel = new JPanel(new BorderLayout());
			console = new JTextPane();
			console.setEditable(false);
			console.setFont(new Font("Monospaced", Font.PLAIN, 10));
			JScrollPane scroll = new JScrollPane(console);
			scroll.setPreferredSize(new Dimension(500, 200));
			consolePanel.add(scroll);
		}

		return consolePanel;
	}

	public JPanel getMainContent() {

		if (mainContent == null) {
			mainContent = new JPanel(new BorderLayout());
			mainContent.add(
					new JLabel(i18n.getString("ConsoleDialogLblOutput")),
					BorderLayout.NORTH);
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
			JPanel buttonPanel = new JPanel();
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
					consoleBufferVO.getBufferedWriter().write("n"); // say no to
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
				String yellowEscape = new String(new char[] { 27, '[', '1',
						';', '3', '3', 'm' });
				String whiteEscape = new String(new char[] { 27, '[', '0', ';',
						'3', '7', 'm' });
				String noEscape = new String(new char[] { 27, '[', 'm' });
				boolean escape = false;
				int tmp;
				StyledDocument doc = console.getStyledDocument();
				MutableAttributeSet keyword = new SimpleAttributeSet();
				StyleConstants.setForeground(keyword, Color.BLACK);
				int offset = doc.getLength();
				int preOffset = offset;
				StringBuilder escCode = new StringBuilder();
				try {
					while ((tmp = consoleBufferVO.getBufferedReader().read()) != -1) {
						if (27 == tmp)
							escape = true;
						if (escape)
							escCode.append((char) tmp);
						else {
							if ('\n' == tmp) {
								offset = doc.getLength();
								preOffset = offset + 1;
							}
							if ('\r' == tmp)
								offset = preOffset;
							else {
								if (offset < doc.getLength()) {
									doc.remove(offset, 1);
								}
								doc.insertString(offset, "" + (char) tmp,
										keyword);
								offset++;
								console.setCaretPosition(preOffset);
							}
							if (console.getText().endsWith("[Y/n] ")) {
								setYesNoEnabled(true);
								doc.insertString(offset, "\n", keyword);
								offset++;
								preOffset = offset;
							}
						}
						if (escape) {
							if ('m' == tmp) {
								String esc = escCode.toString();
								if (esc.equals(yellowEscape)) {
									StyleConstants.setForeground(keyword,
											Color.YELLOW);
									escCode.delete(0, escCode.length());
								} else if (esc.equals(whiteEscape)) {
									StyleConstants.setForeground(keyword,
											Color.GRAY);
									escCode.delete(0, escCode.length());
								} else if (esc.equals(noEscape)) {
									StyleConstants.setForeground(keyword,
											Color.BLACK);
									escCode.delete(0, escCode.length());
								}
								escape = false;
							}
						}
					}
					consoleBufferVO.getBufferedReader().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println("PACMAN STD_OUT DONE");
				btnCloseDialog.setEnabled(true);
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				StyledDocument doc = console.getStyledDocument();
				MutableAttributeSet errorStdOut = new SimpleAttributeSet();
				StyleConstants.setForeground(errorStdOut, Color.RED);
				int tmp;
				try {
					while ((tmp = consoleBufferVO.getBufferedErrorReader()
							.read()) != -1) {
						doc.insertString(doc.getLength(), "" + (char) tmp,
								errorStdOut);
					}
					consoleBufferVO.getBufferedErrorReader().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println("PACMAN ERR_OUT DONE");
			}
		}.start();
	}
}
