module com.example.textprocessing {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.textprocessing to javafx.fxml;
    exports com.example.textprocessing;
    exports com.example.textprocessing.controllers;
    opens com.example.textprocessing.controllers to javafx.fxml;
}