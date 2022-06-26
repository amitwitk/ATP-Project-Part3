package Model;


import Server.*;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.util.Observable;

public class MyModel extends Observable implements IModel  {

    public MyMazeGenerator generator;
    private Maze maze;
    private Solution solution;

    private int playerRow;

    private int playerCol;
    ISearchingAlgorithm solver;

    private Server maze_generate_server;

    private Server maze_solver_server;


    public MyModel() {
        this.generator = null;
        this.maze = null;
        this.solution = null;
        playerCol = 0;
        playerRow = 0;

//        maze_generate_server = new Server(5400, 1000, new ServerStrategyGenerateMaze());
//        maze_solver_server = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void solveMaze() {
        if (maze !=null){
            SearchableMaze searchableMaze = new SearchableMaze(maze);
            solver = new BestFirstSearch();
            //ISearchable problem = searchableMaze;
            solution = solver.solve(searchableMaze);
            setChanged();
            notifyObservers("solved");
        }
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void playerMove(KeyCode direction) {

        switch(direction) {

            case NUMPAD1:
                if (checkBoundary(playerRow + 1, playerCol - 1) && maze.getValue(playerCol - 1, playerRow + 1) == 0
                        && (maze.getValue(playerCol - 1, playerRow) == 0 || maze.getValue(playerCol, playerRow + 1) == 0)) {
                    playerRow += 1;
                    playerCol -= 1;
                }
                break;
            case NUMPAD2:
            case DOWN:
                if (checkBoundary(playerRow + 1, playerCol) && maze.getValue(playerCol, playerRow + 1) == 0) {
                    playerRow += 1;
                }
                break;
            case NUMPAD3:
                if (checkBoundary(playerRow + 1, playerCol + 1) && maze.getValue(playerCol + 1, playerRow + 1) == 0
                        && (maze.getValue(playerCol + 1, playerRow) == 0 || maze.getValue(playerCol, playerRow + 1) == 0)) {
                    playerRow += 1;
                    playerCol += 1;
                }
                break;
            case LEFT:
            case NUMPAD4:
                if (checkBoundary(playerRow, playerCol - 1) && maze.getValue(playerCol - 1, playerRow) == 0) {
                    playerCol -= 1;
                }
                break;
            case RIGHT:
            case NUMPAD6:
                if (checkBoundary(playerRow, playerCol + 1) && maze.getValue(playerCol + 1, playerRow) == 0) {
                    playerCol += 1;
                }
                break;
            case NUMPAD7:
                if (checkBoundary(playerRow - 1, playerCol - 1) && maze.getValue(playerCol - 1, playerRow - 1) == 0
                        && (maze.getValue(playerCol - 1, playerRow) == 0 || maze.getValue(playerCol, playerRow - 1) == 0)) {
                    playerRow -= 1;
                    playerCol -= 1;
                }
                break;
            case UP:
            case NUMPAD8:
                if (checkBoundary(playerRow - 1, playerCol) && maze.getValue(playerCol, playerRow - 1) == 0) {
                    playerRow -= 1;
                }
                break;
            case NUMPAD9:
                if (checkBoundary(playerRow - 1, playerCol + 1) && maze.getValue(playerCol + 1, playerRow - 1) == 0
                        && (maze.getValue(playerCol + 1, playerRow) == 0 || maze.getValue(playerCol, playerRow - 1) == 0)) {
                    playerRow -= 1;
                    playerCol += 1;
                }
                break;
        }
        setChanged();
        notifyObservers("moved");





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
        setChanged();
        notifyObservers("generated");


    }

    public Solution getSolution() {
        return solution;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }



}
