package View;
import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import com.sun.javafx.iio.ImageStorageException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer {

    public TextField Col_textBox;
    public TextField Rows_textBox;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    public Label playerRow;
    public Label playerCol;
    public MyViewModel ViewModel;

    private int[][] maze;
    public MazeDisplay mazeDisplayer;
    public BorderPane myBorderPane;
    public Pane MazePane;
    private Alert alert;
    Alert alertAbout;
    private DialogPane dialog;
    MediaPlayer mediaPlayer;
    MediaPlayer winMedia;
    Boolean isMuted = false;

    public MenuItem new_button;
    private double width;
    private double height;
    Alert alertInfo;
    public Stage stage;
    @FXML
    Button MuteButton;
    Scene scene;
    Parent root;
    Stage Pstage;
    @FXML
    AnchorPane ScenePane;
    @FXML
    BorderPane ChoosePane;

    @FXML
    public ImageView MyImage;
    @FXML
    public VBox HeadStagePane;
    @FXML
    BorderPane headPane;

    @FXML
    ImageView myImageView;



//    Image myImage = new Image(getClass().getResourceAsStream("resources/images/sonic.jpg"));

    public void Switch_to_scene2(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        Pstage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,400,300);
        IModel model = new MyModel();
        MyViewModel ViewModel = new MyViewModel(model);
        IView view = fxmlLoader.getController();
        view.set_Resize(scene);
        view.setViewModel(ViewModel);
        view.setStage(Pstage);
        ViewModel.addObserver((Observer)view);
        MyModel mymodel = (MyModel)model;
        mymodel.addObserver(ViewModel);
        Pstage.setScene(scene);
        Pstage.show();
        //MuteButton.setDisable(true);

    }

    public void ChooseAvatarButton(ActionEvent event) throws IOException {
        if (mediaPlayer!= null){
            mediaPlayer.stop();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChooseStage.fxml"));
        Parent root = fxmlLoader.load();
        Pstage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,400,300);
        try {
            scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        MazeDisplay.avatar =0;
        Pstage.setScene(scene);
        Pstage.show();

    }
    public void changeRight(ActionEvent event) {
        if (MazeDisplay.avatar < MazeDisplay.avatars.length-1) {
            MazeDisplay.avatar++;
            myImageView.setImage(new Image(MazeDisplay.avatars[MazeDisplay.avatar]));
        }


    }
    public void changeLeft(ActionEvent event) {
        if (MazeDisplay.avatar > 0) {
            MazeDisplay.avatar--;
            myImageView.setImage(new Image(MazeDisplay.avatars[MazeDisplay.avatar]));
        }


    }
    public void set_head_scene(Scene scene){
        MyImage.fitHeightProperty().bind(HeadStagePane.heightProperty());
        MyImage.fitWidthProperty().bind(HeadStagePane.widthProperty());


    }




    public void new_maze(){generateMazeButton();}

    public String getUpdatePlayerRow() {return updatePlayerRow.get();}
    public void setUpdatePlayerRow(int updatePlayerRow) {this.updatePlayerRow.set(updatePlayerRow + "");}
    public String getUpdatePlayerCol() {return updatePlayerCol.get();}
    public void setUpdatePlayerCol(int updatePlayerCol) {this.updatePlayerCol.set(updatePlayerCol + "");}
    public void generateMazeButton () {
        ViewModel.generateMaze(Integer.parseInt(Rows_textBox.getText()), Integer.parseInt(Col_textBox.getText()));
        music();
    }
    public void setViewModel(MyViewModel vm) {ViewModel = vm;}

    public void playerMove(KeyEvent ke) {
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();
        ViewModel.playerMove(ke.getCode());
        ke.consume();}

    public void setPlayerPosition(int row, int col){
        if (mazeDisplayer.setPlayerPosition(row, col) == 0) {
            setUpdatePlayerRow(row);
            setUpdatePlayerCol(col);
        }
        else{
            mediaPlayer.stop();
            if (winMedia== null){
                win_music();
            }
            else {
                winMedia.play();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Congratulations! You succesfully solved the maze!",ButtonType.YES);
            alert.setHeaderText("Congratulations! You succesfully solved the maze!");
            alert.setContentText("To progress to the next level, press YES");
            dialog = alert.getDialogPane();
            dialog.getStylesheets().add(getClass().getResource("MainStyle.css").toString());
            dialog.getStyleClass().add("dialog");
            alert.showAndWait();
            int newrow = Integer.parseInt(Rows_textBox.getText());
            newrow = newrow*2;
            Rows_textBox.setText("" + newrow);
            int newCol = Integer.parseInt(Col_textBox.getText()) * 2;
            Col_textBox.setText("" + newCol);
            ViewModel.generateMaze(Integer.parseInt(Rows_textBox.getText()) , Integer.parseInt(Col_textBox.getText()) );
            maze = ViewModel.getMaze();
            mazeDisplayer.drawMaze(maze, ViewModel.getStart_row(), ViewModel.getStart_col(), ViewModel.getEnd_row(), ViewModel.getEnd_col());
            winMedia.stop();
            mediaPlayer.play();

        }
    }

    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            double zoom_fac = 1.05;
            if (scroll.getDeltaY() < 0) {
                zoom_fac = 2.0 - zoom_fac;}
            Scale newScale = new Scale();
            newScale.setPivotX(scroll.getX());
            newScale.setPivotY(scroll.getY());
            newScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
            mazeDisplayer.getTransforms().add(newScale);
            scroll.consume();
        }
    }


    public void set_Resize(Scene scene){
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.widthProperty().bind(MazePane.widthProperty());});
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.heightProperty().bind(MazePane.heightProperty());});
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);

    }

    public void MazeDisplayMouseClicked(MouseEvent mouseEvent) {mazeDisplayer.requestFocus();}
    public void sovleMazeButton(){
        ViewModel.solveMaze();
        mazeDisplayer.requestFocus();}


    @Override
    public void update(Observable o, Object arg) {
        if (o== ViewModel)
        {
            if (arg.equals("moved"))
            {
                setPlayerPosition(ViewModel.getPlayerRow(), ViewModel.getPlayerCol());
            }
            if (arg.equals("generated"))
            {
                maze = ViewModel.getMaze();
                mazeDisplayer.drawMaze(maze, ViewModel.getStart_row(), ViewModel.getStart_col(), ViewModel.getEnd_row(), ViewModel.getEnd_col());
                mazeDisplayer.requestFocus();
            }
            if (arg.equals("solved"))
            {
                mazeDisplayer.drawSol(ViewModel.getSolution());
            }
            if (arg.equals("saved"))
            {
                alertInfo = new Alert(Alert.AlertType.CONFIRMATION);
                alertInfo.setHeaderText("Save");
                alertInfo.setContentText("Save was successful");
                alertInfo.showAndWait();
            }
            if (arg.equals("not saved"))
            {
                alertInfo = new Alert(Alert.AlertType.ERROR);
                alertInfo.setHeaderText("Error saving");
                alertInfo.setContentText("saving encountered a problem");
                alertInfo.showAndWait();
            }
            if (arg.equals("loaded"))
            {
                maze = ViewModel.getMaze();
                mazeDisplayer.drawMaze(maze, ViewModel.getStart_row(), ViewModel.getStart_col(), ViewModel.getEnd_row(), ViewModel.getEnd_col());
                mazeDisplayer.requestFocus();
                alertInfo = new Alert(Alert.AlertType.CONFIRMATION);
                alertInfo.setHeaderText("Load");
                alertInfo.setContentText("Load was successful");
                alertInfo.showAndWait();
            }
            if (arg.equals("not loaded"))
            {
                alertInfo = new Alert(Alert.AlertType.ERROR);
                alertInfo.setHeaderText("Load");
                alertInfo.setContentText("there was an error loading");
                alertInfo.showAndWait();
            }
        }
    }
    public void mouseDragged(MouseEvent mouseEvent) {
        if(maze != null) {
            int max_size = Math.max(maze[0].length, maze.length);
            double mouse_x= wrapDrag(max_size,mazeDisplayer.getHeight(), maze.length,mouseEvent.getX(),mazeDisplayer.getWidth() / max_size);
            double mouse_y= wrapDrag(max_size,mazeDisplayer.getWidth(), maze[0].length,mouseEvent.getY(),mazeDisplayer.getHeight() / max_size);
            if ( mouse_x == ViewModel.getPlayerCol() && mouse_y < ViewModel.getPlayerRow() ) {
                ViewModel.playerMove(KeyCode.NUMPAD8);}
            else if (mouse_y == ViewModel.getPlayerRow() && mouse_x > ViewModel.getPlayerCol() ) {
                ViewModel.playerMove(KeyCode.NUMPAD6);}
            else if ( mouse_y == ViewModel.getPlayerRow() && mouse_x < ViewModel.getPlayerCol() ) {
                ViewModel.playerMove(KeyCode.NUMPAD4);}
            else if (mouse_x == ViewModel.getPlayerCol() && mouse_y > ViewModel.getPlayerRow()  ) {
                ViewModel.playerMove(KeyCode.NUMPAD2);}

        }
    }
    private  double wrapDrag(int maxsize, double canvasSize, int mazeSize, double mouseEvent, double temp){
        double cellSize=canvasSize/maxsize;
        double start = (canvasSize / 2 - (cellSize * mazeSize / 2)) / cellSize;
        double mouse = (int) ((mouseEvent) / (temp) - start);
        return mouse;
    }
    public void music() {
        if (mediaPlayer==null){
            String s = "resources/clips/icy_tower.mp3";
            Media h = new Media(Paths.get(s).toUri().toString());
            mediaPlayer = new MediaPlayer(h);
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);}});
            mediaPlayer.play();
            mediaPlayer.setVolume(0.05);
            MuteButton.setDisable(false);}
    }
    public void win_music() {
        if (winMedia==null){
            String s = "resources/clips/YXFBY9J-win.mp3";
            Media h = new Media(Paths.get(s).toUri().toString());
            winMedia = new MediaPlayer(h);
            winMedia.play();
            winMedia.setVolume(0.1);
            }
    }

    public void muteMusic(ActionEvent actionEvent) {
        if (isMuted == false) {
            mediaPlayer.stop();
            MuteButton.setStyle("-fx-background-color: linear-gradient(#12e136, #50e58d)");
            isMuted=true;}
        else {
            mediaPlayer.play();
            MuteButton.setStyle("-fx-background-color: linear-gradient(#7cafc2,#86c1b9)");
            isMuted=false;}
        mazeDisplayer.requestFocus();
    }


    public void Help_button_func() {

        alertInfo = new Alert(Alert.AlertType.INFORMATION);
        alertInfo.setHeaderText("Rules:");
        TextArea area = new TextArea("Please choose the size of the maze you want and type it in in rows and columns.\n" +
                "Then, click \"generate maze\" to see your beautiful maze. \n" +
                "Use the number pad/ arrows/ or drag with the mouse your player to the end point which is black. \n" +
                "If you need some help you can click the hint button to see a possible solution. \n" +
                "You can zoom in with ctrl+scroll. \n" +
                "If you like the music that awesome but if you don't you can mute it with the mute button. \n" +
                "Once you've made it to the end you can continue to next level. Good luck!\n");
        area.setWrapText(true);
        area.setEditable(false);
        alertInfo.getDialogPane().setContent(area);
        alertInfo.setResizable(true);
        dialog = alertInfo.getDialogPane();
        dialog.getStylesheets().add(getClass().getResource("MainStyle.css").toString());
        dialog.getStyleClass().add("dialog");
        alertInfo.showAndWait();
        mazeDisplayer.requestFocus();
    }
    public void exitButtonFunc(){
        this.stage.close();
        ViewModel.stopServers();
    }

    public void AboutButton_func(ActionEvent actionEvent) {
        alertAbout = new Alert(Alert.AlertType.INFORMATION);
        alertAbout.setHeaderText("About:");
        TextArea area = new TextArea("Searching Algorithms- \n" +
                "1. Breadth First Search- It starts at the root and explores all nodes at the present depth prior to moving on to the nodes at the next depth level.\n" +
                "2. Depth First Search- starts at the root and explores as far as possible along each branch before backtracking.\n" +
                "3. Best First Search - like breadth first search but will give priority to path with the smallest \"cost\" in our case it will prefer a diagnol step over a non- diagnol \n" +
                "\n" +
                "Generating Algorithms-\n" +
                "1. Empty Maze - will generate a maze withput walls\n" +
                "2. Simple Maze - will generate a maze in a way that each cell is picked randomly \n" +
                "3. My Maze - is based on Prim's algorithm \n" +
                "\n" +
                "The Creators-\n" +
                "Amit Vitkovsky \n" +
                "Or Fuchs\n");
        area.setWrapText(true);
        area.setEditable(false);
        alertAbout.getDialogPane().setContent(area);
        alertAbout.setResizable(true);
        dialog = alertAbout.getDialogPane();
        dialog.getStylesheets().add(getClass().getResource("MainStyle.css").toString());
        dialog.getStyleClass().add("dialog");
        alertAbout.showAndWait();
        mazeDisplayer.requestFocus();
    }

    public void Save(ActionEvent actionEvent) {
        FileChooser fs = new FileChooser();
        fs.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze",".maze"));
        File my_file = fs.showSaveDialog(stage);
        ViewModel.save(my_file);
        mazeDisplayer.requestFocus();
    }

    public void setStage(Stage s) {this.stage = s;}

    public void Load(ActionEvent actionEvent) {
        FileChooser fs = new FileChooser();
        fs.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze",".maze"));
        File my_file = fs.showOpenDialog(stage);
        ViewModel.Load(my_file);
        mazeDisplayer.requestFocus();
    }

    public void showProp(ActionEvent actionEvent) {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setContentText("Properties");
        al.setContentText(String.valueOf("Number of threads: " + ViewModel.getThreads())+ "\n"+ "Generating Algorithm: " + ViewModel.getGenerate() + "\n" + "Searching Algortihm: " + ViewModel.getSearch());
        al.setHeaderText("Propereties");
        al.showAndWait();
    }



}
