package tech.altier.synchronizer.LocalHandler;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.API.Threads.FileDeleteThread;
import tech.altier.synchronizer.API.Threads.FileModifyThread;
import tech.altier.synchronizer.API.Threads.FileUploadThread;

import java.nio.file.Path;

import static tech.altier.synchronizer.Main.repository;

public class FileHandler {
    public void handleLocalCreate(Path filePath) {
        log("Upload thread started for new created file " + filePath);
        Thread uploadThread = new Thread(
                new FileUploadThread(repository.getPath() + "\\" + filePath)
        );
        uploadThread.start();
    }

    public void handleLocalDelete(Path filePath) {
        log("Deletion thread started for the file " + filePath);
        Thread deleteThread = new Thread(
                new FileDeleteThread(filePath.toString())
        );
        deleteThread.start();
    }

    public void handleLocalModify(Path filePath) { // TODO
        log("Modification thread started for the file " + filePath);
        Thread modifyThread = new Thread(
                new FileModifyThread(filePath.toString())
        );
        modifyThread.start();
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_CYAN +
                Thread.currentThread().getName() +
                "\tFileHandler: \t" +
                message
        );
    }
}
