package tech.altier.synchronizer.API.Threads;

import com.dropbox.core.DbxException;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.Main;
import tech.altier.synchronizer.RemoteHandler.DropboxClient;

public class FileModifyThread implements Runnable {
    private final String path;
    private final DropboxClient client;

    public FileModifyThread(String path) {
        log("Modification thread initialized for file " + path);
        this.path = path;
        client = new DropboxClient();
    }

    @Override
    public void run() {
        try {
            // Step 1 - Delete the remote file
            log("Deleting remote file " + path);
            client.deleteFile(path);
            log("Remote file deleted " + path);
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }

        try {
            // Step 2 - Upload the local file
            log("Uploading local file " + path);
            client.uploadFile(Main.repository.getPath() + "\\" + path);
            log("Local file uploaded " + path);
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_BLUE +
                Thread.currentThread().getName() +
                "\tDBClient: \t" +
                message
        );
    }
}
