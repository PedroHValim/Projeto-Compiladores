import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

public class Letter extends AFD{

    private static final Map<String, String> RESERVED_KEYWORDS = new HashMap<>();

    static {
        RESERVED_KEYWORDS.put("tupni", "INPUT"); 
        RESERVED_KEYWORDS.put("wri", "PRINT");
        RESERVED_KEYWORDS.put("abt", "IN_RANGE");
        RESERVED_KEYWORDS.put("até", "ATÉ");
        RESERVED_KEYWORDS.put("IV", "FOR");
        RESERVED_KEYWORDS.put("int", "TIPO");
        RESERVED_KEYWORDS.put("float", "TIPO");
        RESERVED_KEYWORDS.put("str", "TIPO");
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
