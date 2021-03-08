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

    public int LENGTH_FOR_CALCULATION_VIEW_WITH_COMMA = 20;
    public int LENGTH_FOR_CALCULATION_VIEW_WITHOUT_COMMA = 15;

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
                int lengthOfCurrentNumber;


                if (operand == null) {
                    currentNumber = checkIfCurrentNumberIsNull(firstNumber, addedNumber);
                    lengthOfCurrentNumber = (!currentNumber.contains(".") ? LENGTH_FOR_CALCULATION_VIEW_WITHOUT_COMMA : LENGTH_FOR_CALCULATION_VIEW_WITH_COMMA);  // If number contains comma, then length should be 20
                    if (currentNumber.length() <= lengthOfCurrentNumber) {
                        // UPDATES VALUES ON DB
                        calculatorDao.updateFirstNumber(formatNumberForExponentialNotation(currentNumber, true)); //FIXME
                        calculatorDao.updateFormattedFirstNumber(formatNumber(currentNumber, true)); //FIXME
                        // END UPDATES ON DB
                        result = currentNumber;
                    }
                } else {
                    currentNumber = checkIfCurrentNumberIsNull(secondNumber, addedNumber);
                    lengthOfCurrentNumber = (!currentNumber.contains(".") ? LENGTH_FOR_CALCULATION_VIEW_WITHOUT_COMMA : LENGTH_FOR_CALCULATION_VIEW_WITH_COMMA);
                    if (currentNumber.length() <= lengthOfCurrentNumber) {
                        // UPDATE VALUES ON DB
                        calculatorDao.updateSecondNumber(formatNumberForExponentialNotation(currentNumber, true)); //FIXME
                        calculatorDao.updateFormattedSecondNumber(formatNumber(currentNumber,true)); //FIXME
                        // END UPDATE VALUES ON DB
                        result = calculateResult(firstNumber, currentNumber, operand);
                    }
                }
