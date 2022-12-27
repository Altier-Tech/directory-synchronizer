package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.ListFolderResult;

public class RemoteListener implements Runnable {
    static RemoteFiles remoteFilesInstance;

    static {
        remoteFilesInstance = RemoteFiles.getInstance();
    }

    @Override
    public void run() {
        // Adding a waiting period of 10 seconds
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            ListFolderResult result = remoteFilesInstance.getDetailedRemoteFileInfo();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }

        
    }
}
