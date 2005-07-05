/*
 * Created on Jun 23, 2005
 *
 */
package andyr.jacman;

import java.util.Locale;

import andyr.jacman.utils.I18nManager;

import com.l2fprod.common.beans.BaseBeanInfo;

public class RemoveOptionsBeanInfo extends BaseBeanInfo {

    public RemoveOptionsBeanInfo() {
        super(RemoveOptions.class);
        
        I18nManager i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());

        addProperty("nodeps").setCategory(i18n.getString("PropertiesPacmanCategory")).setShortDescription("Skips all dependency checks.");
        addProperty("force").setCategory(i18n.getString("PropertiesPacmanCategory")).setShortDescription("Bypass file conflict checks, overwriting conflicting files.");
        
        addProperty("cascade").setCategory(i18n.getString("PropertiesRemoveCategory")).setShortDescription(i18n.getString("PropertiesCascadeDesc"));
        addProperty("recursive").setCategory(i18n.getString("PropertiesRemoveCategory")).setShortDescription(i18n.getString("PropertiesRecursiveDesc"));
        addProperty("keep").setCategory(i18n.getString("PropertiesRemoveCategory")).setShortDescription(i18n.getString("PropertiesKeepDesc"));
        addProperty("nosave").setCategory(i18n.getString("PropertiesRemoveCategory")).setShortDescription(i18n.getString("PropertiesNoSaveDesc"));
    }

}
