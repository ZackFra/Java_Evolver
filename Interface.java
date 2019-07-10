/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolver;

import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Zackary Frazier
 */
public class Interface {
    public static void main(String[] args) {
        ArrayList <String> cmdList = new ArrayList<>();
        String input;
        Scanner scan = new Scanner(System.in);
        Evolver eve = null;
        boolean running = true;
        
        printTitle();
        
        // the meat of the program is here
        while(running) {
            cmdList.clear();
            System.out.print(">> ");
            System.out.flush();
            input = scan.nextLine();
            parseString(cmdList, input);
            
            
            // This is the "event-handler"
            switch(cmdList.get(0)) {                    

                case "iterate":                    
                    if(cmdList.size() == 2 && !cmdList.contains("INVALID")
                            || cmdList.size() == 2 && cmdList.get(1).equals("INVALID")) {
                        eve = new Evolver(cmdList.get(1));
                        eve.generateList();
                        System.out.println("Done. Enter \"print\" to see results.");
                    }
                    else {
                        System.out.println("Syntax error.");
                        System.out.println("Usage: iterate \"[value]\"");
                    }
                    break;
                case "print":
                    if(cmdList.size() == 1) {
                        // no checks for INVALID, because if there was only
                        // one element in cmdList, and it was INVALID, we 
                        // wouldn't have gotten this far
                        if(eve != null) {
                            System.out.println(eve);
                        }
                        else {
                            System.out.println("You must call iterate prior to print.");
                        }
                    }
                    else if(cmdList.size() == 2 && !cmdList.contains("INVALID")) {
                        if(eve != null) {
                            // I can assume cmdList.get(1) is an int at this point
                            int index = Integer.parseInt(cmdList.get(1));
                            
                            // if the index is between 0 and eve.size()
                            if(index <= eve.size() && index > 0) {
                                System.out.println(cmdList.get(1).concat(" ").concat(eve.getIterAt(index)));
                            }
                            else {
                                System.out.printf("Value must be between 1 and %d\n", eve.size());
                            }
                        }
                        else if(cmdList.contains("INVALID")) {
                            System.out.println("Syntax error.");
                            System.out.println("Usage: print");
                            System.out.println("Usage: print [index]");
                        }
                        else {
                            System.out.println("You must call iterate prior to print.");
                        }
                    }
                    else {
                        System.out.println("Syntax error");
                        System.out.println("Usage: print");
                        System.out.println("Usage: print [index]");
                    }
                    break;
                case "quit":
                    if(cmdList.size() == 1) {
                        System.out.println("Goodbye!");
                        running = false;
                    }
                    else {
                        System.out.println("Syntax error.");
                        System.out.println("Usage: quit");
                    }
                    break;
                case "average":
                    // if the size is 3 and it doesn't contain "INVALID", or
                    // if the size is 3 and the secon value specificially is "INVALID"
                    if(cmdList.size() == 3 && !cmdList.contains("INVALID")
                            || cmdList.size() == 3 && cmdList.get(1).equals("INVALID")) {
                        int avg = Evolver.getAvg(cmdList.get(1), Integer.parseInt(cmdList.get(2)), true);
                        System.out.printf("Average of %s iterations: %d\n", cmdList.get(2), avg);
                    }
                    else {
                        System.out.println("Syntax error.");
                        System.out.println("Usage: average \"[value]\" [iterations]");
                    }
                    break;
                case "help":
                    if(cmdList.size() == 1) {
                        printHelp();
                    }
                    else {
                        System.out.println("Syntax error.");
                        System.out.println("Usage: help");
                    }
                    break;
                default: 
                    System.out.println("I am sorry, I did not understand that.");
                    System.out.println("Enter \"help\" for instructions.");
                    break;
            }
        }    
    }
    
    private static void printTitle() {
        System.out.println("Welcome to the Evolver\n\n"
               + "This application show's Darwins principles of intelligent\n"
               + "design by showing an example of it. For any string entered\n"
               + "the computer will generate random strings until it reaches\n"
               + "the reference string. How it does this is important. It will\n"
               + "ignore all letters it has previously guessed correctly, much\n"
               + "like how nature chooses to keep advantageous traits that assist\n"
               + "in survival. If this is your first time using this program enter\n"
               + "\"help\" for instructions\n");
    }
    
    // method to parse the input string 
    private static void parseString(ArrayList<String> cmdList, String str) {
        Scanner scan = new Scanner(str);
        int start, end;
        String arg;
        
        // first tokenize things by spaces
        while(scan.hasNext()) {
            cmdList.add(scan.next());
        }
       
        // if the user just hit 'enter', just return
        if(cmdList.isEmpty()) {
            scan.close();
            return;
        }
        
        switch(cmdList.get(0)) {
            // parse for iterate
            case "iterate":
                parseIterate(cmdList, str);
                break;
            // parse for print
            case "print":
                parsePrint(cmdList, str);
                break;
            // parse for help
            case "help":
                parseHelp(cmdList);
                break;
            // parse for quit
            case "quit":
                parseQuit(cmdList);
                break;
            // parse for average
            case "average":
                parseAvg(cmdList, str);
                break;
            // if anything else in entered,
            // mark it as invalid
            default:
                cmdList.add("INVALID");
                break;
        }

        
        scan.close();
    }
    
    // no reason to do this tbh, I just wanted to be consistent
    private static void parseQuit(ArrayList<String> cmdList) {
        if(cmdList.size() > 1) {
            cmdList.add("INVALID");
        }
    }
    
