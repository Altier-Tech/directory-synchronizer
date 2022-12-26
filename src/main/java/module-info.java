module tech.altier.synchronizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires dropbox.core.sdk;
    requires javafx.web;
    requires java.desktop;

    opens tech.altier.Synchronizer to javafx.fxml;
    exports tech.altier.Synchronizer;
}