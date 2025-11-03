import java.util.List;

public class Parser{

    List<Token> tokens;
    int currentTokenIndex;
    Token token;
    private Node root;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void main() {
        token = getNextToken();
        Node raiz = fun_if();
        if(raiz != null){
            if (token.tipo == "EOF"){
                System.out.println("\nSintaticamente correta");
                this.root = raiz;
                return;
            }
            else{
                erro();
            }
        }
        erro();
    }

    public Node getRootNode() {
        return this.root;
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
            node.addNode(token.tipo);
            token = getNextToken();
            return true;
        }
        return false;
    }

    private boolean matchT(String tipo,String newcode, Node node){
        if(token.tipo.equals(tipo)){
            traduz(newcode);
            node.addNode(token.tipo);
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
        if (declaracao() != null
         || atribuicao() != null
         || fun_if() != null
         || fun_for() != null
         || fun_while() != null
         || tupni() != null) {
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
        if(matchT("TIPO",node) && matchT("IDENTIFICADOR",node) && matchL("=",node)){
            Node child = null;
            if(matchT("IDENTIFICADOR", child) || tupni() != null  || expr() != null){
                return node;
            }
        }

        return null;
    
    }

    private Node atribuicao(){
        Node node = new Node("atribuicao");
        if(matchT("IDENTIFICADOR", node) && matchT("ATRIBUICAO", node)){
            Node child = null;
            if(matchT("IDENTIFICADOR", child) || tupni() != null  || expr() != null){
                return node;
            }
        }
        return null;
    }

    private Node tupni(){
        Node node = new Node("tupni");

        matchT("IDENTIFICADOR", node);
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
        if(matchT("letter", child) || matchT("num", child) || matchT("IDENTIFICADOR", child)){
            return node;
        }
        return null;
    }


    private Node condicao(){
        Node node = new Node("condicao");
        if(expr() != null && matchT("OPERADORES", node) && expr() != null){
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
        if(matchL("(",node) &&  matchT("OPERADORES", node) && var() != null && expr_linha() != null && matchL(")",node)){
            return node;
        }
        return node;
    }

    private Node var(){

        Node node = new Node("var");

        if(num() != null || matchT("IDENTIFICADOR", node)){
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
        if(matchL("IV", node) &&  matchT("IDENTIFICADOR", node) && matchL("abt", node) && matchL("(", node) && var() != null && matchL("até", node) && var() != null && matchL(")", node) && matchL(":", node) && comando() != null){
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
        if(matchT("NUM", node)){
            return node;
        }
        return null;
    }


}