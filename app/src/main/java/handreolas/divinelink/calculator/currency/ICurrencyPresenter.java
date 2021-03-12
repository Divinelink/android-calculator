package handreolas.divinelink.calculator.currency;


import android.content.Context;

public interface ICurrencyPresenter {


    void getCurrencySymbols(Context ctx);

    void getCurrencySelectorFragment(Context ctx, int position);

    void getCurrencyRates(Context ctx, boolean refresh);

    void calculateRates(String value, Context ctx);

    void insertNumber(String value, String insertedNumber, Context ctx);

    void insertComma(Context ctx, String currentValue);

    void backspace(Context ctx, String currentValue);

    void clearValues(Context ctx);

}
