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
        if (code.current() == 'S') {
            code.next();
            if (code.current() == 'T') {
                code.next();
                if(code.current() == 'r'){
                    code.next();
                    return new Token("TIPO", "str");
                }
            }
            return null;
        }

        return null;
    }
}
