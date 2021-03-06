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

package andyr.jacman.gui.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import andyr.jacman.console.ConsoleDialog;
import andyr.jacman.gui.AboutDialog;
import andyr.jacman.gui.InstallPackageDialog;
import andyr.jacman.gui.RemovePackageDialog;
import andyr.jacman.gui.RollbackPackageDialog;
import andyr.jacman.gui.UpdatePackagesDialog;
import andyr.jacman.utils.I18nManager;
import andyr.jacman.utils.JacmanUtils;

public class Tray implements ActionListener {

	private SystemTray tray = SystemTray.getDefaultSystemTray();
	private JFrame frame;
	private I18nManager i18n;
    private Properties jacmanProperties;

	public Tray(final JFrame frame, Properties properties) {
		this.frame = frame;
		i18n = I18nManager.getInstance();
        jacmanProperties = properties;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Integer
				.parseInt(System.getProperty("java.version").substring(2, 3)) >= 5)
			System.setProperty("javax.swing.adjustPopupLocationToFit", "false");

		/* TRAY POPUP MENU */
		JPopupMenu menu = new JPopupMenu();

		// INSTALL
		JMenuItem installPackages = new JMenuItem();
		installPackages.setText(I18nManager.getInstance().getString(
				"JacmanFrameInstallPackagesBtn"));
		installPackages.setIcon(JacmanUtils.loadIcon("icons/install_16x16.png"));
		installPackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new InstallPackageDialog(frame, i18n
								.getString("InstallDialogTitle"), false, jacmanProperties);
					}
				});
			}
		});
		menu.add(installPackages);

		// UPDATE
		JMenuItem updatePackages = new JMenuItem();
		updatePackages.setText(i18n.getString("JacmanFrameUpdatePackagesBtn"));
		updatePackages.setIcon(JacmanUtils.loadIcon("icons/upgrade_16x16.png"));
		updatePackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UpdatePackagesDialog upd = new UpdatePackagesDialog(
								frame, i18n.getString("UpdateDialogTitle"),
								false, jacmanProperties);
						upd.postInit();
					}
				});
			}
		});
		menu.add(updatePackages);

		// REMOVE
		JMenuItem removePackages = new JMenuItem();
		removePackages.setText(i18n.getString("JacmanFrameRemovePackagesBtn"));
		removePackages.setIcon(JacmanUtils.loadIcon("icons/remove_16x16.png"));
		removePackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						RemovePackageDialog rpd = new RemovePackageDialog(frame,
								i18n.getString("RemoveDialogTitle"), false, jacmanProperties);
						rpd.postInit();
					}
				});
			}
		});
		menu.add(removePackages);

		// ROLLBACK
		JMenuItem rollbackPackages = new JMenuItem();
		rollbackPackages.setText(i18n
				.getString("JacmanFrameRollbackPackagesBtn"));
		rollbackPackages.setIcon(JacmanUtils.loadIcon("icons/sync_16x16.png"));
		rollbackPackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new RollbackPackageDialog(frame, i18n
								.getString("RollbackDialogTitle"), false, jacmanProperties);
					}
				});
			}
		});
		menu.add(rollbackPackages);
		
		//OPTIMIZE
		JMenuItem optimizePackages = new JMenuItem();
		optimizePackages.setText(i18n.getString("JacmanFrameOptimiseBtn"));
		optimizePackages.setIcon(JacmanUtils.loadIcon("icons/optimise_16x16.png"));
		optimizePackages.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        String[] command = {"pacman-optimize"};
                        new ConsoleDialog(command,frame, i18n.getString("JacmanFrameOptimiseBtn"), true);
                     }
                });
            }
        });
		menu.add(optimizePackages);

		// ABOUT
		//menu.addSeparator();
		JMenuItem about = new JMenuItem();
		about.setText(i18n.getString("JacmanFrameMenuHelpAbout"));
		about.setIcon(JacmanUtils.loadIcon("icons/jacman_logo_small.png"));
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new AboutDialog(frame, i18n.getString("AboutDialogTitle"), true);
            }
        });

		// QUIT
		menu.addSeparator();
		JMenuItem quit = new JMenuItem();
		quit.setText(i18n.getString("JacmanFrameMenuFileQuit"));
		quit.setIcon(JacmanUtils.loadIcon("icons/exit.png"));
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(quit);

		// TRAY ICON
		TrayIcon ti = new TrayIcon(JacmanUtils.loadIcon("icons/jacman_logo.png"),
				"Jacman", menu);
		ti.setIconAutoSize(true);
		ti.addActionListener(this);
		ti.addBalloonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		tray.addTrayIcon(ti);
	}

	public void actionPerformed(ActionEvent e) {
		frame.setVisible(!frame.isVisible());
	}
}