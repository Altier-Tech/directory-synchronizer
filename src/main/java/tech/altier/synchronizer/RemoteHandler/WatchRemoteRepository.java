package tech.altier.synchronizer.RemoteHandler;

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
