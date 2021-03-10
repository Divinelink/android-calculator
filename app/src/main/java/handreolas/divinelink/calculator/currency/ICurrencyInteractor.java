package handreolas.divinelink.calculator.currency;

import android.content.Context;

import handreolas.divinelink.calculator.calculator.ICalculatorInteractor;

public interface ICurrencyInteractor {

    void setNumber(OnGetCurrencyResultFinishListener listener, Context ctx, String number);

    void onTextViewClick(OnGetCurrencyResultFinishListener listener, Context ctx, int position);

    interface OnGetCurrencyResultFinishListener {

        void onShowResult(String currencyOne, String currencyTwo, String currencyThree);

        void onClear();

        void onTooManyDigits();

        void onError();

    }

}
