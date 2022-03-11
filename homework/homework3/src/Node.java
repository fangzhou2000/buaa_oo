public class Node {
    private Factor factor;
    private Node leftnode;
    private Node rightnode;

    public Node(Factor factor, Node leftnode, Node rightnode) {
        this.factor = factor;
        this.leftnode = leftnode;
        this.rightnode = rightnode;
    }

    public Node(Factor factor) {
        this.factor = factor;
        this.leftnode = null;
        this.rightnode = null;
    }

    public void add(Node node, Node nodeop) {
        if (this.rightnode == null) {
            Node leftNode = new Node(Factor.clone(this.factor));
            this.setFactor(nodeop.getFactor());
            this.setLeftnode(leftNode);
            this.setRightnode(node);
        } else {
            this.rightnode.add(node, nodeop);
        }
    }

    public String toString() {
        if (leftnode == null && rightnode == null) {
            return factor.toString();
        } else {
            return leftnode.toString() + factor.toString() + rightnode.toString();
        }
    }

    public Factor getFactor() {
        return factor;
    }

    public Node getLeftnode() {
        return leftnode;
    }

    public Node getRightnode() {
        return rightnode;
    }

    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    public void setLeftnode(Node leftnode) {
        this.leftnode = leftnode;
    }

    public void setRightnode(Node rightnode) {
        this.rightnode = rightnode;
    }
}
