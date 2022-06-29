package sample;
import View.IView;
import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import com.sun.glass.ui.View;
import View.MyViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.geometry.Rectangle2D;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Observer;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HeadStage.fxml"));
        Parent root  =  fxmlLoader.load();

        primaryStage.setTitle("Maze Game");
        Scene scene = new Scene(root, 400, 300);
        try {
            scene.getStylesheets().add(getClass().getResource("Style1.css").toExternalForm());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        primaryStage.setScene(scene);
        IView view = fxmlLoader.getController();
        view.set_head_scene(scene);
        view.setStage(primaryStage);
        primaryStage.show();





    }











    public static void main(String[] args) {
        launch(args);
    }
}
