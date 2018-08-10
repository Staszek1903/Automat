package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemoryCell {
    private final SimpleIntegerProperty data;
    private final SimpleStringProperty char_representation;

    public MemoryCell(int data) {
        this.data = new SimpleIntegerProperty(data);
        this.char_representation = new SimpleStringProperty(""+(char)data);
    }

    public short getData() {
        return (short)data.get();
    }

    public SimpleIntegerProperty dataProperty() {
        return data;
    }

    public void setData(short data){
        this.data.set(data);
        this.char_representation.set(""+(char)data);
    }

    public String getChar_representation() {
        return char_representation.get();
    }

    public SimpleStringProperty char_representationProperty(){
        return char_representation;
    }

    public void setChar_representation(char a){
        char_representation.set(""+a);
    }
}
