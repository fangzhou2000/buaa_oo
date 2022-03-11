public class Expression extends Factor {
    private String string;

    public Expression(String string) {
        this.string = string;
    }

    public Factor clone() {
        return new Expression(string);
    }

    public String toString() {
        //这个字符串不是符合题目要求的字符串
        String result = string.replaceAll("\\^", "**");
        if (ManageExp.deleteBracket(result)) {
            return result;
        } else {
            return "(" + result + ")";
        }
    }

    public String deri() {
        String substring = ManageInput.manageInput(string);
        //System.out.println("递归子字符串是：" + substring);
        Tree subtree;
        subtree = CreatTree.getRoot(substring);
        String result = Deri.deri(subtree.getRoot());
        if (ManageExp.deleteBracket(result)) {
            return result;
        } else {
            return "(" + result + ")";
        }
    }
}
