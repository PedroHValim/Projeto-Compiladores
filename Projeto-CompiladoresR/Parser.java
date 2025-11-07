import java.util.List;

public class Parser{

    List<Token> tokens;
    Token token;
    private Node root;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void main() {
        Node main = new Node("main");
        token = getNextToken();
        boolean raiz = bloco(main);
        if(raiz != false){
            if (token.tipo == "EOF"){
                System.out.println("\nSintaticamente correta");
                this.root = main;
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

    private boolean  bloco(Node node) {
        Node bloco = node.addNode("bloco");

        if (tokenAtualInFirstComando()) {
            boolean cmd = comando(bloco);
            if (cmd != false) node.addNode(bloco);

            boolean blocoLinha = bloco_linha(bloco);
            if (blocoLinha != false) node.addNode(bloco);
        }

        return true;
    }

    private boolean  bloco_linha(Node node) {
        Node bloco_linha = new Node("bloco_linha");

        if (tokenAtualInFirstComando()) {
            boolean child = bloco(bloco_linha);
            if (child != false) node.addNode(bloco_linha);
            return true;
        }
        return false;
    }

    private boolean tokenAtualInFirstComando() {
        if (token == null) return false;

        switch (token.tipo) {
            case "TIPO":          // início de declaração
            case "IDENTIFICADOR": // atribuição
            case "IF":
            case "FOR":
            case "WHILE":
            case "INPUT":
            case "PRINT":
                return true;
            default:
                return false;
        }
    }

    private boolean  comando(Node node) {
        Node comando = node.addNode("comando");

        boolean child;

        // Testa e guarda o resultado do primeiro comando encontrado
        if ((child = declaracao(comando)) != false
        || (child = atribuicao(comando)) != false
        || (child = fun_if(comando)) != false
        || (child = fun_for(comando)) != false
        || (child = fun_while(comando)) != false
        || (child = fun_print(comando)) != false
        || (child = tupni(comando)) != false) {

            node.addNode(comando);
            return true;
        }

        return false;
    }



    private boolean  declaracao(Node node){
        System.out.println("OIOIOIOIOIOI 0000");

        Node declaracao = node.addNode("DECLARACAO");
        if(matchT("TIPO",token.lexema,declaracao) && matchT("IDENTIFICADOR",token.lexema,declaracao) && matchL("=","=",declaracao)){
            if(matchT("IDENTIFICADOR",token.lexema, declaracao) || tupni(declaracao) != false  || expr(declaracao) != false){
                System.out.println("OIOIOIOIOIOI");
                return true;
            }
            System.out.println("OIOIOIOIOIOI 22");
            return false;
        }
        
        System.out.println("OIOIOIOIOIOI 3333");
        return false;
    
    }

    //ESTÁ ROLANDO UM PROBLEMA PARA DIFERENCIAR DECLARACAO E ATRIBUICAO
    
    private boolean  atribuicao(Node node){
        System.out.println("OIOIOIOIOIOI 0000.555555");
        Node atribuicao = node.addNode("ATRIBUICAO");
        if(matchT("IDENTIFICADOR",token.lexema, atribuicao) && matchL("=", token.lexema,atribuicao)){
            if(matchT("IDENTIFICADOR", token.lexema,atribuicao)){
                return true;
            }
            if(tupni(atribuicao) != false){
                return true;
            }
            if(expr(atribuicao) != false){
                return true;
            }
        }
        return false;
    }

    private boolean tupni(Node node){
        Node tupni = node.addNode("tupni");
        if(matchL("tupni", "input",tupni) && input_linha(tupni) != false){
            return true;
        }
        return false;
    }

    private boolean input_linha(Node node){
        Node input_linha =  node.addNode("Input_linha");
        if(matchL("(", input_linha)){
            if(matchL(")",input_linha)){
                return true;
            }
            if(matchT("STRING",input_linha) && matchL(")",input_linha)){
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean fun_print(Node node){
        Node fun_print = node.addNode("fun_print");
        if(matchL("wri","std::cout", fun_print) && matchL("(","<<", fun_print)){
            if((matchT("IDENTIFICADOR",token.lexema, fun_print) || matchT("NUM",token.lexema, fun_print)) && matchL(")","<<std::endl", fun_print)){
                return true;
            }
        }
        return false;
    }


    private boolean  condicao(Node node){
        Node caondicao = node.addNode("condicao");
        if(expr(caondicao) != false && matchT("OPERADORES",token.lexema, caondicao) && expr(caondicao) != false){
            return true;
        }
        return false;
    }

    private boolean  expr(Node node){
        System.out.println("OI*********************************");
        Node expr =  node.addNode("EXPR");
        if(var(expr) != false && expr_linha(expr) != false){
            return true;
        }
        return false;
    }

    //AQUI TAMBEM ACEITA NULOO, REVISAR ANTES DO FINAL

    private boolean  expr_linha(Node node){
        Node expr_linha = node.addNode("expr_linha");
        if(matchL("(",node)){ 
            if(matchT("OPERADORES",token.lexema, expr_linha) && var(expr_linha) != false && expr_linha(expr_linha) != false && matchL(")",expr_linha)){
                return true;
        } 
            return false;
        }
        return true;
    }

    private boolean  var(Node node){
        Node var = node.addNode("VAR");
        if(matchT("NUM", var) || matchT("IDENTIFICADOR",token.lexema, var) || matchT("STRING", var)){
            return true;
        }
        return false;
    }

    private boolean fun_if(Node node){

        Node fun_if = node.addNode("fun_if");
        
        if(matchL("?", fun_if) && condicao(fun_if) != false && matchL(":", fun_if) && comando(fun_if) != false && fun_if_linha(fun_if) != false){
            return true;
        }
        return false;
    }

    private boolean  fun_if_linha(Node node){
        Node fun_if_linha = node.addNode("fun_if_linha");

        if(matchL("!?", fun_if_linha) && condicao(fun_if_linha) != false && matchL(":", fun_if_linha) && comando(fun_if_linha) != false && fun_if_linha(fun_if_linha) != false){
            return true;
        }else if (fun_else(fun_if_linha) != false) {
            return true;
        }
        return false;
    }

    private boolean  fun_else(Node node){
        Node fun_else =  node.addNode("fun_else");
        if(matchL("!", fun_else) &&  matchL(":", fun_else) && comando(fun_else) != false ){
            return true;
        }
        return false;
    }

    private boolean  fun_for(Node node){
        Node fun_for = node.addNode("fun_for");
        if(matchL("IV", fun_for) &&  matchT("IDENTIFICADOR", fun_for) && matchL("abt", fun_for) && matchL("(", fun_for) && var(fun_for) != false && matchL("até", fun_for) && var(fun_for) != false && matchL(")", fun_for) && matchL(":", fun_for) && comando(fun_for) != false){
            return true;
        }
        return false;
    }

    private boolean  fun_while(Node node){
        Node fun_while = node.addNode("fun_while");
        if(matchL("£", fun_while) &&  condicao(fun_while) != false && matchL(":", fun_while) && comando(fun_while)!= false && fun_else(fun_while) != false ){
            return true;
        }
        return false;
    }
}