package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import tech.altier.AppProperties.RemoteFileInfo;
import tech.altier.synchronizer.Main;

public class RemoteListener implements Runnable {
    static RemoteFiles remoteFilesInstance;
    static RemoteFileInfo remoteFileInfo;
    private ListFolderResult remoteFileListResult;
    DbxClientV2 client;

    static {
        remoteFilesInstance = RemoteFiles.getInstance();
        remoteFileInfo = RemoteFileInfo.getInstance();
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
                // Check if both hashes are the same
                if (remoteFileInfo.get(metadata.getPathLower()) != fileMetadata.getContentHash()) {
                    // If they are not the same, update the local database
                    remoteFileInfo.put(metadata.getPathLower(), fileMetadata.getContentHash());
                }
            }

            if (!remoteFileListResult.getHasMore()) {
                break;
            }

            try {
                remoteFileListResult = client.files().listFolderContinue(remoteFileListResult.getCursor());
            } catch (DbxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
