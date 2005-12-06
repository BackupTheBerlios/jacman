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
package andyr.jacman.prefs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import andyr.jacman.gui.EqualsLayout;
import andyr.jacman.utils.I18nManager;
import andyr.jacman.utils.JacmanUtils;

import com.l2fprod.common.model.DefaultBeanInfoResolver;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;

/**
 * Class for presenting Jacman's preferences via a typical dialog box.
 * 
 * @author Andrew Roberts
 */

public class PreferencesDialog extends JDialog {
    
    private JButtonBar toolbar;
    private JPanel content;
    private Component currentComponent;
    private I18nManager i18n;
    
    public PreferencesDialog(JFrame frame, String title, boolean modal) {
        super(frame, title, modal);
        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        setupGUI();
    }

    private JButtonBar getButtonBar() {
        if (toolbar == null) {
            toolbar = new JButtonBar(JButtonBar.VERTICAL);
            
            toolbar.setUI(new BlueishButtonBarUI());
            ButtonGroup group = new ButtonGroup();
            
            addButton(i18n.getString("PrefsDialogGeneralOptions"), "icons/jacman_logo_32x32.png", new PrefsPanel(i18n.getString("PrefsDialogGeneralOptions"), new GeneralOptions()), toolbar, group);
            addButton(i18n.getString("PrefsDialogAppearanceOptions"), "icons/jacman_logo_32x32.png", new PrefsPanel(i18n.getString("PrefsDialogAppearanceOptions"), new AppearanceOptions()), toolbar, group);
            //addButton("Misc", "icons/jacman_logo_32x32.png", makePanel("Misc"), toolbar, group);
        }
        
        return toolbar;
    }
    
    private JPanel getContentsPanel() {
        
        if (content == null) {
            content = new JPanel(new BorderLayout());
            content.add(getButtonBar(), BorderLayout.WEST);
            
            JPanel buttonPanel = new JPanel(new EqualsLayout(EqualsLayout.HORIZONTAL, EqualsLayout.RIGHT, 3));
            buttonPanel.setBorder(new EmptyBorder(3,3,3,3));
            JButton okButton = new JButton(i18n.getString("OKButton"));
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                   
                }
            
            });
            buttonPanel.add(okButton);
            
            JButton cancelButton = new JButton(i18n.getString("CancelButton"));
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                    
                }
                
            });
            buttonPanel.add(cancelButton);

            content.add(buttonPanel, BorderLayout.SOUTH);
            
        }
        
        return content;
        
    }
    
    private JPanel makePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel top = new JLabel(title);
        top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        top.setFont(top.getFont().deriveFont(Font.BOLD));
        top.setOpaque(true);
        top.setBackground(panel.getBackground().brighter());
        panel.add(top, BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(400, 300));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        return panel;
    }

    private void addButton(String title, String iconUrl,
            final Component component, JButtonBar bar, ButtonGroup group) {
        Action action = new AbstractAction(title, JacmanUtils.loadIcon(iconUrl)) {

            public void actionPerformed(ActionEvent e) {
                show(component);

            }
        };

        JToggleButton button = new JToggleButton(action);
        bar.add(button);

        group.add(button);

        if (group.getSelection() == null) {
            button.setSelected(true);
            show(component);
        }
    }
    
    private void show(Component component) {
        if (currentComponent != null) {
            getContentsPanel().remove(currentComponent);
        }
        currentComponent = component;
        
        getContentsPanel().add(currentComponent, BorderLayout.CENTER);
        getContentsPanel().revalidate();
        getContentsPanel().repaint();
        repaint();
    }
        
    private void setupGUI() {
        
        setLayout(new BorderLayout());
        getContentPane().add(getContentsPanel());
        
        setSize(600, 400);
        setVisible(true);
        
    }
    
    class PrefsPanel extends JPanel {
        
        String title;
        private JPanel pnlOptions;
        private Object options;
        //private AppearanceOptions options;
        
        public PrefsPanel(String title, Object options) {
        //public PrefsPanel(String title, AppearanceOptions options) {
            super(new BorderLayout());
            this.title = title;
            this.options = options;
            setupGUI();
        }
        
        private void setupGUI() {
            JLabel top = new JLabel(title);
            top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            top.setFont(top.getFont().deriveFont(Font.BOLD));
            top.setOpaque(true);
            top.setBackground(this.getBackground().brighter());
            this.add(top, BorderLayout.NORTH);
            this.setPreferredSize(new Dimension(400, 300));
            this.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
            this.add(getOptionsPanel(), BorderLayout.CENTER);
            
        }
        
        private JPanel getOptionsPanel() {
            
            if (pnlOptions == null) {
                pnlOptions = new JPanel(new BorderLayout());
                DefaultBeanInfoResolver resolver = new DefaultBeanInfoResolver();
                 
                BeanInfo beanInfo = resolver.getBeanInfo(options);

                PropertySheetPanel sheet = new PropertySheetPanel();
                sheet.setBorder(null);
                sheet.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
                sheet.setProperties(beanInfo.getPropertyDescriptors());
                sheet.readFromObject(options);
                sheet.setDescriptionVisible(false);
                sheet.setSortingCategories(false);
                sheet.setSortingProperties(true);
                pnlOptions.add(sheet, BorderLayout.CENTER);

                // everytime a property change, update the button with it
                PropertyChangeListener listener = new PropertyChangeListener() {
                  public void propertyChange(PropertyChangeEvent evt) {
                    Property prop = (Property)evt.getSource();
                    prop.writeToObject(options);
                    
                  }
                };
                sheet.addPropertySheetChangeListener(listener);
                
            }
            
            return pnlOptions;
        }

    }
  
}
