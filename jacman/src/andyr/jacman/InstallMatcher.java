/*
 * Created on Dec 12, 2005
 *
 */
package andyr.jacman;

import java.util.Collection;

import ca.odell.glazedlists.matchers.Matcher;

public class InstallMatcher implements Matcher<PacmanPkg> {
    
    private Collection packagesSelectedList;
    
    public InstallMatcher(Collection selectList) {
        
        packagesSelectedList = selectList;
        System.out.println("Printing select list...");
        for (Object ob: packagesSelectedList) {
            System.out.println(ob.toString());
            
        }
    }

    public boolean matches(PacmanPkg pacmanPkg) {
        // TODO Auto-generated method stub
        return false;
    }

    /*public boolean matches(Object o) {
        System.out.println("Matching...");
        if (o == null) return false;
        if (packagesSelectedList.isEmpty()) return true;
        PacmanPkg pkg = (PacmanPkg)o;
               
        if (packagesSelectedList.contains("All")) {
            return true;
        }
        else if (packagesSelectedList.contains("Not installed")) {
            if (pkg.getInstalledVersion().equals("--")) {
                return true;
            }
        }
        
        return false;
    }*/

}
