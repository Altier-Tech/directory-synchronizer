package tech.altier.synchronizer.LocalHandler;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.APIThreads.FileUploadThread;

import java.nio.file.Path;

import static tech.altier.synchronizer.Main.repository;

public class FileHandler {
    public void handleLocalCreate(Path filePath) {
        // TODO recall listview population function

        log("Upload thread started for new created file...");
        Thread uploadThread = new Thread(
                new FileUploadThread(repository.getPath() + "\\" + filePath)
        );
        uploadThread.start();
    }

    public void handleLocalDelete(Path filePath) { // TODO
        // Step 1 - Prompt if the deletion should be permanent

        // If yes, delete the file from the remote repository

    }

    public void handleLocalModify(Path filePath) { // TODO
        // Step 1 - Delete the remote file

        // Step 2 - Upload the new local file

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
