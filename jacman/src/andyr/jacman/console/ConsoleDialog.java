package andyr.jacman.console;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import andyr.jacman.SwingWorker;
/*
 * Created on Jun 19, 2005
 *
 */

public class ConsoleDialog extends JDialog {
    
    JPanel consolePanel;
    JPanel mainContent;
    
    JTextArea outputArea;
    
    JButton btnCloseDialog;
    
    public ConsoleDialog(String[] command, Dialog owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        
        setupGUI();

        final String cmd[] = command;
        final SwingWorker worker = new SwingWorker() {
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
            
            outputArea = new JTextArea(20,80);
            outputArea.setText("");
            outputArea.setEditable(false);
            
            consolePanel.add(new JScrollPane(outputArea));
        }
        
        return consolePanel;
    }
    
    public JPanel getMainContent() {
    
        if (mainContent == null) {
            mainContent = new JPanel(new BorderLayout());
            mainContent.add(new JLabel("Console output:"), BorderLayout.NORTH);
            mainContent.add(getConsolePanel(), BorderLayout.CENTER);
            
            btnCloseDialog = new JButton("Done");
            btnCloseDialog.setEnabled(false);
            btnCloseDialog.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    
                    dispose();
                    
                }
                
            });
            
            mainContent.add(btnCloseDialog, BorderLayout.SOUTH);
        }
        return mainContent;
    }
    
    private void setupGUI() {
        
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        getContentPane().add(getMainContent(), BorderLayout.CENTER);
        pack();
        
        //setVisible(true);
    }
    

    private void runCommand(String[] command) {
        Run run = new Run();
        //String[] cmd = {command, "-l", "/dev"};
        outputArea.append("# ");
        
        for (int i = 0; i < command.length; i++) {
            outputArea.append(command[i] + " ");
        }
        outputArea.append("\n");
        
        //Scroll along with the output
        outputArea.setCaretPosition(outputArea.getText().length()-1);
        try {
            BufferedReader[] outputs = run.it(command , null , null,true);
            String stdoutput = "";
            while (stdoutput != null) {
                String line = "    "+stdoutput+"\n";
                outputArea.append(line);
                
                outputArea.setCaretPosition(outputArea.getText().length()-line.length());
                stdoutput = outputs[0].readLine();
            }
            String stderr = "";
            while (stderr != null) {
                String line = "    "+stderr+"\n";
                outputArea.append(line);
                
                outputArea.setCaretPosition(outputArea.getText().length()-line.length());
                stderr = outputs[1].readLine();
            }
        } catch (Exception e) {
            System.err.println("Error running makepkg");
        }
    } 

}
