import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

public class Letter extends AFD{

    private static final Map<String, String> RESERVED_KEYWORDS = new HashMap<>();

    static {
        RESERVED_KEYWORDS.put("INPUT", "tupni"); 
        RESERVED_KEYWORDS.put("PRINT", "wri");
        RESERVED_KEYWORDS.put("IN_RANGE", "abt");
        RESERVED_KEYWORDS.put("ATÉ", "até");
        RESERVED_KEYWORDS.put("FOR", "IV");
        RESERVED_KEYWORDS.put("TIPO", "int");
        RESERVED_KEYWORDS.put("TIPO", "str");
    }

    @Override
    public Token evaluate(CharacterIterator code){
        if(Character.isAlphabetic(code.current())){
            String letra = readLetter(code);

            if (RESERVED_KEYWORDS.containsKey(letra)) {
                String tokenName = RESERVED_KEYWORDS.get(letra);
                return new Token(tokenName, letra);
            }

            if(isTokenSeparator(code)){
                return new Token("IDENTIFICADOR",letra);
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
