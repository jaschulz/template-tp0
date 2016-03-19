package ar.fiuba.tdd.template.tp0;

import java.util.*;

public class Parser {
    private List<RegExNode> regExList;

    private int compulsory;

    public Parser()  {
        compulsory = 0;
        regExList = new ArrayList<>();
    }

    private boolean isSlash(String regex) {
        return regex.substring(0,1).equalsIgnoreCase("\\");
    }

    private void testValidChar(String curChar) throws NotSupportedRegExException {
        String invalidChars = "([^$|)";
        if (invalidChars.contains(curChar)) {
            throw new NotSupportedRegExException("Regex no válida");
        }
    }

    private String removeEscapedChar(String str) {
        str = str.replace("\\^","");
        str = str.replace("\\$","");
        str = str.replace("\\[","");
        str = str.replace("\\]","");
        return  str;
    }

    private void testValidGroup(String group) throws NotSupportedRegExException {
        group = removeEscapedChar(group);
        if (group.startsWith("^") || group.contains("-") || group.contains("[") || group.contains("]") ) {
            throw new NotSupportedRegExException("Regex no válida");
        }
    }

    private int getIndexOfUnescapedRightBracket(String stringToTest) throws NotSupportedRegExException {
        int index = 1;
        while (index < stringToTest.length()) {
            if (stringToTest.charAt(index) == ']' && stringToTest.charAt(index - 1) != '\\') {
                return index;
            }
            index++;
        }
        throw new NotSupportedRegExException("Regex no válida");
    }

    private String getPartialRegex(String regex) throws NotSupportedRegExException {
        String curChar = regex.substring(0, 1);
        if (curChar.equalsIgnoreCase("[") ) {
            int endGroup = getIndexOfUnescapedRightBracket(regex);
            String group = regex.substring(1, endGroup);
            testValidGroup(group);
            return regex.substring(0,endGroup + 1);
        } else {
            testValidChar(curChar);
            return curChar;
        }
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

    private void testCharAtFirstPosition(String regex) throws NotSupportedRegExException {
        String firstChar = regex.substring(0,1);
        if (isQuantifier(firstChar)) {
            throw new NotSupportedRegExException("Una regex no puede comenzar con" + firstChar);
        }
    }

    private String parseQuantifier(String regex) {
        if (regex.length() > 0) {
            String nextChar = regex.substring(0, 1);
            if (isQuantifier(nextChar)) {
                return nextChar;
            }
        }
        return "";
    }

    public void generateList(String regex) throws NotSupportedRegExException {
        String partialRegex;
        testCharAtFirstPosition(regex);
        while (regex.length() > 0) {
            int slashLength = 0;
            if (isSlash(regex)) {
                partialRegex = getLiteral(regex);
                slashLength++;
            } else {
                partialRegex = getPartialRegex(regex);
            }
            regex = regex.substring(partialRegex.length() + slashLength);
            String quantifier = parseQuantifier(regex);
            regex = regex.substring(quantifier.length());
            compulsory = (quantifier.equalsIgnoreCase("?") || quantifier.equalsIgnoreCase("*") ) ? compulsory : ++compulsory;
            RegExNode regNode = new RegExNode(partialRegex,quantifier);
            regExList.add(regNode);
        }
    }

    public int getCompulsory() {
        return compulsory;
    }

    public List<RegExNode> getList() {
        return regExList;
    }

}
