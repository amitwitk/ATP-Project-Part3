package Model;

import algorithms.mazeGenerators.Maze;

public interface IModel {
    void generateMaze(int row, int col);
    Maze getMaze();

    void playerMove(int direction);
    public int getPlayerRow();
    public int getPlayerCol();
}
