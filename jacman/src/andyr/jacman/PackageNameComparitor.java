package andyr.jacman;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: andyr
 * Date: May 23, 2005
 * Time: 4:21:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PackageNameComparitor implements Comparator {
    public int compare(Object o1, Object o2) {

        String p1 = ((PacmanPkg)o1).getName();
        String p2 = ((PacmanPkg)o2).getName();


        return p1.compareTo(p2);
    }
}
