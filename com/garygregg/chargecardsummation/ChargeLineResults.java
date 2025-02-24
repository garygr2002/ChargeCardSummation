package com.garygregg.chargecardsummation;

/**
 * Contains a template class for charge-line results.
 * 
 * @author Gary Gregg
 * 
 * @param <FirstType>
 *            The type of the first parameter
 * @param <SecondType>
 *            The type of the second parameter
 */
public class ChargeLineResults<FirstType, SecondType> {

    // The first parameter
    private final FirstType first;

    // The second parameter
    private final SecondType second;

    /**
     * Constructs the charge-line results with a default second parameter.
     * 
     * @param first
     *            The first parameter
     */
    ChargeLineResults(FirstType first) {
        this(first, null);
    }

    /**
     * Constructs the charge-line results with an explicit second parameter.
     * 
     * @param first
     *            The first parameter
     * @param second
     *            The second parameter
     */
    ChargeLineResults(FirstType first, SecondType second) {

        // Initialize the member variables.
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first parameter.
     * 
     * @return The first parameter
     */
    public FirstType getFirst() {
        return first;
    }

    /**
     * Gets the second parameter.
     * 
     * @return The second parameter
     */
    public SecondType getSecond() {
        return second;
    }
}