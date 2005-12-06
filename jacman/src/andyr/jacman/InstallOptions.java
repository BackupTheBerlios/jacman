/*
 * 
 * Copyright 2005 The Jacman Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package andyr.jacman;

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
        
    }
}
