package tech.altier.synchronizer.APIThreads;

import tech.altier.synchronizer.RemoteHandler.DropboxClient;
import com.dropbox.core.DbxException;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.RemoteHandler.DropboxClient;

public class FileDownloadThread {
    private final String path;
    private final DropboxClient client;

    public FileDownloadThread(String path) {
        log("Upload thread initialized for file " + path);
        this.path = path;
        client = new DropboxClient();
    }
}
