module com.example.mpa {
    requires javafx.fxml;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires java.sql;
    requires java.desktop;
    requires de.mkammerer.argon2;
    requires com.jfoenix;
    requires javafx.controls;
    requires org.apache.pdfbox;


    opens com.example.mpa to javafx.fxml;
    exports com.example.mpa;
}