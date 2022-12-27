package tech.altier.synchronizer.GUIClient;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class ListViewRefreshService extends ScheduledService<Void> {
    @Override
    protected Task<Void> createTask(){
        return new Task<Void>(){
            @Override
            protected Void call(){
                Platform.runLater(() -> {
                    /* Modify you GUI properties... */

                });
                return null;
            }
        }
    }
}
