package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;


import java.net.URL;
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
        ChooseAvatarBox.getItems().addAll(avatars);
        ViewModel.generateMaze(Integer.parseInt(Rows_textBox.getText()), Integer.parseInt(Col_textBox.getText()));
        maze = ViewModel.getMaze();
        mazeDisplayer.drawMaze(maze);
    }

    public void setViewModel(MyViewModel vm)
    {
        ViewModel = vm;
    }

    public void playerMove(KeyEvent keyevent) {
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();
        ViewModel.playerMove(keyevent);
        setPlayerPosition(ViewModel.getPlayerRow(), ViewModel.getPlayerCol());
        keyevent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
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


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }
    public void MazeDisplayMouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }




    @Override
    public void update(Observable o, Object arg) {

    }
}
