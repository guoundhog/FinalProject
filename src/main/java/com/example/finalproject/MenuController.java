package com.example.finalproject;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class MenuController {
    @FXML
    private ImageView classic;

    @FXML
    private ImageView special;

    //一點點動畫效果
    @FXML
    public void bigText(MouseEvent e){
        Node node = (Node) e.getSource();
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.3), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setCycleCount(1);
        st.play();
    }

    @FXML
    public void smallText(MouseEvent e){
        Node node = (Node) e.getSource();
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.3), node);
        st.setFromX(1.2);
        st.setFromY(1.2);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.play();
    }

    public void classicMode() {

        SceneManager.switchScene("classic.fxml");
    }

    public void specialMode() {

        SceneManager.switchScene("special.fxml");
    }
}