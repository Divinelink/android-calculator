package handreolas.divinelink.calculator.calculator;

import android.content.Context;

public interface ICalculatorInteractor {

    void setNumber(OnGetResultFinishListener listener, Context ctx, String number);

    void setOperand(OnGetResultFinishListener listener, Context ctx, String operand);

    void setComma(OnGetResultFinishListener listener, Context ctx);

    void getResult(OnGetResultFinishListener listener, Context ctx);

    void clearEntries(OnGetResultFinishListener listener, Context ctx);

    void backspace(OnGetResultFinishListener listener, Context ctx);

    void result(OnGetResultFinishListener listener, Context ctx);

    void percentage(OnGetResultFinishListener listener, Context ctx);

    interface OnGetResultFinishListener{
        void onShowResult(CalculatorDomain calculatorDomain);
        void onClear();
        void onError(String error);
        void onTooManyDigits();
    }




}
