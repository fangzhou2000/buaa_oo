import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        Parse parse = new Parse(string);
        try {
            parse.matchExp();
            //先合并
            string = ManageMerge.manageItem(string);
            //System.out.println("合并的输入是:" + string);
            string = ManageInput.manageInput(string);
            //System.out.println("简单处理后的表达式是:" + string);
            //建立树
            Tree tree;
            tree = CreatTree.getRoot(string);
            //System.out.println("构建的表达式树是:" + tree.getRoot().toString());
            //求导
            string = Deri.deri(tree.getRoot());
            //输出结果
            if (string.equals("")) {
                string = "0";
            }
            //System.out.println("化简前的结果:" + string);
            //将结果合并
            string = ManageMerge.manageItem(string);
            System.out.println(string);
        } catch (MyException e) {
            System.out.println("WRONG FORMAT!");
        }
    }
}
