import java.text.CharacterIterator;

public class Letter extends AFD{
    @Override
    public Token evaluate(CharacterIterator code){
        if(Character.isAlphabetic(code.current())){
            String letra = readLetter(code);
            if(isTokenSeparator(code)){
                return new Token("LETTER",letra);
            }
        }
        return null;
    }

    private String readLetter(CharacterIterator code){
        String letter = "";
        while(Character.isAlphabetic(code.current())){
            letter += code.current();
            code.next();
        }
        return letter;
    }
}
