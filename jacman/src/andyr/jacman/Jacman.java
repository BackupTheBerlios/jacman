/*
 * 
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

package andyr.jacman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.apache.commons.collections.MultiHashMap;
import org.jvnet.substance.skin.SubstanceCremeLookAndFeel;

import andyr.jacman.console.ConsoleDialog;
import andyr.jacman.gui.AboutDialog;
import andyr.jacman.gui.Cartouche;
import andyr.jacman.gui.FooterPanel;
import andyr.jacman.gui.HeaderPanel;
import andyr.jacman.gui.InstallPackageDialog;
import andyr.jacman.gui.LocaleChanger;
import andyr.jacman.gui.MoveResizeGlassPane;
import andyr.jacman.gui.RemovePackageDialog;
import andyr.jacman.gui.RollbackPackageDialog;
import andyr.jacman.gui.UpdatePackagesDialog;
import andyr.jacman.gui.tray.Tray;
import andyr.jacman.prefs.PreferencesDialog;
import andyr.jacman.utils.I18nManager;
import andyr.jacman.utils.JacmanUtils;
import ca.odell.glazedlists.EventList;

import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;

/**
 * Main Jacman class that initiates the GUI and contains essential utility methods
 * for obtaining all the pacman and package information for the entire program.
 * 
 * @author Andrew Roberts
 */

public class Jacman {

    public static PacmanConf pacmanConf;
    
    private JFrame jacmanFrame;
    private JMenuBar jacmanMenuBar;
    private JPanel mainContent;
    
    private I18nManager i18n;
    private Properties jacmanProperties;
    
    public static final String JACMAN_PROPERTIES_FILENAME = "jacman.properties";

    public final static String JACMAN_PREFS_DIR = ".jacman";
    public final static String JACMAN_NAME = "Jacman";
    public final static String JACMAN_VERSION_NUMBER = "0.4";
    public final static String JACMAN_DEV = "Andrew Roberts";
    public final static String JACMAN_URL = "http://www.andy-roberts.net/software/jacman/";
    
    public Jacman() {
        this(new File("/etc/pacman.conf"));
   
    }

    public Jacman(File confPath) {
        
            if (!initUserPrefsDirectory()) {
                System.err
                        .println("Can't create user directory. Haven't written code to cope with this yet, therefore I will abort. Sorry!");
            }

            try {

                loadProperties();
                
                Locale.setDefault(new Locale(jacmanProperties.getProperty("jacman.language", "en_US")));

            }
            catch (FileNotFoundException e1) {
                System.err
                        .println("Could not find "
                                + JACMAN_PROPERTIES_FILENAME
                                + "configuration file. Using built-in defaults instead.");

            }
            catch (IOException e1) {
                System.err.println("An error occurred when trying to load"
                        + JACMAN_PROPERTIES_FILENAME
                        + ". Using built-in defaults instead.");
                e1.printStackTrace();
            }

            try {
                pacmanConf = new PacmanConf(confPath);

            }
            catch (FileNotFoundException e) {
                System.err.println("Couldn't load pacman.conf. Exiting.");
                System.exit(1);
            }
            catch (IOException e) {
                System.err.println("Error reading pacman.conf:");
                System.err.println(e);
                System.err.println("Exiting.");
            }
        
        
        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
    }
    
    private void loadProperties() throws FileNotFoundException, IOException {
        
        //i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        //System.out.println(System.getProperty("user.home") + File.separator + JACMAN_PREFS_DIR + File.separator + JACMAN_PROPERTIES_FILENAME);
        jacmanProperties = new Properties();
        jacmanProperties.load(new FileInputStream(System.getProperty("user.home") + File.separator + JACMAN_PREFS_DIR + File.separator + JACMAN_PROPERTIES_FILENAME)); 
        
    }
    
