package handreolas.divinelink.calculator.features;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

public class CalculatorHelper {

    private final static int LENGTH_LIMIT_FOR_DECIMALS = 20;
    private final static int LENGTH_LIMIT_FOR_INTEGERS = 15;
    private final static String NAN = "NaN";
    private final static String DIVISION_BY_ZERO = "Can't divide by zero";

    public String percentNumber(String number) {
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
        if (isExponent(result))
            return formatNumber(result);
        else
            return new DecimalFormat("0.########").format(Double.parseDouble(result)); // Remove trailing zeroes
    }

    public String removeLastElementFromString(String str) {
        if (str == null || str.length() == 1)
            return null;
        else if (str.length() > 1)
            return str.substring(0, str.length() - 1);
        else
            return null;
    }


    public String calculateResult(String firstNumber, String secondNumber, String operand) {
        String result;
        BigDecimal bi1, bi2;

        firstNumber = removeCommasFromNumber(firstNumber);
        secondNumber = removeCommasFromNumber(secondNumber);

        try {
            bi1 = new BigDecimal(firstNumber);
        } catch (Exception e) {
            return NAN;
        }
        try {
            bi2 = new BigDecimal(secondNumber);
        } catch (Exception e) {
            return NAN;
        }
        if (operand.equals("÷")) {
            if (Double.parseDouble(secondNumber) == 0)
                return DIVISION_BY_ZERO;
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

    public String removeCommasFromNumber(String number) {
        if (number != null)
            return number.replaceAll(",", ""); // We need to remove all commas when we save data on DB
        else
            return null;
    }

    public String formatNumberForResult(String resultNumber) {
        Locale.setDefault(Locale.US);
        resultNumber = removeCommasFromNumber(resultNumber);

        if (resultNumber != null) {

            if (resultNumber.equals("∞")) {
                return "∞";
            }

            if (isDivisionByZero(resultNumber)) {
                return DIVISION_BY_ZERO;
            }

            if (resultNumber.endsWith("-"))
                return null;

            if (getIntegerDigits(resultNumber) >= 10 || isExponent(resultNumber)) {
                // This boolean is needed when we save data on DB.
                // When we update data on DB. We don't want to save number in exponential form on DB.
                // We only want to show them that way.
                // For example, if we have the value 4,659,046,196,701
                // We save it on DB as 4659046196701 and we show it on the app as 4.6590462E12.
                if (resultNumber.endsWith("E") || resultNumber.endsWith("e"))
                    return resultNumber;
                else
                    return new DecimalFormat("#.########E0").format(Double.parseDouble(resultNumber));

                //TODO make this dynamic
            } else if (getIntegerDigits(resultNumber) <= 9 && getIntegerDigits(resultNumber) >= 2) {
                // The follow procedure correctly formats result's fraction digits.
                // The larger the number, the less fraction digits we want to show.
                // For example 1,000,000.006 has 7 integer digits, so we'll show 2 fraction digits.
                // So it becomes 1,000,000.01 | 10,000,000.006 turns into 10,000,000.1 etc.
                DecimalFormat formatter = new DecimalFormat("###,###");
                formatter.setMaximumFractionDigits(9 - getIntegerDigits(resultNumber));
                formatter.setMinimumIntegerDigits(getIntegerDigits(resultNumber) - 1);
                return formatter.format(Double.parseDouble(resultNumber));

            } else {
                return new DecimalFormat("###,###.#####").format(Double.parseDouble(resultNumber));
            }
        } else {
            return null;
        }

    }

    public String formatNumberForExponentialNotation(String number, boolean isFloatDesired) {
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


    public String formatNumber(String number) {

        String formattedNumber;
        number = removeCommasFromNumber(number);
        Locale.setDefault(Locale.US);

        if (number != null) {

            if (number.endsWith("-")) //
                return null;
            if (number.length() > LENGTH_LIMIT_FOR_DECIMALS || isExponent(number)) {
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
                if (isDecimal(number) && !isDecimal(formattedNumber))
                    return String.format("%s.", formattedNumber);
                else
                    return formattedNumber;
            }
        } else {
            return null;
        }
    }


    public int getIntegerDigits(String number) {
        String[] arrayOfNumbers = number.split("\\.");

        if (arrayOfNumbers.length > 1) {
            return arrayOfNumbers[0].length();
        } else {
            return number.length();
        }
    }


    public int getFractionDigits(String number) {
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

    public String addDigitIfNumberNonNull(String currentNumber, String addedDigit) {

        if (addedDigit.equals("0") && (currentNumber == null || currentNumber.equals("0"))) {
            return addedDigit;
        }

        if (currentNumber == null || currentNumber.equals("0")) { // If current number is null, then
            if (addedDigit.equals(".")) // if we pressed the comma button, add the comma separator
                return "0" + addedDigit;
            else
                return addedDigit; // or replace null or zero with the added number
        } else {
            if ((isDecimal(currentNumber) && addedDigit.equals("."))) // Do not add another decimal separator
                return currentNumber;
            else
                return currentNumber + addedDigit;
        }
    }


    public int getLengthOfNumberWithoutCommas(String number) {
        number = removeCommasFromNumber(number);
        return number.length();
    }

    private boolean isExponent(String number) {
        return number.contains("E") || number.contains("e");
    }

    public boolean isDecimal(String number) {
//        return Double.parseDouble(number) % 1 != 0;
        return (number.contains(".")); //FIXME find a better way to check if number is decimal.
    }

    public boolean numberTooBig(String number) {
        if (number != null) {
            String[] arrayOfNumbers = number.split("E");

            if (arrayOfNumbers.length > 1) {
                return arrayOfNumbers[1].length() > 2;
            } else {
                return false;
            }
        } else return false;
    }

    public int getLengthLimit(String number) {

        number = removeCommasFromNumber(number);

        if (isDecimal(number))
            return LENGTH_LIMIT_FOR_DECIMALS;
        else
            return LENGTH_LIMIT_FOR_INTEGERS;
    }

    public boolean isNaN(String number) {
        return number.equals(NAN);
    }

    public boolean isDivisionByZero(String number) {
        return number.equals(DIVISION_BY_ZERO);
    }


}
