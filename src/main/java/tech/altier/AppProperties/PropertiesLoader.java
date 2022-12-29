package tech.altier.AppProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    static Properties conf;

    static {
        try {
            conf = loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties");
        configuration.load(inputStream);
        assert inputStream != null;
        inputStream.close();
        return configuration;
    }

    public static void storeConfig() {

    }

    public static String get(String key) {
        return conf.getProperty(key);
    }

    public static void set(String key, String value) {
        conf.setProperty(key, value);
    }
}