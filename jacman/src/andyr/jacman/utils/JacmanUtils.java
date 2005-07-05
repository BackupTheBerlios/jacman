package andyr.jacman.utils;

import java.awt.Insets;
import java.net.URLClassLoader;

import javax.swing.ImageIcon;
import javax.swing.JTable;


public final class JacmanUtils {
	private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);

    
    public static ImageIcon loadIcon(String iconName) {
                
        return new ImageIcon(URLClassLoader.getSystemResource(iconName));
    }
    

    public static void setOptimalTableWidth(JTable table) {
        
        for (int col = 0; col < table.getColumnCount(); col++) {
            JTableHelper.setOptimalColumnWidth(table, col);
        }
    }
}
