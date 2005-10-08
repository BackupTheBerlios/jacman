package andyr.jacman.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/*
 * Created on Jun 16, 2005
 *
 */

public class I18nManager {

    private static I18nManager man;

    private ResourceBundle rb;
    private String basename;
    private ClassLoader classLoader;

    public static I18nManager getI18nManager(String basename, Locale locale,
            ClassLoader loader) {
        if (man == null) {
            man = new I18nManager(basename, locale, loader);

        }
        return man;
    }

    public static I18nManager getI18nManager(String basename, Locale locale) {
        if (man == null) {
            man = new I18nManager(basename, locale);

        }
        return man;
    }

    public static I18nManager getInstance() {
        if (man == null) {
            return null;
        }
        return man;
    }

    //private constructor to ensure singleton
    private I18nManager(String basename, Locale locale, ClassLoader loader) {
        //set up the resource bundle and stuff
        this.basename = basename;
        this.classLoader = loader;
        rb = ResourceBundle.getBundle(basename, locale, loader);
    }

    private I18nManager(String basename, Locale locale) {
        //set up the resource bundle and stuff
        this.basename = basename;
        rb = ResourceBundle.getBundle(basename, locale);
    }

    public String getString(String key) {
        return rb.getString(key);
    }
    
    public void setLocale(Locale newLocale) {
        
        if (classLoader == null) {
            rb = ResourceBundle.getBundle(basename, newLocale);
        }
        else {
            rb = ResourceBundle.getBundle(basename, newLocale, classLoader);
        }
    }

}
