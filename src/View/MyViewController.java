package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer {

    //public MyMazeGenerator generator;
    public TextField Col_textBox;
    public TextField Rows_textBox;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    public Label playerRow;
    public Label playerCol;
    public MyViewModel ViewModel;
    public ComboBox<String> ChooseAvatarBox;

    private String[] avatars={"Icy Tower", "sonicX"};
    private Maze maze;
    public MazeDisplay mazeDisplayer;
    public BorderPane myBorderPane;
    public Pane MazePane;
    private Alert alert;
    private DialogPane dialog;
    MediaPlayer mediaPlayer;

    public MenuItem new_button;
    private double width;
    private double height;

    public Stage stage;
    @FXML
    Button MuteButton;

    public void new_maze()
    {
        generateMazeButton();
    }
    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }
    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }
    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }
    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }
    public void generateMazeButton () {
        ViewModel.generateMaze(Integer.parseInt(Rows_textBox.getText()), Integer.parseInt(Col_textBox.getText()));
        music();
        width = mazeDisplayer.getScaleX();
        height=mazeDisplayer.getScaleY();


    }
    public void setViewModel(MyViewModel vm)
    {
        ViewModel = vm;
    }

    public void playerMove(KeyEvent ke) {
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();
//        int direction =0;
//        switch (ke.getCode()) {
//            case NUMPAD1:
//                direction = 1;
//                break;
//            case DOWN:
//            case NUMPAD2:
//                direction = 2;
//                break;
//            case NUMPAD3:
//                direction = 3;
//                break;
//            case LEFT:
//            case NUMPAD4:
//                direction = 4;
//                break;
//            case NUMPAD5:
//                direction = 5;
//                break;
//            case RIGHT:
//            case NUMPAD6:
//                direction = 6;
//                break;
//            case NUMPAD7:
//                direction = 7;
//                break;
//            case UP:
//            case NUMPAD8:
//                direction = 8;
//                break;
//            case NUMPAD9:
//                direction = 9;
//                break;
//        }
        ViewModel.playerMove(ke.getCode());
        ke.consume();
    }

    public void setPlayerPosition(int row, int col){
        if (mazeDisplayer.setPlayerPosition(row, col) == 0) {
            setUpdatePlayerRow(row);
            setUpdatePlayerCol(col);
        }
        else{
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
            mazeDisplayer.drawMaze(maze);


        }
    }
    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            double zoom_fac = 1.05;
            if (scroll.getDeltaY() < 0) {
                zoom_fac = 2.0 - zoom_fac;
            }
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
        mazeDisplayer.widthProperty().bind(MazePane.widthProperty());
        mazeDisplayer.heightProperty().bind(MazePane.heightProperty());
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.widthProperty().bind(MazePane.widthProperty());
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.heightProperty().bind(MazePane.heightProperty());
        });
        width = mazeDisplayer.getScaleX();
        height=mazeDisplayer.getScaleY();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }
    public void MazeDisplayMouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
    public void sovleMazeButton(){
        ViewModel.solveMaze();
    }
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
                mazeDisplayer.drawMaze(maze);
                mazeDisplayer.requestFocus();
            }
            //todo maybe save solution here also
            if (arg.equals("solved"))
            {
                mazeDisplayer.drawSol(ViewModel.getSolution());
            }
            if (arg.equals("saved"))
            {
                alertInfo = new Alert(Alert.AlertType.CONFIRMATION);
                alertInfo.setHeaderText("Save");
                TextArea area = new TextArea("Save was successful");
            }
        }
    }
    public void mouseDragged(MouseEvent mouseEvent) {
        if(ViewModel.getMaze() != null) {
            int maximumSize = Math.max(ViewModel.getMaze().getColumns(), ViewModel.getMaze().getRows());
            double mousePosX=helperMouseDragged(maximumSize,mazeDisplayer.getHeight(),
                    ViewModel.getMaze().getRows(),mouseEvent.getX(),mazeDisplayer.getWidth() / maximumSize);
            double mousePosY=helperMouseDragged(maximumSize,mazeDisplayer.getWidth(),
                    ViewModel.getMaze().getColumns(),mouseEvent.getY(),mazeDisplayer.getHeight() / maximumSize);
            if ( mousePosX == ViewModel.getPlayerCol() && mousePosY < ViewModel.getPlayerRow() )
                ViewModel.playerMove(KeyCode.NUMPAD8);
            else if (mousePosY == ViewModel.getPlayerRow() && mousePosX > ViewModel.getPlayerCol() )
                ViewModel.playerMove(KeyCode.NUMPAD6);
            else if ( mousePosY == ViewModel.getPlayerRow() && mousePosX < ViewModel.getPlayerCol() )
                ViewModel.playerMove(KeyCode.NUMPAD4);
            else if (mousePosX == ViewModel.getPlayerCol() && mousePosY > ViewModel.getPlayerRow()  )
                ViewModel.playerMove(KeyCode.NUMPAD2);

        }
    }
    private  double helperMouseDragged(int maxsize, double canvasSize, int mazeSize,double mouseEvent,double temp){
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
                    mediaPlayer.seek(Duration.ZERO);
                }
            });
            mediaPlayer.play();
            mediaPlayer.setVolume(0.02);
        }

    }
    Boolean isMuted = false;
    public void muteMusic(ActionEvent actionEvent) {
        if (isMuted == false) {
            mediaPlayer.stop();
            MuteButton.setStyle("-fx-background-color: linear-gradient(#12e136, #50e58d)");
            isMuted=true;
        }
        else {
            mediaPlayer.play();
            MuteButton.setStyle("-fx-background-color: linear-gradient(#7cafc2,#86c1b9)");
            isMuted=false;
        }

    }
    Alert alertInfo;

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
    }
    Alert alertAbout;
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
    }

    public void Save(ActionEvent actionEvent) {
        FileChooser fs = new FileChooser();
        fs.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze",".maze"));
        File my_file = fs.showSaveDialog(stage);
        ViewModel.save(my_file);
    }

    public void setStage(Stage s)
    {
        this.stage = s;
    }
}
