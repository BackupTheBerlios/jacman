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

import java.util.Locale;

import andyr.jacman.utils.I18nManager;

import com.l2fprod.common.beans.BaseBeanInfo;

public class InstallOptionsBeanInfo extends BaseBeanInfo {

    public InstallOptionsBeanInfo() {
        super(InstallOptions.class);
        
        I18nManager i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());

        
        addProperty("nodeps").setCategory(i18n.getString("PropertiesPacmanCategory")).setShortDescription(i18n.getString("PropertiesNoDepsDesc"));
        addProperty("force").setCategory(i18n.getString("PropertiesPacmanCategory")).setShortDescription(i18n.getString("PropertiesForceDesc"));
        
        addProperty("downloadonly").setCategory(i18n.getString("PropertiesInstallCategory")).setShortDescription(i18n.getString("PropertiesDownloadOnlyDesc"));
        
    }

}
