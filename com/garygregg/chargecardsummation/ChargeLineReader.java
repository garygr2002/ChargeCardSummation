package com.garygregg.chargecardsummation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChargeLineReader {

    /**
     * Runs the charge line reader with a sample file.
     *
     * @param args
     *            Command line arguments
     */
    public static void main(String[] args) {

        /*
         * Create the charge line reader around the file given as a command line
         * argument.
         */
        final File file = new File((args.length > 0) ? args[0] : "");
        ChargeLineReader reader = new ChargeLineReader(file);

        // Try to sum charge line results from the file.
        try {

            // Sum the results in the file.
            final ChargeLineResults<Integer, ChargeCardSummation> results = reader
                    .sum();
            ChargeCardSummation summation = results.getSecond();

            // Print the results.
            System.out.println(String.format(
                    "The size of the file is %d; the summation is $%.2f.",
                    results.getFirst(), summation.getSum()));

            // Print the location of any parse errors that occurred.
            printParseErrors(summation.getErrors());
        }

        // Catch any file-not-found exception that may occur.
        catch (FileNotFoundException fileNotFoundException) {

            System.err.println("The indicated file '" + file.getAbsolutePath()
                    + "' was not found.");
        }

        // Catch any I/O exception that may occur.
        catch (IOException ioException) {

            System.err
                    .println("An I/O exception occurred while parsing the named file: '"
                            + file.getAbsolutePath() + "'.");
        }
    }

    /**
     * Prints the location, if any, of any parse errors that occurred.
     *
     * @param errors
     *            An array of parse error locations
     */
    private static void printParseErrors(Integer[] errors) {

        // Print a message if no parse errors occurred.
        if (errors.length < 1) {
            System.out.println("No parse errors occurred.");
        }

        // One or more parse errors occurred.
        else {

            // Print an informational message, and cycle for each parse error.
            System.out
                    .println("Parse errors occurred at the following locations - ");
            for (Integer error : errors) {

                // Print the location of the first/next error.
                System.out.println("Location: " + error);
            }
        }
    }

    // A buffer for receiving bytes
    private final byte[] buffer;

    // The maximum buffer size
    private final int bufferSize;

    // The file to parse for currency values
    private final File file;

    /**
     * Constructs the charge line reader with a default buffer size.
     *
     * @param file
     *            The file from which to read charge lines
     */
    public ChargeLineReader(File file) {
        this(file, file.length());
    }

    /**
     * Constructs the charge line reader with an explicit buffer size.
     *
     * @param file
     *            The file from which to read charge lines
     * @param bufferSize
     *            The explicit buffer size for receiving file contents
     */
    public ChargeLineReader(File file, long bufferSize) {

        // Initialize the member variables.
        this.file = file;
        this.bufferSize = (int) (bufferSize < Integer.MAX_VALUE ? bufferSize
                : Integer.MAX_VALUE);
        this.buffer = new byte[this.bufferSize];
    }

    /**
     * Sums currency values contained the file.
     *
     * @return Charge line results for the summation
     * @throws FileNotFoundException
     *             Indicates that the named file could not be found
     * @throws IOException
     *             Indicates that an error occurred reading contents of the
     *             named file
     */
    public ChargeLineResults<Integer, ChargeCardSummation> sum()
            throws FileNotFoundException, IOException {

        /*
         * Create a file input stream from the named file, and read its
         * contents. Close the input file stream.
         */
        FileInputStream stream = new FileInputStream(file);
        final int length = stream.read(buffer, 0, bufferSize);
        stream.close();

        /*
         * Create a charge card summation object, and add the contents of the
         * named file.
         */
        final ChargeCardSummation summation = new ChargeCardSummation();
        summation.parse(new String(buffer, 0, (length < 0) ? bufferSize
                : length));

        // Return the sum of values in the named file.
        return new ChargeLineResults<Integer, ChargeCardSummation>(length,
                summation);
    }
}
