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
        System.out.println("Please enter an expression with positive integers, '+', '-', 'x', 'sinx', 'cosx' and/or parantheses (for chain rule), without any spaces.");
        System.out.println("\nValid expressions include:\n   x^10 \n   3x^6 \n   x^3+2x^2+12x+6\n   3(15x^6+3)^3\n   cosx+3 \n   5(x+sinx+6x^2)^3\n");
        String userInput = sc.nextLine();

        DerivativeCalc calc1 = null;

        //Determines whether to create a derivative calculator or a trigonometry calculator subclass
        if (userInput.contains("sin") || userInput.contains("cos")) {
            calc1 = new TrigCalc(userInput);
        }
        else {
            calc1 = new DerivativeCalc(userInput);
        }

        List<String> equation1 = new ArrayList<String>();
        List<String> equation2 = new ArrayList<String>();

        //Iterates through the uInput variable in calc1 and adds the corresponding integers/expressions to equation1 and equation2 
        calc1.convertToList(calc1.getInput(), equation1, equation2);

        List<String> answer = calc1.calculate(equation1, equation2);


        for (String temp : answer) {
            System.out.print(temp);
        }
        System.out.println();

    }
}