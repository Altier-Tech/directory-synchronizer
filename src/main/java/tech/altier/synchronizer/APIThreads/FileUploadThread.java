package tech.altier.synchronizer.APIThreads;

import tech.altier.synchronizer.RemoteHandler.DropboxClient;

public class FileUploadThread implements Runnable {
    private String path;
    private DropboxClient client;

    public FileUploadThread(String path) {
        this.path = path;
        client = new DropboxClient();
    }

    @Override
    public void run() {
        DropboxClient.uploadFile(path);
    }
}
