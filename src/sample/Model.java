package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Model {
    private static final int capacity = (int)Math.pow(2,16);
    private ArrayList program_lines;
    private ObservableList<MemoryCell> memory = FXCollections.observableArrayList();
    private short program_counter = 0;
    private Dictionary dictionary = new Dictionary(this);

    public short accumulator = 0;
    public short memory_pointer = 0;


    public Model(){

        memoryAssign();
    }

    public void set_program(String lines){
        program_lines = new ArrayList<String>();
        int last_eol = -1;
        int eol = 0;
        while(true){
            eol = lines.indexOf('\n',last_eol+1);
            if(eol < 0){
                program_lines.add(lines.substring(last_eol+1));
                break;
            }
            program_lines.add(lines.substring(last_eol+1,eol));
            last_eol = eol;
        }
        //look for labels TO DO
        for(Object line : program_lines){
            String label = new Parser((String)line).get_label();
            if(label.length()>0) System.out.println(label);
        }

    }

    public boolean execute_next_command(){

        if(program_counter >= program_lines.size())  return false;

        // parse
        Parser parser = new Parser((String)program_lines.get(program_counter));

        // interpret
        if(!parser.isIgnored()) {
            String label = parser.get_label();
            String inst = parser.get_instruction();
            String param = parser.get_param();
            System.out.println("l: \"" + label + "\" i: \"" + inst + "\" p: \"" + param + '\"');

            if(param.length() == 0) param = "0";

            //exexute
            try{
                dictionary.execute(inst, Short.valueOf(param));
            }
            catch (CodeError error){
                System.out.println(error.getMessage() + ":" + error.getLine());
            }
        }

        ++program_counter;
        return true;
    }

    public void reset(){
        program_counter = 0;
        memoryAssign();
    }

    public ObservableList<MemoryCell> getMemory() {
        return memory;
    }

    public int getProgram_counter(){
        return program_counter;
    }

    private void memoryAssign(){
        for(int i=0; i<10000; ++i){
            memory.add(new MemoryCell(0));
        }
    }
}
