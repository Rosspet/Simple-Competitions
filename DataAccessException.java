/*
 * Student name: Ross Petridis
 * Student ID: 1080249
 * LMS username: rpetridis
 */

/**
 * Exception class for creating exceptions when failiong to open a file
 * for streaming.
 */
public class DataAccessException extends Exception {
    /**
     * Default constructor
     */
    public DataAccessException() {
        super("Failed to access Data. Does the file exist?\n");
    }

    /**
     * Default constructor with fileName.
     * 
     * @param fileName
     */
    public DataAccessException(String fileName) {
        super("Failed to access Data. Does the file '" + fileName + "' exist?\n");
    }

}
