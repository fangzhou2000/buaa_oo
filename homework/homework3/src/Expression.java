public class Expression extends Factor {
    private String string;
    private String origin;
    private String change;

    //输入进来的是指数换了但是括号没换
    public Expression(String string) {
        this.string = string;
        this.origin = string.replaceAll("\\^", "**");
        this.change = ManageInput.manageInput(string);
    }

    public Factor clone() {
        return new Expression(string);
    }

    public String toString() {
        return ManageExp.manageExp(origin);
    }

    public String deri() {
        //System.out.println("递归子字符串是：" + change);
        Tree tree;
        tree = CreatTree.getRoot(change);
        String result = Deri.deri(tree.getRoot());
        return ManageExp.manageExp(result);
    }
}
