import java.util.Objects;

public final class Term {
    private final int coe;
    private final int exp;

    public Term(int coe, int exp) {
        this.coe = coe;
        this.exp = exp;
    }

    public int getCoe() {
        return coe;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Term)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Term term = (Term) o;
        if (this.coe == term.coe && this.exp == term.exp) {   //TODO
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(coe, exp);
    }

    @Override
    public String toString() {
        // TODO
        if (this.exp == 0) {
            if (this.coe > 0) {
                return "+" + coe;
            } else {
                return Integer.toString(coe);
            }
        } else if (this.coe == 1 && this.exp == 1) {
            return "+x";
        } else if (this.coe == 1) {
            return "+x**" + exp;
        } else if (this.coe == -1 && this.exp == 1) {
            return "-x";
        } else if (this.coe == -1) {
            return "-x**" + exp;
        } else if (this.exp == 1) {
            if (this.coe > 0) {
                return "+" + coe + "*x";
            } else {
                return coe + "*x";
            }
        } else {
            if (this.coe > 0 ){
                return "+" + coe + "*x**" + exp;
            } else {
                return coe + "*x**" + exp;
            }
        }
    }
}
