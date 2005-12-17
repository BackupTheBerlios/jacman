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

package andyr.jacman.utils;

import java.util.Locale;
import java.util.ResourceBundle;

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
