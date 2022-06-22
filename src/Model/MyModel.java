package Model;


import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.util.Observable;

public class MyModel extends Observable implements IModel {

    public MyMazeGenerator generator;
    private Maze maze;
    private Solution solution;

    private int playerRow;

    private int playerCol;

    public MyModel() {
        this.generator = null;
        this.maze = null;
        this.solution = null;
        playerCol =0;
        playerRow =0;
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void playerMove(int direction) {

        switch(direction) {
            case 1:
                if (checkBoundary(playerRow + 1, playerCol - 1) && maze.getValue(playerCol - 1, playerRow + 1) == 0
                        && (maze.getValue(playerCol - 1, playerRow) == 0 || maze.getValue(playerCol, playerRow + 1) == 0)) {
                    playerRow += 1;
                    playerCol -= 1;
                }
                break;
            case 2:
                if (checkBoundary(playerRow + 1, playerCol) && maze.getValue(playerCol, playerRow + 1) == 0) {
                    playerRow += 1;
                }
                break;
            case 3:
                if (checkBoundary(playerRow + 1, playerCol + 1) && maze.getValue(playerCol + 1, playerRow + 1) == 0
                        && (maze.getValue(playerCol + 1, playerRow) == 0 || maze.getValue(playerCol, playerRow + 1) == 0)) {
                    playerRow += 1;
                    playerCol += 1;
                }
                break;
            case 4:
                if (checkBoundary(playerRow, playerCol - 1) && maze.getValue(playerCol - 1, playerRow) == 0) {
                    playerCol -= 1;
                }
                break;
            case 6:
                if (checkBoundary(playerRow, playerCol + 1) && maze.getValue(playerCol + 1, playerRow) == 0) {
                    playerCol += 1;
                }
                break;
            case 7:
                if (checkBoundary(playerRow - 1, playerCol - 1) && maze.getValue(playerCol - 1, playerRow - 1) == 0
                        && (maze.getValue(playerCol - 1, playerRow) == 0 || maze.getValue(playerCol, playerRow - 1) == 0)) {
                    playerRow -= 1;
                    playerCol -= 1;
                }
                break;
            case 8:
                if (checkBoundary(playerRow - 1, playerCol) && maze.getValue(playerCol, playerRow - 1) == 0) {
                    playerRow -= 1;
                }
                break;
            case 9:
                if (checkBoundary(playerRow - 1, playerCol + 1) && maze.getValue(playerCol + 1, playerRow - 1) == 0
                        && (maze.getValue(playerCol + 1, playerRow) == 0 || maze.getValue(playerCol, playerRow - 1) == 0)) {
                    playerRow -= 1;
                    playerCol += 1;
                }
                break;
        }
        notifyObservers();





        }



    private boolean checkBoundary(int row, int col)
    {
        if (row < 0 || row == maze.getRows())
        {
            return false;
        }
        if (col <0 || col == maze.getColumns())
        {
            return false;
        }
        return true;
    }




    public void generateMaze(int row, int col)
    {
        if (generator ==  null){
            generator = new MyMazeGenerator();
        }
        this.maze = this.generator.generate(col,row);
        playerRow = maze.getStartPosition().getRowIndex();
        playerCol = maze.getStartPosition().getColumnIndex();
        notifyObservers();


    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }
}
