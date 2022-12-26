package tech.altier.synchronizer.LocalHandler;

import java.io.File;

public class LocalRepository {
    private String absPath;

    public LocalRepository(String absPath) {
        this.absPath = absPath;
    }

    // List all files in the directory
    public void listFiles() {
        File folder = new File(absPath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }
}
