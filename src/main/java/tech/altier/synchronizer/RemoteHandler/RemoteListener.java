package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import tech.altier.synchronizer.Main;

public class RemoteListener implements Runnable {
    static RemoteFiles remoteFilesInstance;
    private ListFolderResult remoteFileListResult;
    DbxClientV2 client;

    static {
        remoteFilesInstance = RemoteFiles.getInstance();
    }

    public RemoteListener() {
        client = Main.client;
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
            remoteFileListResult = remoteFilesInstance.getDetailedRemoteFileInfo();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            for (Metadata metadata : remoteFileListResult.getEntries()) {
                FileMetadata fileMetadata = null;
                if (metadata instanceof FileMetadata) {
                    fileMetadata = (FileMetadata) metadata;
                }

                assert fileMetadata != null;

                // TODO
            }

            if (!remoteFileListResult.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(remoteFileListResult.getCursor());
        }
    }
}
