import java.io.IOException;
import java.util.List;

public class TesteLexer{
    public static void main(String[] args) throws IOException{
        String code = lerArquivo.lerArquivoComLinha("texto.txt");
        System.out.println(code);
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.getTokens();

        for(Token t: tokens){
            System.out.println(t);
        }
    }

}