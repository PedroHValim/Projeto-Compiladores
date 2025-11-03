import java.text.CharacterIterator;

public class Operacoes extends AFD {
    @Override
    public Token evaluate(CharacterIterator code) {
        
        if (code.current() == ':') {
            code.next();
            return new Token("ENTAO", ":");
        }
        if (code.current() == '?') {
            code.next();
            return new Token("IF", "?");
        }
        if (code.current() == '!') {
            code.next();
            if (code.current() == '?') {
                code.next();
                return new Token("ELIF", "!?");
            }
            return new Token("ELSE", "!");
        }
        if (code.current() == '£') {
            code.next();
            return new Token("WHILE", "£");
        }

        if (code.current() == 't') {
            code.next();
            if (code.current() == 'u') {
                code.next();
                if (code.current() == 'p') {
                    code.next();
                    if (code.current() == 'n') {
                        code.next();
                        if (code.current() == 'i') {
                            code.next();
                            return new Token("INPUT", "tupni");
                        }
                    }
                }
            }
            return null; 
        }

        if (code.current() == 'I') {
            code.next();
            if (code.current() == 'V') {
                code.next();
                return new Token("FOR", "IV");
            }
            return null;
        }

        if (code.current() == CharacterIterator.DONE) {
            return new Token("EOF","$");
        }
        
        return null; 
    }
}