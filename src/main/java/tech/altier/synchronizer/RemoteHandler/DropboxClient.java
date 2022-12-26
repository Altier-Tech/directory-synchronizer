package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import tech.altier.synchronizer.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DropboxClient {
    public static DbxClientV2 client;

    static {
        client = Main.client;
    }

    public static void uploadFile(String path) {
        try (InputStream in = new FileInputStream(path)) {
            FileMetadata metadata = client.files().uploadBuilder(path)
                    .uploadAndFinish(in);
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
    }
}
