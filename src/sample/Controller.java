package sample;


import com.sun.org.apache.bcel.internal.classfile.Code;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private TextField program_counter, memory_pointer, state, accumulator;
    @FXML
    private TextArea code_area;
    @FXML
    private TableColumn memory_column;
    @FXML
    private ProgressIndicator running_indicator;


    private Model model = new Model();;

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

    private void updateDisplay() {
        program_counter.setText(String.valueOf(model.program_counter));
        memory_pointer.setText(String.valueOf(model.memory_pointer));
        accumulator.setText(String.valueOf(model.accumulator));
    }

    private void start() {
        timer = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> execute_instruction()));
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
            // TODO: 08.08.18  loging error to code_area beneath error line
        }
    }
}

