package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.v2.DbxClientV2;
import tech.altier.synchronizer.Main;

public class DropboxClient {
    public static DbxClientV2 client;

    static {
        client = Main.client;
    }

    public static void uploadFile(String path) {
        
    }
}
