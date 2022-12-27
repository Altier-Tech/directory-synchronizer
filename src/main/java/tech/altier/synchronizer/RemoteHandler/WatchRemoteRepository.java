package tech.altier.synchronizer.RemoteHandler;

import com.dropbox.core.v2.DbxClientV2;
import tech.altier.synchronizer.Main;

public class WatchRemoteRepository {
    public WatchRemoteRepository() {
        
    }

    public void start() {
        Thread watcher = new Thread(
                new RemoteListener()
        );
        watcher.start();
    }
}
