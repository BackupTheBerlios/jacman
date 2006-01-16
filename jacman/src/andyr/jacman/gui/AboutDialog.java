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

package andyr.jacman.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import andyr.jacman.Jacman;
import andyr.jacman.utils.I18nManager;

public class AboutDialog extends JDialog {
    
    private I18nManager i18n;
    
    public AboutDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        //getContentPane().setLayout(new BorderLayout());
        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        setupGUI();
        this.pack();
        if (parent != null)
        	this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    
    private void setupGUI() {
        JPanel aboutPanel = new JPanel(new BorderLayout());
        
        HeaderPanel header = new HeaderPanel("icons/jacman_logo.png",
                i18n.getString("AboutDialogTitle") + " " + Jacman.JACMAN_NAME + " " + Jacman.JACMAN_VERSION_NUMBER,
                i18n.getString("AboutDialogDevelopedBy") + " " + Jacman.JACMAN_DEV,
                Jacman.JACMAN_URL);
        
        aboutPanel.add(header, BorderLayout.NORTH);
        
        StringBuilder acks = new StringBuilder("<html>" + i18n.getString("AboutDialogAcknowledgments") + "<br>");
        
        acks.append("&nbsp;&nbsp;Jon-Anders Teigen (soniX)" + "<br>");
        acks.append("&nbsp;&nbsp;James Sudbury (Sudman1)" + "<br>");
        acks.append("&nbsp;&nbsp;Romain Guy" + "<br>");
        acks.append("&nbsp;&nbsp;Santhosh Kumar" + "<br>");
        acks.append("&nbsp;&nbsp;Dusty Phillips (Dusty)" + "<br>");
        acks.append("&nbsp;&nbsp;John Lipsky" + "<br>");
        acks.append("<br>" + i18n.getString("AboutDialogTranslations") + "<br>");
        //acks.append("&nbsp;&nbsp;" + i18n.getString("LanguageSpanish") + ": Leonardo Gallego (Sud_crow)" + "<br>");
        acks.append("&nbsp;&nbsp;" + i18n.getString("LanguagePolish") + ": Piotr Mali\u0144ski (Riklaunim)" + "<br>");
        //acks.append("&nbsp;&nbsp;" + i18n.getString("LanguageSwedish") + ": Jens Persson (Xerces2)" + "<br>");
        //acks.append("&nbsp;&nbsp;" + i18n.getString("LanguageGreek") + ": Stavros Griannouris (Stavrosg)" + "<br>");
        acks.append("</html>");
        JLabel lblAck = new JLabel(acks.toString());
        lblAck.setBorder(new EmptyBorder(6,12,6,6));
        aboutPanel.add(lblAck, BorderLayout.CENTER);
        
        JButton okButton = new JButton(i18n.getString("OKButton"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
                
            }});
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        
        this.getContentPane().add(aboutPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

}
