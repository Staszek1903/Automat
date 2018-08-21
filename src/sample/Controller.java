package sample;


import com.sun.org.apache.bcel.internal.classfile.Code;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Controller implements Initializable {

    @FXML
    private TextField program_counter, memory_pointer, state, accumulator;
    @FXML
    private TextArea code_area;
    @FXML
    private TableColumn memory_column, char_column;
    @FXML
    private ProgressIndicator running_indicator;


    private Model model = new Model();

    private Timeline timer;
    private boolean running = false;

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();


    public Controller() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableView table = memory_column.getTableView();
        memory_column.setCellValueFactory(
                new PropertyValueFactory<MemoryCell, Integer>("data")
        );
        char_column.setCellValueFactory(
                new PropertyValueFactory<MemoryCell, String>("char_representation")
        );

        table.setItems(model.getMemory());
    }

    public void startButton(Event event) {
        if (running) {
            stop();
        } else {
            start();
        }
        System.out.println("running " + running);
    }

    public void stepButton(Event event) {
        model.set_program(code_area.getText());
        execute_instruction();

    }

    public void resetButton(Event event) {
        model.reset();
        updateDisplay();
    }

    public void codeSave(Event event){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Code");
        File file = chooser.showSaveDialog(code_area.getScene().getWindow());

        try{
            PrintWriter save = new PrintWriter(file);
            save.print(code_area.getText());
            save.close();
        }
        catch (FileNotFoundException error){
            System.out.println(error.getMessage());
        }
        System.out.println("SAVE");
    }

    public void codeLoad(Event event){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Code");
        File file = chooser.showOpenDialog(code_area.getScene().getWindow());

        try{
            Scanner load = new Scanner(file);
            String temp_code = new String();
            while(load.hasNextLine())
                temp_code += load.nextLine() + "\n";

            code_area.setText(temp_code);
        }
        catch (FileNotFoundException error){
            System.out.println(error.getMessage());
        }
        System.out.println("LOAD");
    }

    public void graphicScreen(Event event){
        PixelScreen.getInstance();
    }

    public void screenFill(Event event){
        PixelScreen.getInstance().initFill();
        PixelScreen.getInstance().setPixel((short)0,(short)0);
        PixelScreen.getInstance().setPixel((short)1,Short.MAX_VALUE);
        PixelScreen.getInstance().setPixel((short)2,(short)(Short.MAX_VALUE/2));
    }

    private void updateDisplay() {
        program_counter.setText(String.valueOf(model.program_counter));
        memory_pointer.setText(String.valueOf(model.memory_pointer));
        accumulator.setText(String.valueOf(model.accumulator));
        state.setText(model.state);

        if(PixelScreen.isInitialized()){
            PixelScreen.getInstance().setPixel(
                    model.memory_pointer,
                    model.getCurrentMemoryCell()
            );
        }
    }

    private void start() {
        timer = new Timeline(new KeyFrame(Duration.millis(10), actionEvent -> execute_instruction()));
        model.set_program(code_area.getText());
        running = true;
        running_indicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void stop() {
        if(timer != null) timer.stop();
        running = false;
        running_indicator.setProgress(1.0f);
    }

    private void execute_instruction(){
        try{
            if (!model.execute_next_command()) stop();
            updateDisplay();
        }
        catch (CodeError error){
            System.out.println(error.getMessage() + " : " + model.program_counter);
            stop();
            String temp_code = code_area.getText();
            int last_endl = 0;
            for(int i=0; i<=model.program_counter; ++i){
                last_endl = temp_code.indexOf("\n", last_endl+1);
            }

            if(last_endl == -1){
                temp_code += "\n" + "//^!! " + error.getMessage() +" !!\n";
            }
            else{
                temp_code = temp_code.substring(0,last_endl+1)
                        + "//^!! " + error.getMessage() +" !!\n"
                        + temp_code.substring(last_endl+1,temp_code.length());
            }

            code_area.setText(temp_code);
        }
    }
}

