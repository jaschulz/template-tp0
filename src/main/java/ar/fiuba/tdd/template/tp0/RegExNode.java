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
}
