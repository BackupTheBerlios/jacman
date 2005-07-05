/*
 * Created on Jun 17, 2005
 *
 */
package andyr.jacman.gui;

import java.util.List;

import andyr.jacman.PacmanPkg;

import ca.odell.glazedlists.TextFilterator;

public class PackageTextFilterator implements TextFilterator {
    
    public void getFilterStrings(List baseList, Object element) {
        PacmanPkg pkg = (PacmanPkg)element;

        baseList.add(pkg.getName());
        baseList.add(pkg.getDescription());
        
      }

}
