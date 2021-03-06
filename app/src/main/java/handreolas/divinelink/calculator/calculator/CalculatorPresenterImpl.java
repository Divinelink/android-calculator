package handreolas.divinelink.calculator.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.ScientificNumberFormatter;
import android.util.Log;

import java.util.Locale;

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
    public void onShowResult(String firstNumber, String secondNumber, String operand, String finalResult) {

        if (operand == null) {
            calculatorView.showResult(firstNumber);
        } else if (secondNumber == null) {
            calculatorView.showResult(firstNumber + operand);
        } else {
            mLengthOfResult = firstNumber.length() + secondNumber.length() + operand.length();
            if (mLengthOfResult >= 25) {
                calculatorView.showResult(String.format("%s\n%s%s", firstNumber, operand, secondNumber));
            } else {
                calculatorView.showResult(String.format("%s%s%s", firstNumber, operand, secondNumber));
            }
        }
        calculatorView.showResultOnResultTV(finalResult);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onTooManyDigits() {
        // Do nothing. Simply do not add any more digits
    }



//    @Override
//    public void onSuccess(String number, String operand) {
////        mLengthOfResult = number.length() + operand.length();
//        calculatorView.showResult(number + operand);
//    }


    @Override
    public void onClear() {
        calculatorView.onClearTextViews();
    }
}
