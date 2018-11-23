/*
 * Author Vytautas Ambrasiunas 2018.
 */

package ambrasiunas.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesServiceImpl implements ApplicationPropertiesService {

    private final String propertiesFileName = "application.properties";

    @Override
    public Properties getAppProperties() {
        Properties properties = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Properties file '" + propertiesFileName + "' is missing from classpath!");
        }

        return properties;
    }

}
