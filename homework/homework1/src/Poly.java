import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Poly {
    private ArrayList<Power> poly = new ArrayList<>();

    public ArrayList<Power> getPoly() {
        return poly;
    }

    public void derivative() {
        for (Power power : poly) {
            power.derivative();
        }
    }

    public void sort() {
        poly.sort(new Comparator<Power>() {
            @Override
            public int compare(Power o1, Power o2) {
                return o1.getIndex().compareTo(o2.getIndex());
            }
        });

    }

    public void mergeOp() {
        //将项的符号全变为+，并相应修改系数的符号
        for (Power power : poly) {
            if (power.getOp().equals("-")) {
                BigInteger c = power.getCoef().negate();
                power.setOp("+");
                power.setCoef(c);
            }
        }
    }

    public void merge() {
        //合并指数相同的幂函数，被合并的幂函数系数为0,依次相互与指数相同的项合并
        for (int i = 0; i < poly.size() - 1; i++) {
            if (poly.get(i).getIndex().equals(poly.get(i + 1).getIndex())) {
                poly.get(i + 1).merge(poly.get(i));
                poly.get(i).setCoef(BigInteger.ZERO);
            }
        }
    }

    public void sortFrist() {
        //选取符号为+的作为表达式开头
        for (int i = 0; i < poly.size(); i++) {
            if (!poly.get(i).getCoef().equals(BigInteger.ZERO)) {
                if (poly.get(i).getOp().equals(poly.get(i).getCoefop())) {
                    if (i == 0) {
                        break;
                    } else {
                        Collections.swap(poly, 0, i);
                        break;
                    }
                }
            }
        }
    }

    public void print() {
        String output = "";
        for (int i = 0; i < poly.size(); i++) {
            //系数为0不输出
            if (!poly.get(i).getCoef().equals(BigInteger.ZERO)) {
                if (poly.get(i).getOp().equals(poly.get(i).getCoefop())) {
                    if (poly.get(i).getIndex().equals(BigInteger.ZERO)) {
                        output = output + "+" + poly.get(i).getCoef().abs();
                    } else if (poly.get(i).getIndex().equals(BigInteger.ONE)) {
                        output = output + "+" + poly.get(i).getCoef().abs() + "*x";
                    } else {
                        output = output + "+" +
                                poly.get(i).getCoef().abs() + "*x**" + poly.get(i).getIndex();
                    }
                } else {
                    if (poly.get(i).getIndex().equals(BigInteger.ZERO)) {
                        output = output + "-" + poly.get(i).getCoef().abs();
                    } else if (poly.get(i).getIndex().equals(BigInteger.ONE)) {
                        output = output + "-" + poly.get(i).getCoef().abs() + "*x";
                    } else {
                        output = output + "-" +
                                poly.get(i).getCoef().abs() + "*x**" + poly.get(i).getIndex();
                    }
                }
            }
        }
        if (output.equals("")) {
            output = "0";
        }
        if (output.charAt(0) == '+') {
            output = output.substring(1);
        }
        System.out.println(output);
    }
}
