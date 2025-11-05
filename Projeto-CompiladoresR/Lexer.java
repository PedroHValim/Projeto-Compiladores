import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private List<Token> tokens;
    private List<AFD> afds;
    private CharacterIterator code;

    private int linha = 1;
    private int coluna = 1;

    public Lexer(String code) {
        this.code = new StringCharacterIterator(code);
        tokens = new ArrayList<>();
        afds = new ArrayList<>();
        afds.add(new MathOperator());
        afds.add(new Number());
        afds.add(new Letter());
        afds.add(new Comments());
        afds.add(new Operacoes());
        afds.add(new Tipos());
    }

    private void skipWhiteSpace() {
        while (code.current() == ' ' || code.current() == '\n') {
            if (code.current() == '\n') {
                linha++;
                coluna = 1;
            } else {
                coluna++;
            }
            code.next();
        }
    }

    private void error() {
        throw new RuntimeException("Token not recognized: " + code.current() +
                "\nLinha: " + linha + " Coluna: " + (coluna));
    }

    private Token searchNextToken() {
        int pos = code.getIndex();
        for (AFD afd : afds) {
            code.setIndex(pos); //aqui resetamos o indice para cada tentativa
            Token t = afd.evaluate(code);
            if (t != null) {
                return t;
            }
        }
        code.setIndex(pos);
        return null;
    }

    public List<Token> getTokens() {
        Token t;
        do {
            skipWhiteSpace();
            int posAntes = code.getIndex();
            t = searchNextToken();

            if (t == null) {
                if (code.current() != CharacterIterator.DONE) { //se o caractere atual não for o final do código, temos erro
                    error();
                }
                // Se for nulo e estamos no fim, saímos do loop.
                continue;
            }

            if (t.tipo.equals("COMMENT") || t.tipo.equals("STRING")) { //nessa função contaremos quantos caracteres tem no comentário
                code.setIndex(posAntes);
                while(code.getIndex() < posAntes + t.lexema.length()) {
                    if (code.current() == '\n') {
                        linha++;
                        coluna = 0;
                    } else {
                        coluna++;
                    }
                    code.next();
                }
                tokens.add(t); //adicionei antes do continue, pois senao o token nao aparece nunca na lista de token
                continue;
            }

            coluna += t.lexema.length();
            tokens.add(t);

        } while (!t.tipo.equals("EOF"));

        return tokens;
    }
}
