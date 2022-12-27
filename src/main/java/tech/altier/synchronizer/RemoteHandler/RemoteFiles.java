package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import tech.altier.AppProperties.RemoteFileInfo;
import tech.altier.synchronizer.Main;

public class RemoteFiles {
    private final DbxClientV2 client;
    private final RemoteFileInfo remoteFileInfo;

    static {
        try {
            RemoteFiles instance = new RemoteFiles();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
    }

    private RemoteFiles() throws DbxException {
        client = Main.client;
        remoteFileInfo = RemoteFileInfo.getInstance();
    }

    /**
     * Populates the local database that contains the paths and the hashes
     * of the files in the remote repository.
     * Do not call this if the database is already populated.
     * @throws DbxException If an error occurs while communicating with Dropbox
     */
    private void populateRemoteFilesInfo() throws DbxException {
        ListFolderResult result = client.files()
                .listFolderBuilder("")
                .withIncludeDeleted(false)
                .withRecursive(true)
                .withIncludeMediaInfo(true)
                .start();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                FileMetadata fileMetadata = null;
                if (metadata instanceof FileMetadata) {
                    fileMetadata = (FileMetadata) metadata;
                }

                assert fileMetadata != null;
                remoteFileInfo.put(metadata.getPathLower(), fileMetadata.getContentHash());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
    }

    public ListFolderResult getDetailedRemoteFileInfo() throws DbxException {
        return client.files()
                .listFolderBuilder("")
                .withIncludeDeleted(false)
                .withRecursive(true)
                .withIncludeMediaInfo(true)
                .start();
    }
}
