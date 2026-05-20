package com.example.finalproject;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class MenuController {
    @FXML
    private Pane menuPane;

    @FXML
    private ImageView picture;

    @FXML
    private void handleKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            picture.setOpacity(0);
        }
    }
}
