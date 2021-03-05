package handreolas.divinelink.calculator.calculator;

import android.content.Context;

interface ICalculatorPresenter {

    void setNumber(Context ctx, int number);
    void getNumber(Context ctx);

    void clearNumber(Context ctx);

    void setOperand(Context ctx, String operand);
    void getOperand(Context ctx);




}
