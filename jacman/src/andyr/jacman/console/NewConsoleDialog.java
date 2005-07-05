package andyr.jacman.console;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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

public class NewConsoleDialog extends JDialog {
    
    JPanel consolePanel;
    JPanel mainContent;
    
    JTextArea outputArea;
    
    JButton btnCloseDialog;
    
    public NewConsoleDialog(String[] command, Dialog owner, String title, boolean modal) throws HeadlessException {
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
            //set monospaced font so prints doesnt get skewed
            outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
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
        //String[] cmd = {command, "-l", "/dev"};
        outputArea.append("# ");
        
        for (int i = 0; i < command.length; i++) {
            outputArea.append(command[i] + " ");
        }
        outputArea.append("\n");
        
        //Scroll along with the output
        outputArea.setCaretPosition(outputArea.getText().length()-1);
        try {
            BufferedReader[] outputs = Run.it(command , null , null,true);
            String stdoutput = "";
            
            String lastName = "";
            boolean iLovecandy = false;
            int nameColumnWidth = 20;
            int percentColumnWidth = 4;
            int sizeColumnWidth = 8;
            int speedColumnWidth = 8;
            
            while (stdoutput != null) {
            	
                String line = "  "+stdoutput+"\n";
            	StringTokenizer tok = new StringTokenizer(stdoutput);
            	int tokens =tok.countTokens();
            	
            	
            	//if there are 6 or 7 tokens, then its most likely a download we should track
            	//6 tokens => ILoveCandy
            	//7 tokens => default pacman output
        		if(tokens==6||tokens==7){
        			try{
        				//set iLoveCandy status on the first valid download run only
                    	if(lastName.equals(""))
                    		iLovecandy=tokens==6;
	        			List<String> elements = new ArrayList<String>();
	                	while(tok.hasMoreTokens()){
	                		elements.add(tok.nextToken());
	                	}
	        			//name of package/repo-list we are downloading
	            		String name = elements.get(0);
	            		for(int i=name.length();i<nameColumnWidth;i++)
	            			name+=" ";
	            		
	            		//percent downloaded
	            		String percentBuf = elements.get(tokens==6?2:3);
	            		int percent = Integer.parseInt(percentBuf.substring(0,percentBuf.length()-1));
	            		String percentString="";
	            		for(int i=percentBuf.length();i<percentColumnWidth;i++)
	            			percentString+=" ";
	            		percentString+=percentBuf;
	            		
	            		//size downloaded
	            		String kBuf = elements.get(tokens==6?3:4);
	            		String k="";
	            		for(int i=kBuf.length();i<sizeColumnWidth;i++)
	            			k+=" ";
	            		k+=kBuf;
	            		
	            		//downloadspeed
	            		String speedBuf = elements.get(tokens==6?4:5);
	            		String speed="";
	            		for(int i=speedBuf.length();i<speedColumnWidth;i++)
	            			speed+=" ";
	            		speed+=speedBuf;
	            		
	            		//time
	            		String time = elements.get(tokens==6?5:6);
	            		
	            		//ascii-progressbar emulation for both regular and iLoveCandy themed pacman
	            		String progress = "[";
	            		int i;
	            		for(i=1;i<percent/5;i++)
	            			progress+=iLovecandy?"-":"#";
	            		progress+=iLovecandy?percent<100?i%2==0?"C":"c":"-":"#";
	            		for(int j=i;j<20;j++)
	            			progress+=iLovecandy?"*":" ";
	            		progress+="]";
	            		
	            		//the line we are going to append
	            		line = name+"  "+progress+"  "+percentString+"  "+k+"  "+speed+"  "+time+"\n";
	            		
	            		//remove last line in texarea if it has the same name as the current element
	            		if(name.trim().equals(lastName.trim()))
	            		{
	            			int index = outputArea.getText().lastIndexOf(lastName.trim());
	            			outputArea.setText(outputArea.getText().substring(0,index));
	            		}
	            		//set current name to lastname for next roundtrip
	            		lastName = name;
        			} catch(Exception e){
        				//if something happens, fall back to normal printing for this element
        				//e.printStackTrace();
        			}
            	}//end if( tokens==6 || tokens==7 )
            		
                outputArea.append(line);
                outputArea.setCaretPosition(outputArea.getText().length()-line.length());
            	
                stdoutput = outputs[0].readLine();
            }
            String stderr = "";
            while (stderr != null) {
                String line = "  "+stderr+"\n";
                outputArea.append(line);
                outputArea.setCaretPosition(outputArea.getText().length()-line.length());
                stderr = outputs[1].readLine();
            }
        } catch (Exception e) {
            System.err.println("Error running pacman");
        }
    } 

}
