package handreolas.divinelink.calculator.calculator;

import android.content.Context;

public interface ICalculatorInteractor {

    void setNumber(OnGetResultFinishListener listener, Context ctx, int number);

    void getResult(OnGetResultFinishListener listener, Context ctx);

    void clearEntries(OnGetResultFinishListener listener, Context ctx);

    void setOperand(OnSetOperandFinishListener listener, Context ctx, String operand);



    interface OnGetResultFinishListener{
        void onShowResult(String firstNumber, String secondNumber, String operand, String finalResult);
        void onClear();
        void onError(String error);
        void onTooManyDigits();
    }

    interface OnSetOperandFinishListener{
        void onSuccess(String number, String operand);
        void onError(String error);
    }



}
