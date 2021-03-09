package handreolas.divinelink.calculator.calculator;

import android.content.Context;
import android.os.AsyncTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

import handreolas.divinelink.calculator.base.HomeDatabase;

public class CalculatorInteractorImpl implements ICalculatorInteractor {

    public int LENGTH_LIMIT_FOR_DECIMALS = 20;
    public int LENGTH_LIMIT_FOR_INTEGERS = 15;

    //TODO Fix what happens when there's a huge number with exponential more than 300
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
                    if (getLengthOfNumberWithoutCommas(currentNumber) <= getLengthLimit(currentNumber)) {  // If number IS decimal, then length should be 24

                        calculatorDao.updateFirstNumber(formatNumber(currentNumber));

                        result = currentNumber;
                    }
                } else {
                    currentNumber = checkIfCurrentNumberIsNull(secondNumber, addedNumber);
                    if (getLengthOfNumberWithoutCommas(currentNumber) <= getLengthLimit(currentNumber)) {

                        result = calculateResult(firstNumber, currentNumber, operand);
                        if (result.equals("Can't divide by zero")) //FIXME
                            listener.onDivisionByZero();
                        else
                            calculatorDao.updateSecondNumber(formatNumber(currentNumber));
                    }
                }
                if (!result.equals("Can't divide by zero")) {
                    calculatorDao.updateResult(formatNumberForResult(result, true));
                    if (getLengthOfNumberWithoutCommas(currentNumber) <= getLengthLimit(currentNumber)) {

                        calculatorDao.updateCalculator(new CalculatorDomain(calculatorDao.getFirstNumber(),
                                calculatorDao.getSecondNumber(),
                                calculatorDao.getResult(),
                                calculatorDao.getOperator()));

                        listener.onShowResult(calculatorDao.getCalculatorDomain());

                    } else { // User is limited to 15 digit number input.
                        listener.onTooManyDigits();
                    }
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
                String firstNumber = calculatorDao.getFirstNumber();

                if (result == null) {
                    listener.onClear();
                } else if (result != null && firstNumber != null) {
                    listener.onShowResult(calculatorDao.getCalculatorDomain());
                } else if (result != null && firstNumber == null) {
                    listener.onShowResult(calculatorDao.getSavedCalculatorDomain());
                    listener.onButtonResult(calculatorDao.getSavedCalculatorDomain());
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
    public void setOperand(OnGetResultFinishListener listener, Context ctx, String operand) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                String result;

                String previousOperand;

                result = calculatorDao.getResult() == null ? "0" : (calculatorDao.getResult());

                previousOperand = calculatorDao.getOperator();

                if (result.equals("NaN")) {
                    calculatorDao.updateFirstNumber(null);
                    calculatorDao.updateResult(null);
                    calculatorDao.updateSecondNumber(null);
                    calculatorDao.updateOperation(null);
                    listener.onError("NaN");
                } else {
                    if (previousOperand == null) { // If there's no current operand, all you have to do is update the first number in DB,
                        if (calculatorDao.getFirstNumber() == null && result.equals("0")) {
                            calculatorDao.updateFirstNumber("0");
                            calculatorDao.updateResult("0");
                        } else if (calculatorDao.getFirstNumber() == null && result != null) {
                            // This is executed when we have pressed result button (everything turns into null except result) and then press some operand.
                            // We basically set first number to be equal to result.
                            calculatorDao.updateFirstNumber(result);
                        }

                    } else { // Otherwise, first number becomes the current result, second number becomes "null" and  we change the current operand.
                        // Set result variable to be the formatted version of the result value we have on DB.
                        // For example, if we have the value 258,297.2117865 on result, what is actually shown on the app is 258,297.212
                        // So, we want to save this value on Result and on NumberA on DB as well.
                        result = calculatorDao.getResult() == null ? "0" : (formatNumberForResult(calculatorDao.getResult(), true));
                        calculatorDao.updateFirstNumber(result);
                        calculatorDao.updateResult(result); // If result is already null, then set firstNumber as zero.
                        calculatorDao.updateSecondNumber(null);
                    }
                    calculatorDao.updateOperation(operand);

                    listener.onShowResult(calculatorDao.getCalculatorDomain());

                }
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
                String result;
                String operand = calculatorDao.getOperator();

                if (operand == null) {
                    currentNumber = checkIfCurrentNumberIsNull(firstNumber, ".");
                    calculatorDao.updateFirstNumber(formatNumber(currentNumber));
                    result = currentNumber;
                } else {
                    currentNumber = checkIfCurrentNumberIsNull(secondNumber, ".");
                    calculatorDao.updateSecondNumber(formatNumber(currentNumber));
                    result = calculateResult(firstNumber, currentNumber, operand);

                }
                calculatorDao.updateResult(formatNumberForResult(result, true));

                if (getLengthOfNumberWithoutCommas(currentNumber) <= getLengthLimit(currentNumber)) { // If number has decimal part, then Length = 20;

                    calculatorDao.updateCalculator(new CalculatorDomain(
                            calculatorDao.getFirstNumber(),
                            calculatorDao.getSecondNumber(),
                            calculatorDao.getResult(),
                            calculatorDao.getOperator()));

                    listener.onShowResult(calculatorDao.getCalculatorDomain());
                } else { // User is limited to 20 or 15 digit number input.
                    listener.onTooManyDigits();
                }
            }
        });
    }

    @Override
    public void backspace(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
                String currentNumber;

                String firstNumber = removeCommasFromNumber(calculatorDao.getFirstNumber())
                        == null ? "0" : removeCommasFromNumber(calculatorDao.getFirstNumber());
                String secondNumber = removeCommasFromNumber(calculatorDao.getSecondNumber());

                String result;
                String operand = calculatorDao.getOperator();

                if (firstNumber.equals("0") && secondNumber == null) {
                    calculatorDao.updateFirstNumber(null);
                    calculatorDao.updateResult(null);
                    calculatorDao.updateSecondNumber(null);
                    calculatorDao.updateOperation(null);

                    result = null;

                    listener.onClear();

                } else if (operand == null) {
                    currentNumber = removeLastElementFromString(firstNumber);
                    calculatorDao.updateFirstNumber(formatNumber(currentNumber));
                    result = currentNumber;
                } else {
                    if (secondNumber != null) {
                        currentNumber = removeLastElementFromString(secondNumber);
                        calculatorDao.updateSecondNumber(formatNumber(currentNumber));
                        if (currentNumber != null)
                            result = calculateResult(firstNumber, currentNumber, operand);
                        else
                            result = firstNumber;
                    } else { // If secondNumber is null, it means there's an operand left, so we only return the first Number.
                        calculatorDao.updateOperation(null);
                        result = firstNumber;
                    }
                }

                calculatorDao.updateResult(formatNumberForResult(result, true));
                listener.onShowResult(calculatorDao.getCalculatorDomain());
            }
        });
    }

    @Override
    public void result(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            // When we press result button, set NumberA, NumberB and Operand to null on DB.
            // Then pass the result on presenter and start animation on result text view.
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                if (calculatorDao.getFirstNumber() != null) // If you double press result button and don't have this condition, saved numbers become null.
                    calculatorDao.insertCalculation(new CalculatorDomain(1, calculatorDao.getFirstNumber(), calculatorDao.getSecondNumber(), calculatorDao.getResult(), calculatorDao.getOperator()));

                calculatorDao.updateOperation(null);
                calculatorDao.updateSecondNumber(null);
                calculatorDao.updateFirstNumber(null);

//                listener.onShowResult(calculatorDao.getSavedCalculatorDomain());
                listener.onButtonResult(calculatorDao.getSavedCalculatorDomain());
            }
        });
    }

    @Override
    public void percentage(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                String currentNumber;
                // When we perform deletion, we calculate result based on the formatted numbers on DB.
                String firstNumber = removeCommasFromNumber(calculatorDao.getFirstNumber())
                        == null ? "0" : removeCommasFromNumber(calculatorDao.getFirstNumber());
                String secondNumber = removeCommasFromNumber(calculatorDao.getSecondNumber());
                String result;
                String operand = calculatorDao.getOperator();

                if (secondNumber == null) {
                    currentNumber = percentNumber(firstNumber);
                    firstNumber = currentNumber;

                    calculatorDao.updateFirstNumber(formatNumber(firstNumber));
                    result = firstNumber;
                } else {
                    currentNumber = percentNumber(secondNumber);
                    secondNumber = currentNumber;
                    calculatorDao.updateSecondNumber(formatNumber(secondNumber));
                    result = calculateResult(firstNumber, secondNumber, operand);
                }

                calculatorDao.updateResult(formatNumberForResult(result, true));
                if (!result.equals("0") || secondNumber != null) {
                    listener.onShowResult(calculatorDao.getCalculatorDomain());
                } else {
                    calculatorDao.updateFirstNumber(null);
                    calculatorDao.updateResult(null);
                    calculatorDao.updateSecondNumber(null);

                    listener.onClear();
                }
            }
        });
    }

    private String percentNumber(String number) {
        String result;
        BigDecimal bi1;

        if (number.equals("0"))
            return "0";

        try {
            bi1 = new BigDecimal(number);
        } catch (Exception e) {
            return "NaN";
        }

        result = String.valueOf(bi1.divide(BigDecimal.valueOf(100), 8, RoundingMode.CEILING));
        if (result.contains("E"))
            return formatNumber(result);
        else
            return new DecimalFormat("0.########").format(Double.parseDouble(result)); // Remove trailing zeroes
    }

    // Helper Method
    public String removeLastElementFromString(String str) {
        if (str == null || str.length() == 1)
            return null;
        else if (str.length() > 1)
            return str.substring(0, str.length() - 1);
        else
            return null;
    }


    private String calculateResult(String firstNumber, String secondNumber, String operand) {
        String result;
        BigDecimal bi1, bi2;

        firstNumber = removeCommasFromNumber(firstNumber);
        secondNumber = removeCommasFromNumber(secondNumber);

        if (operand.equals("÷")) {
            if (secondNumber.equals("0"))
                return "Can't divide by zero";
        }

        try {
            bi1 = new BigDecimal(firstNumber);
        } catch (Exception e) {
            return "NaN";
        }
        try {
            bi2 = new BigDecimal(secondNumber);
        } catch (Exception e) {
            return "NaN";
        }

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

        return new DecimalFormat("0.#######").format(Double.parseDouble(result)); // Remove trailing zeroes from result
    }

    private String formatNumberForResult(String resultNumber, boolean isExponentialDesired) {
        String formattedNumber;

        resultNumber = removeCommasFromNumber(resultNumber);

        Locale.setDefault(Locale.US);
        if (resultNumber != null) {

            if (resultNumber.endsWith("-"))
                return null;

            if (getIntegerDigits(resultNumber) >= 10 || resultNumber.contains("E")) {
                if (isExponentialDesired)
                    // This boolean is needed when we save data on DB.
                    // When we update data on DB. We don't want to save number in exponential form on DB.
                    // We only want to show them that way.
                    // For example, if we have the value 4,659,046,196,701
                    // We save it on DB as 4659046196701 and we show it on the app as 4.6590462E12.
                    if (resultNumber.endsWith("E"))
                        return resultNumber;
                    else
                        return new DecimalFormat("#.########E0").format(Double.parseDouble(resultNumber));

                else
                    return new DecimalFormat("0").format(Double.parseDouble(resultNumber));
                //TODO make this dynamic
            } else if (getIntegerDigits(resultNumber) >= 9) {
                return new DecimalFormat("###,###").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 8) {
                return new DecimalFormat("###,###.#").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 7) {
                return new DecimalFormat("###,###.##").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 6) {
                return new DecimalFormat("###,###.###").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 5) {
                return new DecimalFormat("###,###.####").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 4) {
                return new DecimalFormat("###,###.#####").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 3) {
                return new DecimalFormat("###,###.######").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 2) {
                return new DecimalFormat("###,###.#######").format(Double.parseDouble(resultNumber));
            } else if (getIntegerDigits(resultNumber) >= 1) {
                return new DecimalFormat("###,###.########").format(Double.parseDouble(resultNumber));
            } else {
                DecimalFormat formatter = new DecimalFormat("###,###.#####");
                formattedNumber = formatter.format(new BigDecimal(resultNumber));
                return formattedNumber;
            }
        } else {
            return null;
        }
    }

    // Helper Method
    private String removeCommasFromNumber(String number) {
        if (number != null)
            return number.replaceAll(",", ""); // We need to remove all commas when we save data on DB
        else
            return null;
    }

    // Declared Useless As Of 3/9/21
    private String formatNumberForExponentialNotation(String number, boolean isFloatDesired) {
        Locale.setDefault(Locale.US);
        int length;

        if (isFloatDesired) {
            length = LENGTH_LIMIT_FOR_DECIMALS;
        } else {
            length = LENGTH_LIMIT_FOR_INTEGERS;
        }

        if (number != null) {
            if (number.endsWith("-"))
                return null;
            if (number.length() > length) {
                return new DecimalFormat("#.########E0").format(Double.parseDouble(number));
            } else {
                return number;
            }
        } else {
            return number;
        }
    }

    // Helper Method
    private String formatNumber(String number) {

        String formattedNumber;
        number = removeCommasFromNumber(number);
        int length;
        Locale.setDefault(Locale.US);

        length = LENGTH_LIMIT_FOR_DECIMALS;

        if (number != null) {
            if (number.endsWith("-")) //
                return null;
            if (number.length() > length || number.contains("E")) {
                if (number.endsWith("E")) {
                    return number;
                } else {
                    DecimalFormat formatter = new DecimalFormat("#.########E0");
                    formattedNumber = formatter.format(new BigDecimal(number));
                    return formattedNumber;
                }
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

    private int getIntegerDigits(String number) {
        String[] arrayOfNumbers = number.split("\\.");

        if (arrayOfNumbers.length > 1) {
            return arrayOfNumbers[0].length();
        } else {
            return number.length();
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

    // Helper Method
    private int getLengthOfNumberWithoutCommas(String number) {
        number = removeCommasFromNumber(number);
        return number.length();
    }

    // Helper Method
    private int getLengthLimit(String number) {

        number = removeCommasFromNumber(number);

        if (number.contains("."))
            return LENGTH_LIMIT_FOR_DECIMALS;
        else
            return LENGTH_LIMIT_FOR_INTEGERS;
    }
}
