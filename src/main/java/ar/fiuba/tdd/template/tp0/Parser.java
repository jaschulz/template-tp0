package ar.fiuba.tdd.template.tp0;

import java.util.*;

public class Parser {
    private List<RegExNode> regExList = new ArrayList<>();

    public Parser(String regex) {
        generateList(regex);
    }

    private boolean isSlash(String regex) {
        return regex.substring(0,1).equalsIgnoreCase("\\");
    }

    private String getPartialRegex(String regex) {
        String curChar = regex.substring(0, 1);
        if (curChar.equalsIgnoreCase("[") ) {
            int endGroup = regex.indexOf("]");
            return regex.substring(0,endGroup + 1);
        } else {
            return curChar;
        }
    }

    public int getRegexListSize() {
        return regExList.size();
    }

    private String getLiteral(String regex) {
        return regex.substring(1,2);
    }

    private boolean isQuantifier(String charToTest) {
        Set<String> quantifiers = new HashSet<>();
        quantifiers.add("+");
        quantifiers.add("?");
        quantifiers.add("*");
        return quantifiers.contains(charToTest);
    }

    private void generateList(String regex) {
        String partialRegex;
        String quantifier;
        while (regex.length() > 0) {
            int slashLength = 0;
            if (isSlash(regex)) {
                partialRegex = getLiteral(regex);
                slashLength++;
            } else {
                partialRegex = getPartialRegex(regex);
            }
            regex = regex.substring(partialRegex.length() + slashLength);
            quantifier = "";
            if (regex.length() > 0) {
                String nextChar = regex.substring(0, 1);
                if (isQuantifier(nextChar)) {
                    quantifier = nextChar;
                    regex = regex.substring(1);
                }
            }
            RegExNode regNode = new RegExNode(partialRegex,quantifier);
            regExList.add(regNode);
        }
    }

    public RegExNode getNodeAt(int index) {
        return  regExList.get(index);
    }
}
