package tech.altier.synchronizer.LocalHandler;

import tech.altier.Thread.ThreadColor;

import java.io.IOException;
import java.nio.file.*;

public class LocalRepository {
    private String path;

    private LocalRepository(String path) {
        this.path = path;
    }

    public static LocalRepository setupRepositoryOnDir(String path) {
        System.out.println(ThreadColor.ANSI_GREEN + "LocalListener: \t" + "Setting up repository on directory: " + path);
        return new LocalRepository(path);
    }

    public void startListening() {
        Thread watcher = new Thread(
                new LocalListener(path)
        );
        watcher.start();
    }

    public String getPath() {
        return path;
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_GREEN +
                Thread.currentThread().getName() +
                "\tLocalListener: \t" +
                message
        );
    }
}

class LocalListener implements Runnable {
    private String path;
    private FileHandler fileHandler;

    public LocalListener(String path) {
        this.path = path;
        fileHandler = new FileHandler();
    }

    public void watch() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        Path directory = Path.of(path);

        log("Listening directory " + directory + " for changes...");

        WatchKey watchKey = directory.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
        );

        while (true) {
            for (WatchEvent<?> event : watchKey.pollEvents()) {

                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

                Path filePath = pathEvent.context();

                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    log("A new file is created : " + filePath);
                    fileHandler.handleLocalCreate (filePath);
                }
                if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    log("A file has been deleted: " + filePath);

                    System.out.println(Thread.currentThread().getName());

                    fileHandler.handleLocalDelete (filePath);
                }
                if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    log("A file has been modified: " + filePath);
                    fileHandler.handleLocalModify (filePath);
                }
            }

            boolean valid = watchKey.reset();
            if (!valid) {
                break;
            }
        }
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_GREEN +
                Thread.currentThread().getName() +
                "\tLocalListener: \t"
                + message
        );
    }

    @Override
    public void run() {
        try {
            watch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
