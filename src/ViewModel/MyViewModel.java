package ViewModel;

import Model.IModel;
import Model.MyModel;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.control.Alert;

import javafx.scene.input.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import static javafx.scene.input.KeyCode.NUMPAD1;
import static javafx.scene.input.KeyCode.NUMPAD2;


public class MyViewModel extends Observable implements Observer {

    public IModel model;
    private Maze maze;

    private int playerRow;
    private int playerCol;

    private Solution solution;

    public MyViewModel(IModel model) {
        this.model = model;
        this.maze = null;
        playerCol=0;
        playerRow =0;
    }

    public Solution getSolution() {
        return solution;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model)
        {
            if (arg.equals("moved"))
            {
                playerRow = model.getPlayerRow();
                playerCol= model.getPlayerCol();
                setChanged();
                notifyObservers("moved");
            }
            if (arg.equals("generated"))
            {
                maze = model.getMaze();
                playerCol = model.getPlayerCol();
                playerRow= model.getPlayerRow();
                setChanged();
                notifyObservers("generated");
            }
            if (arg.equals("solved"))
            {
                solution = model.getSolution();
                setChanged();
                notifyObservers("solved");
            }
        }

    }

    public void generateMaze(int row, int col) {
        if (row < 0 || col < 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("problem with rows or columns");
            alert.show();
        }
        else {
            model.generateMaze(row, col);

        }
    }


    public Maze getMaze() {
        return maze;
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void playerMove(KeyEvent ke) {
        int direction =0;
        switch (ke.getCode()) {
            case NUMPAD1:
                direction = 1;
                break;
            case DOWN:
            case NUMPAD2:
                direction = 2;
                break;
            case NUMPAD3:
                direction = 3;
                break;
            case LEFT:
            case NUMPAD4:
                direction = 4;
                break;
            case NUMPAD5:
                direction = 5;
                break;
            case RIGHT:
            case NUMPAD6:
                direction = 6;
                break;
            case NUMPAD7:
                direction = 7;
                break;
            case UP:
            case NUMPAD8:
                direction = 8;
                break;
            case NUMPAD9:
                direction = 9;
                break;
        }
        model.playerMove(direction);


        }

    public void solveMaze() {
         model.solveMaze();
    }
}

