package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import tech.altier.synchronizer.Main;

public class WatchRemoteRepository {
    public static DbxClientV2 client;

    public WatchRemoteRepository() {
        client = Main.client;
    }

    // Start monitoring the remote directory for any file changes
    public void start() throws DbxException {

    }
}
