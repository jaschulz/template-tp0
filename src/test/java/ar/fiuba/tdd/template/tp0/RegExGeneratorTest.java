package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private static final int MAX_LENGTH = 10;

    private boolean validate(String regEx, int numberOfResults)
            throws NotSupportedRegExException, LengthNotSupportedException {
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
    public void testAnyCharacter() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate(".", 1));
    }

    @Test
    public void testMultipleCharacters() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("...", 1));
    }

    @Test
    public void testLiteral() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("\\@", 1));
    }

    @Test
    public void testLiteralDotCharacter() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[abc+]+", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers2() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[abc]?", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers3() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[abc]*", 4));
    }

    @Test
    public void testCharacterSetWithQuantifiers4() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[abc]*a", 4));
    }

    @Test
    public void testLiteralBrackets() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("\\[*", 4));
    }

    @Test
    public void testLiteralSlash() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("\\\\", 4));
    }

    @Test
    public void testMultipleLiteralSlashes() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("\\\\*", 4));
    }

    @Test
    public void testLiteralChar() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[a\\[b]*", 4));
    }

    @Test
    public void testLiteralChar2() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[a)b]*", 4));
    }

    @Test
    public void testLiteralChar3() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate("[a\\]b]*", 4));
    }

    @Test
    public void testQuantifiersAndGroup()
            throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate(".*[XYZ]?a*.?", 4));
    }

    @Test
    public void testBracketAtIndexOne() throws NotSupportedRegExException, LengthNotSupportedException {
        assertTrue(validate(".*[\\]]?a*.?", 4));
    }

    @Test(expected = NotSupportedRegExException.class)
    public void testInvalidRegEx() throws NotSupportedRegExException, LengthNotSupportedException {
        validate("[a[bc]*",1);
    }

    @Test(expected = NotSupportedRegExException.class)
    public void testInvalidRegEx2() throws NotSupportedRegExException, LengthNotSupportedException {
        validate("+[a[bc]*",1);
    }

    @Test(expected = LengthNotSupportedException.class)
    public void testNotSupportedLength() throws NotSupportedRegExException, LengthNotSupportedException {
        validate("aaaaaaaaaa.+",1);
    }
}

