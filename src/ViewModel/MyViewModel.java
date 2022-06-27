package ViewModel;

import Model.IModel;
import Model.MyModel;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import static javafx.scene.input.KeyCode.NUMPAD1;
import static javafx.scene.input.KeyCode.NUMPAD2;


public class MyViewModel extends Observable implements Observer {

    public IModel model;
    private int[][] maze;

    private int playerRow;
    private int playerCol;

    private int start_row;
    private int start_col;
    private int end_row;
    private int end_col ;

    public int getStart_row() {
        return start_row;
    }

    public int getStart_col() {
        return start_col;
    }

    public int getEnd_row() {
        return end_row;
    }

    public int getEnd_col() {
        return end_col;
    }

    private ArrayList<javafx.geometry.Point2D> solution;

    public MyViewModel(IModel model) {
        this.model = model;
        this.maze = null;
        playerCol=0;
        playerRow =0;
        start_col =0;
        start_row = 0;
        end_col =0;
        end_row =0;
    }

    public ArrayList<javafx.geometry.Point2D> getSolution() {
        return model.getSolution();
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

                start_row = model.getStart_row();
                start_col = model.getStart_col();
                end_row = model.getEnd_row();
                end_col = model.getEnd_col();
                playerRow = start_row;
                playerCol= start_col;
                setChanged();
                notifyObservers("generated");
            }
            if (arg.equals("solved"))
            {
                solution = model.getSolution();
                setChanged();
                notifyObservers("solved");
            }
            if (arg.equals("saved")) {
                setChanged();
                notifyObservers("saved");}
            if (arg.equals("not saved"))
            {
                setChanged();
                notifyObservers("not saved");
            }
            if (arg.equals("loaded"))
            {
                maze = model.getMaze();
                start_row = model.getStart_row();
                start_col = model.getStart_col();
                end_row = model.getEnd_row();
                end_col = model.getEnd_col();
                playerRow = start_row;
                playerCol= start_col;
                setChanged();
                notifyObservers("loaded");
            }
            if (arg.equals("not loaded"))
            {
                setChanged();
                notifyObservers("not loaded");
            }
        }
    }

    public void generateMaze(int row, int col) {
        if (row < 0 || col < 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("problem with rows or columns");
            alert.show();
        }
        else {
            model.generateMaze(row, col);}
    }


    public int[][] getMaze() {return maze;}

    public int getPlayerRow() {return playerRow;}

    public int getPlayerCol() {return playerCol;}

    public void playerMove(KeyCode direction) {model.playerMove(direction);}

    public void solveMaze() {model.solveMaze();}

    public void save(File my_file) {model.save(my_file);}

    public void Load(File my_file) {
        model.Load(my_file);
    }

    public void stopServers() {model.stopServers();}

    public int getThreads() {
        return model.getThreads();
    }

    public String getGenerate() {
        return model.getGenerating();
    }

    public String getSearch() {
        return model.getSearching();
    }
}

