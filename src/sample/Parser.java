package sample;

public class Parser {

    private String line;
    private boolean is_ignored = false;
    private int label_end;
    private int instruction_end;
    private int param_end;


    public Parser(String line){
        this.line = line;

        deleteComment();
        ommit_white_spaces(0);

        if(line.length() == 0){
            is_ignored = true;
            return;
        }

        label_end = this.line.indexOf(":");
        ommit_white_spaces(label_end+1);
        instruction_end = this.line.indexOf(" ");
        ommit_white_spaces(instruction_end);
        param_end = this.line.indexOf(" ");

        instruction_end = (instruction_end == -1)? this.line.length() : instruction_end;
        param_end = (param_end == -1)? this.line.length() : param_end;
    }

    public String get_instruction(){ return line.substring(label_end+1, instruction_end); }

    public String get_param(){
        return  line.substring(instruction_end, param_end);
    }

    public String get_label(){ return line.substring(0,(label_end<0)?0:label_end); }

    private int ommit_white_spaces(int begin){
        String new_line = new String("");
        int offset = 0;
        boolean ommiting = true;

        for(int i=0; i<line.length(); ++i){
            char a = line.charAt(i);
            if(ommiting && i >= begin && ((a == ' ' || a == '\n' || a == '\t'))){
                ++offset;
                continue;
            }
            else if(ommiting && i >=begin) ommiting = false;

            new_line += a;
        }

        line = new_line;
        return offset;
    }

    private void deleteComment(){
        int comment_index = line.indexOf("//");
        if(comment_index == -1) return;

        line = line.substring(0,comment_index);
    }

    public boolean isIgnored(){return is_ignored;}
}
