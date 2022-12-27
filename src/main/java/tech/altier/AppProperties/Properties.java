package tech.altier.AppProperties;

public class Properties {
    private static PropertiesLoader instance;

    static {
        instance = PropertiesLoader.getInstance();
    }

    public static String get(String key) {
        return instance.getProperty(key);
    }
}
