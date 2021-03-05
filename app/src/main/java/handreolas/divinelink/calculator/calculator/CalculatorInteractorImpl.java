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

    @RequiresApi(api = Build.VERSION_CODES.N) //FIXME
    @Override
    public void setNumber(final OnGetResultFinishListener listener, final Context ctx, int addedNumber) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                String currentNumber;
                String operand = null;

                String firstNumber = calculatorDao.getFirstNumber();
                String secondNumber = calculatorDao.getSecondNumber();
                String result = calculatorDao.getResult();

                operand = calculatorDao.getOperator();

                if (operand == null) {
                    currentNumber = checkIfCurrentNumberIsNull(firstNumber, addedNumber);
                    if (currentNumber.length() <= 15) {
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
    public void setOperand(OnSetOperandFinishListener listener, Context ctx, String operand) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                String result;
                String previousOperand;

                previousOperand = calculatorDao.getOperator();

                if (previousOperand == null) { // If there's no current operand, all you have to do is update the first number in DB,
                    result = calculatorDao.getFirstNumber();
                } else { // Otherwise, first number becomes the current result, second number becomes "null" and  we change the current operand.
                    result = calculatorDao.getResult();
                    calculatorDao.updateFirstNumber(result);
                    calculatorDao.updateSecondNumber(null);
                }
                calculatorDao.updateOperation(operand);
                listener.onSuccess(formatNumber(result, NUMBER_LENGTH_FOR_CALCULATION), operand);

            }
        });
    }

    //FIXME This is shiet!
    @RequiresApi(api = Build.VERSION_CODES.N)
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

    private String formatNumber(String number, int roundAfter) {

        // roundAfter - How long do we want number to be in order to show scientific number notation.

        Locale.setDefault(Locale.US);
        if (number != null) {
            if (number.length() > roundAfter) {

//                if (number.compareTo(String.valueOf(BigDecimal.ZERO)) > 0){}

                DecimalFormat formatter = new DecimalFormat("0.#######E0");
                return formatter.format(new BigDecimal(number));
            } else {
                DecimalFormat formatter = new DecimalFormat("#,##0");
                return formatter.format(new BigDecimal(number));
            }
        } else {
            return null;
        }
    }

    private String checkIfCurrentNumberIsNull(String currentNumber, int addedNumber) {

        if (currentNumber == null) {
            return Integer.toString(addedNumber);
        } else {
            return currentNumber + addedNumber;
        }
    }




}
