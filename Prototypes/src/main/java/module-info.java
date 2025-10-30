module com.kiarash.tournamentsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.librepdf.openpdf;
    requires javafx.media;
    requires java.desktop;
    opens com.kiarash.tournamentsystem to javafx.fxml;
    exports com.kiarash.tournamentsystem;
}
