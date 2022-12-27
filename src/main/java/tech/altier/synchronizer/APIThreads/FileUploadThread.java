package tech.altier.synchronizer.APIThreads;

import com.dropbox.core.DbxException;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.RemoteHandler.DropboxClient;

public class FileUploadThread implements Runnable {
    private final String path;
    private final DropboxClient client;

    public FileUploadThread(String path) {
        log("Upload thread initialized for file " + path);
        this.path = path;
        client = new DropboxClient();
    }

    @Override
    public void run() {
        try {
            client.uploadFile(path);
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
