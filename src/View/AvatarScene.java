package View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AvatarScene extends Canvas {
    private int avatar=0;
    String[] avatars = {"resources/images/sonic.jpg","resources/images/IcyTower.PNG"};
    public AvatarScene(){
        widthProperty().addListener(e->draw());
        heightProperty().addListener(e->draw());
    }
    public void draw(){
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        drawAvatar(graphicsContext , canvasHeight, canvasWidth,avatar);
    }
    public void drawAvatar(GraphicsContext graphicsContext, double cellHeight,double cellWidth, int avatar){
        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(avatars[avatar]));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        graphicsContext.drawImage(playerImage, cellWidth, cellHeight);

    }
}
