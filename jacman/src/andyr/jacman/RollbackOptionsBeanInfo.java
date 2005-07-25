/*
 * Created on Jun 23, 2005
 *
 */
package andyr.jacman;

import java.util.Locale;

import andyr.jacman.utils.I18nManager;

import com.l2fprod.common.beans.BaseBeanInfo;

public class RollbackOptionsBeanInfo extends BaseBeanInfo {

    public RollbackOptionsBeanInfo() {
        super(InstallOptions.class);
        
        I18nManager i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());

        
        addProperty("nodeps").setCategory(i18n.getString("PropertiesPacmanCategory")).setShortDescription(i18n.getString("PropertiesNoDepsDesc"));
        addProperty("force").setCategory(i18n.getString("PropertiesPacmanCategory")).setShortDescription(i18n.getString("PropertiesForceDesc"));
        
        
    }

}
