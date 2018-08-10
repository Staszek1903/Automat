package sample;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private Map<String, Instruction> instructions;
    Model model;

    Dictionary(Model m){
        this.model = m;
        instructions = new HashMap<String, Instruction>();
        
        instructions.put("lac",
                (Model model, short param) ->  model.accumulator = param);

        instructions.put("in",
                (Model model, short param) ->  model.accumulator = model.getCurrentMemoryCell());

        instructions.put("out",
                (Model model, short param) ->  model.setCurrentMemoryCell(model.accumulator));

        instructions.put("pfr",
                (Model model, short param) ->  model.memory_pointer += param);

        instructions.put("pbc",
                (Model model, short param) ->  model.memory_pointer -= param);
        
        instructions.put("drf",
                (Model model, short param) ->  model.memory_pointer = model.getCurrentMemoryCell());

        instructions.put("add",
                (Model model, short param) ->  {
                    int temp = model.accumulator + model.getCurrentMemoryCell();
                    if(temp > Short.MAX_VALUE) model.state = "OVERFLOW";
                    model.accumulator = (short)temp;
                });

        instructions.put("sub",
                (Model model, short param) ->  {
                    int temp = model.accumulator - model.getCurrentMemoryCell();
                    if(temp < Short.MIN_VALUE) model.state = "UNDERFLOW";
                    model.accumulator = (short)temp;
                });

        instructions.put("cmp",
                (Model model, short param) ->  {
                    if(model.accumulator == model.getCurrentMemoryCell()){
                        model.state = "EQ";
                    }else if(model.accumulator > model.getCurrentMemoryCell()){
                        model.state = "GT";
                    }else if(model.accumulator < model.getCurrentMemoryCell()){
                        model.state = "LS";
                    }
                });

        instructions.put("jmp",
                (Model model, short param) ->  model.program_counter = param);

        instructions.put("jeq",
                (Model model, short param) -> {
                    if(model.state.contentEquals("EQ")) model.program_counter = param;
                });

        instructions.put("jgt",
                (Model model, short param) -> {
                    if(model.state.contentEquals("GT")) model.program_counter = param;
                });

        instructions.put("jls",
                (Model model, short param) -> {
                    if(model.state.contentEquals("LS")) model.program_counter = param;
                });

        instructions.put("str",
                (Model model, short param) ->{
                    model.state = "";
                });
    }

    public void execute(String inst, short param) throws CodeError {
        if(!instructions.containsKey(inst)){
            throw new CodeError("Bad intruction: \"" + inst + "\"", 0);
        }

        Instruction instruction = instructions.get(inst);
        instruction.execute(model,param);
    }
}
