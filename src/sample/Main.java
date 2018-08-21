package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("start_start");
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Automat");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        System.out.println("start_end");
    }


    public static void main(String[] args) {
        System.out.println("main_start");
        launch(args);
        System.out.println("main_end");
    }
}
