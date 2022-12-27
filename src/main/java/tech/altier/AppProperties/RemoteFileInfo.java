package tech.altier.AppProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class RemoteFileInfo extends HashMap<String, String> {
    static Properties conf;
    public static HashMap<String, String> remoteFiles;

    static {
        try {
            conf = loadProperties();
            loadRemoteFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        return remoteFiles.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public String get(Object key) {
        return super.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public String put(String key, String value) {
        return super.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return super.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    private static Properties loadProperties() throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream("remote.properties");
        configuration.load(inputStream);
        assert inputStream != null;
        inputStream.close();
        return configuration;
    }

    private static void loadRemoteFiles() {
        remoteFiles = new HashMap<>();
        for (String key : conf.stringPropertyNames()) {
            remoteFiles.put(key, conf.getProperty(key));
        }
    }

    private static String get(String key) {
        return conf.getProperty(key);
    }
}
