module com.nocturnal {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.nocturnal to javafx.fxml;
    exports com.nocturnal;
}
