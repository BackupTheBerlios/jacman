package andyr.jacman.console;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import andyr.jacman.Jacman;
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
	private JTextArea outputArea;
	private JButton btnCloseDialog;
	private ConsoleBufferVO consoleBufferVO;
    
    private I18nManager i18n;

	public ConsoleDialog(String[] command, Dialog owner, String title,
			boolean modal) throws HeadlessException {
		super(owner, title, modal);
        
        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());

		setupGUI();

		final String cmd[] = command;
		final SwingWorker worker = new SwingWorker() {
			@Override
			public Object construct() {
				runCommand(cmd);
				btnCloseDialog.setEnabled(true);
				return "done";
			}
		};
		worker.start();
		setVisible(true);

	}

	public JPanel getConsolePanel() {

		if (consolePanel == null) {
			consolePanel = new JPanel(new BorderLayout());

			outputArea = new JTextArea(20, 80);
			outputArea.setText("");
			// set monospaced font so prints doesnt get skewed
			outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
			outputArea.setEditable(false);

			consolePanel.add(new JScrollPane(outputArea));
		}

		return consolePanel;
	}

	public JPanel getMainContent() {

		if (mainContent == null) {
			mainContent = new JPanel(new BorderLayout());
			mainContent.add(new JLabel(i18n.getString("ConsoleDialogLblOutput")), BorderLayout.NORTH);
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
																	// to
																	// pacmans
																	// question
					consoleBufferVO.getBufferedWriter().write("\n");// hit enter
																	// to send
																	// the
																	// answer
					consoleBufferVO.getBufferedWriter().flush(); // flush,
																	// just to
																	// be sure
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
																	// pacmans
																	// question
					consoleBufferVO.getBufferedWriter().write("\n");// hit enter
																	// to send
																	// the
																	// answer
					consoleBufferVO.getBufferedWriter().flush(); // flush,
																	// just to
																	// be sure
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				setYesNoEnabled(false);
			}
		});
	}

	private void runCommand(String[] command) {
		
		outputArea.append("# ");

		for (int i = 0; i < command.length; i++) {
			outputArea.append(command[i] + " ");
		}
		outputArea.append("\n");

		// Scroll along with the output
		outputArea.setCaretPosition(outputArea.getText().length() - 1);
		try {
			consoleBufferVO = Run.getConsoleBufferVO(command, null, null);
			String stdoutput = "";

			String lastName = "";
			boolean iLovecandy = Jacman.pacmanConf.getILoveCandy();
			int nameColumnWidth = 20;
			int percentColumnWidth = 4;
			int sizeColumnWidth = 8;
			int speedColumnWidth = 8;

			while (stdoutput != null) {

				String line = "  " + stdoutput + "\n";
				StringTokenizer tok = new StringTokenizer(stdoutput);
				int tokens = tok.countTokens();
				// System.out.println(tokens);

				// if there are 6 or 7 tokens, then its most likely a download
				// we should track
				// 6 tokens => ILoveCandy
				// 7 tokens => default pacman output
				if (tokens == 6 || tokens == 7) {
					try {
						// set iLoveCandy status on the first valid download run
						// only
						// REPLACE WITH PacmanConf property
						if (lastName.equals(""))
							iLovecandy = tokens == 6;

						List<String> elements = new ArrayList<String>();
						while (tok.hasMoreTokens()) {
							String tmp = tok.nextToken();
							// skip the progressbar
							if (!tmp.startsWith("[") && !tmp.endsWith("]"))
								elements.add(tmp);
						}
						// name of package/repo-list we are downloading
						String name = elements.get(0);
						for (int i = name.length(); i < nameColumnWidth; i++)
							name += " ";

						// percent downloaded
						String percentBuf = elements.get(1);
						int percent = Integer.parseInt(percentBuf.substring(0,
								percentBuf.length() - 1));
						String percentString = "";
						for (int i = percentBuf.length(); i < percentColumnWidth; i++)
							percentString += " ";
						percentString += percentBuf;

						// size downloaded
						String kBuf = elements.get(2);
						String k = "";
						for (int i = kBuf.length(); i < sizeColumnWidth; i++)
							k += " ";
						k += kBuf;

						// downloadspeed
						String speedBuf = elements.get(3);
						String speed = "";
						for (int i = speedBuf.length(); i < speedColumnWidth; i++)
							speed += " ";
						speed += speedBuf;

						// time
						String time = elements.get(4);

						
						// ascii progressbar
						String progress = "[";
						int i;
						for (i = 1; i < percent / 5; i++)
							if (iLovecandy)
								progress += "-";
							else
								progress += "#";
						if (iLovecandy)
							if (percent < 100)
								if (i % 2 == 0)
									progress += "C";
								else
									progress += "c";
							else
								progress += "-";
						else
							progress += "#";
						for (int j = i; j < 20; j++)
							if (iLovecandy)
								progress += "*";
							else
								progress += " ";
						progress += "]";

						// the line we are going to append
						line = name + "  " + progress + "  " + percentString
								+ "  " + k + "  " + speed + "  " + time;

						// remove last line in texarea if it has the same name
						// as the current element
						if (name.trim().equals(lastName.trim())) {
							int index = outputArea.getText().lastIndexOf(
									lastName.trim());
							outputArea.setText(outputArea.getText().substring(
									0, index));
						}
						// set current name to lastname for next roundtrip
						lastName = name;
					} catch (Exception e) {
						// if something happens, fall back to normal printing
						// for this element
						//e.printStackTrace();
					}
				}// end if( tokens==6 || tokens==7 )

				outputArea.append(line);
				outputArea.setCaretPosition(outputArea.getText().length()
						- line.length());

				// READ CONFIRM OPTION!!!
				stdoutput = "";
				while (stdoutput != null) {
					int tmp = consoleBufferVO.getBufferedReader().read();

					if (tmp == '\n' || tmp == '\r') {
						break;
					}
					if (tmp == -1) {
						stdoutput = null;
						break;
					}

					stdoutput += (char) tmp;
					if (stdoutput.endsWith("[Y/n] ")) {
						// show confirmdialog of some sort
						outputArea.append(stdoutput); // output the confirm
														// text to console
						setYesNoEnabled(true);
						stdoutput = ""; // clear stdotput as we have already
										// shown it in console
					}

				}
			}
			String stderr = "";
			while (stderr != null) {
				String line = "  " + stderr + "\n";
				outputArea.append(line);
				outputArea.setCaretPosition(outputArea.getText().length()
						- line.length());
				stderr = consoleBufferVO.getBufferedErrorReader().readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

}
