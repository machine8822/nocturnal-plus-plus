module com.nocturnal {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires jbcrypt;
    requires junit;

    opens com.controllers to javafx.fxml;

    exports com.nocturnal;
    exports com.model;
}
