package sample;

import javafx.beans.property.SimpleIntegerProperty;

public class MemoryCell {
    private final SimpleIntegerProperty data;

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
