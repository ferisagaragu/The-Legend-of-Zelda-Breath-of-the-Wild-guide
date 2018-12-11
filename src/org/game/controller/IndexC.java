package org.game.controller;

import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.game.model.ViewCreator;
import org.javabrain.fx.container.Modal;
import org.javabrain.fx.structure.Controller;
import org.javabrain.util.web.service.Email;


public class IndexC extends Controller implements EventHandler {

    @FXML private JFXTabPane tabPane;
    @FXML private HBox box;
    @FXML private Button homeBtn;
    @FXML private Button sendBtn;
    @FXML private StackPane root;

    private Button resetBtn;

    @Override
    public void init(Object... params) {
        resetBtn = new Button("Reset ^");
        super.init(params);
    }

    @Override
    public void custom() {
        box.getStyleClass().add("box-back");
        ViewCreator.load(this,tabPane);
    }

    @Override
    public void onAction() {

        homeBtn.setOnAction( evt -> {
            tabPane.getSelectionModel().select(0);
        });

        sendBtn.setOnAction( evt -> {
            JFXTextArea editor = new JFXTextArea();
            Modal.showInput(root,"Reportar un error.","Si tienes algun error con el programa puedes reportarlo",editor).setOnAction( ev -> {
                Email.sendMail("ferisagaragu@gmail.com","ERROR ZELDA - GUIE",editor.getText());
            });
        });

        root.setOnKeyPressed( evt -> {

            if (evt.getCode() == KeyCode.F1) {
                box.getChildren().add(resetBtn);
            }

        });

        resetBtn.setOnAction( evt -> {
            ViewCreator.reset(this,tabPane);
        });
    }

    @Override
    public void handle(Event event) {

        if (event.getSource().getClass().getName().equals("com.jfoenix.controls.JFXCheckBox")) {
            ViewCreator.searchCheck(event,this,tabPane);
        }

        if (event.getSource().getClass().getName().equals("javafx.scene.control.Hyperlink")) {
            ViewCreator.searchHyper(event,this,tabPane);
        }

    }
}
