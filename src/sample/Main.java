package sample;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import com.sun.glass.ui.View;
import View.MyViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.geometry.Rectangle2D;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Observer;

public class Main extends Application {
    AudioClip mediaPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //mediaPlayer = new AudioClip(this.getClass().getResource("resources/clips/icy_tower.mp3").toString());
        //mediaPlayer.play();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze Game");
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel ViewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.set_Resize(scene);
        view.setViewModel(ViewModel);
        ViewModel.addObserver((Observer)view);
        MyModel mymodel = (MyModel)model;
        mymodel.addObserver(ViewModel);




    }











    public static void main(String[] args) {
        launch(args);
    }
}
