import java.text.CharacterIterator;

public class MathOperator extends AFD{
    @Override
    public Token evaluate(CharacterIterator code){

        if (code.current() == '=') {
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token("OPR_IGUALDADE", "=="); // '=='
            } else {
                return new Token("OPR_ATRI", "=");     // '='
            }
        }

        if (code.current() == '>') {
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token("OPERADORES", ">=");
            } else {
                return new Token("OPERADORES", ">");
            }
        }

        if (code.current() == '<') {
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token("OPERADORES", "<=");
            } else {
                return new Token("OPERADORES", "<");
            }
        }

        switch(code.current()){
            case '+':
                code.next();
                return new Token("OPERADOR", "+");
            case '-':
                code.next();
                return new Token("OPERADOR", "-");
            case '/':
                code.next();
                return new Token("OPERADOR", "/");
            case '*':
                code.next();
                return new Token("OPERADOR", "*");
            case '(':
                code.next();
                return new Token("PARENTESES", "(");
            case ')':
                code.next();
                return new Token("PARENTESES", ")");
            case '[':
                code.next();
                return new Token("COLCHETES", "[");
            case ']':
                code.next();
                return new Token("COLCHETES", "]");
            case '{':
                code.next();
                return new Token("CHAVES", "{");
            case '}':
                code.next();
                return new Token("CHAVES", "}");
            case CharacterIterator.DONE:
                return new Token("EOF","$");
            default:
                return null;
        }
    }
}
