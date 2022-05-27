/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class DataAccessException extends Exception {
    public DataAccessException(){
        super("Failed to access Data. Does the file exist?\n");
    }
    public DataAccessException(String fileName){
        super("Failed to access Data. Does the file '" + fileName + "' exist?\n");
    }
    
}
