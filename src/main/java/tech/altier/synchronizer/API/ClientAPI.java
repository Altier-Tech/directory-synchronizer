package tech.altier.synchronizer.API;

import com.dropbox.core.DbxException;
import tech.altier.synchronizer.Main;

import java.io.*;

public interface ClientAPI {
    public void uploadFile(String path) throws DbxException;

    public void downloadFile(String remotePath) throws IOException, DbxException;

    public void deleteFile(String path) throws DbxException;
}
