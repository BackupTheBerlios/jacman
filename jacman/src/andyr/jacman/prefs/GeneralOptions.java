/*
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

package andyr.jacman.prefs;

import java.util.Properties;

public class GeneralOptions {
    
    private boolean disposeMainMenu = false;
    private boolean enableSystemTray = false;
    private boolean startHiddenInTray = false;
    
    public GeneralOptions(Properties properties) {
        super();
        setDisposeMainMenu(new Boolean(properties.getProperty("jacman.disposeMainMenu")).booleanValue());
        setEnableSystemTray(new Boolean(properties.getProperty("jacman.enableTray")).booleanValue());
        setStartHiddenInTray(new Boolean(properties.getProperty("jacman.startHiddenInTray")).booleanValue());
    }

    

    /**
     * @return Returns the disposeMainMenu.
     */
    public boolean isDisposeMainMenu() {
        return disposeMainMenu;
    }

    /**
     * @param disposeMainMenu The disposeMainMenu to set.
     */
    public void setDisposeMainMenu(boolean disposeMainMenu) {
        this.disposeMainMenu = disposeMainMenu;
    }

    /**
     * @return Returns the enableSystemTray.
     */
    public boolean isEnableSystemTray() {
        return enableSystemTray;
    }

    /**
     * @param enableSystemTray The enableSystemTray to set.
     */
    public void setEnableSystemTray(boolean enableSystemTray) {
        this.enableSystemTray = enableSystemTray;
    }

    /**
     * @return Returns the startHiddenInTray.
     */
    public boolean isStartHiddenInTray() {
        return startHiddenInTray;
    }

    /**
     * @param startHiddenInTray The startHiddenInTray to set.
     */
    public void setStartHiddenInTray(boolean startHiddenInTray) {
        this.startHiddenInTray = startHiddenInTray;
    }
    
}
