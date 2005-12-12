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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import andyr.jacman.InstallListFilter;
import andyr.jacman.InstalledPacmanPkg;
import andyr.jacman.Jacman;
import andyr.jacman.PackageComparitor;
import andyr.jacman.PackageNameComparitor;
import andyr.jacman.PacmanPkg;
import andyr.jacman.RemoveOptions;
import andyr.jacman.SwingWorker;
import andyr.jacman.console.ConsoleDialog;
import andyr.jacman.utils.I18nManager;
import andyr.jacman.utils.JTableHelper;
import andyr.jacman.utils.JacmanUtils;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

import com.l2fprod.common.model.DefaultBeanInfoResolver;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

/**
 * A dialog for the removal of Pacman packages.
 * 
 * @author Andrew Roberts
 *
 */

public class RemovePackageDialog extends JDialog {

    private JSplitPane horizontalSplit;
    private JSplitPane verticalSplit;
    private JPanel pnlRemoveOptions;
    private JPanel pnlPackageDescPane;
    private JPanel pnlPackageList;
    private JTextField txtSearch = new JTextField();
    
    private ElegantPanel optionsView;
    private ElegantPanel descView;
    private ElegantPanel packageView;
    
    private JTable tblPackageList;
    
    private JTree treeDepends;
    
    private JLabel packageName = new JLabel();
    private JLabel packageDesc = new JLabel();
    private JLabel installedVersion = new JLabel();
    private JLabel repository = new JLabel();
    private JLabel size = new JLabel();
    private JLabel md5sum = new JLabel();
    
    private EventList packageEventList = new BasicEventList();
    private SortedList sortedPackages;
    private EventTableModel packagesTableModel;
    private CheckableTableFormat checkableTableFormat;

    private final RemoveOptions options = new RemoveOptions();
    
    private I18nManager i18n;
    private Properties jacmanProperties;
    
    public static final int PACMAN_RAN = 1;
    private int returnVal = -1;
    
    public RemovePackageDialog(Frame parent, String title, boolean modal, Properties properties) {
        super(parent, title, modal);
        
        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        jacmanProperties = properties;
         
        sortedPackages = new SortedList(packageEventList, new PackageComparitor());
        FilterList textFilteredIssues = new FilterList(sortedPackages, new TextComponentMatcherEditor(txtSearch, new PackageTextFilterator()));
        
        String[] propertyNames = {"name", "version", "description", "size"};
        String[] columnNames = { i18n.getString("PackageTableColumnName"),
                i18n.getString("PackageTableColumnInstalledVer"),
                i18n.getString("PackageTableColumnDescription"),
                i18n.getString("PackageTableColumnSize") };
        
        checkableTableFormat = new CheckableTableFormat(new BeanTableFormat(InstalledPacmanPkg.class, propertyNames, columnNames));
        packagesTableModel = new EventTableModel(textFilteredIssues, checkableTableFormat);
        
        this.setLocation(parent.getLocation());
        setupGUI();
        
        final InfiniteProgressPanel pane = new InfiniteProgressPanel(i18n.getString("LoadingPackagesMessage"));
        setGlassPane(pane);
        validate();
        pane.start();
        SwingWorker worker = new SwingWorker() {
            public Object construct() {
                
                Jacman.findInstalledPackages(packageEventList);
                return "done";
            }
            public void finished() {
                JacmanUtils.setOptimalTableWidth(getPackageListTable());
                pane.stop();
            }
        };
        worker.start();
        
    }
    
    private JPanel getRemoveOptionsPanel() {
        
        if (pnlRemoveOptions == null) {
            pnlRemoveOptions = new JPanel(new BorderLayout());
            DefaultBeanInfoResolver resolver = new DefaultBeanInfoResolver();
             
            BeanInfo beanInfo = resolver.getBeanInfo(options);

            PropertySheetPanel sheet = new PropertySheetPanel();
            sheet.setMode(PropertySheet.VIEW_AS_CATEGORIES);
            sheet.setProperties(beanInfo.getPropertyDescriptors());
            sheet.readFromObject(options);
            sheet.setDescriptionVisible(true);
            sheet.setSortingCategories(true);
            sheet.setSortingProperties(true);
            pnlRemoveOptions.add(sheet, BorderLayout.CENTER);

            // everytime a property change, update the button with it
            PropertyChangeListener listener = new PropertyChangeListener() {
              public void propertyChange(PropertyChangeEvent evt) {
                Property prop = (Property)evt.getSource();
                prop.writeToObject(options);
                
              }
            };
            sheet.addPropertySheetChangeListener(listener);
            
        }
        
        return pnlRemoveOptions;
    }
    
