package org.game.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.javabrain.Neuron;
import org.javabrain.fx.container.Modal;
import org.javabrain.util.data.Json;
import org.javabrain.util.data.Numeric;
import org.javabrain.util.resource.Archive;

import java.util.ArrayList;

public class ViewCreator {

    private static int totalDo = 0;
    private static int hero = 0;
    private static int zoneComplete = 0;
    private static ArrayList<StackPane> roots =  new ArrayList<>();

    public static void load(EventHandler evt, TabPane tabPane) {

        Json js = (Json) Neuron.getStash("levels_data");
        ArrayList<Tab> tabs = new ArrayList<>();

        int totalSantu = 0;
        int totalSantuDo = 0;

        if (js != null) {

            for(Json json:js.values()) {

                //COMPONENETES DEL TAB
                Tab index = new Tab();
                VBox conte = new VBox();
                conte.setPadding(new Insets(20,15,20,15));
                conte.getStyleClass().add("stack-pane");

                for(Json obj :json.getJSONArray("sanctuary").values()){

                    HBox box = new HBox();
                    box.setSpacing(20);

                    //CONFIGURACION DEL LINK
                    Hyperlink link = new Hyperlink();
                    link.setText(obj.getString("name"));
                    link.setOnAction(evt);
                    link.setId(obj.getString("name")+","+json.getString("name"));

                    //CONFIGURACION DEL CHECKBOX
                    JFXCheckBox checkBox = new JFXCheckBox("");
                    checkBox.setSelected(obj.getBoolean("do"));
                    checkBox.setOnAction(evt);
                    checkBox.setId(obj.getString("name")+","+json.getString("name"));
                    if (obj.getBoolean("do")){
                        totalDo++;
                        totalSantuDo++;
                    }

                    //DESCRIPCION
                    if (!obj.getString("description").isEmpty()) {
                        Tooltip description = new Tooltip(obj.getString("description"));
                        link.setTooltip(description);
                    }

                    //VESES VISTO
                    Label see = new Label(obj.getString("see"));

                    //DIFICULTAD
                    if (obj.getBoolean("requires")) {
                        link.getStyleClass().add("hard");
                        checkBox.getStyleClass().add("hard");
                        hero++;
                    } else {
                        link.getStyleClass().add("normal");
                        checkBox.getStyleClass().add("normal");
                    }

                    //CSS
                    link.getStyleClass().add("normal-list");
                    checkBox.getStyleClass().add("normal-list");

                    //ASIGNACION DE DATOS
                    box.getChildren().addAll(checkBox,link,see);
                    conte.getChildren().add(box);

                    //CONTADOR DE SANTUARIOS
                    totalSantu++;
                }

                if (totalSantuDo == totalSantu) {
                    index.setText(json.getString("name") + " *");
                    zoneComplete++;
                } else {
                    index.setText(json.getString("name") + " ( " + totalSantuDo + " / " + totalSantu + ")");
                }

                totalSantu = 0;
                totalSantuDo = 0;

                StackPane finalPane =  new StackPane(conte);
                index.setContent(finalPane);
                roots.add(finalPane);
                tabs.add(index);
            }


        }

        VBox box = new VBox();
        StackPane pane = new StackPane(box);
        double pors = Numeric.percentage(115,totalDo);

        //CSS
        pane.getStyleClass().add("stack-pane");
        pane.getStyleClass().add("status");
        box.setPadding(new Insets(20,15,20,15));

        Label level = new Label("-Nivel : ❤❤❤");

        int multip = 4;
        int herad = 3;
        boolean pass = true;
        for (int i = 1; i <= totalDo; i++) {
            if (i == multip) {
                herad++;
                if (herad > 15 && pass) {
                    level.setText(level.getText() + "\n           ");
                    pass = false;
                }
                level.setText(level.getText() + "❤");
                multip = multip + 4;
            }
        }

        //DATOS PRINCIPALES
        box.getChildren().addAll(new Label("-Porcentaje : " + pors + " %"),
                new Label("-Completados : " + totalDo),
                new Label("-Restantes : " + (115 - totalDo)),
                level,
                new Label("-Misiónes Heroicas : " + hero),
                new Label("-Zonas completadas : " + zoneComplete));

        //ASIGNACION DE TABS
        Tab tab = new Tab("Home/Status - " + pors + " %");
        tab.setContent(pane);

        tabPane.getTabs().add(tab);
        tabPane.getTabs().addAll(tabs);
    }

