package handreolas.divinelink.calculator.calculator;

import android.content.Context;
import android.os.AsyncTask;
import handreolas.divinelink.calculator.base.HomeDatabase;
import handreolas.divinelink.calculator.features.CalculatorHelper;

public class CalculatorInteractorImpl implements ICalculatorInteractor {

    private final CalculatorHelper calculatorHelper = new CalculatorHelper();

    @Override
    public void setNumber(final OnGetResultFinishListener listener, final Context ctx, String addedNumber) {
        AsyncTask.execute(() -> {
            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

            String currentNumber;

            String firstNumber = calculatorDao.getFirstNumber();
            String secondNumber = calculatorDao.getSecondNumber();
            String result = calculatorDao.getResult();

            String operand = calculatorDao.getOperator();

            if (!calculatorHelper.numberTooBig(firstNumber) && !calculatorHelper.numberTooBig(secondNumber)) {

                if (operand == null) {
                    // Add to First Number
                    currentNumber = calculatorHelper.addDigitIfNumberNonNull(firstNumber, addedNumber);
                    if (calculatorHelper.getLengthOfNumberWithoutCommas(currentNumber) <= calculatorHelper.getLengthLimit(currentNumber)) {  // If number IS decimal, then length should be 24

                        calculatorDao.updateFirstNumber(calculatorHelper.formatNumber(currentNumber));

                        result = currentNumber;
                    }
                } else {
                    // Add to Second Number
                    currentNumber = calculatorHelper.addDigitIfNumberNonNull(secondNumber, addedNumber);
                    if (calculatorHelper.getLengthOfNumberWithoutCommas(currentNumber) <= calculatorHelper.getLengthLimit(currentNumber)) {

                        result = calculatorHelper.calculateResult(firstNumber, currentNumber, operand);
                        if (calculatorHelper.isDivisionByZero(result)) {
                            listener.onDivisionByZero(firstNumber, currentNumber, operand);
                        }
                        calculatorDao.updateSecondNumber(calculatorHelper.formatNumber(currentNumber));
                    }
                }
                if (!calculatorHelper.isDivisionByZero(result)) {
                    calculatorDao.updateResult(calculatorHelper.formatNumberForResult(result));
                    if (calculatorHelper.getLengthOfNumberWithoutCommas(currentNumber) <= calculatorHelper.getLengthLimit(currentNumber)) {

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
        AsyncTask.execute(() -> {
            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

            String result = calculatorDao.getResult();
            String firstNumber = calculatorDao.getFirstNumber();

            if (result == null) {
                calculatorDao.clearEntries();
                listener.onClear();
            } else if (firstNumber != null) {
                listener.onShowResult(calculatorDao.getCalculatorDomain());
            } else { // Result != null and firstNumber == null
                listener.onShowResult(calculatorDao.getSavedCalculatorDomain());
                listener.onButtonResult(calculatorDao.getSavedCalculatorDomain());
            }

        });
    }

    @Override
    public void clearEntries(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(() -> {
            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
            calculatorDao.clearEntries();
            listener.onClear();
        });
    }

    @Override
    public void setOperand(OnGetResultFinishListener listener, Context ctx, String operand) {
        AsyncTask.execute(() -> {
            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
            String result;
            String previousOperand;

            result = calculatorDao.getResult() == null ? "0" : (calculatorDao.getResult());
            previousOperand = calculatorDao.getOperator();

            if (calculatorHelper.isNaN(result)|| calculatorHelper.isDivisionByZero(result)) {
                calculatorDao.updateFirstNumber(null);
                calculatorDao.updateResult(null);
                calculatorDao.updateSecondNumber(null);
                calculatorDao.updateOperation(null);
                listener.onError("NaN");
            } else {
                if (previousOperand == null) { // If there's no current operand, all you have to do is update the first number in DB,
                    if ((calculatorDao.getFirstNumber() == null) && result.equals("0")) {
                        calculatorDao.updateFirstNumber("0");
                        calculatorDao.updateResult("0");
                    } else if ((calculatorDao.getFirstNumber() == null)) {
                        // This is executed when we have pressed result button (everything turns into null except result) and then press some operand.
                        // We basically set first number to be equal to result.
                        calculatorDao.updateFirstNumber(result);
                    }

                } else { // Otherwise, first number becomes the current result, second number becomes "null" and  we change the current operand.
                    // Set result variable to be the formatted version of the result value we have on DB.
                    // For example, if we have the value 258,297.2117865 on result, what is actually shown on the app is 258,297.212
                    // So, we want to save this value on Result and on NumberA on DB as well.
                    result = calculatorDao.getResult() == null ? "0" : (calculatorHelper.formatNumberForResult(calculatorDao.getResult()));
                    calculatorDao.updateFirstNumber(result);
                    calculatorDao.updateResult(result); // If result is already null, then set firstNumber as zero.
                    calculatorDao.updateSecondNumber(null);
                }
                calculatorDao.updateOperation(operand);

                listener.onShowResult(calculatorDao.getCalculatorDomain());

            }
        });
    }

    @Override
    public void setComma(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(() -> {
            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
            String currentNumber;

            String firstNumber = calculatorDao.getFirstNumber();
            String secondNumber = calculatorDao.getSecondNumber();
            String result;
            String operand = calculatorDao.getOperator();

            if (operand == null) {
                currentNumber = calculatorHelper.addDigitIfNumberNonNull(firstNumber, ".");
                calculatorDao.updateFirstNumber(calculatorHelper.formatNumber(currentNumber));
                result = currentNumber;
            } else {
                currentNumber = calculatorHelper.addDigitIfNumberNonNull(secondNumber, ".");
                calculatorDao.updateSecondNumber(calculatorHelper.formatNumber(currentNumber));
                result = calculatorHelper.calculateResult(firstNumber, currentNumber, operand);

            }
            calculatorDao.updateResult(calculatorHelper.formatNumberForResult(result));

            if (calculatorHelper.getLengthOfNumberWithoutCommas(currentNumber) <= calculatorHelper.getLengthLimit(currentNumber)) { // If number has decimal part, then Length = 20;

                calculatorDao.updateCalculator(
                        new CalculatorDomain(
                                calculatorDao.getFirstNumber(),
                                calculatorDao.getSecondNumber(),
                                calculatorDao.getResult(),
                                calculatorDao.getOperator()));

                listener.onShowResult(calculatorDao.getCalculatorDomain());
            } else { // User is limited to 20 or 15 digit number input.
                listener.onTooManyDigits();
            }
        });
    }

    @Override
    public void backspace(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(() -> {

            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();
            String currentNumber;

            String firstNumber = calculatorHelper.removeCommasFromNumber(calculatorDao.getFirstNumber())
                    == null ? "0" : calculatorHelper.removeCommasFromNumber(calculatorDao.getFirstNumber());
            String secondNumber = calculatorHelper.removeCommasFromNumber(calculatorDao.getSecondNumber());

            String result = calculatorDao.getResult();
            String operand = calculatorDao.getOperator();

            if (firstNumber.equals("0") && secondNumber == null) {
                calculatorDao.updateFirstNumber(null);
                calculatorDao.updateResult(null);
                calculatorDao.updateSecondNumber(null);
                calculatorDao.updateOperation(null);

                result = null;

                calculatorDao.clearEntries();
                listener.onClear();

            } else if (operand == null) {
                currentNumber = calculatorHelper.removeLastElementFromString(firstNumber);
                calculatorDao.updateFirstNumber(calculatorHelper.formatNumber(currentNumber));
                result = currentNumber;
            } else {
                if (secondNumber != null) {
                    currentNumber = calculatorHelper.removeLastElementFromString(secondNumber);
                    calculatorDao.updateSecondNumber(calculatorHelper.formatNumber(currentNumber));
                    if (currentNumber != null)
                        result = calculatorHelper.calculateResult(firstNumber, currentNumber, operand);
                    else
                        result = firstNumber;
                } else { // If secondNumber is null, it means there's an operand left, so we only return the first Number.
                    calculatorDao.updateOperation(null);
                    result = firstNumber;
                }
            }
            if (result == null) {
                listener.onClear();
            } else if (calculatorHelper.isDivisionByZero(result))
                listener.onDivisionByZero(calculatorDao.getFirstNumber(), calculatorDao.getSecondNumber(), operand);
            else {
                calculatorDao.updateResult(calculatorHelper.formatNumberForResult(result));
                listener.onShowResult(calculatorDao.getCalculatorDomain());
            }
        });
    }

    @Override
    public void result(OnGetResultFinishListener listener, Context ctx) {
        // When we press result button, set NumberA, NumberB and Operand to null on DB.
// Then pass the result on presenter and start animation on result text view.
        AsyncTask.execute(() -> {
            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

            if (calculatorDao.getFirstNumber() != null) // If you double press result button and don't have this condition, saved numbers become null.
                calculatorDao.insertCalculation(new CalculatorDomain(1, calculatorDao.getFirstNumber(), calculatorDao.getSecondNumber(), calculatorDao.getResult(), calculatorDao.getOperator()));


            calculatorDao.updateOperation(null);
            calculatorDao.updateSecondNumber(null);
            calculatorDao.updateFirstNumber(null);

            if ((calculatorDao.getSavedResult() != null))
                listener.onButtonResult(calculatorDao.getSavedCalculatorDomain());
        });
    }

    @Override
    public void percentage(OnGetResultFinishListener listener, Context ctx) {
        AsyncTask.execute(() -> {
            final CalculatorDao calculatorDao = HomeDatabase.getDatabase(ctx).calculatorDao();

            String currentNumber;
            // When we perform deletion, we calculate result based on the formatted numbers on DB.
            String firstNumber = calculatorHelper.removeCommasFromNumber(calculatorDao.getFirstNumber())
                    == null ? "0" : calculatorHelper.removeCommasFromNumber(calculatorDao.getFirstNumber());
            String secondNumber = calculatorHelper.removeCommasFromNumber(calculatorDao.getSecondNumber());
            String result = calculatorDao.getResult();
            String operand = calculatorDao.getOperator();

            if (secondNumber == null) {
                if (!calculatorHelper.numberTooBig(firstNumber)) { //FIXME
                    currentNumber = calculatorHelper.percentNumber(firstNumber);
                    firstNumber = currentNumber;

                    calculatorDao.updateFirstNumber(calculatorHelper.formatNumber(firstNumber));
                    result = firstNumber;
                }
            } else {
                if (!calculatorHelper.numberTooBig(secondNumber)) { //FIXME
                    currentNumber = calculatorHelper.percentNumber(secondNumber);
                    secondNumber = currentNumber;
                    calculatorDao.updateSecondNumber(calculatorHelper.formatNumber(secondNumber));
                    result = calculatorHelper.calculateResult(firstNumber, secondNumber, operand);
                }
            }

            calculatorDao.updateResult(calculatorHelper.formatNumberForResult(result));
            if (!result.equals("0") || secondNumber != null) {
                listener.onShowResult(calculatorDao.getCalculatorDomain());
            } else {
                calculatorDao.updateFirstNumber(null);
                calculatorDao.updateResult(null);
                calculatorDao.updateSecondNumber(null);

                listener.onClear();
            }
        });
    }
}
