package tech.altier.AppProperties;

public class Properties {
    private static PropertiesLoader instance;

    static {
        instance = PropertiesLoader.getInstance();
    }
}
