/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * Custom exception for handling input files that do not have
 * expected format.
 * @author Ross Petridis
 */
public class DataFormatException extends Exception {
    public DataFormatException() {
        super("File in unparsable format - Aborting!!\n");
    }

    public DataFormatException(String message) {
        super(message);
    }
}
