package tech.altier.synchronizer.LocalHandler;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import tech.altier.Thread.ThreadColor;

import java.io.IOException;
import java.nio.file.*;

public class LocalRepository {
    private final String path;
    private Thread watcher;

    private LocalRepository(String path) {
        this.path = path;
    }

    public static LocalRepository setupRepositoryOnDir(String path) {
        log("Setting up repository on directory: " + path);
        return new LocalRepository(path);
    }

    public void startListening() {
        watcher = new Thread(
                new LocalListener(path)
        );
        watcher.start();
    }

    public String getPath() {
        return path;
    }

    private static void log(String message) {
        System.out.println(
                ThreadColor.ANSI_GREEN +
                Thread.currentThread().getName() +
                "\tLocalListener: \t" +
                message
        );
    }
}

class LocalListener implements Runnable {
    private final String path;
    private final FileHandler fileHandler;

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

                // File Create event
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    log("A new file is created : " + filePath);
                    fileHandler.handleLocalCreate (filePath);
                }

                // File Delete event
                if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    log("A file has been deleted: " + filePath);

                    Platform.runLater(() -> {
                        // Prompt if the deletion should be permanent
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        log("Prompting user for deletion confirmation of file " + filePath);
                        alert.setTitle("Are you sure?");
                        alert.setContentText("Do you wish to make the deletion permanent?");
                        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);

                        alert.showAndWait().ifPresent(type -> {
                            log("Pressed " + type.getText());
                            if (type.getText().equalsIgnoreCase("Yes")) {
                                log("User confirmed deletion of file " + filePath);

                                // If yes, delete the file from the remote repository
                                fileHandler.handleLocalDelete (filePath);
                            } else if (type.getText().equalsIgnoreCase("No")) {
                                log("User denied deletion of file " + filePath);
                                // If no, do nothing
                            }
                        });
                    });
                }

                // File Modify event
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
