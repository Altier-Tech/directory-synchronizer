module tech.altier.synchronizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires dropbox.core.sdk;

    opens tech.altier.synchronizer to javafx.fxml;
    exports tech.altier.synchronizer;
}