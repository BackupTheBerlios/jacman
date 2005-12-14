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

import java.util.Locale;

import andyr.jacman.utils.I18nManager;

import com.l2fprod.common.beans.BaseBeanInfo;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;

/**
 * 
 * @author Andrew Roberts
 *
 */

public class GeneralOptionsBeanInfo extends BaseBeanInfo {

    public GeneralOptionsBeanInfo() {
        super(GeneralOptions.class);
        I18nManager i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        
        addProperty("disposeMainMenu").setCategory("Behaviour").setDisplayName(i18n.getString("GeneralOptionsDisposeMainMenu"));
        
        // System tray properties
        ExtendedPropertyDescriptor epd = addProperty("enableSystemTray");
        epd.setCategory("System tray");
        epd.setDisplayName(i18n.getString("GeneralOptionsEnableTray"));
        epd.setShortDescription(i18n.getString("GeneralOptionsEnableTrayDesc"));
        
        epd = addProperty("startHiddenInTray");
        epd.setCategory("System tray");
        epd.setDisplayName(i18n.getString("GeneralOptionsStartInTray"));
        epd.setShortDescription(i18n.getString("GeneralOptionsStartInTrayDesc"));
        
        
    }
    
}