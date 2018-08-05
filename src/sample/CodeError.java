package sample;

public class CodeError extends Exception{
    private int line;

    public CodeError(String error, int line){
        super(error);

        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
