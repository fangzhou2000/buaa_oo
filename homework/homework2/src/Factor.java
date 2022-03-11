public class Factor {
    public static Factor clone(Factor factor) {
        if (factor.getClass().getName().equals("Oper")) {
            Oper oper = (Oper) factor;
            return oper.clone();
        } else if (factor.getClass().getName().equals("Const")) {
            Const con = (Const) factor;
            return con.clone();
        } else if (factor.getClass().getName().equals("Power")) {
            Power power = (Power) factor;
            return power.clone();
        } else if (factor.getClass().getName().equals("Cos")) {
            Cos cos = (Cos) factor;
            return cos.clone();
        } else if (factor.getClass().getName().equals("Sin")) {
            Sin sin = (Sin) factor;
            return sin.clone();
        } else if (factor.getClass().getName().equals("Expression")) {
            Expression exp = (Expression) factor;
            return exp.clone();
        }
        return null;
    }

    public static String deri(Factor factor) {
        if (factor.getClass().getName().equals("Oper")) {
            Oper oper = (Oper) factor;
            return "";
        } else if (factor.getClass().getName().equals("Const")) {
            Const con = (Const) factor;
            return con.deri();
        } else if (factor.getClass().getName().equals("Power")) {
            Power power = (Power) factor;
            return power.deri();
        } else if (factor.getClass().getName().equals("Cos")) {
            Cos cos = (Cos) factor;
            return cos.deri();
        } else if (factor.getClass().getName().equals("Sin")) {
            Sin sin = (Sin) factor;
            return sin.deri();
        } else if (factor.getClass().getName().equals("Expression")) {
            Expression exp = (Expression) factor;
            return exp.deri();
        }
        return null;
    }
}
