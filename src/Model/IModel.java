package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IModel {
    void generateMaze(int row, int col);
    Maze getMaze();
    public Solution solveMaze();
    void playerMove(int direction);
    public int getPlayerRow();
    public int getPlayerCol();
}
