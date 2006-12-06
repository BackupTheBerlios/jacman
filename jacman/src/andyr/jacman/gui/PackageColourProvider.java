/*
 * Created on Dec 4, 2006
 *
 */
package andyr.jacman.gui;

import java.awt.Color;

import javax.swing.table.TableModel;

public class PackageColourProvider implements ColourProvider {

    private TableModel model;
    private static Color darkGreen = new Color(51,153,0);
    private static Color darkRed = new Color(204,0,0);
    
    public PackageColourProvider(TableModel model) {
        this.model = model;
    }
    public Color getBackground(int row, int column) {
        // TODO Auto-generated method stub
        return null;
    }

    public Color getForeground(int row, int column) {
        if (column == 2) {
            String installedVer = (String)model.getValueAt(row, 2);
            
            String availableVer = (String)model.getValueAt(row, 3);
            
            
            if (!installedVer.equals("--")) {
                if (installedVer.equals(availableVer)) {
                    return darkGreen;
                }
                return darkRed;
            }
        }
        return null;
    }

}
