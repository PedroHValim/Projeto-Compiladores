import java.util.List;

public class Parser{

    List<Token> tokens;
    Token token;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void main() {
        token = getNextToken();
        if(fun_if() != null){
            if (token.tipo == "EOF"){
                System.out.println("\nSintaticamente correta");
                return;
            }
            else{
                erro();
            }
        }
        erro();
    }

    private void erro() {
        System.out.println("token inválido: " + token.lexema);
    }

    public Token getNextToken() {
        if (tokens.size() > 0) {
            return tokens.remove(0);
        } else
            return null;
    }

    private void traduz(String newcode) {
        System.out.print(newcode + " ");
    }

    private boolean matchT(String tipo,Node node){
        if(token.tipo.equals(tipo)){
            node.addNode(token.lexema);
            token = getNextToken();
            return true;
        }
        return false;
    }

    private boolean matchT(String tipo,String newcode, Node node){
        if(token.tipo.equals(tipo)){
            traduz(newcode);
            node.addNode(token.lexema);
            token = getNextToken();
            return true;
        }
        return false;
    }

    private boolean matchL(String lexema, Node node){
        if(token.lexema.equals(lexema)){
            node.addNode(lexema);
            token = getNextToken();
            return true;
        }
        return false;
    }


    private boolean matchL(String lexema,String newcode, Node node){
        if(token.lexema.equals(lexema)){
            traduz(newcode);
            node.addNode(lexema);
            token = getNextToken();
            return true;
        }
        return false;
    }

    private Node bloco(){
        Node node = new Node("bloco");

        Node cmd = comando();
        if (cmd != null) node.addNode(cmd);

        Node blocoLinha = bloco_linha();
        if (blocoLinha != null) node.addNode(blocoLinha);

    return node;
    }

    private Node comando() {
        Node node = new Node("comando");

        Node child = null;
        if ((child = declaracao()) != null
         || (child = atribuicao()) != null
         || (child = fun_if()) != null
         || (child = fun_for()) != null
         || (child = fun_while()) != null
         || (child = tupni()) != null) {
            node.addNode(child);
            return node;
        }

        return null;
    }

    private Node bloco_linha() {
        Node node = new Node("bloco_linha");
        Node child = bloco();
        if (child != null) {
            node.addNode(child);
            return node;
        }
        return null;
    }

    private Node declaracao(){

        Node node = new Node("declaracao");

        matchT("tipo", node);
        matchT("identificador", node);
        matchT("opr_atribuicao", node);

        Node child = null;
        if(matchT("letter", child) || tupni() != null  || expr() != null){
            return node;
        }
        return null;
    
    }

    private Node atribuicao(){
        Node node = new Node("atribuicao");
        if(matchT("identificador", node) && matchT("opr_atribuicao", node)){
            Node child = null;
            if(matchT("letter", child) || tupni() != null  || expr() != null){
                return node;
            }
        }
        return null;
    }

    private Node tupni(){
        Node node = new Node("tupni");

        matchT("identificador", node);
        matchT("opr_atribuicao", node);
        matchL("tupni", node);
        if(input_linha() != null){
            return node;
        }
        return null;
    }

    private Node input_linha(){
        Node node =  new Node ("Input_linha");
        if(matchL("(", node) && matchL(")", node)){
            return node;
        }else if (matchL("(", node) && matchL("''", node) && matchT("letter", node) && matchL("''", node) && matchL(")", node)) {
            return node;
        }
        return null;
    }

    private Node fun_print(){
        Node node = new Node("fun_print");
        matchL("wri", node);
        matchL("(", node);

        Node child = null;
        if(matchT("letter", child) || matchT("num", child) || matchT("identificador", child)){
            return node;
        }
        return null;
    }


    private Node condicao(){
        Node node = new Node("condicao");
        if(expr() != null && matchT("operadores", node) && expr() != null){
            return node;
        }
        return null;
    }

    private Node expr(){
        Node node =  new Node("expr");
        if(var() != null && expr_linha() != null){
            return node;
        }
        return null;
    }

    //AQUI TAMBEM ACEITA NULOO, REVISAR ANTES DO FINAL

    private Node expr_linha(){
        Node node = new Node("expr_linha");
        if(matchT("operador", node) && var() != null && expr_linha() != null){
            return node;
        }
        return null;
    }

    private Node var(){

        Node node = new Node("var");
        if(num() != null || matchT("identificador", node)){
            return node;
        }
        return null;
    }

    private Node fun_if(){

        Node node = new Node("fun_if");
        
        if(matchL("?", node) && condicao() != null && matchL(":", node) && comando() != null && fun_if_linha() != null){
            return node;
        }
        return null;
    }

    private Node fun_if_linha(){
        Node node = new Node("fun_if_linha");

        if(matchL("!?", node) && condicao() != null && matchL(":", node) && comando() != null && fun_if_linha() != null){
            return node;
        }else if (fun_else() != null) {
            return node;
        }
        return null;
    }

    private Node fun_else(){

        Node node =  new Node("fun_else");
        if(matchL("!", node) &&  matchL(":", node) && comando() != null ){
            return node;
        }
        return null;
    }

    private Node fun_for(){
        Node node = new Node("fun_for");
        if(matchL("IV", node) &&  matchT("identificador", node) && matchL("abt", node) && matchL("(", node) && var() != null && matchL("até", node) && var() != null && matchL(")", node) && matchL(":", node) && comando() != null){
            return node;
        }
        return null;
    }

    private Node fun_while(){
        Node node = new Node("fun_while");
        if(matchL("£", node) &&  condicao() != null && matchL(":", node) && comando()!= null && fun_else() != null ){
            return node;
        }
        return null;
    }

    private Node num(){
        Node node = new Node("num");
        if(matchT("int", node) || matchT("float", node)){
            return node;
        }
        return null;
    }


}