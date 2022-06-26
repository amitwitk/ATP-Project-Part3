package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {
    void generateMaze(int row, int col);
    Maze getMaze();
    public void solveMaze();
    void playerMove(KeyCode direction);
    public int getPlayerRow();
    public int getPlayerCol();

    public Solution getSolution();

    public void save(File my_file);
}
