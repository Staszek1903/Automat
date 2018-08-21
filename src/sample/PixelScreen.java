package sample;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PixelScreen {

    private static PixelScreen instance;

    private Stage screen = new Stage();
    private Canvas canvas = new Canvas();
    private GraphicsContext context;

    public static PixelScreen getInstance(){
        if(instance == null)
            instance = new PixelScreen();
        return instance;
    }

    public static void nullInstance(){
        instance = null;
    }

    public static boolean isInitialized(){
        return (instance != null);
    }

    private PixelScreen(){
        screen.setOnCloseRequest(windowEvent -> {
            nullInstance();
            System.out.println("close screen");
        });

        canvas.setHeight(128*5);
        canvas.setWidth(128*5);
        context = canvas.getGraphicsContext2D();

        screen.setTitle("Automat - Screen");
        screen.setScene(new Scene(new Pane(canvas)));

        screen.setResizable(false);
        screen.show();
    }

    public void initFill(){
        context.setFill(Color.GRAY);
        context.fillRect(0,0, 128*5, 128*5);

        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                context.setFill(Color.BLACK);
                context.fillRect(i*5, j*5,4,4);
            }
        }
    }

    public void setPixel(short index, short value){
        short x = (short)(index % 128);
        short y = (short)((index/128)%128);

        double white = Short.MAX_VALUE;
        double color = value;

        context.setFill(Color.gray(color/white));
        context.fillRect(x*5,y*5,4,4);

        System.out.println("pixel: " + index + " set to:" + value);
    }
}
