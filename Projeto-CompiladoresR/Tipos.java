import java.text.CharacterIterator;

public class Tipos extends AFD{
    @Override
    public Token evaluate(CharacterIterator code){

        if (code.current() == 'i') {
            code.next();
            if (code.current() == 'n') {
                code.next();
                if(code.current() == 't'){
                    code.next();
                    return new Token("TIPO", "int");
                }
            }
            return null;
        }
        if (code.current() == 'i') {
            code.next();
            if (code.current() == 'n') {
                code.next();
                if(code.current() == 't'){
                    code.next();
                    return new Token("TIPO", "int");
                }
            }
            return null;
        }
        if (code.current() == 'f') {
            code.next();
            if (code.current() == 'l') {
                code.next();
                if (code.current() == 'o') {
                    code.next();
                    if (code.current() == 'a') {
                        code.next();
                        if (code.current() == 't') {
                            code.next();
                            return new Token("TIPO", "float");
                        }
                    }
                }
            }
            return null;
        }


        return null;
    }
}
