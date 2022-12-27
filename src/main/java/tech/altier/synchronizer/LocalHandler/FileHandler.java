package tech.altier.synchronizer.LocalHandler;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.APIThreads.FileUploadThread;

import java.nio.file.Path;

import static tech.altier.synchronizer.Main.repository;

public class FileHandler {
    public void handleLocalCreate(Path filePath) {
        // TODO kick off a FileUploadThread with the path
        log("Upload thread started for new created file...");
        Thread uploadThread = new Thread(
                new FileUploadThread(repository.getPath() + "\\" + filePath)
        );
        uploadThread.start();
    }

    public void handleLocalDelete(Path filePath) { // TODO
    }

    public void handleLocalModify(Path filePath) { // TODO
        // Step 1 - Delete the remote file

        // Step 2 - Upload the new local file
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_CYAN + "FileHandler: \t" + message);
    }
}
