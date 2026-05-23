module FinalProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    // 開放你的 package 讓 JavaFX 系統讀取（假設你的 main 在 com.example 套件下）
    opens com.example.finalproject to javafx.fxml;
    exports com.example.finalproject;
}