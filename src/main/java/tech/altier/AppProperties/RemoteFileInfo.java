package tech.altier.AppProperties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class RemoteFileInfo extends HashMap<String, String> {
    private static Properties conf;
    public static HashMap<String, String> remoteFiles;

    static {
        try {
            conf = loadProperties();
            loadRemoteFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeProperties() {
        writeRemoteFiles();
        
        try (FileOutputStream outputStream = new FileOutputStream("repository.properties")) {
            conf.store(outputStream, "Repository Properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream("repository.properties");
        configuration.load(inputStream);
        assert inputStream != null;
        inputStream.close();
        return configuration;
    }

    private static void writeRemoteFiles() {
        // Clearing the Properties before rewriting to avoid deleted files existing in the dataset
        conf.clear();

        Set<String> keys = remoteFiles.keySet();
        for (String key : keys) {
            conf.setProperty(key, remoteFiles.get(key));
        }
        writeProperties();
    }

    private static void loadRemoteFiles() {
        remoteFiles = new HashMap<>();
        for (String key : conf.stringPropertyNames()) {
            remoteFiles.put(key, conf.getProperty(key));
        }
    }

    public static RemoteFileInfo getInstance() {
        return new RemoteFileInfo();
    }

    @Override
    public int size() {
        return remoteFiles.size();
    }

    @Override
    public boolean isEmpty() {
        return remoteFiles.isEmpty();
    }

    @Override
    public String get(Object key) {
        return remoteFiles.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return remoteFiles.containsKey(key);
    }

    @Override
    public String put(String key, String value) {
        return remoteFiles.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return remoteFiles.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return remoteFiles.keySet();
    }
}
