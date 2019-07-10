/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolver;

import java.util.Stack;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author zack11frazier22
 */

// an Evolver object works with the assumption that reference != ""
public class Evolver {
    private int avg;
    private final String reference;
    Stack<String> iterationList;
    Random generator = new Random();
    
    public Evolver(String reference) {
        this.reference = reference;
        iterationList = new Stack<>();
    }
    
    // Generates a list of iterations that ends when guess == reference
    public void generateList() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Character> guessList = new ArrayList<>();
        ArrayList<Character> refList = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        
        // initially set everything to null
        for(int i = 0; i < reference.length(); i++) {
            guessList.add((char)0);
            refList.add(reference.charAt(i));
        }
      
       
        while(!refList.equals(guessList)) {
            for(int i = 0; i < reference.length(); i++) {
                // only change the guess when the char's are different
                if(!guessList.get(i).equals(refList.get(i))) {
                    guessList.remove(i);
                    guessList.add(i, genChar());
                }
            }
            
            // now populate sb with the new guess' chars
            for(Character ch : guessList) {
                sb.append(ch);
            }
            
            // now fill the iterationList and reset the StringBuilder
            iterationList.push(sb.toString());
            sb.setLength(0);
        }
    }
    
    // returns the size of the Evolver
    public int size() {
        return iterationList.size();
    }
    
    // method to return a random char from ascii 32 - 125 inclusive
    private char genChar() {
        return (char) ((generator.nextInt() % 96) + 32);
    }
    
    // returns a specific iteration
    public String getIterAt(int index) {
        if(index <= 0 || index > iterationList.size()) {
            return null;
        }
        return iterationList.get(index - 1);
    }
    
    // gets the average number of iterations for a number of iterations
    // returns -1 on error
    public static int getAvg(String reference, int iterations, boolean printAll) {
        if(reference.equals("") || iterations <= 0) {
            return -1;
        }
        
        StringBuilder sb = new StringBuilder();
        Evolver eve = new Evolver(reference);
        int avg = 0;
        
        for(int i = 0; i < iterations; i++) {
            eve.generateList();
            if(printAll) {
                System.out.printf("Iterations for Test %d: %d\n", i + 1, eve.size() );
            }
            avg += eve.size();
            eve = new Evolver(reference);
        }
        
        avg /= iterations;
        
        return avg;
    }
    
    // returns the entire list as a string
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int i = 1;
        for(String iter : iterationList) {
            str.append(i++).append(" ").append(iter).append("\n");
        }
        return str.toString();
    }
}
