import java.text.CharacterIterator;

public class MathOperator extends AFD{
    @Override
    public Token evaluate(CharacterIterator code){
        switch(code.current()){
            case '+':
                code.next();
                return new Token("PLUS", "+");
            case '-':
                code.next();
                return new Token("MINUS", "-");
            case '/':
                code.next();
                return new Token("DIVIDE", "/");
            case '*':
                code.next();
                return new Token("MULTIPLY", "*");
            case '(':
                code.next();
                return new Token("OP", "(");
            case ')':
                code.next();
                return new Token("CP", ")");
            case '[':
                code.next();
                return new Token("OS", "[");
            case ']':
                code.next();
                return new Token("CS", "]");
            case '{':
                code.next();
                return new Token("OC", "{");
            case '}':
                code.next();
                return new Token("CC", "}");
            case CharacterIterator.DONE:
                return new Token("EOF","$");
            default:
                return null;
        }
    }
}
