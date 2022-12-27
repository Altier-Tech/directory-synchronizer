package tech.altier.synchronizer.APIThreads;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.RemoteHandler.DropboxClient;

public class FileUploadThread implements Runnable {
    private String path;
    private DropboxClient client;

    public FileUploadThread(String path) {
        log("Upload thread initialized for file " + path);
        this.path = path;
        client = new DropboxClient();
    }

    @Override
    public void run() {
        client.uploadFile(path);
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
