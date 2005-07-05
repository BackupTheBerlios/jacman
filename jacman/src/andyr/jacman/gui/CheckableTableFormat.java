/*
 * Created on Jun 17, 2005
 *
 */
package andyr.jacman.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.WeakHashMap;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;

public class CheckableTableFormat implements AdvancedTableFormat, WritableTableFormat {
    IdentityHashMap selection;
    //WeakHashMap selection;
    BeanTableFormat wrapped;

    public CheckableTableFormat(BeanTableFormat wrapped) {
      this.wrapped = wrapped;
      this.selection = new IdentityHashMap();
      //this.selection = new WeakHashMap();
    }

    public Class getColumnClass(int column) {
      if (column == 0)
        return Boolean.class;
      else
        return wrapped.getColumnClass(column - 1);
    }

    public Comparator getColumnComparator(int column) {
      if (column == 0)
        return GlazedLists.booleanComparator();
      else
        return wrapped.getColumnComparator(column - 1);
    }

    public int getColumnCount() {
      return wrapped.getColumnCount() + 1;
    }

    public String getColumnName(int column) {
      if (column == 0)
        return "Sel.";
      else
        return wrapped.getColumnName(column - 1);
    }

    public Object getColumnValue(Object baseObject, int column) {
      if (column > 0)
        return wrapped.getColumnValue(baseObject, column - 1);
      else
        return Boolean.valueOf(isSelected(baseObject));
    }

    /**
     * @param bean
     * @return
     */
    boolean isSelected(Object bean) {
      return selection.containsKey(bean);
    }

    public List getSelection() {
      return new ArrayList(selection.values());
    }

    public boolean isEditable(Object baseObject, int column) {
      if (column == 0)
        return true;
      else
        return wrapped.isEditable(baseObject, column - 1);
    }

    public Object setColumnValue(Object baseObject, Object editedValue, int column) {
      if (column == 0) {
        if (editedValue.equals(Boolean.TRUE)) {
          selection.put(baseObject, baseObject);
        } else {
          selection.remove(baseObject);
        }
        return null;
      } else {
        return wrapped.setColumnValue(baseObject, editedValue, column);
      }
    }

  }