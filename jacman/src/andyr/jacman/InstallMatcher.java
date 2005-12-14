/*
 * Created on Dec 12, 2005
 *
 */
package andyr.jacman;

import java.util.Collection;

import ca.odell.glazedlists.matchers.Matcher;

public class InstallMatcher implements Matcher {
    
    private Collection packagesSelectedList;
    public InstallMatcher(Collection selectList) {
        packagesSelectedList = selectList;
    }

    public boolean matches(Object o) {
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
    }

}
