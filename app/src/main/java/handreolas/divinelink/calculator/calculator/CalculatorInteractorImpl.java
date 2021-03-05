package handreolas.divinelink.calculator.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import handreolas.divinelink.calculator.base.HomeDatabase;
import handreolas.divinelink.calculator.features.SharedPrefManager;

public class CalculatorInteractorImpl implements ICalculatorInteractor {


    private final SharedPrefManager prefManagerList = new SharedPrefManager();

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
                            formatNumber(calculatorDao.getFirstNumber()),
                            formatNumber(calculatorDao.getSecondNumber()),
                            calculatorDao.getOperator(),
                            getScientificNotationOfNumber(calculatorDao.getResult()));
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

                    listener.onShowResult(formatNumber(firstNumber), formatNumber(secondNumber), operand, getScientificNotationOfNumber(result));
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
                listener.onSuccess(formatNumber(result), operand);

            }
        });
    }

    //FIXME This is shiet!
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String calculateResult(String firstNumber, String secondNumber, String operand) {

        String result;

        switch (operand) {
            case "+":
                result = String.valueOf(Math.addExact(Long.parseLong(firstNumber), Long.parseLong(secondNumber)));
                break;
            case "-":
                result = String.valueOf(Math.subtractExact(Long.parseLong(firstNumber), Long.parseLong(secondNumber)));
                break;
            case "×":

                try {
                    result = String.valueOf(Math.multiplyExact(Long.parseLong(firstNumber), Long.parseLong(secondNumber)));
                } catch (Exception e) {
//                    result = b
                    BigInteger bi1, bi2, bi3;
                    bi1 = new BigInteger(firstNumber);
                    bi2 = new BigInteger(secondNumber);

                    bi3 = bi1.multiply(bi2);
                    result = String.valueOf(Long.parseLong(bi3.toString()));
                }
                break;
            case "÷":
                result = String.valueOf(Math.floorDiv(Long.parseLong(firstNumber), Long.parseLong(secondNumber)));
                break;
            default:
                result = String.valueOf(firstNumber);
        }
        return result;
    }

    @SuppressLint("DefaultLocale")
    private String getScientificNotationOfNumber(String number) {

        if (number != null) {
            if (Long.parseLong(number) >= 1000000000) {
                DecimalFormat formatter = new DecimalFormat("0.###E0");
                return formatter.format(Long.parseLong(number));
            } else {
                Locale.setDefault(Locale.US);
                return String.format("%,d", Long.parseLong(number));
            }
        } else {
            return null;
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatNumber(String number) {
        if (number != null) {
            if (number.length() > 15) {
                DecimalFormat formatter = new DecimalFormat("0.###E0");
                return formatter.format(Long.parseLong(number));
            } else {
                Locale.setDefault(Locale.US);
                return String.format("%,d", Long.parseLong(number));
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
