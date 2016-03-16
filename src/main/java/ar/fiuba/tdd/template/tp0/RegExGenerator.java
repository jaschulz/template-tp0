package ar.fiuba.tdd.template.tp0;

import java.util.*;
import java.util.Random;

public class RegExGenerator {

    private int maxLength;
    private  Parser myParser;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    private String getAllowedCharsGroup(String regEx) {
        String allowedChars;
        int index = regEx.indexOf("]");
        allowedChars = regEx.substring(1, index);
        return allowedChars;
    }

    private String getAllowedChars(String pattern) {
        if (pattern.equalsIgnoreCase(".")) {
            return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        }
        return pattern;
    }

    public int calculateMinRep(String quantifier) {
        return (quantifier.equalsIgnoreCase("*") || quantifier.equalsIgnoreCase("?")) ? 0 : 1;
    }

    public int calculateMaxRep(String quantifier) {
        return (quantifier.equalsIgnoreCase("*") || quantifier.equalsIgnoreCase("+")) ? maxLength : 1;
    }

    private String getPassingValue(String quantifier, String allowedChars) {
        int minRep;
        int maxRep;
        StringBuilder value = new StringBuilder("");
        Random randRepetitions = new Random();
        Random randomChar = new Random();
        minRep = calculateMinRep(quantifier);
        maxRep = calculateMaxRep(quantifier);
        int repetitions = (randRepetitions.nextInt((maxRep - minRep) + 1) + minRep);
        for (int i = 0; i < repetitions; i++) {
            value.append(allowedChars.charAt(randomChar.nextInt(allowedChars.length())));
        }
        return value.toString();
    }


    private String generateValue() {
        StringBuilder value = new StringBuilder("");
        String allowedChars;
        String partialRegex;
        String quantifier;
        for (int i = 0; i < myParser.getRegexListSize(); i++) {
            RegExNode regExNode = myParser.getNodeAt(i);
            partialRegex = regExNode.getRegex();
            if (partialRegex.substring(0, 1).equalsIgnoreCase("[") && partialRegex.contains("]")) {
                allowedChars = getAllowedCharsGroup(partialRegex);
            } else {
                allowedChars = getAllowedChars(partialRegex);
            }
            quantifier = regExNode.getQuantifier();
            value.append(getPassingValue(quantifier, allowedChars));
        }
        return value.toString();
    }





    public List<String> generate(String regEx, int numberOfResults) {
        List<String> resultStrings = new ArrayList<>();
        myParser = new Parser(regEx);
        String value;
        for (int j = 0; j < numberOfResults; j++) {
            value = generateValue();
            resultStrings.add(value);
        }
        return  resultStrings;
    }
}