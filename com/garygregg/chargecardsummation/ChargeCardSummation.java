package com.garygregg.chargecardsummation;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains a charge card summation class.
 * 
 * @author Gary Gregg
 */
public class ChargeCardSummation {

    // The default initial sum
    private static final double initialSum = 0.0;

    // The set of characters that need escapes
    private static final Set<Character> specialSet = new HashSet<Character>();

    static {

        // Add elements to the special set.
        specialSet.add('\\');
        specialSet.add('^');
        specialSet.add('$');
        specialSet.add('.');
        specialSet.add('|');
        specialSet.add('?');
        specialSet.add('*');
        specialSet.add('+');
        specialSet.add('(');
        specialSet.add(')');
        specialSet.add('[');
        specialSet.add(']');
    }

    /**
     * Adds regular expression escapes for individual characters in a given
     * string.
     * 
     * @param string
     *            The given string for which to add regular expression escapes
     * @return The modified input string
     */
    private static String addEscapes(String string) {

        /*
         * Declare and initialize a string buffer to build the modified string.
         * Get the length of the input character string. Declare and initialize
         * a character array of this length to receive the characters from the
         * input string.
         */
        final StringBuffer buffer = new StringBuffer();
        final int length = string.length();
        final char[] characters = new char[length];

        // Get the characters from the input string, and cycle for each.
        string.getChars(0, length, characters, 0);
        for (char character : characters) {

            /*
             * Append the escape character if the first/next character is
             * contained in the special set.
             */
            if (specialSet.contains(character)) {
                buffer.append('\\');
            }

            // Append the first/next character whether it was escaped or not.
            buffer.append(character);
        }

        // Return the buffer as a string.
        return buffer.toString();
    }

    // The parse errors
    private final Collection<Integer> errors = new ArrayList<Integer>();

    // The number format used for string parsing
    private final NumberFormat format = NumberFormat.getCurrencyInstance();

    // The regular expression containing the symbol
    private final String regex;

    // The current sum
    private double sum = initialSum;

    // The currency symbol
    private final String symbol = format.getCurrency().getSymbol();

    /**
     * Initializes the charge card summation.
     */
    public ChargeCardSummation() {
        this(initialSum);
    }

    /**
     * Initializes the charge card summation with an explicit initial sum.
     * 
     * @param initialSum
     *            The explicit initial sum
     */
    public ChargeCardSummation(double initialSum) {

        /*
         * Reinitialize the charge card summation with the explicit initial sum,
         * and build the regular expression search string.
         */
        reinitialize(initialSum);
        regex = addEscapes(symbol);
    }

    /**
     * Clears the parse errors.
     */
    public void clearErrors() {
        errors.clear();
    }

    /**
     * Gets the parse errors.
     * 
     * @return the parse errors
     */
    public Integer[] getErrors() {
        return errors.toArray(new Integer[errors.size()]);
    }

    /**
     * Gets the current sum.
     * 
     * @return The current sum
     */
    public double getSum() {
        return sum;
    }

    /**
     * Parses a string for charge values, and adds the values to the current
     * sum.
     * 
     * @param string
     *            The item to parse
     */
    public void parse(String string) {

        /*
         * Clear any existing parse errors. Split the item into currency values.
         * Cycle for each value after the first value.
         */
        clearErrors();
        final String[] values = string.split(regex);
        for (int index = 1; index < values.length; ++index) {

            // Try to add the first/next value to the sum.
            try {
                addToSum(format.parse(symbol + values[index]).doubleValue());
            }

            /*
             * Catch any parse exception that may occur. Add the current index
             * to the parse error collection.
             */
            catch (ParseException exception) {
                addError(index);
            }
        }
    }

    /**
     * Reinitializes the charge card summation.
     */
    public void reinitialize() {
        reinitialize(initialSum);
    }

    /**
     * Reinitializes the charge card summation with an explicit initial sum.
     * 
     * @param sum
     *            The explicit initial sum
     */
    public void reinitialize(double sum) {
        this.sum = sum;
    }

    /**
     * Adds a parse error.
     * 
     * @param index
     *            The index of the value that had a parse error
     */
    private void addError(int index) {
        errors.add(index);
    }

    /**
     * Adds a value to the current sum.
     * 
     * @param value
     *            The value to add to the current sum
     */
    private void addToSum(double value) {
        sum += value;
    }
}
