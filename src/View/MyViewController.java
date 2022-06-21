package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class MyViewController implements IView {

    public MyMazeGenerator generator;
    public TextField Col_textBox;
    public TextField Rows_textBox;
//    StringProperty updatePlayerRow = new SimpleStringProperty();
//    StringProperty updatePlayerCol = new SimpleStringProperty();
//    public Label playerRow;
//    public Label playerCol;

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

    }


//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//            playerRow.textProperty().bind(updatePlayerRow);
//            playerCol.textProperty().bind(updatePlayerCol);
//
//
//    }
}
