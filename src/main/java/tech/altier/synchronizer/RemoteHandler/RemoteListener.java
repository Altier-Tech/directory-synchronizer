package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import tech.altier.AppProperties.RemoteFileInfo;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.Main;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;

public class RemoteListener implements Runnable {
    static RemoteFiles remoteFilesInstance;
    static RemoteFileInfo remoteFileInfo;
    private ListFolderResult remoteFileListResult;
    private HashMap<String, String> tempRemoteFileInfo;

    DbxClientV2 client;

    static {
        remoteFilesInstance = RemoteFiles.getInstance();
        remoteFileInfo = RemoteFileInfo.getInstance();
    }

    public RemoteListener() {
        client = Main.client;
        tempRemoteFileInfo = new HashMap<>();
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

                // Case 1: File is new
                if (!remoteFileInfo.containsKey(metadata.getPathLower())) {
                    // Doesn't exist, so need to download the remote file
                    // But this scenario is handled in the Main class
                    log("New file detected: " + metadata.getPathLower());
                } else if (!remoteFileInfo.get(metadata.getPathLower()).equals(fileMetadata.getContentHash())) {
                    // Case 2: File has been modified
                    log("File " + metadata.getPathLower() + " has been modified");
                    // Not the same, so need to..

                    // TODO Step 1: Delete the local file

                    // TODO Step 2: Download the remote file

                }

                // Add the metadata to a temporary map for test Case 3
                tempRemoteFileInfo.put(metadata.getPathLower(), fileMetadata.getContentHash());

                // Case 3: Check if any files have been deleted
                for (String path : remoteFileInfo.keySet()) {
                    if (!tempRemoteFileInfo.containsKey(path)) {
                        // TODO File has been deleted, so need to delete the local file
                        // Alert the user to check if the deletion should be permanent

                        // If yes, delete the local file

                        // If no, upload the file back to the remote repository
                    }
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

    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (NoSuchFileException e) {
            log("File delete error: No such file/directory exists");
        } catch (DirectoryNotEmptyException e) {
            log("File delete error: Directory is not empty!");
        } catch (IOException e) {
            log("File delete error: Invalid permissions!");
        }

        System.out.println("Deletion successful for file " + filePath);
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_PURPLE +
                        Thread.currentThread().getName() +
                        "\tRemoteListener: \t" +
                        message
        );
    }
}
