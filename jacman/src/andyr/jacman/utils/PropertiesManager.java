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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


/*
 * Created on Jun 16, 2005
 *
 */

public class PropertiesManager {

    private static PropertiesManager man;

    private Properties properties;

    public static PropertiesManager getPropertiesManager(
            InputStream propertiesStream) throws IOException {
        if (man == null) {
            man = new PropertiesManager(propertiesStream);

        }
        return man;
    }

    public static PropertiesManager getInstance() {
        if (man == null) {
            return null;
        }
        return man;
    }

    private PropertiesManager(InputStream propertiesStream) throws IOException {
        
        properties = new Properties();
        properties.load(propertiesStream);

    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public void setProperty(String key, String value) {
        properties.getProperty(key, value);
    }
    
    public Properties getProperties() {
        
        return properties;
    }
    
    public void store(OutputStream out, String header) throws IOException {
        properties.store(out, header);
    }

}
