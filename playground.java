import java.util.ArrayList;

public class playground {
    public static void main(String[] args){
        int intArr[] = {1,2,3,4,5, 4};
        if (notAllDifferent(intArr)){
            System.out.println("YEEEEEET");
            System.out.println("YEEEEEET");
            System.out.println("YEEEEEET");
            System.out.println("YEEEEEET");
        }
        /**
        if (mtStr.matches("[0-9 ]+")){
            System.out.println("MATCH!!!");
            System.out.println("MATCHfffff!!!");
            System.out.println("MATCH!!!");
        */
        }



    private static boolean notAllDifferent(int[] numbers){
        ArrayList<Integer> currentNumbers = new ArrayList<Integer>();

        for (int num : numbers){
            if (currentNumbers.contains(num)){
                return false;
            }
            currentNumbers.add(num);
        }
        return true;
    }
}