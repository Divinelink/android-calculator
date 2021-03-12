package handreolas.divinelink.calculator.currency;

import android.content.Context;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public interface ICurrencyInteractor {

    void insertNumber(OnGetCurrencyResultListener listener, Context ctx, String insertedNumber, String currencyValue);

    void insertComma(OnGetCurrencyResultListener listener, Context ctx, String currencyValue);

    void onTextViewClick(OnGetCurrencyResultListener listener, Context ctx, int position);

    void calculateRates(OnGetCurrencyResultListener listener, Context ctx, String currentValue);

    void getSymbols(OnGetCurrencyResultListener listener, Context ctx);

    void getRates(OnGetCurrencyResultListener listener, Context ctx, boolean refresh);

    void clearValues(OnGetCurrencyResultListener listener, Context ctx);

    void backspace(OnGetCurrencyResultListener listener, Context ctx, String currentValue);

    void getSymbolSelectorFragment(OnGetCurrencyResultListener listener, Context ctx, int position);

    interface OnGetCurrencyResultListener {

        void onShowResult(String currencyOne, String currencyTwo, String currencyThree);

        void onShowSymbols(ArrayList<CurrencyDomain> symbols, int position);

        void onUpdateTime(Long date);

        void onUpdateCurrencyRates(ArrayList<String> rates, int selectedPosition);

        void onBeforeUpdateTime(String updating);

        void onShowSymbols(ArrayList<CurrencyDomain> symbols);

        void onAddSingleCharOnCurrentRate(String currentRate, int position);

        void onAddSelectorFragment(int position);

        void onClear();

        void doNothing();

        void onError(Context ctx, int errorCode);

    }

    interface OnGetCurrencySelectorResultListener {

        void onShowSymbols(ArrayList<CurrencyDomain> symbols, int position);

    }



}
