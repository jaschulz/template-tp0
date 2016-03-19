package ar.fiuba.tdd.template.tp0;

import java.util.*;
import java.util.Random;

public class RegExGenerator {

    private int maxLength;
    private  Parser myParser;
    private int available;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
        myParser = new Parser();
        available = maxLength;
    }

    private String generateAllowedChars() {
        StringBuilder allowedChars = new StringBuilder("");
        for (int i = 0; i < 256 ; i++) {
            allowedChars.append((char)i);
        }
        return allowedChars.toString();
    }

    private String getAllowedChars(String pattern) {
        if (pattern.equalsIgnoreCase(".")) {
            return generateAllowedChars();
        } else if (pattern.substring(0, 1).equalsIgnoreCase("[") && pattern.contains("]")) {
            String allowedChars;
            int index = pattern.length();
            allowedChars = pattern.substring(1, index - 1).replace("\\","");
            return allowedChars;
        }
        return pattern;
    }

    private String getPassingValue(RegExNode regexNode, String allowedChars, int compulsoryLeft) {
        int minRep;
        int maxRep;
        StringBuilder value = new StringBuilder("");
        Random randRepetitions = new Random();
        Random randomChar = new Random();
        minRep = regexNode.getQuantifierMin();
        maxRep = regexNode.getQuantifierMax(available, compulsoryLeft);
        int repetitions = (randRepetitions.nextInt((maxRep - minRep) + 1) + minRep);
        for (int i = 0; i < repetitions; i++) {
            value.append(allowedChars.charAt(randomChar.nextInt(allowedChars.length())));
        }
        return value.toString();
    }

    private String generateCharsToAppend(RegExNode regExNode, int compulsoryLeft) {
        String allowedChars;
        allowedChars = getAllowedChars(regExNode.getRegex());
        return getPassingValue(regExNode, allowedChars, compulsoryLeft);
    }

    private String generateValue() throws LengthNotSupportedException {
        StringBuilder value = new StringBuilder("");
        available = maxLength - myParser.getCompulsory();
        int compulsoryLeft = myParser.getCompulsory();
        if (available < 0) {
            throw new LengthNotSupportedException("Largo no soportado");
        }
        for (RegExNode regExNode : myParser.getList()) {
            String charsToAppend = generateCharsToAppend(regExNode, compulsoryLeft);
            available = available - charsToAppend.length();
            compulsoryLeft = compulsoryLeft - regExNode.getQuantifierMin();
            value.append(charsToAppend);
            if (available == 0 && compulsoryLeft == 0) {
                return value.toString();
            }
        }
        return value.toString();
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public List<String> generate(String regEx, int numberOfResults) throws NotSupportedRegExException, LengthNotSupportedException  {
        List<String> resultStrings = new ArrayList<>();
        myParser.generateList(regEx);
        String value;
        for (int j = 0; j < numberOfResults; j++) {
            value = generateValue();
            resultStrings.add(value);
            setAvailable(maxLength);
        }
        return  resultStrings;
    }
}