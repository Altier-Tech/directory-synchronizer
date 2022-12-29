package tech.altier.AppProperties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class RemoteFileInfo extends HashMap<String, String> {
    private static final Properties conf;
    public static HashMap<String, String> remoteFiles;
    private static final RemoteFileInfo instance;

    static {
        try {
            conf = loadProperties();
            loadRemoteFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        instance = new RemoteFileInfo();
    }

    private RemoteFileInfo(){}

    public static void storeRemoteProperties() {
        writeProperties();
    }

    private static void writeProperties() {
        writeRemoteFilesList();

        try (FileOutputStream outputStream = new FileOutputStream("src\\main\\resources\\repository.properties")) {
            conf.store(outputStream, null);
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

    private static void writeRemoteFilesList() {
        // Clearing the Properties before rewriting to avoid deleted files existing in the dataset
        conf.clear();

        Set<String> keys = remoteFiles.keySet();
        for (String key : keys) {
            conf.setProperty(key, remoteFiles.get(key));
        }
        // writeProperties(); // Circular ???? Bug ????
    }

    private static void loadRemoteFiles() {
        remoteFiles = new HashMap<>();
        for (String key : conf.stringPropertyNames()) {
            remoteFiles.put(key, conf.getProperty(key));
        }
    }

    /**
     * Returns the singleton instance of the RemoteFileInfo class.
     * @return the singleton instance of the RemoteFileInfo class
     */
    public static RemoteFileInfo getInstance() {
        return instance;
    }

    /**
     * Returns the number of files in the remote repository that
     * currently written into the database.
     * @return the number of files in the remote repository
     */
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
