package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.v2.files.ListFolderResult;

public class RemoteListener implements Runnable {
    @Override
    public void run() {
        // Adding a waiting period of 10 seconds
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ListFolderResult result = RemoteFiles.getDetailedRemoteFileInfo();
    }
}
