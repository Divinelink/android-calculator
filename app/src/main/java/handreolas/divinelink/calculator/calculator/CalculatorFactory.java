package handreolas.divinelink.calculator.calculator;

import java.util.ArrayList;

abstract class Calculator {

    protected ArrayList<Numbers> calculatorNumbers;
    protected ArrayList<Operations> calculatorOperations;

    abstract void setCalculator();

    public Numbers getNumber(int number) {

        return this.calculatorNumbers.get(number);
    }

    public Operations getOperations(int operationNumber) {

        return this.calculatorOperations.get(operationNumber);
    }


}

class CalculatorNumbers extends Calculator {

    @Override
    void setCalculator() {
        calculatorNumbers.add(new Numbers(1));
        calculatorNumbers.add(new Numbers(2));
        calculatorNumbers.add(new Numbers(3));
        calculatorNumbers.add(new Numbers(4));
        calculatorNumbers.add(new Numbers(5));
        calculatorNumbers.add(new Numbers(6));
        calculatorNumbers.add(new Numbers(7));
        calculatorNumbers.add(new Numbers(8));
        calculatorNumbers.add(new Numbers(9));
        calculatorNumbers.add(new Numbers(0));
    }
}

class CalculatorOperations extends Calculator {

    public static int ADDITION = 0;
    public static int SUBTRACTION = 1;
    public static int MULTIPLICATION = 2;
    public static int DIVISION = 3;
    public static int PERCENTAGE = 4;
    public static int RESULT = 5;
    public static int BACKSPACE = 6;
    public static int DELETE = 7;

    @Override
    void setCalculator() {
        calculatorOperations.add(new Operations(ADDITION));
        calculatorOperations.add(new Operations(SUBTRACTION));
        calculatorOperations.add(new Operations(MULTIPLICATION));
        calculatorOperations.add(new Operations(DIVISION));
        calculatorOperations.add(new Operations(PERCENTAGE));
        calculatorOperations.add(new Operations(RESULT));

        calculatorOperations.add(new Operations(BACKSPACE));
        calculatorOperations.add(new Operations(DELETE));
    }
}

class GetCalculatorFactory {

    public Numbers getNumber(int value) {
        return new CalculatorNumbers().calculatorNumbers.get(value);
    }

    public Operations getOperation(int operation)
    {
        return new CalculatorOperations().calculatorOperations.get(operation);
    }


}
