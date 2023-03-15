package edu.missouriwestern.csc406team1;

import java.util.ArrayList;

public class ExampleUsage {
    public static void main(String[] args) {

        // Create special arraylist
        ArrayListFlow<Integer> numbers_flow = new ArrayListFlow<>();

        // Add to special arraylist
        numbers_flow.add(1);
        numbers_flow.add(2);
        numbers_flow.add(3);

        // Print each number in special arraylist
        for (Integer number : numbers_flow) {
            System.out.println("Number is: " + number);
        }

        // Create normal arraylist
        ArrayList<Integer> numbers_arraylist = new ArrayList<>();

        // Add to normal arraylist
        numbers_arraylist.add(1);
        numbers_arraylist.add(2);
        numbers_arraylist.add(3);

        // Print each number in normal arraylist
        for (Integer number : numbers_arraylist) {
            System.out.println("Number is: " + number);
        }

    }
}
