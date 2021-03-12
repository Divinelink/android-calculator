package handreolas.divinelink.calculator.currency;

import android.content.Context;

import java.util.ArrayList;

import handreolas.divinelink.calculator.calculator.ICalculatorInteractor;

public interface ICurrencyInteractor {

    void setNumber(OnGetCurrencyResultListener listener, Context ctx, String number);

    void onTextViewClick(OnGetCurrencyResultListener listener, Context ctx, int position);

    void calculateRates(OnGetCurrencyResultListener listener, Context ctx, int position);

    void getSymbols(OnGetCurrencyResultListener listener, Context ctx);

    void getRates(OnGetCurrencyResultListener listener, Context ctx);

    interface OnGetCurrencyResultListener {

        void onShowResult(String currencyOne, String currencyTwo, String currencyThree);

        void onShowSymbols(ArrayList<SymbolsDomain> symbols, int position);

        void onUpdateTime(Long date);

        void onUpdateCurrencyRates(ArrayList<Double> rates, int selectedPosition);

        void onBeforeUpdateTime(String updating);

        void onShowSymbols(ArrayList<SymbolsDomain> symbols);

        void onClear();

        void onTooManyDigits();

        void onError();

    }

}
