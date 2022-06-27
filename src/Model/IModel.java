package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;

public interface IModel {
    void generateMaze(int row, int col);
    int[][] getMaze();
    public void solveMaze();
    void playerMove(KeyCode direction);
    public int getPlayerRow();
    public int getPlayerCol();

    public ArrayList<Point2D> getSolution();

    public int getStart_row();
    public int getStart_col();
    public int getEnd_row();
    public int getEnd_col();

    public void save(File my_file);

    public void Load(File my_file);

    public void stopServers();
    public int getThreads();
    public String getGenerating();
    public  String getSearching();
}
