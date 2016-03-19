package ar.fiuba.tdd.template.tp0;

public class RegExNode {

    private String regex;

    private String quantifier;

    public RegExNode(String reg, String quantifier) {
        regex = reg;
        this.quantifier = quantifier;
    }

    public String getRegex() {
        return regex;
    }

    public String getQuantifier() {
        return quantifier;
    }

    public int getQuantifierMin() {
        return (quantifier.equalsIgnoreCase("+") || quantifier.isEmpty() ) ? 1 : 0;
    }

    public int getQuantifierMax(int available, int compulsoryLeft) {
        if (quantifier.equalsIgnoreCase("*") || quantifier.equalsIgnoreCase("+")) {
            return (available - compulsoryLeft);
        } else {
            return 1;
        }
    }

}
