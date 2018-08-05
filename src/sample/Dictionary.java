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
                (Model model, short param) ->  model.accumulator = model.getMemory()[model.memory_pointer]);

        instructions.put("out",
                (Model model, short param) ->  model.getMemory()[model.memory_pointer] = model.accumulator);

        instructions.put("pfr",
                (Model model, short param) ->  model.memory_pointer += param);

        instructions.put("pbc",
                (Model model, short param) ->  model.memory_pointer -= param);
        
        instructions.put("drf",
                (Model model, short param) ->  model.accumulator = model.getMemory()[model.memory_pointer]);

        instructions.put("add",
                (Model model, short param) ->  model.accumulator += model.getMemory()[model.memory_pointer]);

        instructions.put("sub",
                (Model model, short param) ->  model.accumulator -= model.getMemory()[model.memory_pointer]);

        instructions.put("cmp",
                (Model model, short param) ->  {});

        instructions.put("jmp",
                (Model model, short param) ->  {});
    }

    public void execute(String inst, short param) throws CodeError {
        if(!instructions.containsKey(inst)){
            throw new CodeError("Bad intruction: \"" + inst + "\"", 0);
        }

        Instruction instruction = instructions.get(inst);
        instruction.execute(model,param);
    }
}
