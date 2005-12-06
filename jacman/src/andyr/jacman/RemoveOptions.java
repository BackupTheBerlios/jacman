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

public class RemoveOptions {

    private boolean nodeps = false;
    private boolean force = false;
    private boolean cascade = false;
    private boolean recursive = false;
    private boolean keep = false;
    private boolean nosave = false;
    
    public RemoveOptions() {
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
    public boolean isCascade() {
        return cascade;
    }
    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }
    public boolean isKeep() {
        return keep;
    }
    public void setKeep(boolean keep) {
        this.keep = keep;
    }
    public boolean isNosave() {
        return nosave;
    }
    public void setNosave(boolean nosave) {
        this.nosave = nosave;
    }
    public boolean isRecursive() {
        return recursive;
    }
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }
    
    public List<String> getRemoveOptionsArgs() {
        List<String> args = new ArrayList<String>();
        args.add("-R");
        
        if (nodeps) {
            args.add("-d");
        }
        
        if (force) {
            args.add("-f");
        }

        if (cascade) {
            args.add("-c");
        }
        
        if (recursive) {
            args.add("-s");
        }
        
        if (keep) {
            args.add("-k");
        }
        
        if (nosave) {
            args.add("-n");
        }

        return args;
        
        //return args.toArray((String[])Array.newInstance(String.class, args.size()));
        
    }
}
