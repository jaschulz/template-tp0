package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private static final int MAX_LENGTH = 100;

    private boolean validate(String regEx, int numberOfResults) throws NotSupportedRegExException {
        RegExGenerator generator = new RegExGenerator(MAX_LENGTH);
        List<String> results = generator.generate(regEx, numberOfResults);
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }

    @Test
    public void testAnyCharacter() throws NotSupportedRegExException {
        assertTrue(validate(".", 1));
    }

    @Test
    public void testMultipleCharacters() throws NotSupportedRegExException {
        assertTrue(validate("...", 1));
    }

    @Test
    public void testLiteral() throws NotSupportedRegExException {
        assertTrue(validate("\\@", 1));
    }

    @Test
    public void testLiteralDotCharacter() throws NotSupportedRegExException {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() throws NotSupportedRegExException {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() throws NotSupportedRegExException {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() throws NotSupportedRegExException {
        assertTrue(validate("[abc]+", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers2() throws NotSupportedRegExException {
        assertTrue(validate("[abc]?", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers3() throws NotSupportedRegExException {
        assertTrue(validate("[abc]*", 4));
    }

    @Test
    public void testCharacterSetWithQuantifiers4() throws NotSupportedRegExException {
        assertTrue(validate("[abc]*a", 4));
    }

    @Test
    public void testLiteralBrackets() throws NotSupportedRegExException {
        assertTrue(validate("\\[*", 4));
    }

    @Test
    public void testLiteralSlashes() throws NotSupportedRegExException {
        assertTrue(validate("\\\\", 4));
    }

    @Test
    public void testMultipleLiteralSlashes() throws NotSupportedRegExException {
        assertTrue(validate("\\\\*", 4));
    }

    @Test
    public void testLiteralChar() throws NotSupportedRegExException {
        assertTrue(validate("[a\\[b]*", 4));
    }

    @Test
    public void testLiteralChar2() throws NotSupportedRegExException {
        assertTrue(validate("[a^b]*", 4));
    }


    @Test
    public void testQuantifiersAndGroup() throws NotSupportedRegExException {
        assertTrue(validate(".*[XYZ]?a*.?", 4));
    }

    @Test(expected = NotSupportedRegExException.class)
    public void testInvalidRegEx() throws NotSupportedRegExException {
        validate("[a[bc]*",1);
    }
}

