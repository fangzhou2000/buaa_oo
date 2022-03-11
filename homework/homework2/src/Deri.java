public class Deri {
    public static String deri(Node root) {
        if (root.getLeftnode() == null && root.getRightnode() == null) {
            return Factor.deri(root.getFactor());
        } else {
            String result1 = Deri.deri(root.getLeftnode());
            String result2 = Deri.deri(root.getRightnode());
            String origin1 = root.getLeftnode().toString();
            String origin2 = root.getRightnode().toString();
            if (root.getFactor().toString().equals("+")) {
                if (result1.equals("0") && result2.equals("0")) {
                    return "0";
                } else if (result1.equals("0")) {
                    return result2;
                } else if (result2.equals("0")) {
                    return result1;
                } else {
                    return result1 + "+" + result2;
                }
            } else if (root.getFactor().toString().equals("-")) {
                if (result1.equals("0") && result2.equals("0")) {
                    return "0";
                } else if (result1.equals("0")) {
                    return "(-" + result2 + ")";
                } else if (result2.equals("0")) {
                    return result1;
                } else {
                    return result1 + "-" + result2;
                }
            } else if (root.getFactor().toString().equals("*")) {
                return deritoolong(result1, result2, origin1, origin2);
            }
        }
        return "";
    }

    public static String deritoolong(String result01, String result02,
                                     String origin1, String origin2) {
        String result1 = result01;
        String result2 = result02;
        if (!ManageExp.deleteBracket(result1)) {
            result1 = "(" + result1 + ")";
        }
        if (!ManageExp.deleteBracket(result2)) {
            result2 = "(" + result2 + ")";
        }
        if ((result1.equals("0") || origin2.equals("0")) &&
                (result2.equals("0") || origin1.equals("0"))) {
            return "0";
        } else if (result1.equals("0") || origin2.equals("0")) {
            if (origin1.equals("1")) {
                return result2;
            } else if (result2.equals("1")) {
                return origin1;
            } else {
                return origin1 + "*" + result2;
            }
        } else if (origin1.equals("0") || result2.equals("0")) {
            if (result1.equals("1")) {
                return origin2;
            } else if (origin2.equals("1")) {
                return result1;
            } else {
                return result1 + "*" + origin2;
            }
        } else {
            String resultplus1;
            String resultplus2;
            if (result1.equals("1")) {
                resultplus1 = origin2;
            } else if (origin2.equals("1")) {
                resultplus1 = result1;
            } else {
                resultplus1 = result1 + "*" + origin2;
            }
            if (origin1.equals("1")) {
                resultplus2 = result2;
            } else if (result2.equals("1")) {
                resultplus2 = origin1;
            } else {
                resultplus2 = origin1 + "*" + result2;
            }
            return "(" + resultplus1 + "+" + resultplus2 + ")";
        }
    }
}


