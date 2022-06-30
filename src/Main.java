package sample;
import View.IView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/HeadStage.fxml"));
        Parent root  =  fxmlLoader.load();

        primaryStage.setTitle("Maze Game");
        Scene scene = new Scene(root, 400, 300);
        try {
            scene.getStylesheets().add(getClass().getResource("/View/Style1.css").toExternalForm());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        primaryStage.setScene(scene);
        IView view = fxmlLoader.getController();
        view.set_head_scene(scene);
        view.setStage(primaryStage);
        //view.resizeVbox();
        primaryStage.show();





    }











    public static void main(String[] args) {
        launch(args);
    }
}
