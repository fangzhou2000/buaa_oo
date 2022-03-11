import java.util.regex.Matcher;

public class CreatTree {
    public static Tree getRoot(String string0) {
        String string = string0;
        Tree tree = new Tree();
        while (!string.equals("")) {
            String o = string.substring(0, 1);
            Factor op = new Oper(o);
            Node nodeOp = new Node(op);
            string = string.substring(1);
            Matcher matcherExp = Macro.getPatternExp().matcher(string);
            Matcher matcherPower = Macro.getPatternPower().matcher(string);
            Matcher matcherSin = Macro.getPatternSin().matcher(string);
            Matcher matcherCos = Macro.getPatternCos().matcher(string);
            Matcher matcherConst = Macro.getPatternConst().matcher(string);
            if (matcherExp.find()) {
                Factor factor = FactorFactory.generateFactor(matcherExp.group(0));
                string = string.substring(matcherExp.group(0).length());
                Node node = new Node(factor);
                tree = tree.add(node, nodeOp);
            } else if (matcherPower.find()) {
                Factor factor = FactorFactory.generateFactor(matcherPower.group(0));
                string = string.substring(matcherPower.group(0).length());
                Node node = new Node(factor);
                tree = tree.add(node, nodeOp);
            } else if (matcherSin.find()) {
                Factor factor = FactorFactory.generateFactor(matcherSin.group(0));
                string = string.substring(matcherSin.group(0).length());
                Node node = new Node(factor);
                tree = tree.add(node, nodeOp);
            } else if (matcherCos.find()) {
                Factor factor = FactorFactory.generateFactor(matcherCos.group(0));
                string = string.substring(matcherCos.group(0).length());
                Node node = new Node(factor);
                tree = tree.add(node, nodeOp);
            } else if (matcherConst.find()) {
                Factor factor = FactorFactory.generateFactor(matcherConst.group(0));
                string = string.substring(matcherConst.group(0).length());
                Node node = new Node(factor);
                tree = tree.add(node, nodeOp);
            }
        }
        return tree;
    }
}

/*if (matcherConst.find()) {
                BigInteger c = new BigInteger(matcherConst.group(1));
                Factor factor = new Const(op,c);
            } else if (matcherPower.find()) {
                BigInteger index;
                if (matcherPower.groupCount() == 2) {
                    index = new BigInteger(matcherPower.group(2));
                } else {
                    index = BigInteger.ZERO;
                }
                Factor factor = new Power(op,index);
            } else if (matcherSin.find()) {
                BigInteger index;
                if (matcherSin.groupCount() == 2) {
                    index = new BigInteger(matcherSin.group(2));
                } else {
                    index = BigInteger.ZERO;
                }
                Factor factor = new Sin(op , index);
            } else if (matcherCos.find()) {
                BigInteger index;
                if (matcherCos.groupCount() == 2) {
                    index = new BigInteger(matcherCos.group(2));
                } else {
                    index = BigInteger.ZERO;
                }
                Factor factor = new Cos(op, index);
            } else {
                System.out.println("creat wrong");
            }*/
