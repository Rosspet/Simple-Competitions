/*
 * Student name: XXX
 * Student ID: YYY
 * LMS username: ZZZ
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Competition {
    private String name; //competition name
    private int id; //competition identifier

    public abstract void addEntries();

    public abstract void drawWinners();

    public void report() {
    }
}
