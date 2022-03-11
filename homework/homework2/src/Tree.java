public class Tree {
    private Node root;

    public Tree() {
        root = null;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Tree add(Node node, Node nodeop) {
        if (root == null) {
            root = node;
            return this;
        } else if (nodeop.toString().equals("+") || nodeop.toString().equals("-")) {
            nodeop.setLeftnode(this.root);
            nodeop.setRightnode(node);
            Tree newTree = new Tree();
            newTree.setRoot(nodeop);
            return newTree;
        } else if (nodeop.toString().equals("*")) {
            root.add(node, nodeop);
            return this;
        } else {
            return this;
        }
    }
}
