package handreolas.divinelink.calculator.calculator;

import android.content.Context;

interface ICalculatorPresenter {

    void setNumber(Context ctx, String number);
    void setComma(Context ctx);
    void getNumber(Context ctx);

    void clearNumber(Context ctx);

    void backspace(Context ctx);

    void setOperand(Context ctx, String operand);
    void getOperand(Context ctx);




}
