/*
 * Created on Jun 23, 2005
 *
 */
package andyr.jacman;

import java.util.ArrayList;
import java.util.List;

public class RollbackOptions {

    private boolean nodeps = false;
    private boolean force = false;
    
    public RollbackOptions() {
        super();
    }

    public boolean isForce() {
        return force;
    }
    public void setForce(boolean force) {
        this.force = force;
    }
    public boolean isNodeps() {
        return nodeps;
    }
    public void setNodeps(boolean nodeps) {
        this.nodeps = nodeps;
    }
    
    public List<String> getRollbackOptionsArgs() {
        List<String> args = new ArrayList<String>();
        args.add("-S");
        
        if (nodeps) {
            args.add("-d");
        }
        
        if (force) {
            args.add("-f");
        }

        return args;
        
    }
}
