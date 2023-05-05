//Max Yang B1
//Recursion Calculator Project
//04.03.23

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter an expression with only positive integers, without spaces, and only using these symbols:  +, -, *, /, ^, (, ) ");
        String input = sc.nextLine();
        
        //Creates an ArrayList that contains all of the numbers and modifiers
        List<String> eq1 = new ArrayList<String>();
        List<String> eq2 = new ArrayList<String>();
        
        /* List<String> equation3 = new ArrayList<String>();
                equation3 = equation.subList(parPos+1, endParPos);
                System.out.println("Equation3 " + equation3);
                */
        
        
        boolean innerList = false;
        for (int i = 0; i < input.length(); i++) {
            
            //Checks if input contains only integers and symbols
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
            }

            else if (input.substring(i,i+1).equals("(")) {
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
                    if (input.substring(i-1, i+n).equals("^") || input.substring(i+1, i+n+2).equals("^") || input.substring(i+1, i+n+2).equals("x")) {
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

                catch (Exception e) {
                    
                    try {
                        if (input.substring(i+1, i+n+2).equals("^") || input.substring(i+1, i+n+2).equals("x")) {
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
            
            System.out.println(equation);

            int total = 0;
            //Checks for statements in parantheses and then performs recursion to calculate value
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

                //Chain Rule calculation
                if (!equation.get(endParPos+1).equals("^")) {
                    equation.remove(endParPos);
                }
                
                else {
                    int exponent = Integer.parseInt(equation.get(endParPos+2));
                    
                    int coef = 1;
                    try {
                        if (isInt(equation.get(parPos-1)) == true) {
                            coef = Integer.parseInt(equation.get(parPos-1));
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

                    //performs calculation
                    equation.set(parPos, Integer.toString(coef * exponent));
                    
                    if (exponent != 1) {
                        System.out.println("Now " + equation);
                        equation.set(endParPos+3, Integer.toString(exponent-1));
                        equation.set(parPos+1, "(");
                        equation.set(endParPos+1, ")");

                        List<String> chnRule = equation.subList(parPos+1, endParPos+2);
                        int oPos = chnRule.indexOf("o");
                        System.out.println("Equation 2 " + equation2);
                        
                        int size = chnRule.size();
                        
                        /* 
                        for (int i = endParPos+1; i > parPos; i--) {
                            equation.remove(i);
                        }
                        */

                        
                        equation.addAll(parPos+1+size, equation2);
                        //equation.set(oPos+size+1, "x");
                        
                        //equation.addAll(parPos+1, chnRule);
                        //System.out.println("chnRule " + chnRule);
                        
                        
                        System.out.println("Equation All 2 " + equation);
                        
                        
                        System.out.println("Here " + equation.subList(parPos+1, parPos+size));
                        
                        System.out.println(equation);
                        //System.out.println(equation.subList(parPos+2+chnRule.size(), parPos+2+2*chnRule.size()));

                        calculate(equation.subList(parPos+2+size, parPos+2*size), equation2);

                    }
                    else {
                        //equation.set(xPos, "x");
                        //equation.remove(xPos+2);
                        //equation.remove(xPos+1);
                    }
                    
                    //equation.set(xPos, "x");
                }

                //equation.remove(endParPos);
                
            }

            //Checks for statements with "o" (placeholder for x) to derive
            if (equation.contains("o")) {
                //finds index
                int xPos = equation.indexOf("o");
                int coef = 0;
                
                
                try {
                    if (!equation.get(xPos+1).equals("^")) {
                        //Removes x if there is no exponent, ex: 12x -> 12
                        equation.remove(xPos);
                    }
                    
                    else {
                        int exponent = Integer.parseInt(equation.get(xPos+2));
                        try {
                            if (isInt(equation.get(xPos-1)) == true) {
                                coef = Integer.parseInt(equation.get(xPos-1));
                            }
                        } 
                        catch (Exception e) {
                            coef = 1;
                            equation.set(xPos, "1");
                        }

                        //performs calculation
                        equation.set(xPos-1, Integer.toString(coef * exponent));
                        
                        if (exponent != 1) {
                            equation.set(xPos+2, Integer.toString(exponent-1));
                        }
                        else {
                            equation.set(xPos, "x");
                            equation.remove(xPos+2);
                            equation.remove(xPos+1);
                        }
                        
                        equation.set(xPos, "x");
                    }
                }
                
                catch (Exception g) {
                    equation.remove(xPos);
                }

                System.out.println(equation);

            }
            
        }

        while (equation.contains("del")) {
            int delPos = equation.indexOf("del");
            equation.remove(delPos);

            try {
                equation.remove(delPos-1);
            } catch (Exception h) {
                
            }
            
        }

        return equation;
    }

    //Tests if input contains only integers and symbols
    public static boolean isValid(String s) {
        if (s.equals("(") || s.equals(")") || s.equals("x") || s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^")) {
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

    //Tests if input is an integer
    public static boolean isInt(String strNum) {
            if (strNum.equals("x") || strNum.equals("+") || strNum.equals("-") || strNum.equals("*") || strNum.equals("/") || strNum.equals("^") || strNum.equals("(") || strNum.equals(")")) {
                return false;
            }
            else {
                return true;
            } 
    }


    /*
    //ex: x^2
    public static String polynomialDerivative(String input, String exponent, int coefficient)
    {
      String output;
      int newCoeff = Integer.parseInt(exponent) * coefficient;

      output = newCoeff + "*" + input;

      int power = Integer.parseInt(exponent);
      power--;
      exponent = power + "";

      if (power != 0) {
        output = output.substring(0, output.indexOf("^")) + "^" +  exponent;
      }
      
      else {
        output = "1";
      }
      
      return output;
      
    }
     */
   

}
