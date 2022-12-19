module tech.ultier.synchronizer {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens tech.ultier.synchronizer to javafx.fxml;
    exports tech.ultier.synchronizer;
}