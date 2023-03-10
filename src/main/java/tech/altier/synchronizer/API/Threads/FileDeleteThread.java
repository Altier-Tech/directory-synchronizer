package tech.altier.synchronizer.API.Threads;

import com.dropbox.core.DbxException;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.API.DropboxClient;

public class FileDeleteThread implements Runnable {
    private final String path;
    private final DropboxClient client;

    public FileDeleteThread(String path) {
        log("Deletion thread initialized for file " + path);
        this.path = path;
        client = new DropboxClient();
    }

    @Override
    public void run() {
        try {
            client.deleteFile(path);
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
