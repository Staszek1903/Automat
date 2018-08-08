package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemoryCell {
    private final SimpleIntegerProperty data;
    // TODO: 08.08.18  private final SimpleStringProperty char_represenation;

    public MemoryCell(int data) {
        this.data = new SimpleIntegerProperty(data);
        
    }

    public short getData() {
        return (short)data.get();
    }

    public SimpleIntegerProperty dataProperty() {
        return data;
    }

    public void setData(short data){
        this.data.set(data);
    }
}