//                result = calculateResult(firstNumber, currentNumber, operand);
                calculatorDao.updateResult(formatNumberForExponentialNotation(result, false)); //FIXME
                calculatorDao.updateFormattedResult(formatNumberForResult(calculatorDao.getResult(), true)); //FIXME
                if (currentNumber.length() <= lengthOfCurrentNumber) {

                    calculatorDao.updateCalculator(
                            new CalculatorDomain(0, calculatorDao.getFirstNumber(), calculatorDao.getSecondNumber(), calculatorDao.getResult(), calculatorDao.getOperator()),
                            new CalculatorDomain(1, calculatorDao.getFormattedFirstNumber(), calculatorDao.getFormattedSecondNumber(), calculatorDao.getFormattedResult(), calculatorDao.getOperator()));

                    listener.onShowResult( //FIXME pass Object instead of Strings
                            calculatorDao.getFormattedFirstNumber(),
                            calculatorDao.getFormattedSecondNumber(),
                            calculatorDao.getOperator(),
                            calculatorDao.getFormattedResult());
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

                if (result == null) {
                    listener.onClear();
                } else {
                    listener.onShowResult(
                            calculatorDao.getFormattedFirstNumber(),
                            calculatorDao.getFormattedSecondNumber(),
                            calculatorDao.getOperator(),
                            calculatorDao.getFormattedResult());
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

                String resultFormatted, resultNonFormatted;
                String previousOperand;

                resultNonFormatted = calculatorDao.getResult() == null ? "0" : (calculatorDao.getResult());

                previousOperand = calculatorDao.getOperator();

                if (resultNonFormatted.equals("NaN")) {
                    calculatorDao.updateFormattedFirstNumber(null);
                    calculatorDao.updateFormattedResult(null);

                    calculatorDao.updateSecondNumber(null);
                    calculatorDao.updateFormattedSecondNumber(null);

                    calculatorDao.updateFirstNumber(null);
                    calculatorDao.updateResult(null);

                    calculatorDao.updateOperation(null);

                    listener.onError("NaN");
                } else {
                    if (previousOperand == null) { // If there's no current operand, all you have to do is update the first number in DB,
                        if (calculatorDao.getFirstNumber() == null) {
                            calculatorDao.updateFirstNumber("0");
                            calculatorDao.updateFormattedFirstNumber("0");
                            calculatorDao.updateResult("0");
                            calculatorDao.updateFormattedResult("0");
                        }
                    } else { // Otherwise, first number becomes the current result, second number becomes "null" and  we change the current operand.
                        // Set result variable to be the formatted version of the result value we have on DB.
                        // For example, if we have the value 258,297.2117865 on result, what is actually shown on the app is 258,297.212
                        // So, we want to save this value on Result and on NumberA on DB as well.
                        resultFormatted = calculatorDao.getResult() == null ? "0" : (formatNumberForResult(calculatorDao.getResult(), true));
                        resultNonFormatted = calculatorDao.getResult() == null ? "0" : (calculatorDao.getResult());

                        calculatorDao.updateFormattedFirstNumber(resultFormatted);
                        calculatorDao.updateFormattedResult(resultFormatted); // If result is already null, then set firstNumber as zero.

                        calculatorDao.updateFirstNumber(formatNumberForExponentialNotation(resultNonFormatted, true));
                        calculatorDao.updateResult(formatNumberForExponentialNotation(resultNonFormatted, false)); // resultNonFormatted or resultFormatted?

                        // This happens when firstNumber == 0 and we change operands two times in a row.
                        calculatorDao.updateSecondNumber(null);
                        calculatorDao.updateFormattedSecondNumber(null);
                    }
                    calculatorDao.updateOperation(operand);

                    listener.onShowResult(
                            calculatorDao.getFormattedFirstNumber(),
                            calculatorDao.getFormattedSecondNumber(),
                            calculatorDao.getOperator(),
                            calculatorDao.getFormattedResult());

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
                String result = calculatorDao.getResult();
                String operand = calculatorDao.getOperator();
                int lengthOfCurrentNumber;

                if (operand == null) {
                    currentNumber = checkIfCurrentNumberIsNull(firstNumber, ".");
                    calculatorDao.updateFirstNumber(formatNumberForExponentialNotation(currentNumber, true));
                    calculatorDao.updateFormattedFirstNumber(formatNumber(currentNumber, true));
                    result = currentNumber;
                } else {
                    currentNumber = checkIfCurrentNumberIsNull(secondNumber, ".");
                    calculatorDao.updateSecondNumber(formatNumberForExponentialNotation(currentNumber, true));
                    calculatorDao.updateFormattedSecondNumber(formatNumber(currentNumber, true));
                    result = calculateResult(firstNumber, currentNumber, operand);

                }
                calculatorDao.updateResult(formatNumberForExponentialNotation(result, false));
                calculatorDao.updateFormattedResult(formatNumberForResult(result, true));

                lengthOfCurrentNumber = (!currentNumber.contains(".") ? LENGTH_FOR_CALCULATION_VIEW_WITHOUT_COMMA : LENGTH_FOR_CALCULATION_VIEW_WITH_COMMA);

                if (currentNumber.length() <= lengthOfCurrentNumber) { // If number has decimal part, then Length = 20;

                    calculatorDao.updateCalculator(
                            new CalculatorDomain(0, calculatorDao.getFirstNumber(), calculatorDao.getSecondNumber(), calculatorDao.getResult(), calculatorDao.getOperator()),
                            new CalculatorDomain(1, calculatorDao.getFormattedFirstNumber(), calculatorDao.getFormattedSecondNumber(), calculatorDao.getFormattedResult(), calculatorDao.getOperator()));

                    listener.onShowResult(
                            calculatorDao.getFormattedFirstNumber(),
                            calculatorDao.getFormattedSecondNumber(),
                            calculatorDao.getOperator(),
                            calculatorDao.getFormattedResult());
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
                // When we delete a number in exponential form, we
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
                String currentNumber;
                // When we perform deletion, we calculate result based on the formatted numbers on DB.
                String firstNumber = removeCommasFromNumber(calculatorDao.getFormattedFirstNumber())
                        == null ? "0" : removeCommasFromNumber(calculatorDao.getFormattedFirstNumber());
                String secondNumber = removeCommasFromNumber(calculatorDao.getFormattedSecondNumber());
                String result = calculatorDao.getResult();
                String operand = calculatorDao.getOperator();

                if (firstNumber.equals("0") && secondNumber == null) {
                    calculatorDao.updateFormattedFirstNumber(null);
                    calculatorDao.updateFormattedResult(null);

                    calculatorDao.updateSecondNumber(null);
                    calculatorDao.updateFormattedSecondNumber(null);

                    calculatorDao.updateFirstNumber(null);
                    calculatorDao.updateOperation(null);

                    result = null;

                    listener.onClear();

                } else if (operand == null) {
                    currentNumber = removeLastElementFromString(firstNumber);
                    calculatorDao.updateFirstNumber(formatNumberForExponentialNotation(currentNumber, true));
                    calculatorDao.updateFormattedFirstNumber(formatNumber(currentNumber, true));
                    result = currentNumber;
                } else {
                    if (secondNumber != null) {
                        currentNumber = removeLastElementFromString(secondNumber);
                        calculatorDao.updateSecondNumber(formatNumberForExponentialNotation(currentNumber, true));
                        calculatorDao.updateFormattedSecondNumber(formatNumber(currentNumber, true));
                        if (currentNumber != null)
                            result = calculateResult(firstNumber, currentNumber, operand);
                        else
                            result = firstNumber;
                    } else { // If secondNumber is null, it means there's an operand left, so we only return the first Number.
                        calculatorDao.updateOperation(null);
                        result = firstNumber;
                    }
                }
                calculatorDao.updateResult(formatNumberForExponentialNotation(result, false));
                calculatorDao.updateFormattedResult(formatNumberForResult(result, true));
                listener.onShowResult(
                        calculatorDao.getFormattedFirstNumber(),
                        calculatorDao.getFormattedSecondNumber(),
                        calculatorDao.getOperator(),
                        calculatorDao.getFormattedResult());
            }
        });
    }

    @Override
    public void result(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            //TODO change what result does. Focus on Result View and remove other values from number A and B.
            public void run() {
                final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

                calculatorDao.updateOperation(null);
                calculatorDao.updateSecondNumber(null);
                calculatorDao.updateFormattedSecondNumber(null);

                calculatorDao.updateFirstNumber(calculatorDao.getResult());
                calculatorDao.updateFormattedFirstNumber(calculatorDao.getFormattedResult());

                listener.onShowResult(calculatorDao.getFormattedFirstNumber(),
                        null,
                        null,
                        calculatorDao.getFormattedResult());
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
                String firstNumber = removeCommasFromNumber(calculatorDao.getFormattedFirstNumber())
                        == null ? "0" : removeCommasFromNumber(calculatorDao.getFormattedFirstNumber());
                String secondNumber = removeCommasFromNumber(calculatorDao.getFormattedSecondNumber());
                String result;
                String operand = calculatorDao.getOperator();

                if (secondNumber == null) {
                    currentNumber = percentNumber(firstNumber);
                    firstNumber = currentNumber;
                    calculatorDao.updateFirstNumber(formatNumberForExponentialNotation(firstNumber, true)); // FIXME
                    calculatorDao.updateFormattedFirstNumber(formatNumber(firstNumber, true));
                    result = firstNumber;
                } else {
                    currentNumber = percentNumber(secondNumber);
                    secondNumber = currentNumber;
                    calculatorDao.updateSecondNumber(formatNumberForExponentialNotation(secondNumber, true));
                    calculatorDao.updateFormattedSecondNumber(formatNumber(secondNumber, true));
                    result = calculateResult(firstNumber, secondNumber, operand);
                }


                calculatorDao.updateResult(formatNumberForExponentialNotation(result, false));
                calculatorDao.updateFormattedResult(formatNumberForResult(result, true));
                if (!result.equals("0") || secondNumber != null) {
                    listener.onShowResult(
                            calculatorDao.getFormattedFirstNumber(),
                            calculatorDao.getFormattedSecondNumber(),
                            calculatorDao.getOperator(),
                            calculatorDao.getFormattedResult());
                } else {
                    //calculatorDao//
                    calculatorDao.updateFirstNumber(null);
                    calculatorDao.updateFormattedFirstNumber(null);
                    calculatorDao.updateResult(null);
                    calculatorDao.updateFormattedResult(null);
                    calculatorDao.updateSecondNumber(null);
                    calculatorDao.updateFormattedSecondNumber(null);

                    listener.onClear();
                }
            }
        });
    }

    private String percentNumber(String number) {
        String result;
        BigDecimal bi1;
        try {
            bi1 = new BigDecimal(number);
        } catch (Exception e) {
            return "NaN";
        }

        result = String.valueOf(bi1.divide(BigDecimal.valueOf(100), 7, RoundingMode.FLOOR));

        return new DecimalFormat("0.#######").format(Double.parseDouble(result)); // Remove trailing zeroes
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

    private String formatNumberForExponentialNotation(String number, boolean isFloatDesired) {
        Locale.setDefault(Locale.US);
        int length;

        if (isFloatDesired) {
            length = LENGTH_FOR_CALCULATION_VIEW_WITH_COMMA;
        } else {
            length = LENGTH_FOR_CALCULATION_VIEW_WITHOUT_COMMA;
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
    private String formatNumber(String number, boolean isFloatDesired) {
        // roundAfter - How long do we want number to be in order to show scientific number notation.
        String formattedNumber;
        int length;
        Locale.setDefault(Locale.US);

        if (isFloatDesired) {
            // We need this so when we enter a big decimal number, it doesn't turn it to exponential notation.
            // For Example 1.000000000000000000 to 1E0
            length = LENGTH_FOR_CALCULATION_VIEW_WITH_COMMA;
        } else {
            length = LENGTH_FOR_CALCULATION_VIEW_WITHOUT_COMMA;
        }

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
}
