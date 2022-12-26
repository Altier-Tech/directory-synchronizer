package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DropboxClient {
    public static DbxClientV2 client;

    public DropboxClient() {
        client = Main.client;
    }

    public void uploadFile(String path) {
        try (InputStream in = new FileInputStream(path)) {
            log("File upload started: " + path + " on thread: " + Thread.currentThread().getName());
            FileMetadata metadata = client.files().uploadBuilder(path)
                    .uploadAndFinish(in);
            log("File upload finished: " + path + " on thread: " + Thread.currentThread().getName());
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
    }

    private void log(String message) {
        System.out.println(ThreadColor.ANSI_CYAN + "DBClient: \t" + message);
    }
}