    private JPanel getPackageDescPanel() {
   
        if (pnlPackageDescPane == null) {
            
            JPanel pnlPackageDesc = new JPanel();
            
            GridBagConstraints gridBagConstraints;
            
            JLabel lblPackageName = new JLabel(i18n.getString("DescPackageName"));
            JLabel lblPackageDesc = new JLabel(i18n.getString("DescPackageDesc"));
            JLabel lblInstalledVersion = new JLabel(i18n.getString("DescInstalledVersion"));
            JLabel lblSize = new JLabel(i18n.getString("DescSize"));
            
            pnlPackageDesc.setLayout(new GridBagLayout());
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(0, 0, 0, 3);
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            pnlPackageDesc.add(lblPackageName, gridBagConstraints);

            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(0, 3, 0, 0);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 1.0;
            pnlPackageDesc.add(packageName, gridBagConstraints);

            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.insets = new Insets(3, 0, 0, 0);
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            pnlPackageDesc.add(lblPackageDesc, gridBagConstraints);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.insets = new Insets(3, 3, 0, 0);
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 1.0;
            pnlPackageDesc.add(packageDesc, gridBagConstraints);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(3, 3, 0, 0);
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            pnlPackageDesc.add(lblInstalledVersion, gridBagConstraints);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.insets = new Insets(3, 3, 0, 0);
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            pnlPackageDesc.add(installedVersion, gridBagConstraints);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.insets = new Insets(3, 3, 0, 0);
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            pnlPackageDesc.add(lblSize, gridBagConstraints);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            gridBagConstraints.insets = new Insets(3, 3, 0, 0);
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            pnlPackageDesc.add(size, gridBagConstraints);
            
            
            pnlPackageDescPane = new JPanel(new BorderLayout());
            JScrollPane scroll = new JScrollPane(pnlPackageDesc);
            pnlPackageDescPane.add(scroll, BorderLayout.CENTER);
            
        }
        
        return pnlPackageDescPane;
        
    }
    
    private JTree getDependsTree() {

        if (treeDepends == null) {
            treeDepends = new JTree(new DefaultMutableTreeNode());
            treeDepends.setOpaque(false);

            expandTree(treeDepends);
          
            treeDepends.setCellRenderer(new DependsTreeCellRenderer());

            treeDepends.setEnabled(false);
        }

        return treeDepends;
    }

    private void expandTree(JTree tree) {
        
        if (tree != null) {
        
            for (int row = 0; row < tree.getRowCount(); row++) {
                tree.expandRow(row);
            }
        }
    }
    
