package tech.altier.synchronizer.LocalHandler;

import tech.altier.Thread.ThreadColor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class LocalRepository {
    private String path;

    public LocalRepository(String path) {
        this.path = path;
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
                    FileHandler.handleLocalCreate (filePath);
                }
                if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    log("A file has been deleted: " + filePath);
                    FileHandler.handleLocalDelete (filePath);
                }
                if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    log("A file has been modified: " + filePath);
                    FileHandler.handleLocalModify (filePath);
                }
            }

            boolean valid = watchKey.reset();
            if (!valid) {
                break;
            }
        }
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_GREEN + "LocalListener: \t" + message);
    }
}
