/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

public class DataFormatException extends Exception {
    public DataFormatException(){
        super("File in unparsable format - Aborting!!\n");
    }
    public DataFormatException(String message){
        super(message);
    }
}
