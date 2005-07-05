/*
 * Created on Jun 17, 2005
 *
 */
package andyr.jacman.gui;

import java.util.Comparator;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import andyr.jacman.PacmanPkg;


public class PackageTableFormat implements AdvancedTableFormat, WritableTableFormat {

    public int getColumnCount() {
        
        return 6;
    }

    public String getColumnName(int column) {
        if(column == 0) {
            return "Install";
          } else if(column == 1) {
            return "Name";
          } else if(column == 2) {
            return "Installed version";
          } else if(column == 3) {
            return "Available version";
          } else if(column == 4) {
            return "Description";
          } else if(column == 5) {
            return "Size";
          }
        return null;
    }

    public Object getColumnValue(Object baseObject, int column) {
        
        if(baseObject == null || !(baseObject instanceof PacmanPkg)) return null;
        PacmanPkg pkg = (PacmanPkg)baseObject;
        
        if (column == 0) {
            return new Boolean(pkg.getInstallSelected());
        }
        else if (column == 1) {
            return pkg.getName();
        }
        else if (column == 2) {
            return "";
        }
        else if (column == 3) {
            return pkg.getVersion();
        }
        else if (column == 4) {
            return pkg.getDescription();
        }
        else if (column == 5) {
            return new Long(pkg.getSize());
        }
        
        return null;
    }

    public void setChecked(Object baseObject, boolean checked) {
        // TODO Auto-generated method stub
        
    }

    public boolean getChecked(Object baseObject) {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isEditable(Object baseObject, int column) {
        
        if (column == 0) {
            return true;
        }
        return false;
    }

    public Object setColumnValue(Object baseObject, Object editedValue, int column) {
       
        if (column == 0) {
            //return editedValue;
            return setColumnValue(baseObject, editedValue, column);
        }
        
        return null;
    }

    public Class getColumnClass(int column) {
        // TODO Auto-generated method stub
        if (column == 0) {
            return Boolean.class;
        }
        else if (column == 5) {
            return Long.class;
        }
        
        return String.class;
        
    }

    public Comparator getColumnComparator(int column) {
        if (column == 0) {
            return GlazedLists.booleanComparator();
        }
        // TODO Auto-generated method stub
        return null;
    }

}
