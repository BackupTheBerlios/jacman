package andyr.jacman.utils;
import java.io.IOException;
import java.io.InputStream;
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

}
