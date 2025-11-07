import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<Token> tokens = new ArrayList<>();
    tokens.add(new Token("IF","?"));
    tokens.add(new Token("IDENTIFICADOR","id"));
    tokens.add(new Token("OPERADORES","<"));
    tokens.add(new Token("NUM","5"));
    tokens.add(new Token("ENTAO",":"));
    tokens.add(new Token("IDENTIFICADOR","soma"));
    tokens.add(new Token("OPR_ATRI","="));
    tokens.add(new Token("NUM","3"));
    tokens.add(new Token("ELSE","!"));
    tokens.add(new Token("ENTAO",":"));
    tokens.add(new Token("IDENTIFICADOR","soma"));
    tokens.add(new Token("OPR_ATRI","="));
    tokens.add(new Token("NUM","2"));
    tokens.add(new Token("EOF","$"));
    Parser parser = new Parser(tokens);
    parser.main();
  }
}
