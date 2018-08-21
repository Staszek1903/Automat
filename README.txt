LINE:
    <label>: <instruction> <arg> //<ignored>

INSTRUCTIONS:
    lac <arg>   - acc := arg
    in          - acc := mem[mp]
    out         - mem := mem[mp]
    pfr <arg>   - mp += arg
    pbc <arg>   - mp -= arg
    drf         - mp := mem[mp]
    add         - acc += mem[mp]
    sub         - acc -= mem[mp]
    cmp         - acc == mem[mp] : state := EQ
                  acc > mem[mp]  : state := GT
                  acc < mem[mp]  : state := LS
    jmp <arg>   - pc := arg
    jeq <arg>   - state == EQ : pc := arg
    jgt <arg>   - state == GT : pc := arg
    jls <arg>   - state == LS : pc := arg