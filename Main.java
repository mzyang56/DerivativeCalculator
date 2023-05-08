//Max Yang and Andrew Stappenbeck B1
//Derivative Calculator Project
//05.08.23

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Clears screen
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
        System.out.println("Please enter an expression with positive integers, '+', '-', 'x' and/or parantheses (for chain rule), without any spaces.");
        System.out.println("\nValid expressions include:\n   x^10 \n   3x^6 \n   x^3+2x^2+12x+6\n   3(15x^6+3)^3\n");
        String input = sc.nextLine();
        
        //Creates an ArrayList that contains all of the numbers and modifiers
        List<String> eq1 = new ArrayList<String>();
        List<String> eq2 = new ArrayList<String>();
        
        //Variable that when true creates a second array that will not be differentiated due to the chain rule
        boolean innerList = false;

        for (int i = 0; i < input.length(); i++) {
            
            //Checks if input contains only integers and valid symbols
            if (isValid(input.substring(i,i+1)) == false) {
                System.out.println("Syntax Error: Please enter a valid input");
                System.exit(0);
            }

            //replaces "x" with "o" to keep track of differentiation process
            if (input.substring(i,i+1).equals("x")) {
                eq1.add("o");
                if (innerList == true) {
                    eq2.add("x");
                }

                try {
                    //If x is not preceded by a coefficient, this adds a "1"
                    if (isInt(input.substring(i-1,i)) == false) {
                        eq1.add(i, "1");
                    }
                    
                } catch (Exception k) {
                    eq1.add(i, "1");
                    
                }
            }

            else if (input.substring(i,i+1).equals("(")) {
                //Brackets signify an equation that needs to be differentiated with the chain rule
                eq1.add("[");
                eq2.add("(");
                innerList = true;
            }

            else if (input.substring(i,i+1).equals(")")) {
                eq1.add("]");
                eq2.add(")");
                innerList = false;
            }
            
            else if (isInt(input.substring(i,i+1)) == false) {
                eq1.add(input.substring(i,i+1));
                if (innerList == true) {
                    eq2.add(input.substring(i,i+1));
                }
            }

            else {
                //Ensures numbers with more than one digit are not split across indices
                int n = 0;
                while ((i+n < input.length()-1) && (isInt(input.substring(i+n, i+n+1)) && isInt(input.substring(i+n+1, i+n+2)))) {
                    n++;
                }
                
                //Finds integers that do not accompany a variable
                try {
                    //Checks if the number is a coefficient for x or is an exponent
                    if (input.substring(i-1, i).equals("^") || input.substring(i+n+1, i+n+2).equals("^") || input.substring(i+n+1, i+n+2).equals("x") || input.substring(i+n+1, i+n+2).equals("(") ) {
                        eq1.add(input.substring(i, i+n+1));
                        if (innerList == true) {
                            eq2.add(input.substring(i, i+n+1));
                        }
                    }
                    
                    else {
                        //Adds a "del" otherwise, which will be removed once differentiated
                        eq1.add("del");
                        //However, if the equation involves Chain Rule, the number will be added to the eq2 ArrayList which represents the undifferentiated part of the Chain Rule
                        if (innerList == true) {
                            eq2.add(input.substring(i, i+n+1));
                        }
                    }

                }

                catch (Exception e) {
                    
                    try {
                        if (input.substring(i+n+1, i+n+2).equals("x") || input.substring(i+n+1, i+n+2).equals("(")) {
                            eq1.add(input.substring(i, i+n+1));
                            if (innerList == true) {
                                eq2.add(input.substring(i, i+n+1));
                            }
                        }
                        else {
                            eq1.add("del");
                            if (innerList == true) {
                                eq2.add(input.substring(i, i+n+1));
                            }
                        }
                    }

                    catch (Exception f) {
                        eq1.add("del");
                        if (innerList == true) {
                            eq2.add(input.substring(i,i+n+1));
                        }
                    }

                } 

                //Increments i by an additional int n so integers with several digits are properly separated
                i += n;
            }
            
        }

        List<String> answer = calculate(eq1, eq2);
        for (String temp : answer) {
            System.out.print(temp);
        }
        System.out.println();

    }

    public static List<String> calculate(List<String> equation, List<String> equation2) {

        while (equation.contains("o")) {

            //Checks for statements in parantheses and then performs recursion to calculate value using manual Chain Rule
            if (equation.contains("[")) {
                int parCounter = 0;
                int parPos = equation.indexOf("[");

                boolean endFound = false;
                int endParPos = 0;

                //Uses a counter system to ensure there is an appropriate amounts of opening and closing parantheses
                while (endFound == false) {
                    for (int i = parPos; i < equation.size(); i++) {
                        if (equation.get(i).equals("[")) {
                            parCounter++;
                        }
                        if (equation.get(i).equals("]")) {
                            parCounter--;
                        }
                        if (parCounter == 0) {
                            endParPos = i;
                            endFound = true;
                            break;                            
                        }
                    }

                    if (parCounter != 0) {
                        System.out.println("Syntax Error: Please enter a valid input with correct number of parantheses");
                        System.exit(0);
                    }

                }

                if (!equation.get(endParPos+1).equals("^")) {
                    equation.remove(endParPos);
                }
                
                else {
                    int exponent = Integer.parseInt(equation.get(endParPos+2));
                    int coef = 1;

                    int digitsAdded = 0;
                    try {
                        if (isInt(equation.get(parPos-1)) == true) {
                            coef = Integer.parseInt(equation.get(parPos-1));
                            //Variable used to account for the absence of an index shift when a coefficient is already present for Chain Rule
                            digitsAdded = 1;
                        }

                        else {
                            coef = 1;
                            equation.add(parPos, "1");
                        }
                    } 
                    catch (Exception e) {
                        coef = 1;
                        equation.add(parPos, "1");
                    }

                    //Calculates coefficient
                    equation.set(parPos-digitsAdded, Integer.toString(coef * exponent));
                    
                    if (exponent != 1) {
                        equation.set(endParPos+3-digitsAdded, Integer.toString(exponent-1));
                        //Converts "[" and "]" to "(" and ")" to indicate that the chain rule differentiation has been applied
                        equation.set(parPos+1-digitsAdded, "(");
                        equation.set(endParPos+1-digitsAdded, ")");

                        List<String> chnRule = equation.subList(parPos+1-digitsAdded, endParPos+2);
                        int size = chnRule.size();
                        
                        //Merges the undifferentiated equation2 with the rest of the equation
                        equation.addAll(parPos+1+size-2*digitsAdded, equation2);

                        //Performs recursion for the rest of the equation that needs to be differentiated due to the Chain Rule
                        calculate(equation.subList(parPos+2+size-digitsAdded, parPos+2*size-digitsAdded), equation2);

                    }
                } 
            }

            //Checks for statements with "o" (placeholder for x) to determine where to derive
            if (equation.contains("o")) {
                //finds index
                int xPos = equation.indexOf("o");
                int coef = 0;
                
                //Checks if there is an exponent sign behind the x
                try {
                    if (!equation.get(xPos+1).equals("^")) {
                        //Removes x if there is no exponent, ex: 12x -> 12
                        equation.remove(xPos);
                    }
                    
                    else {
                        //Stores the exponent value
                        int exponent = Integer.parseInt(equation.get(xPos+2));
                        try {
                            //Checks if there is a coefficient in front of the x
                            if (isInt(equation.get(xPos-1)) == true) {
                                coef = Integer.parseInt(equation.get(xPos-1));
                            }
                        } 
                        catch (Exception e) {
                            //Runs if there is no coefficient, in which case the coefficient is 1
                            coef = 1;
                            equation.set(xPos, "1");
                        }

                        //Calculates the new coefficient
                        equation.set(xPos-1, Integer.toString(coef * exponent));
                        
                        if (exponent != 1) {
                            //Manually performs power rule
                            equation.set(xPos+2, Integer.toString(exponent-1));
                        }
                        else {
                            //Converts "o" to "x" so program knows differentiation has been completed
                            equation.set(xPos, "x");
                            equation.remove(xPos+2);
                            equation.remove(xPos+1);
                        }
                        
                        //Converts "o" to "x" so program knows differentiation has been completed
                        equation.set(xPos, "x");
                    }
                }
                
                catch (Exception g) {
                    equation.remove(xPos);
                }

            }
            
        }

        //Removes integers that are not coefficients or exponents that are already marked as "del"
        while (equation.contains("del")) {
            int delPos = equation.indexOf("del");
            equation.remove(delPos);

            //Removes any "+" or "-" operators if present
            try {
                equation.remove(delPos-1);
            } 
            catch (Exception h) {
                
            }
        }

        return equation;
    }

    //Tests if input contains only integers and symbols
    public static boolean isValid(String s) {
        if (s.equals("(") || s.equals(")") || s.equals("x") || s.equals("+") || s.equals("-") || s.equals("^")) {
            return true;
        }

        else {
            try {
                Integer.parseInt(s);
                return true;
            }
            
            catch (NumberFormatException e)
            {
                return false;
            }
        }
    }

    //Method that checks if input is an integer
    public static boolean isInt(String strNum) {
            if (strNum.equals("x") || strNum.equals("+") || strNum.equals("-") || strNum.equals("^") || strNum.equals("(") || strNum.equals(")")) {
                return false;
            }
            else {
                return true;
            } 
    }

}
