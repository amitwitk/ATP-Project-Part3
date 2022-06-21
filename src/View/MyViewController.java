package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MyViewController implements IView {

    public MyMazeGenerator generator;
    public TextField Col_textBox;
    public TextField Rows_textBox;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    public Label playerRow;
    public Label playerCol;
    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }
    public String[] Avat;
    public ObservableList<String> avatars ;


    public ComboBox<String> ChooseAvatarBox;

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }
    public MazeDisplay mazeDisplayer;
    public void generateMazeButton () {

        if (generator ==  null){
            generator = new MyMazeGenerator();
        }

        int rows = Integer.valueOf(Rows_textBox.getText());
        int cols = Integer.valueOf(Col_textBox.getText());

        Maze maze = this.generator.generate(cols,rows);
        mazeDisplayer.drawMaze(maze);


    }

    public void keyPressed(KeyEvent keyEvent) {
        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();

        switch (keyEvent.getCode()) {
            case UP -> row -= 1;
            case DOWN -> row += 1;
            case RIGHT -> col += 1;
            case LEFT -> col -= 1;
        }
        setPlayerPosition(row, col);

        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            playerRow.textProperty().bind(updatePlayerRow);
            playerCol.textProperty().bind(updatePlayerCol);









    }
    public void MazeDisplayMouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }



}
