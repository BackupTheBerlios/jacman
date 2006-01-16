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

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLClassLoader;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import andyr.jacman.utils.I18nManager;

public class LocaleChanger extends JMenu implements ActionListener {
    
    private I18nManager i18n;
    private ComponentOrientation co;
    private JFrame parent;
    
    public LocaleChanger(JFrame parent) {
        super();
        
        this.parent = parent;
        
        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        
        setText(i18n.getString("JacmanFrameMenuViewLanguage"));
        setIcon(new ImageIcon(URLClassLoader.getSystemResource("icons/languages.png")));
        //setFont(menuFont);
        //setMnemonic(resources.getString("LanguageMenuMnemonic").charAt(0));

        ButtonGroup langGroup = new ButtonGroup();
        //String language = Locale.getDefault().getLanguage();

        // Sort the language names according to the rules specific to each
        // locale
        RuleBasedCollator rbc = (RuleBasedCollator) Collator.getInstance();
        ArrayList<String> al = new ArrayList<String>();
        //al.add(i18n.getString("LanguageChinese"));
        al.add(i18n.getString("LanguageEnglish"));
        //al.add(i18n.getString("LanguageFinnish"));
        //al.add(i18n.getString("LanguageFrench"));
        //al.add(i18n.getString("LanguageGreek"));
        al.add(i18n.getString("LanguagePolish"));
        //al.add(i18n.getString("LanguageSpanish"));
        //al.add(i18n.getString("LanguageSwedish"));

        Collections.sort(al, rbc);

        String langName = Locale.getDefault().getDisplayLanguage();
        
        for (int i = 0; i < al.size(); i++) {
            JRadioButtonMenuItem mi;
            mi = (JRadioButtonMenuItem) add(new JRadioButtonMenuItem((String) al.get(i)));

            if (langName.equalsIgnoreCase((String) al.get(i))) {
                mi.setSelected(true);
            }
            mi.addActionListener(this);
            langGroup.add(mi);
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();
            if (rb.isSelected()) {
                String selected = rb.getText();
                if (selected.equals(i18n.getString("LanguageChinese"))) {
                    Locale.setDefault(Locale.CHINESE);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } 
                else if (selected.equals(i18n.getString("LanguageEnglish"))) {
                    Locale.setDefault(Locale.US);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } 
                else if (selected.equals(i18n.getString("LanguageFinnish"))) {
                    Locale.setDefault(new Locale("fi"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageFrench"))) {
                    Locale.setDefault(Locale.FRENCH);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageGreek"))) {
                    Locale.setDefault(new Locale("el"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguagePolish"))) {
                    Locale.setDefault(new Locale("pl"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageSpanish"))) {
                    Locale.setDefault(new Locale("es"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageSwedish"))) {
                    Locale.setDefault(new Locale("sv"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
            }
            
            /*
             * Need to update the I18nManager since it will not keep the old labels
             * for the old locale
             */
            
            i18n.setLocale(Locale.getDefault());
            
            JOptionPane.showMessageDialog(parent, i18n.getString("GeneralOptionsStartInTrayDesc"));
            
        } 
        catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

    }
    
    /*public void itemStateChanged(ItemEvent e) {
        try {
            JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();
            if (rb.isSelected()) {
                String selected = rb.getText();
                if (selected.equals(i18n.getString("LanguageChinese"))) {
                    Locale.setDefault(Locale.CHINESE);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } 
                else if (selected.equals(i18n.getString("LanguageEnglish"))) {
                    System.out.println("Selected English");
                    Locale.setDefault(Locale.UK);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } 
                else if (selected.equals(i18n.getString("LanguageFinnish"))) {
                        Locale.setDefault(new Locale("fi"));
                        co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageFrench"))) {
                    Locale.setDefault(Locale.FRENCH);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageGreek"))) {
                    Locale.setDefault(new Locale("el"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguagePolish"))) {
                    System.out.println("Selected Polish");
                    Locale.setDefault(new Locale("pl"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageSpanish"))) {
                    Locale.setDefault(new Locale("es"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
                else if (selected.equals(i18n.getString("LanguageSwedish"))) {
                    Locale.setDefault(new Locale("sv"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
            }
            
            
            //System.out.println(Locale.getDefault());
            i18n.setLocale(Locale.getDefault());
            //parent.validate();
            //System.out.println(Locale.getDefault());
            
            JOptionPane.showMessageDialog(parent, i18n.getString("GeneralOptionsStartInTrayDesc"));
            
                  
            
        } 
        catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

    }*/
}

