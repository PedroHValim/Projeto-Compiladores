public class MainTreeTeste{
    public static void main(String[] args){
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");
        Node nodeG = new Node("G");
        Node nodeH = new Node("H");


        nodeA.addNode(nodeB);
        nodeA.addNode(nodeC);
        nodeA.addNode(nodeD);
        nodeB.addNode(nodeE);
        nodeB.addNode(nodeF);
        nodeC.addNode(nodeG);
        nodeC.addNode(nodeH);

        Tree tree = new Tree(nodeA);

        tree.preOrder();
        tree.printCode();
        tree.printTree();
    }
}