package View;

import java.awt.*;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


public class MazeDisplay extends Canvas {
    Maze maze;
    Solution solution;
    int row_index;
    int col_index;

    public MazeDisplay(GraphicsConfiguration config, int row_index, int col_index) {
        super(config);
        this.row_index = row_index;
        this.col_index = col_index;

    }
}
