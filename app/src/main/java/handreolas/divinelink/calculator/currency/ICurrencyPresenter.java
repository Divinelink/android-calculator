package handreolas.divinelink.calculator.currency;


import android.content.Context;

public interface ICurrencyPresenter {


    void getCurrencyRatios(Context ctx);

    void calculateRates(int position, long value, Context ctx);

    void getCurrencyList(int position, Context ctx);



}
