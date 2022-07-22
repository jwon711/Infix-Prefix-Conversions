// Infix / Postfix conversions

import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;

class Main {
  // Declares a linked list of type String to store the user's converted equations
  LinkedList<String> results = new LinkedList<String>();

  public static void main(String[] args) {
    boolean exit = false;

    // Creates an instance of main to run the menu
    Main menu = new Main();

    //Repeatedly prints the menu, prompts the user to make a selection and detects user input
    while (!exit) {
      menu.printMenu();
      int userInput = menu.getUserInput();
      menu.detectUserInput(userInput);
    }
  }
  // Method for printing the menu to the user
  private void printMenu() {
    System.out.println("Enter a number for selection: ");
    System.out.println("1) Infix to Postfix");
    System.out.println("2) Postfix to Infix");
    System.out.println("3) Print Equations");
    System.out.println("4) EXIT");
  }
  // Method for getting the user's input 
  private int getUserInput() {
    Scanner scnr = new Scanner(System.in);
    int userInput = -1;
    while (userInput < 0 || userInput > 4) {
      System.out.print("Enter your choice:\n");
      userInput = scnr.nextInt();
    }
    return userInput;
  }
  // Method for detecting the user's input to do what the user has selected
  private void detectUserInput(int userInput) {
    if (userInput == 1) {
      InfixToPostFix();
    } else if (userInput == 2) {
      PostfixToInfix();
    } else if (userInput == 3) {
      printResults();
    } else if (userInput == 4) {
      printResults();
      System.out.println("Program has exited.");
      System.exit(0);
    }
  }
  // Method that gives the correct order of precedence for the operators
  private int precedence(char c) {
    // 1 is returned for + and - because they have lowest precedence
    if (c == '+' || c == '-') {
      return 1;
    }
    // 2 is returned for * and / and % because they have second to lowest precedence
    if (c == '*' || c == '/' || c == '%') {
      return 2;
    }
    // 3 is returned for ^ because it has the highest precedence
    if (c == '^') {
      return 3;
    }
    return 0;
  }
  // Method for converting Infix to Postfix
  private String InfixToPostFix() {
    Scanner scnr = new Scanner(System.in);

    // Declares a inString that holds the user's input equation
    String inString = new String("");

    // Declares an outString that holds the user's converted input equation
    String outString = new String("");

    // Declares a Stack of type Character that will hold characters within the inString during the conversion process
    Stack<Character> operatorStack = new Stack<Character>();

    // Prompts the user to enter an equation to be converted
    System.out.println("Enter an equation: ");
    inString = scnr.nextLine();

    // Goes through the user's inputed equation and starts conversion
    for (int i = 0; i < inString.length(); i++) {
      char c = inString.charAt(i);
      
      // if the current read character is a semicolon stop reading the string
      if(c == ';'){
        break;
      }
      // if the character in the inString is an operand, then it will automatically be outputed to the outString
      if (Character.isLetterOrDigit(c)) {
        outString = outString + c;
      } 
      // if the stack is empty, than we add the currently read character into the stack
      else if (operatorStack.isEmpty()) {
        operatorStack.push(c);
      } 
      // if the currentl read character is a left parenthesis, add it to the stack
      else if (c == '(') {
        operatorStack.push(c);
      } 
      // if the currently read character is a right parenthesis
      else if (c == ')') {
        // check to see if the stack is empty or not
        while (!operatorStack.isEmpty()) {
          // if the stack is not empty, check to see if the top of the stack is a left parenthesis
          if (operatorStack.peek() != '(') {
            // if the top of the stack is not a left parenthesis, then it will add the entire stack to the outString
            outString = outString + operatorStack.pop();
          } 
          // else if the stack is not empty
          else if (!operatorStack.isEmpty()) {
            // removes all the parenthesis in the stack which we don't want in our final equation
            operatorStack.pop();
            // breaks the while loop, so we dont infinitely loop 
            break;
          }
        }
      }
      // if the precedence of the currently read character is greater than the precedence of the character at the top of the stack, then you add the currently read character to the stack
      else if (precedence(c) > precedence(operatorStack.peek())) {
        operatorStack.push(c);
      }
      // if the stack is not empty and the precedence of the currently read character is less than or equal to the precedence of the character at the top of the stack
      else {
        while (!operatorStack.isEmpty() && precedence(c) <= precedence(operatorStack.peek())) {
          // then add everything from the stack to the outString
          outString = outString + operatorStack.pop();
        }
        // and then add the currently read character into the stack
        operatorStack.push(c);
      }
    }
    // checks to see if the stack is empty or not
    while (!operatorStack.isEmpty()) {
      // if the stack is not empty, then add everything from the stack to the outString
      outString = outString + operatorStack.pop();
    }
    System.out.println("----------------------");
    // Prints the user's input equation 
    System.out.println("Infix is: " + inString);
    // Prints the user's input equation converted to Postfix
    System.out.println("Postfix is: " + outString);
    System.out.println("----------------------");
    // Adds the converted Postfix equation to a linkedlist so the user can have the option to print the results
    results.add(outString);
    return outString;
  }
  // Method for converting Postfix to Infix
  private String PostfixToInfix() {
    Scanner scnr = new Scanner(System.in);

    // Declares a inString that holds the user's input equation
    String inString = new String("");

    // Declares an outString that holds the user's converted input equation
    String outString = new String("");

    // Declares a stack of type String that will hold characters and strings within the inString during the conversion process
    Stack<String> operandStack = new Stack<String>();
    
    // Prompts the user to enter an equation
    System.out.println("Enter an equation: ");
    inString = scnr.nextLine();

    // Reads the user's input and stores each element in between the spaces as individual substrings. This allows us to read in double digital numbers from the user
    String[] inString_splitted = inString.split("\\s");

    // Goes through each string in the array of splitted substrings of the user's inputed inString, 
    for(String s : inString_splitted){
      char c = s.charAt(0);
      // if the currently read substring is an operator,
      if ((!Character.isLetterOrDigit(c))) {
        // set the substring at the top of the stack to operand1
        String operand1 = operandStack.pop();
         // set the next substring that is at the top of the stack to operand2
        String operand2 = operandStack.pop();
        // combines the two substrings that were at the top of the stack with left and right parenthesis and the currently read operator and add that entire thing back into a single slot of the stack
        operandStack.push("(" + operand2 + s + operand1 + ")");
      } 
      // if the currently read substring is an operand,
      else {
        // push it to the stack
        operandStack.push(s);
      }
    }
    // Adds the entire stack into the outString
    outString = outString + operandStack.pop();
    System.out.println("----------------------");
    // Prints the user's input equation
    System.out.println("Postfix is: " + inString);
    // Prints the user's converted input equation to Infix
    System.out.println("Infix is: " + outString);
    System.out.println("----------------------");
    // Adds the converted Infix equation to a linkedlist so the user can have the option to print the results
    results.add(outString);
    return outString;
  }
  // Method for printing every converted equation that the user has entered
  private void printResults() {
    System.out.println("----------------------");
    System.out.println("RESULTS ARE: ");

    // Goes through the linked list
    for (int i = 0; i < results.size(); i++) {

      // Sets the first element in the linked list to a temporary String
      String temp = results.getFirst();
      // Removes the first element in the linked list
      results.removeFirst();
      // Adds the element that was stored in the temporary String
      results.addLast(temp);
      // Prints out the element that was stored in the temporary String
      System.out.println(temp);
    }
    System.out.println("----------------------");
  }
}
