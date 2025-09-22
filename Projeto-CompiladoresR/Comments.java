import java.text.CharacterIterator;

public class Comments extends AFD {
    @Override
    public Token evaluate(CharacterIterator code) {
        if (code.current() != '@') return null;

        StringBuilder sb = new StringBuilder();
        sb.append('@');
        char c = code.next();

        while (c != CharacterIterator.DONE && c != '@') {
            sb.append(c);
            c = code.next();
        }

        if (c == '@') {
            sb.append('@'); 
            code.next();        
            return new Token("COMMENT", sb.toString());
        } else {
            return new Token("COMMENT", sb.toString());
        }
    }

    @Override
    public boolean isTokenSeparator(CharacterIterator code) {
        return code.current() == '@' || super.isTokenSeparator(code);
    }
}