    // same as parseQuit
    private static void parseHelp(ArrayList<String> cmdList) {
        if(cmdList.size() > 1) {
            cmdList.add("INVALID");
        }
    }
    
    // parses the iterate command by slicing the input string between
    // its double-quotes and setting cmd(1) equal to the string
    private static void parseIterate(ArrayList<String> cmdList, String str) {
        // add invalid twice the the interface doesn't confuse the user
        // entering iterate "INVALID" as an invalid choice
        if(cmdList.size() == 1) {
            cmdList.add("INVALID");
            cmdList.add("INVALID");
            return;
        }
        
        Scanner scan = new Scanner(str);
        int start, end;
        String arg;
        String lastElement = cmdList.get(cmdList.size() - 1);
        
        // if the last itemi in cmdList doesn't hava a quote as its final char
        if(lastElement.charAt(lastElement.length()-1) != '\"' || cmdList.get(1).charAt(0) != '\"') {
            cmdList.add("INVALID");
            scan.close();
            return;
        }
        // if the next arg contains a quote and the last arg contains quote
        else if(cmdList.get(1).contains("\"") && cmdList.get(cmdList.size()-1).contains("\"")) {
            start = str.indexOf("\"") + 1;
        }
        else {
            cmdList.add("INVALID");
            scan.close();
            return;
        }
            
        end = str.indexOf("\"", start + 1);
        
        // check that there aren't extra quotes
        if(str.indexOf("\"", end + 1) != -1) {
            cmdList.add("INVALID");
            scan.close();
            return;
        }
           
        arg = str.substring(start, end);
        cmdList.clear();
        cmdList.add("iterate");
        cmdList.add(arg);  
        
        // now we check that no invalid characters were entered
        for(int i = 0; i < arg.length(); i++) {
            if( (int) arg.charAt(i) < 32 || (int) arg.charAt(i) > 126) {
                cmdList.add("INVALID");
                return;
            }
        }
    }
    
    private static void parseAvg(ArrayList<String> cmdList, String str) {
        // handle an invalid amount of arguments
        if(cmdList.size() < 3) {
            cmdList.add("INVALID");
            cmdList.add("INVALID");
            cmdList.add("INVALID");
            return;
        }
        
        String firstStringPiece = cmdList.get(1), 
                lastStringPiece = cmdList.get(cmdList.size()-2);
        
        // if the first string doesn't start with " or the 2nd to last string doesn't end with "
        if( (firstStringPiece.charAt(0) != '\"') 
                || (lastStringPiece.charAt(lastStringPiece.length() - 1) != '\"') ) {
            cmdList.add("INVALID");
            return;
        }
        // if the user entered average "INVALID" 100, or something like that
        // check that the last paramter is an int, if so, allow this command
        // to be run normally
        if(cmdList.size() == 3 && firstStringPiece.equals("INVALID")) {
            try {
                Integer.parseInt(cmdList.get(2));
                return;
            }
            catch(NumberFormatException e) {
                cmdList.add("INVALID");
                return;
            }
        }
        
        // parse the string for the input tokens to be placed
        // in the cmdList list
        int start, end;
        String strArg, intArg;
        
        start = str.indexOf("\"");
        end = str.indexOf('\"', start + 1);
        
        if(str.indexOf('\"', end + 1) != -1) {
            cmdList.add("INVALID");
            return;
        } 
        
        strArg = str.substring(start, end);
        
        // check that intArg is in fact an integer
        try {
            Integer.parseInt(cmdList.get(cmdList.size()-1));
            intArg = cmdList.get(cmdList.size()-1);
        }
        catch(NumberFormatException e) {
            cmdList.add("INVALID");
            return;
        }
        
        // now build the new, appropriate cmdList
        cmdList.clear();
        cmdList.add("average");
        cmdList.add(strArg);
        cmdList.add(intArg);
    }
    
    private static void printHelp() {
        System.out.println("\n\nInstructions:");
        System.out.println("iterate \"[value]\"");
        System.out.println("\tGenerates an iteration list for [value]");
        System.out.println("print");
        System.out.println("\tPrints the iteration list for the last [value] iterated");
        System.out.println("print [index]");
        System.out.println("\tPrints the [index]th iteration of the last iteration list");
        System.out.println("average \"[value]\" [iterations]");
        System.out.println("\tComputes the average of iterating through [value] [iterations] times");
        System.out.println("help");
        System.out.println("\tPrints the help screen");
        System.out.println("quit");
        System.out.println("\tExits the program");
    }
    
    private static void parsePrint(ArrayList<String> cmdList, String str) {
        if(cmdList.size() == 1) {
            return;
        }
        else if(cmdList.size() != 2) {
            cmdList.add("INVALID");
            return;
        }
        
        // check that the argument is an int
        try {
            Integer.parseInt(cmdList.get(1));
        }
        catch(Exception e) {
            cmdList.add("INVALID");
        }
    }
    
    private static void test0() {
        Evolver eve = new Evolver("Helllo, world!");
        eve.generateList();
        System.out.println(eve);
    }
    
    private static void test1() {
        final int index = 300;
        Evolver eve = new Evolver("Hello, world!");
        eve.generateList();
        System.out.println(eve);
        System.out.println(index <= eve.size() ? index + " " + eve.getIterAt(index) : "Index too large");
    }
    
    private static void test2() {
        final int tests = 200;
        System.out.printf("Average iterations in %d tests is %d\n", 
                tests, Evolver.getAvg("Hello, world!", tests, true));
    }
}
