package Model;
import Client.Client;
import Client.IClientStrategy;
import Server.Configurations;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import algorithms.search.MazeState;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;


public class MyModel extends Observable implements IModel {
    public MyMazeGenerator generator;
    private Maze maze;
    private Solution solution;
    private int start_row;
    private int start_col;
    private int end_row;
    private int end_col;
    private int playerRow;
    private int playerCol;

    ISearchingAlgorithm solver;
    private Server maze_generate_server;
    private Server maze_solver_server;
    private static final Logger LOG = LogManager.getLogger();


    Configurations co = Configurations.getInstance();

    public MyModel() {
        this.generator = null;
        this.maze = null;
        this.solution = null;
        playerCol = 0;
        playerRow = 0;
        start_col = 0;
        start_row = 0;
        end_col =0;
        end_row = 0;
        maze_generate_server = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        maze_solver_server = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void solveMaze() {
        if (maze !=null){
            maze_solver_server.start();
            CommunicateWithServer_SolveSearchProblem(maze);
            setChanged();
            notifyObservers("solved");
        }
    }
    //LOG.info(str);

    //from part 2
    private void CommunicateWithServer_SolveSearchProblem(Maze maze) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        updateSolution(mazeSolution);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            client.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String str = "hi tal is gay";
        LOG.info(str);

    }

    private void updateSolution(Solution mazeSolution) {
        this.solution = mazeSolution;
    }

    public int[][] getMaze() {
        int[][] grid_maze = new int[maze.getRows()][maze.getColumns()];
        for (int i= 0; i<maze.getRows(); i++)
        {
            for (int j=0; j<maze.getColumns(); j++)
            {
                grid_maze[i][j] = maze.getValue(j , i);
            }
        }
        return grid_maze;
    }
    public int getStart_row() {return start_row;}

    public int getStart_col() {return start_col;}

    public int getEnd_row() {return end_row;}

    public int getEnd_col() {return end_col;}
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
    public void Load(File my_file)  {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(my_file.getPath().toString()));
            Maze maze = (Maze)in.readObject();
            this.maze = maze;
            start_row = maze.getStartPosition().getRowIndex();
            start_col = maze.getStartPosition().getColumnIndex();
            end_row = maze.getGoalPosition().getRowIndex();
            end_col = maze.getGoalPosition().getColumnIndex();
            playerRow = start_row;
            playerCol= start_col;
            setChanged();
            notifyObservers("loaded");
        }
        catch (Exception e)
        {
            setChanged();
            notifyObservers("not loaded");
        }
    }


    public void  save(File my_file){
        try {
            if (maze!=null) {
                FileOutputStream file = new FileOutputStream(my_file.getPath());
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(maze);
                out.flush();
                out.close();
                file.close();
                setChanged();
                notifyObservers("saved");
            }
        }
        catch(Exception e){
            setChanged();
            notifyObservers("not saved");
        }
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
        maze_generate_server.start();
        CommunicateWithServer_MazeGenerating(row, col);
        start_row = maze.getStartPosition().getRowIndex();
        start_col = maze.getStartPosition().getColumnIndex();
        end_row = maze.getGoalPosition().getRowIndex();
        end_col = maze.getGoalPosition().getColumnIndex();
        playerRow = start_row;
        playerCol= start_col;
        setChanged();
        notifyObservers("generated");
    }
    //from part 2
    private void CommunicateWithServer_MazeGenerating(int my_row, int my_col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{my_row, my_col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[1000012 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        updateMaze(maze);
                        //maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void updateMaze(Maze maze) {this.maze = maze;}

    public ArrayList<javafx.geometry.Point2D> getSolution() {
        ArrayList<javafx.geometry.Point2D> sol = new ArrayList<>(solution.getSolutionPath().size());
        for (AState state: solution.getSolutionPath())
        {
            MazeState ms = (MazeState) state;
            sol.add(new Point2D(ms.getRow(), ms.getCol()));
        }
        return sol;
    }

    public int getPlayerRow() {return playerRow;}

    public int getPlayerCol() {return playerCol;}

    public void stopServers()
    {
        try {
            maze_solver_server.stop();
            maze_generate_server.stop();
        }
        catch(Exception e) {}
    }

    public int getThreads()
    {
        return Integer.parseInt(co.getProp("threadPoolSize"));
    }

    public String getGenerating()
    {
        return co.getProp("mazeGeneratingAlgorithm");
    }

    public  String getSearching()
    {
        return co.getProp("mazeSearchingAlgorithm");
    }



}
