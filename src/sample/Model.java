package sample;

import com.sun.org.apache.bcel.internal.classfile.Code;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Model {
    private static final int capacity = (int)Math.pow(2,16);
    private ArrayList program_lines;
    private ObservableList<MemoryCell> memory = FXCollections.observableArrayList();
    private Dictionary dictionary = new Dictionary(this);
    private MacroMap macro_map = new MacroMap();

    public short accumulator = 0;
    public short memory_pointer = 0;
    public short program_counter = 0;


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
        //look for labels
        macro_map.clear();
        for (int i = 0; i < program_lines.size(); i++) {
            String line = (String)program_lines.get(i);
            String label = new Parser(line).get_label();
            if(label.length() > 0){
                macro_map.setMacro(label, (short)i);
                System.out.println(label + " := " + i);
            }
        }
    }

    public boolean execute_next_command() throws CodeError {

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
            short true_param = 0;
            if(Character.isDigit(param.charAt(0))){
                true_param = Short.valueOf(param);
            }
            else {
                true_param = macro_map.getValue(param);
            }

            dictionary.execute(inst,true_param);

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

    private void memoryAssign(){
        for(int i=0; i<capacity; ++i){
            memory.add(new MemoryCell(0));
        }
    }
}