    private JPanel getPackageListPanel() {
        
        if (pnlPackageList == null) {
            pnlPackageList = new JPanel(new BorderLayout());
            
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            
            JLabel lblSearch = new JLabel(i18n.getString("SearchLabel"));
            lblSearch.setLabelFor(txtSearch);
            
            JPanel filterPanel = new JPanel(new GridBagLayout());
            filterPanel.setBorder(new EmptyBorder(3,0,3,0));
            ImageIcon icon = new ImageIcon(URLClassLoader.getSystemResource("icons/erase.png"));
            JButton eraseSearch = new JButton(icon);
            eraseSearch.setToolTipText(i18n.getString("SearchResetButtonTooltip"));
            eraseSearch.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    txtSearch.setText("");
                    
                }
                
            });
            Dimension d = eraseSearch.getPreferredSize();
            d.setSize(icon.getIconWidth() * 2, txtSearch.getPreferredSize().getHeight());
            eraseSearch.setPreferredSize(d);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
            
            
            filterPanel.add(eraseSearch, gridBagConstraints);
            filterPanel.add(lblSearch, gridBagConstraints);
            
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            
            filterPanel.add(txtSearch, gridBagConstraints);
            
            pnlPackageList.add(filterPanel, BorderLayout.NORTH);
            pnlPackageList.add(new JScrollPane(getPackageListTable()), BorderLayout.CENTER);
        }
        
        return pnlPackageList;
    }

    private void setupGUI() {

        initSplitPanes();
        
        getContentPane().add(horizontalSplit, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new EqualsLayout(EqualsLayout.HORIZONTAL, EqualsLayout.RIGHT, 3));
        buttonPanel.setBorder(new EmptyBorder(3,3,3,3));
        JButton removeButton = new JButton(i18n.getString("RemoveDialogRemoveButton"));
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               
                List<String> commandArgs = new ArrayList<String>();
                commandArgs.add("pacman");
                //commandArgs.add("--noconfirm");
                commandArgs.addAll(options.getRemoveOptionsArgs());
                
                List selectedPackages = checkableTableFormat.getSelection();
                for (Iterator p = selectedPackages.iterator(); p.hasNext();) {
                    commandArgs.add(((PacmanPkg)p.next()).getName());
                }
                
                String[] command = new String[commandArgs.size()];
                
                for (int i = 0; i < commandArgs.size(); i++) {
                    command[i] = (String)commandArgs.get(i);
                     
                }
                
                new ConsoleDialog(command, (Dialog)SwingUtilities.getRoot(RemovePackageDialog.this), i18n.getString("RemoveDialogConsoleTitle"), true);
                returnVal = RemovePackageDialog.PACMAN_RAN;
                String dispose = jacmanProperties.getProperty("jacman.disposeMainMenu", "true");
                if (dispose.equals("true") && returnVal == RemovePackageDialog.PACMAN_RAN) {
                    ((Frame)getParent()).dispose();
                }
                else {
                    ((Frame)getParent()).setVisible(true);
                }
                dispose();
            }
        
        });
        buttonPanel.add(removeButton);
        
        JButton closeButton = new JButton(i18n.getString("CloseButton"));
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
                
            }
            
        });
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(800,600);
        
        setVisible(true);
        postInit();

    }
    

    private void createViews() {
       
        optionsView = new ElegantPanel(i18n.getString("RemoveDialogRemoveOptionsTitle"), getRemoveOptionsPanel());
        descView = new ElegantPanel(i18n.getString("RemoveDialogDescriptionTitle"), getPackageDescPanel());
        packageView = new ElegantPanel(i18n.getString("RemoveDialogRemovablePackagesTitle"), getPackageListPanel());
    }
  
    private void initSplitPanes() {
        createViews();
        
        verticalSplit = createSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplit.setTopComponent(packageView);
        verticalSplit.setBottomComponent(descView);
        
        horizontalSplit = createSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        horizontalSplit.setRightComponent(verticalSplit);
        horizontalSplit.setLeftComponent(optionsView);
        
        postInit();
    }
    
    public void postInit() {
        
        verticalSplit.setDividerLocation(0.6d);
        horizontalSplit.setDividerLocation(0.31d);
        
    }

    private static JSplitPane createSplitPane(int orientation) {
        JSplitPane split = new JSplitPane(orientation);
        // remove the border from the split pane
        split.setBorder(null);
         
        // set the divider size for a more reasonable, less bulky look 
        split.setDividerSize(3);

        // check the UI.  If we can't work with the UI any further, then
        // exit here.
        if (!(split.getUI() instanceof BasicSplitPaneUI))
           return split;

        //  grab the divider from the UI and remove the border from it
        BasicSplitPaneDivider divider =
                       ((BasicSplitPaneUI) split.getUI()).getDivider();
        if (divider != null)
           divider.setBorder(null);

        return split;
    }

    protected JTable getPackageListTable() {

        if (tblPackageList == null) {
            tblPackageList = new JTable(packagesTableModel);
            TableComparatorChooser tableSorter = new TableComparatorChooser(tblPackageList, sortedPackages, true);
            //tblPackageList = new JTable(new PackageListTableModel(columns,0));
            tblPackageList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tblPackageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            ListSelectionModel rowSM = tblPackageList.getSelectionModel();
            
            rowSM.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {

                    //Ignore extra messages.
                    if (e.getValueIsAdjusting()) return;

                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (!lsm.isSelectionEmpty()) {

                        int selectedRow = lsm.getMinSelectionIndex();
                        
                        String currentPackage = (String) tblPackageList.getValueAt(selectedRow, (tblPackageList.getColumnModel().getColumn(1)).getModelIndex());
                        updateDescriptionPanel(currentPackage);
                        
                    }
                }
            
            });
            
        }
        return tblPackageList;
    }


    private void setOptimalTableWidth() {
        
        for (int col = 0; col < getPackageListTable().getColumnCount(); col++) {
            JTableHelper.setOptimalColumnWidth(getPackageListTable(), col);
        }
    }
    
    private void updateDescriptionPanel(String pkgName) { 
        //lblPackageDesc.setText(packageName);
        //pnlPackageDesc = createPackageDescPanel(pkgName);
        
        if (!pkgName.trim().equals("")) {
            // Get the package from the package list and update the labels accordingly.
            InstalledPacmanPkg tmpPkg = new InstalledPacmanPkg();
            tmpPkg.setName(pkgName); 
            int index = Collections.binarySearch(packageEventList, tmpPkg, new PackageNameComparitor());
            if (index >= 0) {
                InstalledPacmanPkg p = (InstalledPacmanPkg)packageEventList.get(index);
                
                packageName.setText(p.getName());
                packageDesc.setText(p.getDescription());
                //installDate.setText(p.getInstallDate());
                installedVersion.setText(p.getVersion());
                size.setText(Long.toString(p.getSize()));
                repository.setText(p.getRepository());
                md5sum.setText(p.getMd5sum());
                //packager.setText(p.getPackager());
                //licence.setText(p.getLicence());
                
                /*DefaultMutableTreeNode node = getDepends(pkgName);
                if (node == null) {
                    System.out.println("Node was empty.");
                   treeDepends = new JTree(new DefaultMutableTreeNode());
                }
                else {
                    System.out.println("Node was not null");
                    treeDepends.setModel(new DefaultTreeModel(node));
                    expandTree(treeDepends);
                    //treeDepends = new JTree(node);
                }*/
                
            }
            else {
                System.err.println("Couldn't find " + pkgName + " in packageEventList");
            }
        }
        
    }
    
    DefaultMutableTreeNode getDepends(String pkgName) {
        PacmanPkg tmpPkg = new PacmanPkg();
        tmpPkg.setName(pkgName);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(pkgName);
        int index = Collections.binarySearch(packageEventList, tmpPkg,
                new PackageNameComparitor());
        if (index >= 0) {
            PacmanPkg p = (PacmanPkg) packageEventList.get(index);

            List deps = p.getDepends();
            

            for (Iterator i = deps.iterator(); i.hasNext();) {
                DefaultMutableTreeNode branch = getDepends((String) i.next());
                if (branch != null) {
                    node.add(branch);
                }

            }

            return node;

        }
        else {
            return node;
        }

    }
    
    class DependsTreeCellRenderer extends JLabel implements TreeCellRenderer {

        public DependsTreeCellRenderer() {
            super();
            setOpaque(false);
            setBackground(null);
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus)

        {
            setFont(tree.getFont());
            String stringValue = tree.convertValueToText(value, sel, expanded,
                    leaf, row, hasFocus);

            setEnabled(tree.isEnabled());
            setText(stringValue);
            if (sel)
                setForeground(Color.blue);
            else
                setForeground(Color.black);
            if (leaf) {
                setIcon(UIManager.getIcon("Tree.leafIcon"));
            }
            else if (expanded) {
                setIcon(UIManager.getIcon("Tree.openIcon"));
            }
            else {
                setIcon(UIManager.getIcon("Tree.closedIcon"));
            }
            return this;
        }
    }

}
