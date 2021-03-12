package handreolas.divinelink.calculator.calculator;

import android.content.Context;

public class CalculatorPresenterImpl implements ICalculatorPresenter, ICalculatorInteractor.OnGetResultFinishListener {

    private final ICalculatorView calculatorView;
    private final ICalculatorInteractor interactor;
    private int mLengthOfResult;

    public CalculatorPresenterImpl(ICalculatorView calculatorView) {
        this.calculatorView = calculatorView;
        interactor = new CalculatorInteractorImpl();
    }

    @Override
    public void getNumber(Context ctx) {
        interactor.getResult(this, ctx);
    }

    @Override
    public void clearNumber(Context ctx) {
        interactor.clearEntries(this, ctx);
    }

    @Override
    public void setNumber(Context ctx, String number) {
        interactor.setNumber(this, ctx, number);
    }

    @Override
    public void setOperand(Context ctx, String operand) {
        interactor.setOperand(this, ctx, operand);
    }

    @Override
    public void setComma(Context ctx) {
        interactor.setComma(this, ctx);
    }

    @Override
    public void getOperand(Context ctx) {

    }


    @Override
    public void onShowResult(CalculatorDomain calculatorDomain) {

        int lengthLimitOnCalculation = 20; //FIXME
        String firstNumber = calculatorDomain.getNumberA();
        String secondNumber = calculatorDomain.getNumberB();
        String operand = calculatorDomain.getOperation();
        String finalResult = calculatorDomain.getResult();

        if (firstNumber == null)
            calculatorView.onClearTextViews();
        else if (operand == null) {
            calculatorView.showResult(firstNumber);
        } else if (secondNumber == null) {
            calculatorView.showResult(firstNumber + operand);
        } else {
            mLengthOfResult = firstNumber.length() + secondNumber.length() + operand.length();
            if (firstNumber.contains("E")) // If First Number is in exponential form, then @lengthLimitOnCaluclation becomes 23, so UI doesn't break.
                lengthLimitOnCalculation =19;
            if (mLengthOfResult >= lengthLimitOnCalculation) {
                calculatorView.showResult(String.format("%s\n%s%s", firstNumber, operand, secondNumber));
            } else {
                calculatorView.showResult(String.format("%s%s%s", firstNumber, operand, secondNumber));
            }
        }

        if (finalResult != null)
            calculatorView.showResultOnResultTV(finalResult);
    }

    @Override
    public void onButtonResult(CalculatorDomain calculatorDomain) {
        // This is called from @result method on Domain Model, when we press the Result Button.
        calculatorView.resultOnButtonPress(calculatorDomain.getResult());

    }

    @Override
    public void onError(String error) {
        calculatorView.onClearTextViews();
    }

    @Override
    public void onTooManyDigits() {
        // Do nothing. Simply do not add any more digits
    }

    @Override
    public void result(Context ctx) {
        interactor.result(this, ctx);
    }

    @Override
    public void percentage(Context ctx) {
        interactor.percentage(this, ctx);
    }

    @Override
    public void backspace(Context ctx) {
        interactor.backspace(this, ctx);
    }


    @Override
    public void onClear() {
        calculatorView.onClearTextViews();
    }

    @Override
    public void onDivisionByZero(String numberA, String numberB, String operand) {
//        calculatorView.showResult(calculatorDomain.getNumberA() + calculatorDomain.getOperation() + calculatorDomain.getNumberB());
        calculatorView.showResult(numberA + operand + numberB);
        calculatorView.showResultOnResultTV("Can't divide by zero");
    }
}
