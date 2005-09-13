package andyr.jacman.gui.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import andyr.jacman.gui.InstallPackageDialog;
import andyr.jacman.gui.RemovePackageDialog;
import andyr.jacman.gui.RollbackPackageDialog;
import andyr.jacman.gui.UpdatePackagesDialog;
import andyr.jacman.utils.I18nManager;
import andyr.jacman.utils.JacmanUtils;

public class Tray implements ActionListener {

	private SystemTray tray = SystemTray.getDefaultSystemTray();
	private TrayIcon ti;
	private JFrame frame;
	private I18nManager i18n;

	public Tray(JFrame frame) {
		this.frame = frame;
		i18n = I18nManager.getInstance();

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
		installPackages.setIcon(JacmanUtils.loadIcon("icons/install.png"));
		installPackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new InstallPackageDialog(null, i18n
								.getString("InstallDialogTitle"), false);
					}
				});
			}
		});
		menu.add(installPackages);

		// UPDATE
		JMenuItem updatePackages = new JMenuItem();
		updatePackages.setText(i18n.getString("JacmanFrameUpdatePackagesBtn"));
		updatePackages.setIcon(JacmanUtils.loadIcon("icons/upgrade.png"));
		updatePackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UpdatePackagesDialog upd = new UpdatePackagesDialog(
								null, i18n.getString("UpdateDialogTitle"),
								false);
						upd.postInit();
					}
				});
			}
		});
		menu.add(updatePackages);

		// REMOVE
		JMenuItem removePackages = new JMenuItem();
		removePackages.setText(i18n.getString("JacmanFrameRemovePackagesBtn"));
		removePackages.setIcon(JacmanUtils.loadIcon("icons/remove.png"));
		removePackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						RemovePackageDialog rpd = new RemovePackageDialog(null,
								i18n.getString("RemoveDialogTitle"), false);
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
		rollbackPackages.setIcon(JacmanUtils.loadIcon("icons/sync.png"));
		rollbackPackages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new RollbackPackageDialog(null, i18n
								.getString("RollbackDialogTitle"), false);
					}
				});
			}
		});
		menu.add(rollbackPackages);

		// ABOUT
		// TODO Refactor about dialog out from Jacman.java
		menu.addSeparator();
		JMenuItem about = new JMenuItem();
		about.setText(i18n.getString("JacmanFrameMenuHelpAbout"));
		about.setIcon(JacmanUtils.loadIcon("icons/about.png"));

		// QUIT
		menu.addSeparator();
		JMenuItem quit = new JMenuItem();
		quit.setText(i18n.getString("JacmanFrameMenuFileQuit"));
		quit.setIcon(JacmanUtils.loadIcon("icons/exit.png"));
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if (JOptionPane.showConfirmDialog(null,
				// "Are you sure you want to quit Jacman ?") ==
				// JOptionPane.OK_OPTION)
				System.exit(0);
			}
		});
		menu.add(quit);

		// TRAY ICON
		ti = new TrayIcon(JacmanUtils.loadIcon("icons/jacman_logo.png"),
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