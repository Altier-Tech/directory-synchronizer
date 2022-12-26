package tech.altier.synchronizer.LocalHandler;

public class FileUploadThread implements Runnable {
    private String path;

    public FileUploadThread(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        // TODO upload file
    }
}
