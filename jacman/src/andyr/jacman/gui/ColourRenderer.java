/*
 * Created on Oct 2, 2005
 *
 */
package andyr.jacman.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class ColourRenderer implements TableCellRenderer {
    
    protected TableCellRenderer delegate;
    protected ColourProvider provider;

    public ColourRenderer(TableCellRenderer anotherRenderer, ColourProvider provider) {
        this.delegate = anotherRenderer;
        this.provider = provider;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    
        Color background = null;
        Color foreground = null;
        
        if (isSelected) {
            foreground = table.getSelectionForeground();
            background = table.getSelectionBackground();
        }
        else {
            // Adjust for columns moving around
            int mcol = table.convertColumnIndexToModel(column);
            
            // get the colours from the provider
            foreground = provider.getForeground(row, mcol);
            background = provider.getBackground(row, mcol);
            
        }
        
        Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        c.setForeground(foreground);
        //c.setBackground(background);
        
        return c;
    }

}
