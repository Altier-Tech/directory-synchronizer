package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import tech.altier.AppProperties.RemoteFileInfo;
import tech.altier.synchronizer.Application;

public class RemoteFiles {
    private final DbxClientV2 client;
    private static final RemoteFileInfo remoteFileInfo;
    private static final RemoteFiles instance;

    static {
        try {
            instance = new RemoteFiles();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
        remoteFileInfo = RemoteFileInfo.getInstance();
    }

    private RemoteFiles() throws DbxException {
        client = Application.client;
    }

    /**
     * Returns the singleton instance of this class.
     * @return The singleton instance of RemoteFiles class
     */
    public static RemoteFiles getInstance() {
        return instance;
    }

    /**
     * Populates the local database that contains the paths and the hashes
     * of the files in the remote repository.
     * Do not call this if the database is already populated.
     * @throws DbxException If an error occurs while communicating with Dropbox
     */
    public static void populateRemoteFilesInfo() throws DbxException {
        ListFolderResult result = getDetailedRemoteFileInfo();

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

    /**
     * Returns a ListFolderResult object that contains the detailed information
     * of the files in the remote repository.
     * @return ListFolderResult object that contains the detailed information
     * @throws DbxException If an error occurs while communicating with Dropbox
     */
    public ListFolderResult getDetailedRemoteFileInfo() throws DbxException {
        return client.files()
                .listFolderBuilder("")
                .withIncludeDeleted(false)
                .withRecursive(true)
                .withIncludeMediaInfo(true)
                .start();
    }
}