    /**
     * This method checks for a ~/.jacman dir as this is what will contain their properties.
     * If it doesn't exist, creates it and copies default properties file to that directory,
     * and will be updated with subsequent use.
     * @return true is the user prefs directory is ready for use
     */
    private boolean initUserPrefsDirectory() {
        
        String userDir = System.getProperty("user.home");
        File jacmanUserPrefs = new File(userDir + File.separator + ".jacman");
        
        boolean ready = false; // returns true afterwards if init is successful
        
        if (!jacmanUserPrefs.exists()) {
            // Create the user directory
            if (jacmanUserPrefs.mkdir()) {
                
                // Save the default properties file to this dir
                try {
                    getDefaultProperties().store(new FileOutputStream(jacmanUserPrefs.getPath() + File.separator + JACMAN_PROPERTIES_FILENAME), "Jacman user properties");
                    //JacmanUtils.copyFile(JACMAN_PROPERTIES_FILENAME, jacmanUserPrefs.getPath() + File.separator + JACMAN_PROPERTIES_FILENAME);
                    //System.out.println("~/.jacman/" + JACMAN_PROPERTIES_FILENAME + " created.");
                    // if all went well, set ready to true
                    ready = true;
                }
                catch (FileNotFoundException e) {
                    System.err.println("Initialisation error: Couldn't find properties file.");
                    e.printStackTrace();
                }
                catch (IOException e) {
                    System.err.println("Initialisation error: Couldn't copy properties file.");
                    e.printStackTrace();
                }
            }
        }
        else {
            
            // The ~/.jacman dir exists. But we need to check that the Jacman properties file exists
            // within.
            if (new File(jacmanUserPrefs.getPath() + File.separator + JACMAN_PROPERTIES_FILENAME).exists()) {
                
                ready = true;
            }
            
        }
        
        return ready;
    }
    
    private Properties getDefaultProperties() {
        
        Properties defaultProperties = new Properties();
        
        defaultProperties.setProperty("jacman.showWindowInfo", "false");
        defaultProperties.setProperty("jacman.useAntiAliasText", "true");
        defaultProperties.setProperty("jacman.disposeMainMenu", "false");
        defaultProperties.setProperty("jacman.jacman.enableTray", "false");
        defaultProperties.setProperty("jacman.startHiddenInTray", "false");
        
        return defaultProperties;
    }
   
