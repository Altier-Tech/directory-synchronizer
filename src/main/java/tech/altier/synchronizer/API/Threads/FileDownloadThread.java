package tech.altier.synchronizer.API.Threads;

import com.dropbox.core.DbxException;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.RemoteHandler.DropboxClient;

import java.io.IOException;

public class FileDownloadThread implements Runnable {
    private final String path;
    private final DropboxClient client;

    public FileDownloadThread(String path) {
        log("Download thread initialized for file " + path);
        this.path = path;
        client = new DropboxClient();
    }

    @Override
    public void run() {
        try {
            client.downloadFile(path);
        } catch (DbxException | IOException e) {
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
