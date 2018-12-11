import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.game.controller.IndexC;
import org.javabrain.Neuron;
import org.javabrain.fx.control.Frame;
import org.javabrain.util.data.Json;
import org.javabrain.util.resource.Archive;
import org.javabrain.util.resource.R;

public class App extends Application{

    private Json levels_data;
    private Frame index = new Frame(R.getDrawable("index.fxml"),"The Legend of Zelda: Breath of the Wild - Guide (Build " + Neuron.build() + ")",new Image(R.getImg("icon.png")),R.getStyle("style.css"));

    @Override
    public void start(Stage primaryStage) {
        //Neuron.init();

        Font.loadFont(App.class.getResource("/res/font/TRIFORCE.ttf").toExternalForm(), 14);

        //PRECARGA DE DATOS
        levels_data = new Json(Archive.PROYECT_PATH + Neuron.param("levels_data"));
        Neuron.setStash("levels_data",levels_data);
        //=============================================================================

        //VISTAS DE JAVA FX
        IndexC indexC = (IndexC) index.getFXML();
        indexC.setStage(index.getStage());
        indexC.init();
        index.getStage().setMinWidth(600);
        index.getStage().setMinHeight(600);
        //=============================================================================

        index.showOnCenter();
    }

    public static void main(String[] args) {
        launch(args);
        /*int i = 0;
        File[] file = new File("C:\\GitKraken\\bread_of_the_wild\\src\\html").listFiles();
        ArrayList<String> datas = new ArrayList<>();

        for (File f:file) {
            if (f.getName().contains(".html")) {

                String string = Archive.read(f.getPath());
                string = string.replace("\n","").replace("<img                    src=\"img/","<img src=\"img/")
                .replace("                                                                                alt=", "alt=");

                String[] data = string.split("<img src=\"img/");

                for (String dt : data) {
                    if (!dt.contains("<!DOCTYPE html PUBLIC")) {
                        String[] split = dt.split("\" alt=\"");
                        i++;
                        if (!split[0].equals("botonvolver.gif")) {

                            String out = split[0].replace("\"                                   alt=\"The Legend of Zelda: Breath of the Wild\" width=\"336\" height=\"194\" border=\"1\"","").replace("\"                                                         alt=\"The Legend of Zelda: Breath of the Wild\" width=\"336\"","")
                                    .replace("                                   class=\"aSwitch\"/>","")
                                    .replace("\"                                                          alt=\"The Legend of Zelda: Breath of the Wild\" width=\"336\"                                                          height=\"194\" border=\"1\" class=\"aSwitch\"/></p>            <p> Las importantes son las que est&aacute;n dentro de la cueva, en el techo. Debes usar una <a                    href=\"inventario-arcos.html\">flecha bomba</a> para alcanzarla f&aacute;cilmente. Cuando la                destruyas ver&aacute;s la corriente pasar por el interior de la cueva.</p>            <p align=\"center\">","");

                            if (out.contains("</p>")) {
                                out = out.split("                                                         height=\"194\" border=\"1\" class=\"aSwitch\"/></p>")[0];
                            }

                            datas.add(out);
                        }

                    }
                }

            }
        }

        System.out.println("Archivos usados : " + i);
        System.out.println(datas);


        datas.forEach( e ->{
            System.out.println(e);
            Console.command(new String[]{"MOVE \"C:\\GitKraken\\bread_of_the_wild\\src\\html\\img\\"+e+"\" \"C:\\GitKraken\\bread_of_the_wild\\src\\html\\imagenUse\\"+e+"\""});
        });

        System.out.println(datas.size());*/
    }

}
