package handreolas.divinelink.calculator.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import handreolas.divinelink.calculator.base.HomeDatabase;
import handreolas.divinelink.calculator.features.SharedPrefManager;

public class CalculatorInteractorImpl implements ICalculatorInteractor {

    public int NUMBER_LENGTH_FOR_RESULT = 10;
    public int NUMBER_LENGTH_FOR_CALCULATION = 15;

    @Override
    public void setNumber(final OnGetResultFinishListener listener, final Context ctx, String addedNumber) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                String currentNumber;
                String firstNumber = calculatorDao.getFirstNumber();
                String secondNumber = calculatorDao.getSecondNumber();
                String result = calculatorDao.getResult();
                String operand = calculatorDao.getOperator();


                if (operand == null) {
                    currentNumber = checkIfCurrentNumberIsNull(firstNumber, addedNumber);
                    if (currentNumber.length() <= 15) { // If number contains comma, then length should be 20
                        calculatorDao.updateFirstNumber(currentNumber);
                        result = currentNumber;
                    }
                } else {
                    currentNumber = checkIfCurrentNumberIsNull(secondNumber, addedNumber);
                    if (currentNumber.length() <= 15) {
                        calculatorDao.updateSecondNumber(currentNumber);
                        result = calculateResult(firstNumber, currentNumber, operand);
                    }

                }
//                result = calculateResult(firstNumber, currentNumber, operand);
                calculatorDao.updateResult(result);
                if (currentNumber.length() <= 15) {
                    calculatorDao.updateCalculator(new CalculatorDomain(calculatorDao.getFirstNumber(), calculatorDao.getSecondNumber(), calculatorDao.getResult(), calculatorDao.getOperator()));
                    listener.onShowResult(
                            formatNumber(calculatorDao.getFirstNumber(), NUMBER_LENGTH_FOR_CALCULATION),
                            formatNumber(calculatorDao.getSecondNumber(), NUMBER_LENGTH_FOR_CALCULATION),
                            calculatorDao.getOperator(),
                            formatNumber(calculatorDao.getResult(), NUMBER_LENGTH_FOR_RESULT));
                } else { // User is limited to 15 digit number input.
                    listener.onTooManyDigits();
                }
            }
        });
    }

    @Override
    public void getResult(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                String result = calculatorDao.getResult();

                String firstNumber;
                String secondNumber;
                String operand = null;

                if (result == null) {
                    listener.onClear();
                } else {
                    firstNumber = calculatorDao.getFirstNumber();
                    secondNumber = calculatorDao.getSecondNumber();
                    operand = calculatorDao.getOperator();

                    listener.onShowResult(
                            formatNumber(firstNumber, NUMBER_LENGTH_FOR_CALCULATION),
                            formatNumber(secondNumber, NUMBER_LENGTH_FOR_CALCULATION),
                            operand,
                            formatNumber(result, NUMBER_LENGTH_FOR_RESULT));
                }
            }
        });
    }

    //TODO check what happens when entering zeros at start
    @Override
    public void clearEntries(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
                calculatorDao.clearEntries();
                listener.onClear();
            }
        });
    }

    @Override
    public void setOperand(OnGetResultFinishListener listener, Context ctx, String operand) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                String result;
                String previousOperand;

                previousOperand = calculatorDao.getOperator();

                if (previousOperand == null) { // If there's no current operand, all you have to do is update the first number in DB,
                    if (calculatorDao.getFirstNumber() == null) {
                        calculatorDao.updateFirstNumber("0");
                    }
                    result = calculatorDao.getFirstNumber();
                } else { // Otherwise, first number becomes the current result, second number becomes "null" and  we change the current operand.
                    result = calculatorDao.getResult() == null ? "0" : calculatorDao.getResult();
                    calculatorDao.updateFirstNumber(result); // If result is already null, then set firstNumber as zero.
                    // This happens when firstNumber == 0 and we change operands two times in a row.
                    calculatorDao.updateSecondNumber(null);
                }
                calculatorDao.updateOperation(operand);

                listener.onShowResult(
                        formatNumber(calculatorDao.getFirstNumber(), NUMBER_LENGTH_FOR_CALCULATION),
                        formatNumber(calculatorDao.getSecondNumber(), NUMBER_LENGTH_FOR_CALCULATION),
                        calculatorDao.getOperator(),
                        formatNumber(result, NUMBER_LENGTH_FOR_RESULT));

            }
        });
    }

    @Override
    public void setComma(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
                String currentNumber;
                String firstNumber = calculatorDao.getFirstNumber();
                String secondNumber = calculatorDao.getSecondNumber();
                String result = calculatorDao.getResult();
                String operand = calculatorDao.getOperator();

                if (operand == null) {
                    currentNumber = checkIfCurrentNumberIsNull(firstNumber, ".");
                    calculatorDao.updateFirstNumber(currentNumber);
                    result = currentNumber;

                } else {
                    currentNumber = checkIfCurrentNumberIsNull(secondNumber, ".");
                    calculatorDao.updateSecondNumber(currentNumber);
                    result = calculateResult(firstNumber, currentNumber, operand);

                }
                calculatorDao.updateResult(result);

                if (currentNumber.length() <= 15) {
                    calculatorDao.updateCalculator(new CalculatorDomain(calculatorDao.getFirstNumber(), calculatorDao.getSecondNumber(), calculatorDao.getResult(), calculatorDao.getOperator()));
                    listener.onShowResult(
                            formatNumber(calculatorDao.getFirstNumber(), NUMBER_LENGTH_FOR_CALCULATION),
                            formatNumber(calculatorDao.getSecondNumber(), NUMBER_LENGTH_FOR_CALCULATION),
                            calculatorDao.getOperator(),
                            formatNumber(calculatorDao.getResult(), NUMBER_LENGTH_FOR_RESULT));
                } else { // User is limited to 15 digit number input.
                    listener.onTooManyDigits();
                }
            }
        });
    }


    private String calculateResult(String firstNumber, String secondNumber, String operand) {
        String result;
        BigDecimal bi1, bi2;
        bi1 = new BigDecimal(firstNumber);
        bi2 = new BigDecimal(secondNumber);

        switch (operand) {
            case "+":
                result = String.valueOf(bi1.add(bi2));
                break;
            case "-":
                result = String.valueOf(bi1.subtract(bi2));
                break;
            case "×":
                result = String.valueOf(bi1.multiply(bi2));
                break;
            case "÷":
                result = String.valueOf(bi1.divide(bi2, 10, RoundingMode.CEILING));
                break;
            default:
                result = String.valueOf(firstNumber);
        }
        return result;
    }


    // Helper Method
    private String formatNumber(String number, int roundAfter) {
        // roundAfter - How long do we want number to be in order to show scientific number notation.
        String formattedNumber;

        Locale.setDefault(Locale.US);
        if (number != null) {
            if (number.length() > roundAfter) {
                DecimalFormat formatter = new DecimalFormat("#.########E0");
                formattedNumber = formatter.format(new BigDecimal(number));
                return formattedNumber;
            } else {
                DecimalFormat formatter = new DecimalFormat("###,###.#");
                formatter.setMinimumFractionDigits(getFractionDigits(number));
                formattedNumber = formatter.format(new BigDecimal(number));
                // DecimalFormat does not return comma separator if there's no number in the decimal part of the number.
                // So if we want to show 0. after the comma button is pressed, it only returns 0
                // But we want it to also show the decimal separator. That's why we have the following condition.
                if (number.contains(".") && !formattedNumber.contains("."))
                    return String.format("%s.", formattedNumber);
                else
                    return formattedNumber;
            }
        } else {
            return null;
        }
    }

    // Helper Method
    private int getFractionDigits(String number) {
        // Find Number of Faction Digits of a number.
        // We need this to manually set MinimumFractionDigits on @formatNumber method
        // In order to show the correct amount of zeros after the comma separator.
        // For example 0.00000002 -> Number of Fraction Digits = 7.

        String[] arrayOfNumbers = number.split("\\.");

        if (arrayOfNumbers.length > 1) {
            return arrayOfNumbers[1].length();
        } else {
            return 0;
        }
    }

    // Helper Method
    private String checkIfCurrentNumberIsNull(String currentNumber, String addedDigit) {

        if (addedDigit.equals("0") && (currentNumber == null || currentNumber.equals("0"))) {
            return addedDigit;
        }

        if (currentNumber == null || currentNumber.equals("0")) { // If current number is null, then
            if (addedDigit.equals(".")) // if we pressed the comma button, add the comma separator
                return "0" + addedDigit;
            else
                return addedDigit; // or replace null or zero with the added number
        } else {
            if ((currentNumber.contains(".") && addedDigit.equals("."))) // Do not add another decimal separator
                return currentNumber;
            else
                return currentNumber + addedDigit;
        }
    }


}
