/*
 * Created on Oct 2, 2005
 *
 */
package andyr.jacman.gui;

import java.awt.Color;

public interface ColourProvider {
    
    public Color getForeground(int row, int column);
    public Color getBackground(int row, int column);

}