    public static void searchCheck(Event event, EventHandler evt, TabPane tabPane) {

        JFXCheckBox checkBox = (JFXCheckBox) event.getSource();
        String[] location = checkBox.getId().split(",");
        Json js = (Json) Neuron.getStash("levels_data");

        int loc1 = 0;
        int loc2 = 0;

        for (Json loc : js.values()) {

            if (loc.getString("name").equals(location[1])) {

                for (Json san : loc.getJSONArray("sanctuary").values()) {

                    if (san.getString("name").equals(location[0])) {

                        Json newData = js.getJSONArray(loc1).getJSONArray("sanctuary").getJSONArray(loc2);
                        String in = js.toString();
                        String out = newData.toString();
                        Json outJS = new Json(in.replace(out,newData.put("do",!newData.getBoolean("do")).toString()));
                        outJS.write(Archive.PROYECT_PATH + Neuron.param("levels_data"));
                        tabPane.getTabs().clear();
                        Json levels_data = new Json(Archive.PROYECT_PATH + Neuron.param("levels_data"));
                        Neuron.setStash("levels_data",levels_data);

                        ViewCreator.reset(evt,tabPane);

                        tabPane.getSelectionModel().select(loc1 + 1);
                    }
                    loc2++;
                }

            }
            loc1++;

        }
    }

    public static void searchHyper(Event event, EventHandler evt, TabPane tabPane) {

        Hyperlink hyperlink = (Hyperlink) event.getSource();
        String[] location = hyperlink.getId().split(",");
        Json js = (Json) Neuron.getStash("levels_data");

        int loc1 = 0;
        int loc2 = 0;

        for (Json loc : js.values()) {

            if (loc.getString("name").equals(location[1])) {

                for (Json san : loc.getJSONArray("sanctuary").values()) {

                    if (san.getString("name").equals(location[0])) {

                        Json newData = js.getJSONArray(loc1).getJSONArray("sanctuary").getJSONArray(loc2);
                        //Petition.openURL(newData.getString("link"));
                        String in = js.toString();
                        String out = newData.toString();
                        Json outJS = new Json(in.replace(out,newData.put("see",newData.getInteger("see") + 1).toString()));
                        outJS.write(Archive.PROYECT_PATH + Neuron.param("levels_data"));
                        tabPane.getTabs().clear();
                        Json levels_data = new Json(Archive.PROYECT_PATH + Neuron.param("levels_data"));
                        Neuron.setStash("levels_data",levels_data);

                        ViewCreator.reset(evt,tabPane);
                        tabPane.getSelectionModel().select(loc1 + 1);

                        WebView browser = new WebView();
                        WebEngine webEngine = browser.getEngine();
                        webEngine.load(ViewCreator.class.getResource("/html/" + san.getString("link")).toExternalForm());

                        JFXButton close = new JFXButton("X");
                        close.setOnAction( ev ->{
                            Modal.getDialog().close();
                        });
                        StackPane pane = new StackPane(new VBox(close,browser));
                        pane.getStyleClass().add("box-back");
                        Modal.showCustom(roots.get(loc1),pane);

                    }
                    loc2++;
                }

            }
            loc1++;

        }
    }

    public static void reset(EventHandler evt, TabPane tabPane) {

        totalDo = 0;
        hero = 0;
        zoneComplete = 0;
        roots.clear();
        int selection = tabPane.getSelectionModel().getSelectedIndex();
        tabPane.getTabs().clear();

        Json levels_data = new Json(Archive.PROYECT_PATH + Neuron.param("levels_data"));
        Neuron.setStash("levels_data",levels_data);

        load(evt,tabPane);

        tabPane.getSelectionModel().select(selection);
    }

}
