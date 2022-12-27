package tech.altier.synchronizer.RemoteHandler;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.Main;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DropboxClient {
    public static DbxClientV2 client;

    public DropboxClient() {
        client = Main.client;
    }

    public void uploadFile(String path) {
        File absPath = new File(path);
        String relPath = absPath.getName();
        try (InputStream in = new FileInputStream(path)) {
            log("File upload started: " + path);
            FileMetadata metadata = client.files().uploadBuilder("/" + relPath)
                    .uploadAndFinish(in);
            log("File upload finished: " + path);
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadFile(String remotePath) {
        // TODO
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_CYAN +
                Thread.currentThread().getName() +
                "DBClient: \t" +
                message
        );
    }
}
