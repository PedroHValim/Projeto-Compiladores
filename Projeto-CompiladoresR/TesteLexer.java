import java.io.IOException;
import java.util.List;

public class TesteLexer{
    public static void main(String[] args) throws IOException{
        String code = lerArquivo.lerArquivoComLinha("texto.txt");
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.getTokens();
        Parser parser = new Parser(tokens);
        parser.main();
        Node raiz = parser.getRootNode();
        if (raiz != null) {
            System.out.println("\n--- Árvore Sintática (AST) ---");
            Tree tree = new Tree(raiz);
            tree.printTree();
        }
        /*for(Token t: tokens){
            System.out.println(t);
        }*/
    }

} 