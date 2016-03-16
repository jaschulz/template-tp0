package ar.fiuba.tdd.template.tp0;

import java.util.*;

public class Parser {
    private List<RegExNode> regExList = new ArrayList<>();

    public Parser(String regex)  throws NotSupportedRegExException {
        generateList(regex);
    }

    private boolean isSlash(String regex) {
        return regex.substring(0,1).equalsIgnoreCase("\\");
    }

    private void testValidChar(String curChar) throws NotSupportedRegExException {
        Set<String> invalidChars = new HashSet<>();
        invalidChars.add("^");
        invalidChars.add("$");
        invalidChars.add("]");
        invalidChars.add("[");
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
        /*Set<String> invalidChars = new HashSet<>();
        invalidChars.add("^");
        invalidChars.add("$");
        invalidChars.add("]");
        invalidChars.add("[");*/
        group = removeEscapedChar(group);
        if (group.startsWith("^") || group.contains("$") || group.contains("[") || group.contains("]") ) {
            throw new NotSupportedRegExException("Regex no válida");
        }
    }

    private String getPartialRegex(String regex) throws NotSupportedRegExException {
        String curChar = regex.substring(0, 1);
        if (curChar.equalsIgnoreCase("[") ) {
            int endGroup = regex.indexOf("]");
            String group = regex.substring(1, endGroup);
            testValidGroup(group);
            return regex.substring(0,endGroup + 1);
        } else {
            testValidChar(curChar);
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

    /*private void validateRegEx(String regex){

    }*/

    private void generateList(String regex)  throws NotSupportedRegExException {
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

    public List<RegExNode> getList() {
        return regExList;
    }
}
