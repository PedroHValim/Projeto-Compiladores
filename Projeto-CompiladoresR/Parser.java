import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class Parser{

    List<Token> tokens;
    Token token;
    private Node root;
    private StringBuilder codigoFinal = new StringBuilder(); // guarda a tradução para C++

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void salvarCodigoCPP(String codigoGerado) {
        try (FileWriter writer = new FileWriter("Código.cpp")) {
            writer.write(codigoGerado);
            System.out.println("Arquivo C++ gerado: Código.cpp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compilarCPP() {
        try {
            ProcessBuilder pb = new ProcessBuilder("g++", "Código.cpp", "-o", "output");
            pb.inheritIO(); 
            Process p = pb.start();
            p.waitFor(); 
            System.out.println("Compilação concluída!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executarCPP() {
        try {
            ProcessBuilder pb = new ProcessBuilder("output.exe");
            // Se estiver no Windows: new ProcessBuilder("output.exe");
            pb.inheritIO(); 
            
            pb.redirectErrorStream(true); // junta saída de erro e saída normal
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }

            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void traduzirEExecutar(String codigoGerado) {
        salvarCodigoCPP(codigoGerado);
        compilarCPP();
        executarCPP();
    }



    public void main() {
        Node main = new Node("main");
        token = getNextToken();
        this.root = main;
        traduz("#include <iostream>\n#include <string>\nint main() {\n");
        boolean raiz = bloco(main);
        if(raiz != false){
            if (token.tipo.equals("EOF")){
                System.out.println("\nSintaticamente correta");
                System.out.println("\n=== Tradução Gerada ===");
                traduz("\nreturn 0;\n}");
                System.out.println(codigoFinal.toString());
                traduzirEExecutar(codigoFinal.toString());
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
        codigoFinal.append(newcode).append(" ");
    }

    private boolean matchT(String tipo,Node node){
        if(token.tipo.equals(tipo)){
            node.addNode(token.tipo);
            token = getNextToken();
            return true;
        }
        return false;
    }

    private boolean matchT(String tipo, String newcode, Node node) {
        if (token.tipo.equals(tipo)) {
            String traduzido = newcode;
            traduz(traduzido);
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
        Node bloco = new Node("bloco");
        if (tokenAtualInFirstComando()) {
            node.addNode(bloco);
            if (comando(bloco) != false && bloco(node) != false){
                return true;
            } 
        }
        if(inFollow("bloco")){
            node.addNode(bloco);
            return true;
        } 
        return false;
    }
    
    //FUNCAO PARA FIRST COMANDO JA QUE COMANDO SEMPRE COMECA COM OUTRAS FUNCOES
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
            case "COMMENT":
                return true;
            default:
                return false;
        }
    }


    //UMA FUNCAO PARA O CASO FOLLOW -> QUANDO TEMOS EPSILON
    //TEM TODOS OS FOLLOWS DE CADA REGRA
    private boolean inFollow(String regra) {
        if (token == null) return true; 
        String tk = token.lexema;

        switch (regra) {
            case "bloco":
            case "comando":
            case "fun_if_linha":
            case "fun_else":
                return tk.equals("?") || tk.equals("!") || tk.equals("!?") || 
                    tk.equals("IV") || tk.equals("£") || tk.equals(")") ||
                    token.tipo.equals("EOF");
            case "expr":
            case "soma":
            case "termo":
            case "fator":
                return tk.equals(")") || tk.equals(":") || tk.equals("até") || 
                    token.tipo.equals("OPERADORES");
            case "condicao":
                return tk.equals(":") || tk.equals(")") || tk.equals("até");
        }
        return false;
    }


    private boolean  comando(Node node) {
        Node comando = new Node("comando");
        
        // Testa e guarda o resultado do primeiro comando encontrado
        if ((declaracao(comando)) != false
        || (atribuicao(comando)) != false
        || (fun_if(comando)) != false
        || (fun_for(comando)) != false
        || (fun_while(comando)) != false
        || (fun_print(comando)) != false
        || (tupni(comando)) != false) {
            if(comando(comando) != false){
                node.addNode(comando);
                return true;
            }
        }
        if (inFollow("comando")){
            return true;
        } 

        return false;
    }



    private boolean  declaracao(Node node){
        Node declaracao = new Node("declaracao");
        if((matchL("int",token.lexema,declaracao) || matchL("float",token.lexema,declaracao) || matchL("str","std::string",declaracao))){
            String tokenID = token.lexema;
            if(matchT("IDENTIFICADOR",token.lexema,declaracao) && matchL("=","=",declaracao)){
                if(tupni(declaracao) != false){
                    node.addNode(declaracao);
                    traduz("std::cin >>" + tokenID + ";\n"); 
                    return true;
                }
                if(expr(declaracao) != false){
                    node.addNode(declaracao);
                    traduz(";\n"); 
                    return true;
                }
                return false;
            }
        }
        return false;
    
    }

    private boolean  atribuicao(Node node){
        Node atribuicao = new Node("atribuicao");
        String tokenID = token.lexema;
        if(matchT("IDENTIFICADOR",token.lexema, atribuicao) && matchL("=", token.lexema,atribuicao)){
            if(tupni(atribuicao) != false){
                node.addNode(atribuicao);
                traduz("std::cin >>" + tokenID + ";\n"); 
                return true;
            }
            if(expr(atribuicao) != false){
                node.addNode(atribuicao);
                traduz(";\n"); 
                return true;
            }
        }
        return false;
    }

    private boolean tupni(Node node){
        Node tupni = new Node("tupni");
        if(node.nome.equals("declaracao")){
            codigoFinal.deleteCharAt(codigoFinal.length() - 2);
            traduz(";");
            if(matchL("tupni", "std::cout <<",tupni)){
                 if(input_linha(tupni) != false){
                    node.addNode(tupni);
                    traduz(";\n"); 
                    return true;
                }
            }
            codigoFinal.deleteCharAt(codigoFinal.length() - 2);
            traduz("=");
        }
        if(matchL("tupni", "std::cout <<",tupni) && input_linha(tupni) != false){
            node.addNode(tupni);
            traduz(";\n"); 
            return true;
        }
        return false;
    }

    private boolean input_linha(Node node){
        Node input_linha = new Node("input_linha");
        if(matchL("(", input_linha)){
            if(matchL(")",input_linha)){
                node.addNode(input_linha);
                return true;
            }
            if(matchT("STRING",token.lexema,input_linha) && matchL(")",input_linha)){
                node.addNode(input_linha);
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean fun_print(Node node){
        Node fun_print = new Node("fun_print");
        if(matchL("wri","std::cout", fun_print) && matchL("(","<<", fun_print)){
            if(expr(fun_print) != false && matchL(")","<<std::endl", fun_print)){
                node.addNode(fun_print);
                traduz(";"); 
                return true;
            }
        }
        return false;
    }


    private boolean  condicao(Node node){
        Node caondicao = new Node("comando");
        traduz("(");
        if(expr(caondicao) != false){
            node.addNode(caondicao);
            traduz(") {");
            return true;
        }
        return false;
    }

    private boolean expr(Node node) {
        Node expr = new Node("expr");
        if (soma(expr)) {   
            while (matchT("OPERADORES", token.lexema, expr)) {
                if (!soma(expr)) return false;
            }
            node.addNode(expr);
            return true;
        }
        if (inFollow("expr")) return true; 
        return false;
    }

    private boolean soma(Node node) {
        Node soma = new Node("soma");
        if (termo(soma)) {
            while (matchL("+", token.lexema, soma) || matchL("-", token.lexema, soma)) {
                if (!termo(soma)) return false;
            }
            node.addNode(soma);
            return true;
        }
        if (inFollow("soma")) return true; 
        return false;
    }
    //A IDEIA É SEPARAR A PRIORIDADE DAS EQUACOES MATEMATICAS (* e /) e (+ e -) PARA LOGICA NO C++
    private boolean termo(Node node) {
        Node termo = new Node("termo");
        if (fator(termo)) {
            while (matchL("*", token.lexema, termo) || matchL("/", token.lexema, termo)) { 
                if (!fator(termo)) return false;
            }
            node.addNode(termo);
            return true;
        }
        if (inFollow("termo")) return true; 
        return false;
    }

    private boolean fator(Node node) {
        Node fator = new Node("fator");
        
        //AQUI DEIXIA SER POSSÍVEL POR EXEMPLO, FAZERMOS NOVAS EXPRESSÕES
        if (matchL("(", fator)) {
            if (!expr(fator)) return false;
            if (!matchL(")", fator)) return false;
            node.addNode(fator);
            return true;
        }
        //AQUI DEIXA SER POSSÍVEL POR EXEMPLO, EM DECLARAÇÃO SÓ DEIXARMOS 1 NUM, IDENT OU STRING
        if (matchT("NUM",token.lexema, fator) || matchT("IDENTIFICADOR", token.lexema, fator) || matchT("STRING",token.lexema, fator)) {
            node.addNode(fator);
            return true;
        }

        return false;
    }


    private boolean fun_if(Node node){
        Node fun_if = new Node("fun_if");
        
        if(matchL("?","if", fun_if) && condicao(fun_if) != false && matchL(":", fun_if) && comando(fun_if) != false && fun_if_linha(fun_if) != false){
            node.addNode(fun_if);
            return true;
        }
        return false;
    }

    private boolean  fun_if_linha(Node node){
        Node fun_if_linha = new Node("fun_if_linha");
        
        if(matchL("!?","}else if", fun_if_linha) && condicao(fun_if_linha) != false && matchL(":", fun_if_linha) && comando(fun_if_linha) != false && fun_if_linha(fun_if_linha) != false){
            node.addNode(fun_if_linha);
            return true;
        }else if (fun_else(fun_if_linha) != false) {
            node.addNode(fun_if_linha);
            return true;
        }
        if (inFollow("fun_if_linha")) return true; 
        return false;
    }

    private boolean  fun_else(Node node){
        Node fun_else =  new Node("fun_else");
        if(node.nome.equals("fun_while")){
            if(matchL("!", "}  {",fun_else) &&  matchL(":", fun_else) && comando(fun_else) != false ){
                node.addNode(fun_else);
                traduz("}");
                return true;
            }
        }   

        if(matchL("!", "} else {",fun_else) &&  matchL(":", fun_else) && comando(fun_else) != false ){
            node.addNode(fun_else);
            traduz("}");
            return true;
        }
        return false;
    }

    private boolean  fun_for(Node node){
        Node fun_for = new Node("fun_for");
        if(matchL("IV","for(", fun_for) &&  matchT("IDENTIFICADOR","int i = ", fun_for) && matchL("abt", fun_for) && matchL("(", fun_for) && expr(fun_for) != false && matchL("até","; i < ", fun_for) && expr(fun_for) != false && matchL(")", fun_for) && matchL(":","; i++) {", fun_for) && comando(fun_for) != false){
            node.addNode(fun_for);
            traduz("}");
            return true;
        }
        return false;
    }

    private boolean  fun_while(Node node){
        Node fun_while = new Node("fun_while");
        if(matchL("£","while", fun_while) &&  condicao(fun_while) != false && matchL(":", fun_while) && comando(fun_while)!= false && fun_else(fun_while) != false ){
            node.addNode(fun_while);
            return true;
        }
        return false;
    }
}
