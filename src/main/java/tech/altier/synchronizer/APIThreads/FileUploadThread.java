package tech.altier.synchronizer.APIThreads;

import tech.altier.synchronizer.RemoteHandler.DropboxClient;

public class FileUploadThread implements Runnable {
    private String path;

    public FileUploadThread(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        DropboxClient.uploadFile(path);
    }
}
