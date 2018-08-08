package sample;

import java.util.HashMap;
import java.util.Map;

public class MacroMap {
    private Map macros = new HashMap<String, Short>();

    public MacroMap(){
    }

    public void setDefaultMacros(){
        macros.put("MAX", Short.MAX_VALUE);
        macros.put("MIN", Short.MIN_VALUE);
    }

    public void clear(){
        macros.clear();
    }

    public void setMacro(String name, short value){
        macros.put(name, new Short(value));
    }

    public boolean hasMacro(String name){
        return macros.containsKey(name);
    }

    public short getValue(String name) throws CodeError{
        if(!hasMacro(name)) {
            throw new CodeError("undefined macro \"" + name + '\"',0);
        }
        Integer val = (Integer)macros.get(name);
        return val.shortValue();
    }

}
