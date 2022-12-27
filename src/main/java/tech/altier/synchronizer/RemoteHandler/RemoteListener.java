package tech.altier.synchronizer.RemoteHandler;

public class RemoteListener implements Runnable {
    @Override
    public void run() {
        // Adding a waiting period of 10 seconds
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
