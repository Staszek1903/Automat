package sample;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Duration;


public class Controller {

    @FXML
    private TextField program_counter, memory_pointer, state, accumulator;
    @FXML
    private TextArea code_area, memory_area, message_area;
    @FXML
    private ProgressIndicator running_indicator;

    private Model model;

    private Timeline timer;
    private boolean running = false;

    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();


    public Controller(){
        model = new Model();
    }

    public void startButton(Event event){
        if(running){
            stop();
        }else{
            start();
        }
        System.out.println("running " + running);
    }

    public void stepButton(Event event){
        model.set_program(code_area.getText());
        model.execute_next_command();
        updateDisplay();

    }

    public void resetButton(Event event){
        model.reset();
        updateDisplay();
    }

    private void updateDisplay(){
        program_counter.setText(String.valueOf(model.getProgram_counter()));
        memory_pointer.setText(String.valueOf(model.memory_pointer));
        accumulator.setText(String.valueOf(model.accumulator));

        short mem [] = model.getMemory();
        String mem_text = new String("");

        for(int i=0; i<20; ++i){
            mem_text += String.valueOf(mem[i]) + '\n';
        }

        memory_area.setText(mem_text);
    }

    private void start(){
        timer = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            if(!model.execute_next_command()) stop();
            updateDisplay();
        }));
        model.set_program(code_area.getText());
        running = true;
        running_indicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void stop(){
        timer.stop();
        running = false;
        running_indicator.setProgress(1.0f);
    }
}
