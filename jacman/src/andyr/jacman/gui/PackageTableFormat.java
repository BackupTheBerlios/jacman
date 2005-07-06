/*
 * Created on Jun 17, 2005
 *
 */
package andyr.jacman.gui;

import java.util.Comparator;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;

public class PackageTableFormat implements AdvancedTableFormat {
    BeanTableFormat wrapped;

    public PackageTableFormat(BeanTableFormat wrapped) {
      this.wrapped = wrapped;
    }

    public Class getColumnClass(int column) {
        return wrapped.getColumnClass(column);
    }

    public Comparator getColumnComparator(int column) {
        return wrapped.getColumnComparator(column);
    }

    public int getColumnCount() {
      return wrapped.getColumnCount();
    }

    public String getColumnName(int column) {
        return wrapped.getColumnName(column);
    }

    public Object getColumnValue(Object baseObject, int column) {
        return wrapped.getColumnValue(baseObject, column);
      
    }

    public boolean isEditable(Object baseObject, int column) {
      
        return wrapped.isEditable(baseObject, column - 1);
    }
    

  }