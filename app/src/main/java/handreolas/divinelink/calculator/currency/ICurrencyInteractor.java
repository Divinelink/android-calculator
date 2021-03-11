package handreolas.divinelink.calculator.currency;

import android.content.Context;

import java.util.ArrayList;

import handreolas.divinelink.calculator.calculator.ICalculatorInteractor;

public interface ICurrencyInteractor {

    void setNumber(OnGetCurrencyResultFinishListener listener, Context ctx, String number);

    void onTextViewClick(OnGetCurrencyResultFinishListener listener, Context ctx, int position);

    void getSymbols(OnGetCurrencyResultFinishListener listener, Context ctx);

    interface OnGetCurrencyResultFinishListener {

        void onShowResult(String currencyOne, String currencyTwo, String currencyThree);

        void onShowSymbols(ArrayList<SymbolsDomain> symbols, int position);

        void onShowSymbols(ArrayList<SymbolsDomain> symbols);

        void onClear();

        void onTooManyDigits();

        void onError();

    }

}
