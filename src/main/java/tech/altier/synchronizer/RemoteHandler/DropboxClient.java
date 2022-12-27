package tech.altier.synchronizer.RemoteHandler;

import tech.altier.Thread.ThreadColor;
import tech.altier.synchronizer.Main;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.io.*;

public class DropboxClient {
    public static DbxClientV2 client;

    public DropboxClient() {
        client = Main.client;
    }

    public void uploadFile(String path) throws DbxException {
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

    public void downloadFile(String remotePath) throws IOException, DbxException {
        OutputStream downloadFile = new FileOutputStream(Main.repository.getPath() + "\\" + remotePath);
        log("File download started: " + remotePath);
        client.files().downloadBuilder("/" + remotePath).download(downloadFile);
        log("File download finished: " + remotePath);
    }

    public void deleteFile(String path) throws DbxException {
        log("File deletion started: " + path);
        client.files().deleteV2("/" + path);
        log("File deletion finished: " + path);
    }

    private void log(String message) {
        System.out.println(
                ThreadColor.ANSI_CYAN +
                Thread.currentThread().getName() +
                "\tDBClient: \t" +
                message
        );
    }
}
