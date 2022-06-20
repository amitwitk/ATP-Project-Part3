package View;

import java.awt.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;


public class MazeDisplay extends Canvas {
    Maze maze;
    Solution solution;
    int row_index;
    int col_index;

    public MazeDisplay(GraphicsConfiguration config, int row_index, int col_index) {
        //super(config);
        this.row_index = row_index;
        this.col_index = col_index;

    }

    public void drawMaze(Maze maze) {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getRows();
            int cols = maze.getColumns();

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.RED);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if(maze.getValue(j,i) == 1){
                        //if it is a wall:
                        double x = j * cellWidth;
                        double y = i * cellHeight;
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    }
                }
            }
        }
    }


}