    public static void findAvailablePackages(EventList availablePkgList, EventList installedPackages) {
        File dbPath = pacmanConf.getDbPath();
        
        List wantedRepos = pacmanConf.getRepositories();

        List<PacmanPkg> availablePackages = new ArrayList<PacmanPkg>();
        
        File[] repoDirs = dbPath.listFiles(directoryFilter);
        
        for (int i = 0; i < repoDirs.length; i++) {
            if (wantedRepos.contains(repoDirs[i].getName())) {
                File[] repoPackages = repoDirs[i].listFiles(directoryFilter);
                for (int j = 0; j < repoPackages.length; j++) {
                    try {
                        PacmanPkg currentPkg = new PacmanPkg(repoPackages[j]);
                        if (!currentPkg.getName().trim().equals("")) {
                            int index = Collections.binarySearch(installedPackages, currentPkg, new PackageNameComparitor());
                            if (index >= 0) {
                                currentPkg.setInstalledVersion(((PacmanPkg)installedPackages.get(index)).getVersion());
                            }
                            
                            index = Collections.binarySearch(availablePackages, currentPkg);
                            if (index < 0) {
                                availablePackages.add((index + 1) * -1, currentPkg);
                            }
                        }
                        else {
                           System.out.println("Package " + currentPkg.getName() + "(" + currentPkg.getVersion() + ") already exists.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        availablePkgList.addAll(availablePackages);


    }
    
    static FileFilter directoryFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory();
        }
    };
    
    static FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isFile();
        }
    };
    
    public static void findInstalledForUpdatePackages(EventList availablePkgList, EventList installedPackages) {
        File dbPath = pacmanConf.getDbPath();
        
        List wantedRepos = pacmanConf.getRepositories();

        List<PacmanPkg> availablePackages = new ArrayList<PacmanPkg>();
        
        File[] repoDirs = dbPath.listFiles(directoryFilter);
        
        for (int i = 0; i < repoDirs.length; i++) {
            if (wantedRepos.contains(repoDirs[i].getName())) {
                File[] repoPackages = repoDirs[i].listFiles(directoryFilter);
                for (int j = 0; j < repoPackages.length; j++) {
                    try {
                        PacmanPkg currentPkg = new PacmanPkg(repoPackages[j]);
                        
                        if (!currentPkg.getName().trim().equals("")) {
                            int index = Collections.binarySearch(installedPackages, currentPkg, new PackageNameComparitor());
                            if (index >= 0) {
                                currentPkg.setInstalledVersion(((PacmanPkg)installedPackages.get(index)).getVersion());
                            }
                            if (currentPkg.isInstalled()) {                            
                                index = Collections.binarySearch(availablePackages, currentPkg);
                                if (index < 0) {
                                    availablePackages.add((index + 1) * -1, currentPkg);
                                }
                            }
                        }
                        else {
                           System.err.println("Package " + currentPkg.getName() + "(" + currentPkg.getVersion() + ") already exists.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        availablePkgList.addAll(availablePackages);


    }
    
    public static void findInstalledPackages(EventList installedPkgsList) {
        File dbPath = pacmanConf.getDbPath();
        ArrayList<PacmanPkg> installedPackages = new ArrayList<PacmanPkg>();

        File installedPath = new File(dbPath.getPath() + "/local");
        File[] repoPackages = installedPath.listFiles(directoryFilter);
        for (int j = 0; j < repoPackages.length; j++) {
            try {

                PacmanPkg currentPkg = new InstalledPacmanPkg(repoPackages[j]);
                if (!currentPkg.getName().trim().equals("")) {
                    int index = Collections.binarySearch(installedPackages, currentPkg);
                    installedPackages.add((index + 1) * -1, currentPkg);
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        installedPkgsList.addAll(installedPackages);

    }
    
    
    
    public static void findRollbackPackages(EventList rollbackPkgsList, MultiHashMap map) {
        //rollbackPkgsList = new BasicEventList();
     
        if (map == null) {
            map = new MultiHashMap();    
        }
        
        
        File[] cachedPackages = pacmanConf.getCachePath().listFiles(fileFilter);
        for (int j = 0; j < cachedPackages.length; j++) {
     
                // Parse file to separate pkgname and version
                String filename = cachedPackages[j].getName();
                
                // Check that the filename isn't blank
                
                if (!filename.trim().equals("")) {
                
                    filename = filename.replace(".pkg.tar.gz", "");

                    int revIndex = filename.lastIndexOf("-");
                    String revision = filename.substring(revIndex+1);
                    
                    int verIndex = filename.lastIndexOf("-", revIndex-1);
                    
                    String version = filename.substring(verIndex+1, revIndex);
                    
                    String name = filename.substring(0, verIndex);
                    
                    // add name and ver to map.
                    map.put(name, version+"-"+revision);
                }
                
        }
        
        File dbPath = pacmanConf.getDbPath();
        ArrayList<PacmanPkg> rollbackPackages = new ArrayList<PacmanPkg>();

        File installedPath = new File(dbPath.getPath() + "/local");
        File[] repoPackages = installedPath.listFiles(directoryFilter);
        for (int j = 0; j < repoPackages.length; j++) {
            try {
                // Parse file to separate pkgname and version
                String filename = repoPackages[j].getName();
                
                // Check that the filename isn't blank
                if (!filename.trim().equals("")) {
                    PacmanPkg currentPkg = new InstalledPacmanPkg(repoPackages[j]);
                    if (map.containsKey(currentPkg.getName())) {
                        
                        if (map.getCollection(currentPkg.getName()).size() > 1) {
                            
                            int index = Collections.binarySearch(rollbackPackages, currentPkg);
                            rollbackPackages.add((index + 1) * -1, currentPkg);
                        }
                    }
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        rollbackPkgsList.addAll(rollbackPackages);
        
    }

    public JMenuBar getJacmanMenus() {

        if (jacmanMenuBar == null) {

            jacmanMenuBar = new JMenuBar();

            JMenu fileMenu = new JMenu(i18n.getString("JacmanFrameMenuFile"));
            
            JMenuItem filePrefsMenuItem = new JMenuItem(i18n.getString("JacmanFrameMenuFilePrefs"));
            filePrefsMenuItem.setIcon(JacmanUtils.loadIcon("icons/preferences_16x16.png"));
            filePrefsMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new PreferencesDialog(jacmanFrame, i18n.getString("PrefsDialogTitle"), true, jacmanProperties);
                    
                }
                
            });
            
            fileMenu.add(filePrefsMenuItem);

            JMenuItem fileExitMenuItem = new JMenuItem(i18n.getString("JacmanFrameMenuFileQuit"), JacmanUtils.loadIcon("icons/exit.png"));
            fileExitMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    exitGUI();
                    
                }
            });
            fileMenu.add(fileExitMenuItem);

            jacmanMenuBar.add(fileMenu);
            
            JMenu viewMenu = new JMenu(i18n.getString("JacmanFrameMenuView"));
            JMenu viewLanguageMenu = new JMenu(i18n.getString("JacmanFrameMenuViewLanguage"));
            viewLanguageMenu.setIcon(JacmanUtils.loadIcon("icons/languages.png"));
            
            viewMenu.add(new LocaleChanger(getFrame()));
            
            jacmanMenuBar.add(viewMenu);
            
            
            JMenu helpMenu = new JMenu(i18n.getString("JacmanFrameMenuHelp"));

            JMenuItem helpAboutMenuItem = new JMenuItem(i18n.getString("JacmanFrameMenuHelpAbout"), JacmanUtils.loadIcon("icons/jacman_logo_small.png"));
            
            helpAboutMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new AboutDialog(jacmanFrame, i18n.getString("AboutDialogTitle"), true);
                }
            });
            helpMenu.add(helpAboutMenuItem);

            jacmanMenuBar.add(helpMenu);
        }
        jacmanMenuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        return jacmanMenuBar;

    }
    
    private void exitGUI() {
        
        try {
            
            File out = new File(System.getProperty("user.home") + File.separator + JACMAN_PREFS_DIR + File.separator + JACMAN_PROPERTIES_FILENAME);
            
            jacmanProperties.put("jacman.language", Locale.getDefault().getLanguage());
            jacmanProperties.store(new FileOutputStream(out), "Jacman user properties");
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            jacmanFrame.dispose();
        }
    }
    
    public JPanel getMainContent() {
        
        if (mainContent == null) {
            
            mainContent = new JPanel(new BorderLayout());
            
            JPanel headerPanel = new JPanel(new BorderLayout());
            
            HeaderPanel enTete = new HeaderPanel("icons/jacman_logo.png",
                    i18n.getString("JacmanFrameHeaderTitle") + " " + JACMAN_VERSION_NUMBER,
                    i18n.getString("JacmanFrameHeaderSub1"),
                    i18n.getString("JacmanFrameHeaderSub2"));
            
            headerPanel.add(BorderLayout.NORTH, enTete);
            headerPanel.add(BorderLayout.SOUTH, new JSeparator(JSeparator.HORIZONTAL));
            headerPanel.setBorder(new EmptyBorder(0, 0, 6, 0));

            JPanel buttons = new JPanel(new GridBagLayout());
            GridBagConstraints gridBagConstraints;
            
            Cartouche c;
            c = new Cartouche(JacmanUtils.loadIcon("icons/install.png"), i18n.getString("JacmanFrameInstallPackagesBtn"));
            
            c.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            
                            InstallPackageDialog ipd = new InstallPackageDialog(jacmanFrame, i18n.getString("InstallDialogTitle"), false, jacmanProperties);
                            
                        }
                    });
                }
            });
            int y = 0;
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y++;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTH;
            gridBagConstraints.insets = new Insets(12, 0, 0, 0);
            buttons.add(c, gridBagConstraints);

            c = new Cartouche(JacmanUtils.loadIcon("icons/upgrade.png"), i18n.getString("JacmanFrameUpdatePackagesBtn"));
            buttons.add(c);
            buttons.add(Box.createVerticalStrut(12));
            c.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            UpdatePackagesDialog upd = new UpdatePackagesDialog(jacmanFrame, i18n.getString("UpdateDialogTitle"), false, jacmanProperties);
                            upd.postInit();
                        }
                    });
                }
            });
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y++;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTH;
            gridBagConstraints.insets = new Insets(12, 0, 0, 0);
            buttons.add(c, gridBagConstraints);

            c = new Cartouche(JacmanUtils.loadIcon("icons/remove.png"), i18n.getString("JacmanFrameRemovePackagesBtn"));
            buttons.add(c);
            buttons.add(Box.createVerticalStrut(12));
            c.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            RemovePackageDialog rpd = new RemovePackageDialog(jacmanFrame, i18n.getString("RemoveDialogTitle"), false, jacmanProperties);
                            rpd.postInit();
                        }
                    });
                }
            });
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y++;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTH;
            gridBagConstraints.insets = new Insets(12, 0, 0, 0);
            buttons.add(c, gridBagConstraints);

            c = new Cartouche(JacmanUtils.loadIcon("icons/sync.png"), i18n.getString("JacmanFrameRollbackPackagesBtn"));
            buttons.add(c);
            buttons.add(Box.createVerticalStrut(12));
            c.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            RollbackPackageDialog rpd = new RollbackPackageDialog(jacmanFrame, i18n.getString("RollbackDialogTitle"), false, jacmanProperties);
                         }
                    });
                }
            });
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y++;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTH;
            gridBagConstraints.insets = new Insets(12, 0, 0, 0);
            buttons.add(c, gridBagConstraints);
            
            c = new Cartouche(JacmanUtils.loadIcon("icons/optimise_48x48.png"), i18n.getString("JacmanFrameOptimiseBtn"));
            buttons.add(c);
            buttons.add(Box.createVerticalStrut(12));
            c.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            String[] command = {"pacman-optimize"};
                            new ConsoleDialog(command, jacmanFrame, i18n.getString("JacmanFrameOptimiseBtn"), true);
                         }
                    });
                }
            });
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = y++;
            gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTH;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new Insets(12, 0, 0, 0);
            buttons.add(c, gridBagConstraints);
            
            Box rangee = Box.createHorizontalBox();
            
            JLabel copyright;
            rangee.add(copyright = new JLabel("© 2006 Andrew Roberts and the Jacman Team"));
            copyright.setFont(copyright.getFont().deriveFont(Font.BOLD));
            rangee.setBorder(new EmptyBorder(3, 3, 3, 3));
                      
            rangee.add(Box.createHorizontalGlue());
            
            JButton quitButton;
            quitButton = new JButton(i18n.getString("JacmanFrameMenuFileQuit"), JacmanUtils.loadIcon("icons/exit.png"));
            
            rangee.add(quitButton);
            
            quitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    exitGUI();
                    
                }
            });
            FooterPanel pied = new FooterPanel();
            pied.setLayout(new BorderLayout());
            pied.add(BorderLayout.CENTER, rangee);

            JPanel footerPanel = new JPanel(new BorderLayout());
            footerPanel.add(BorderLayout.CENTER, pied);
            footerPanel.add(BorderLayout.NORTH, new JSeparator(JSeparator.HORIZONTAL));
            footerPanel.setBorder(new EmptyBorder(6, 0, 0, 0));

            
            mainContent.add(BorderLayout.NORTH, headerPanel);
            mainContent.add(BorderLayout.CENTER, buttons);
            mainContent.add(BorderLayout.SOUTH, footerPanel);
        }
        
        return mainContent;
    }
        
    public void createGUI() {

        //configureJGoodiesLAF();
        configureSubstanceLAF();
        
        jacmanFrame = new JFrame("Jacman - " + JACMAN_VERSION_NUMBER);
        
        jacmanFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jacmanFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                exitGUI();
            }

        });

        jacmanFrame.setJMenuBar(getJacmanMenus());
        
        jacmanFrame.getContentPane().add(getMainContent());

        jacmanFrame.setSize(800,600);
        jacmanFrame.setLocationRelativeTo(null); // centre frame on screen
        jacmanFrame.setIconImage(JacmanUtils.loadIcon("icons/jacman_logo_small.png").getImage());
        
        
        if (jacmanProperties.getProperty("jacman.showWindowInfo", "false").equals("true")) {    
            MoveResizeGlassPane.registerFrame(jacmanFrame);
        }   
        
        if (jacmanProperties.getProperty("jacman.enableTray", "false").equals("true")) {
        	new Tray(jacmanFrame, jacmanProperties);
            if (jacmanProperties.getProperty("jacman.startHiddenInTray", "false").equals("false"))
                jacmanFrame.setVisible(true);
        }
        
    }
    
    private void configureSubstanceLAF() {
        try {
            SubstanceCremeLookAndFeel laf = new SubstanceCremeLookAndFeel();
            //getFrame().getRootPane().putClientProperty(SubstanceLookAndFeel. NO_EXTRA_ELEMENTS, Boolean.TRUE);
            //laf.setCurrentTheme(new SubstanceCremeTheme());
            //-Dsubstancelaf.noExtraElements
            UIManager.setLookAndFeel(laf);
            
        }
        catch (UnsupportedLookAndFeelException e) {
            System.err.println("Couldn't set Look and Feel to Substance.");
            e.printStackTrace();
        }
        
    }

    private void configureJGoodiesLAF() {
        Settings settings = Settings.createDefault();
        Options.setDefaultIconSize(new Dimension(18, 18));

        UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, settings.isUseSystemFonts());
        Options.setGlobalFontSizeHints(settings.getFontSizeHints());
        Options.setUseNarrowButtons(settings.isUseNarrowButtons());

        Options.setTabIconsEnabled(settings.isTabIconsEnabled());
        
        UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY, settings.isPopupDropShadowEnabled());

        LookAndFeel selectedLaf = settings.getSelectedLookAndFeel();
        if (selectedLaf instanceof PlasticLookAndFeel) {
            PlasticLookAndFeel.setMyCurrentTheme(settings.getSelectedTheme());
            PlasticLookAndFeel.setTabStyle(settings.getPlasticTabStyle());
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(settings.isPlasticHighContrastFocusEnabled());
        } else if (selectedLaf.getClass() == MetalLookAndFeel.class) {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }

        JRadioButton radio = new JRadioButton();
        radio.getUI().uninstallUI(radio);
        JCheckBox checkBox = new JCheckBox();
        checkBox.getUI().uninstallUI(checkBox);

        try {
            UIManager.setLookAndFeel(selectedLaf);
        } catch (Exception e) {
            System.err.println("Could not change the look and feel: " + e);
        }
    }
    
    public JFrame getFrame() {
        return jacmanFrame;
    }
    
    public void setVisible(boolean visible) {
        jacmanFrame.setVisible(visible);
    }

    public static void main(final String[] args) {

        // EventDispatchThreadHangMonitor.initMonitoring();
        
        /*
         * This is a bit of a hack. To allow anti-aliasing, I must read the
         * properties file before using any Swing classes and then set the Swing
         * aatext property accordingly.
         */
        
        try {
            
            System.setProperty("substancelaf.noExtraElements", "");

            String useaa = "true";
            
            if (new File(System.getProperty("user.home") + File.separator + JACMAN_PROPERTIES_FILENAME).exists()) {
               
                Properties jacmanProperties = new Properties();
                jacmanProperties.load(URLClassLoader.getSystemResourceAsStream(JACMAN_PROPERTIES_FILENAME));
                useaa = jacmanProperties.getProperty("jacman.useAntiAliasText", "true");
                
                
            }
            if (useaa.equals("true")) {
                System.setProperty("swing.aatext", "true");
            }
        }
        catch (FileNotFoundException e1) {
            System.err.println("Could not find " + JACMAN_PROPERTIES_FILENAME
                    + "configuration file. Using built-in defaults instead.");

        }
        catch (IOException e1) {
            System.err.println("An error occurred when trying to load"
                    + JACMAN_PROPERTIES_FILENAME
                    + ". Using built-in defaults instead.");
            e1.printStackTrace();
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                Jacman jm;

                // I hate checking command line args! Need to find a decent
                // (lightweight) package
                // to avoid all these ifs.

                if (args.length == 0) {
                    jm = new Jacman();

                }
                else {
                    if (args.length != 2) {
                        throw new IllegalArgumentException(
                                "Incorrect number of arguments. Expected two.");
                    }

                    String filename = "";

                    if (!args[0].equals("-c")) {
                        throw new IllegalArgumentException(args[0]
                                + ": argument not recognised.");
                    }
                    else {
                        filename = args[1];
                    }

                    if (!filename.trim().equals("")) {
                        throw new IllegalArgumentException(
                                "No filename specified");
                    }

                    File conf = new File(filename);

                    if (!conf.exists()) {
                        throw new IllegalArgumentException(args[1]
                                + " does not exist! Exiting.");
                    }

                    jm = new Jacman(conf);
                }

                jm.createGUI();
                //if (jacmanProperties.getProperty("jacman.startHiddenInTray", "false").equals("false"))
                jm.setVisible(true);
                
                
            }
        });

    }


}
