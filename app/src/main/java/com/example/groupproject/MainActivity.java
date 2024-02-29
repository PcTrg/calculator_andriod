package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView resultTv,solutionTv;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonEquals;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonAC,buttonDot;
    MaterialButton buttonSin, buttonCos, buttonTan, buttonCot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);

        assignId(buttonC,R.id.button_c);
        assignId(buttonBrackOpen,R.id.button_open_bracket);
        assignId(buttonBrackClose,R.id.button_close_bracket);
        assignId(buttonDivide,R.id.button_divide);
        assignId(buttonMultiply,R.id.button_multiply);
        assignId(buttonPlus,R.id.button_plus);
        assignId(buttonMinus,R.id.button_minus);
        assignId(buttonEquals,R.id.button_equals);
        assignId(button0,R.id.button_0);
        assignId(button1,R.id.button_1);
        assignId(button2,R.id.button_2);
        assignId(button3,R.id.button_3);
        assignId(button4,R.id.button_4);
        assignId(button5,R.id.button_5);
        assignId(button6,R.id.button_6);
        assignId(button7,R.id.button_7);
        assignId(button8,R.id.button_8);
        assignId(button9,R.id.button_9);
        assignId(buttonAC,R.id.button_ac);
        assignId(buttonDot,R.id.button_dot);

        assignId(buttonSin,R.id.button_sin);
        assignId(buttonCos,R.id.button_cos);
        assignId(buttonTan,R.id.button_tan);
        assignId(buttonCot,R.id.button_cot);
    }

    void assignId(MaterialButton btn,int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    public static boolean checkBalancedParentheses(String input) {
        int counter = 0;

        for (char ch : input.toCharArray()) {
            if (ch == '(') {
                counter++;
            } else if (ch == ')') {
                counter--;
            }
        }

        if(counter>0) return true;
        return false;
    }

    @Override
    public void onClick(View view) {
        MaterialButton button =(MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if(buttonText.equals("AC")){
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        if(buttonText.equals("sin") || buttonText.equals("cos") || buttonText.equals("tan") || buttonText.equals("cot")){
            solutionTv.setText(dataToCalculate.concat(buttonText.concat("(")));
            return;
        }

        if(buttonText.equals(")")){
            if(!checkBalancedParentheses(dataToCalculate)) return;
        }

        if(buttonText.equals("=")){
            solutionTv.setText(resultTv.getText());
            return;
        }
        if(buttonText.equals("C")){
            dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
        }else{
            dataToCalculate = dataToCalculate+buttonText;
        }
        solutionTv.setText(dataToCalculate);

        String finalResult = solve(dataToCalculate);

        if(!finalResult.equals("Err")){
            resultTv.setText(finalResult);
        }

    }

    public static String solve(String equation) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < equation.length(); i++) {
            char c = equation.charAt(i);
            if (Character.isDigit(c)) {
                // Build the number
                StringBuilder numberBuilder = new StringBuilder();
                while (i < equation.length() && (Character.isDigit(equation.charAt(i)) || equation.charAt(i) == '.')) {
                    numberBuilder.append(equation.charAt(i));
                    i++;
                }
                i--; // decrement i to offset the outer loop's increment
                numbers.push(Double.parseDouble(numberBuilder.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(') {
                // Process operator based on precedence
                while (!operators.isEmpty() && c != '(' && hasHigherPrecedence(operators.peek(), c)) {
                    if (numbers.size() < 2) {
                        return "Error: The equation is not properly formatted.";
                    }
                    processOperation(numbers, operators);
                }
                operators.push(c);
            } else if (c == ')') {
                // Process operators until the corresponding opening bracket
                while (!operators.isEmpty() && operators.peek() != '(') {
                    if (numbers.size() < 2) {
                        return "Error: The equation is not properly formatted.";
                    }
                    processOperation(numbers, operators);
                }
                if (operators.isEmpty()) {
                    return "Error: Mismatched brackets in the equation.";
                }
                operators.pop(); // Remove the opening bracket
            } else if (i < equation.length() - 4) {
                String function = equation.substring(i, i + 5);
                if (function.equals("sin^-") || function.equals("cos^-") || function.equals("tan^-")) {
                    i += 5; // Skip the function name and the opening bracket
                    // Build the number
                    StringBuilder numberBuilder = new StringBuilder();
                    while (i < equation.length() && (Character.isDigit(equation.charAt(i)) || equation.charAt(i) == '.')) {
                        numberBuilder.append(equation.charAt(i));
                        i++;
                    }
                    if (numberBuilder.length() == 0) {
                        return "Error: The equation is not properly formatted.";
                    }
                    double number = Double.parseDouble(numberBuilder.toString());
                    // Apply the function
                    switch (function) {
                        case "sin^-":
                            numbers.push(Math.asin(number));
                            break;
                        case "cos^-":
                            numbers.push(Math.acos(number));
                            break;
                        case "tan^-":
                            numbers.push(Math.atan(number));
                            break;
                    }
                } else if (i < equation.length() - 3) {
                    function = equation.substring(i, i + 3);
                    if (function.equals("sin") || function.equals("cos") || function.equals("tan") || function.equals("log") || function.equals("sqt")) {
                        i += 3; // Skip the function name
                        if (equation.charAt(i) == '(') {
                            i++; // Skip the opening bracket
                        }
                        // Build the number
                        StringBuilder numberBuilder = new StringBuilder();
                        while (i < equation.length() && (Character.isDigit(equation.charAt(i)) || equation.charAt(i) == '.')) {
                            numberBuilder.append(equation.charAt(i));
                            i++;
                        }
                        if (numberBuilder.length() == 0) {
                            return "Error: The equation is not properly formatted.";
                        }
                        double number = Double.parseDouble(numberBuilder.toString());
                        // Apply the function
                        switch (function) {
                            case "sin":
                                numbers.push(Math.sin(number));
                                break;
                            case "cos":
                                numbers.push(Math.cos(number));
                                break;
                            case "tan":
                                numbers.push(Math.tan(number));
                                break;
                            case "log":
                                numbers.push(Math.log10(number));
                                break;
                            case "sqt":
                                numbers.push(Math.sqrt(number));
                                break;
                        }
                    } else if (i < equation.length() - 2) {
                        function = equation.substring(i, i + 2);
                        if (function.equals("pi")) {
                            numbers.push(Math.PI);
                            i += 2; // Skip the pi
                        } else if (function.equals("ln")) {
                            i += 2; // Skip the ln
                            if (equation.charAt(i) == '(') {
                                i++; // Skip the opening bracket
                            }
                            // Build the number
                            StringBuilder numberBuilder = new StringBuilder();
                            while (i < equation.length() && (Character.isDigit(equation.charAt(i)) || equation.charAt(i) == '.')) {
                                numberBuilder.append(equation.charAt(i));
                                i++;
                            }
                            if (numberBuilder.length() == 0) {
                                return "Error: The equation is not properly formatted.";
                            }
                            double number = Double.parseDouble(numberBuilder.toString());
                            numbers.push(Math.log(number));
                        }
                    }
                } else if (c == '^') {
                    operators.push(c);
                }
            }
        }

        // Process remaining operators
        while (!operators.isEmpty()) {
            if (numbers.size() < 2) {
                return "Error: The equation is not properly formatted.";
            }
            processOperation(numbers, operators);
        }

        if (numbers.size() != 1) {
            return "Error: The equation is not properly formatted.";
        }

        return String.valueOf(numbers.pop());
    }

    private static void processOperation(Stack<Double> numbers, Stack<Character> operators) {
        if (numbers.size() < 2) {
            throw new IllegalArgumentException("The equation is not properly formatted.");
        }
        double secondOperand = numbers.pop();
        double firstOperand = numbers.pop();
        char operator = operators.pop();
        numbers.push(applyOperation(operator, firstOperand, secondOperand));
    }

    private static boolean hasHigherPrecedence(char firstOperator, char secondOperator) {
        if (firstOperator == '(' || secondOperator == '(') {
            return false;
        }
        if ((firstOperator == '+' || firstOperator == '-') && (secondOperator == '*' || secondOperator == '/' || secondOperator == '^')) {
            return false;
        } else {
            return true;
        }
    }

    private static double applyOperation(char operator, double firstOperand, double secondOperand) {
        switch (operator) {
            case '+': return firstOperand + secondOperand;
            case '-': return firstOperand - secondOperand;
            case '*': return firstOperand * secondOperand;
            case '/': return firstOperand / secondOperand;
            case '^': return Math.pow(firstOperand, secondOperand);
            default: throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

}