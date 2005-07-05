package andyr.jacman;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: andyr
 * Date: May 23, 2005
 * Time: 4:21:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PackageComparitor implements Comparator {
    public int compare(Object o1, Object o2) {

        if (o1 instanceof PacmanPkg && o2 instanceof PacmanPkg) {
            PacmanPkg p1 = (PacmanPkg)o1;
            PacmanPkg p2 = (PacmanPkg)o2;
    
    
            return p1.getName().compareTo(p2.getName());
        }
        
        return 0;
    }
}
