/*
 * Created on Jun 23, 2005
 *
 */
package andyr.jacman;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InstallOptions {

    private boolean nodeps = false;
    private boolean force = false;
    private boolean downloadonly = false;
    
    public InstallOptions() {
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
    
    public boolean isDownloadonly() {
        return this.downloadonly;
    }
    
    public void setDownloadonly(boolean downloadonly) {
        this.downloadonly = downloadonly;
    }
    
    public List<String> getInstallOptionsArgs() {
        List<String> args = new ArrayList<String>();
        args.add("-S");
        
        if (nodeps) {
            args.add("-d");
        }
        
        if (force) {
            args.add("-f");
        }

        if (downloadonly) {
            args.add("-w");
        }
        
        return args;
        
        //return args.toArray((String[])Array.newInstance(String.class, args.size()));
        
    }
}
