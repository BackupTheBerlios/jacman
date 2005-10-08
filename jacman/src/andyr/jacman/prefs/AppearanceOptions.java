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

public class AppearanceOptions {
    private boolean useAntiAliasText = true;
    private boolean showWindowInfo = false;
    /**
     * @return Returns the showWindowInfo.
     */
    
    public AppearanceOptions() {
        super();
    }
    public boolean isShowWindowInfo() {
        return showWindowInfo;
    }
    /**
     * @param showWindowInfo The showWindowInfo to set.
     */
    public void setShowWindowInfo(boolean showWindowInfo) {
        this.showWindowInfo = showWindowInfo;
    }
    /**
     * @return Returns the useAntiAliasText.
     */
    public boolean isUseAntiAliasText() {
        return useAntiAliasText;
    }
    /**
     * @param useAntiAliasText The useAntiAliasText to set.
     */
    public void setUseAntiAliasText(boolean useAntiAliasText) {
        this.useAntiAliasText = useAntiAliasText;
    }
    
}